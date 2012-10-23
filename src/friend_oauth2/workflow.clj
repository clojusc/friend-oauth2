(ns friend-oauth2.workflow
  (:require [cemerick.friend :as friend]
            [clj-http.client :as client]
            [ring.util.codec :as codec]
            [cheshire.core :as j]))

(defn format-config-uri
  "Formats URI from domain and path pairs in a map"
  [client-config]
  (reduce
   #(str %1 (get-in client-config [:callback %2]))
   "" [:domain :path]))

(defn format-authorization-uri
  "Formats the authorization uri"
  [{:keys [authorization-uri]}]
  (str (authorization-uri :url) "?"
       (codec/form-encode (authorization-uri :query))))

(defn replace-authorization-code
  "Formats the token uri with the authorization code"
  [uri-config code]
  (assoc-in (uri-config :query) [:code] code))

;; http://tools.ietf.org/html/draft-ietf-oauth-v2-31#section-4.1.2
(defn extract-code
  "Returns the authentication code from a request query-string"
  [request]
  (if-let [query-string (request :query-string)]
    ((codec/form-decode query-string) "code")
    nil))

;; http://tools.ietf.org/html/draft-ietf-oauth-v2-31#section-5.1
(defn extract-access-token
  "Returns the access token from a JSON response body"
  [response]
  ((clojure.walk/keywordize-keys
    (j/parse-string (response :body)))
   :access_token))

(defn workflow
  "Workflow for OAuth2"
  [config]
  (fn [request]
    (if (or (= (:uri request)
               (:path (:callback (:client-config config))))
            (= (:uri request)
               (or (config :login-uri) "/login")))

      ;; Step 2, 3:
      ;; accept callback and get access_token (via POST)
      (if-let [code (extract-code request)]
        (let [access-token-uri ((config :uri-config) :access-token-uri)
              token-url (assoc-in access-token-uri [:query]
                                  (replace-authorization-code access-token-uri code))

              ;; Step 4:
              ;; access_token response. Custom function for handling
              ;; response body is passwed in via the :access-token-parsefn
              access-token ((or (config :access-token-parsefn)
                                extract-access-token)
                            (client/post
                             (:url token-url)
                             {:form-params (:query token-url)}))]

          ;; Auth Map, as expected by Friend on a successful authentication:
          (vary-meta
           ;; Identity map
           (merge
            ;; At the least we will have the access-token,
            ;; so we will use that for the identity (for now).
            {:identity access-token
             :access_token access-token}
            (:config-auth config))  ;; config-auth is provider specific auth settings
           ;; Meta-data
           merge
           {:type ::friend/auth}
           {::friend/workflow :oauth2
            ::friend/redirect-on-auth? true}))

        ;; Step 1: redirect to OAuth2 provider.  Code will be in response.
        (ring.util.response/redirect
         (format-authorization-uri (config :uri-config)))))))
