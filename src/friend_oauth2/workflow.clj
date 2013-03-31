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

(defn format-authentication-uri
  "Formats the client authentication uri"
  [{:keys [authentication-uri]}]
  (str (authentication-uri :url) "?"
       (codec/form-encode (authentication-uri :query))))

(defn replace-authorization-code
  "Formats the token uri with the authorization code"
  [uri-config code]
  (assoc-in (uri-config :query) [:code] code))



;; http://tools.ietf.org/html/draft-ietf-oauth-v2-31#section-5.1
(defn extract-access-token
  "Returns the access token from a JSON response body"
  [response]
  ((clojure.walk/keywordize-keys
    (j/parse-string (response :body)))
   :access_token))

(defn make-auth
  "Creates the auth-map for Friend"
  [identity]
  (with-meta identity
    {:type ::friend/auth
     ::friend/workflow :email-login
     ::friend/redirect-on-auth? true}))
  
(defn workflow
  "Workflow for OAuth2"
  [config]
  (fn [request]
    ;; If we have a callback for this workflow
    ;; or a login URL in the request, process it.
    (if (or (= (:uri request)
               (:path (:callback (:client-config config))))
            (= (:uri request)
               (or (config :login-uri) "/login")))

      ;; Steps 2 and 3:
      ;; accept auth code callback, get access_token (via POST)

      ;; http://tools.ietf.org/html/draft-ietf-oauth-v2-31#section-4.1.2
      (if-let [code (-> request :params :code)]
        (let [access-token-uri ((config :uri-config) :access-token-uri)
              token-url (assoc-in access-token-uri [:query]
                                  (replace-authorization-code access-token-uri code))
              ;; Step 4:
              ;; access_token response. Custom function for handling
              ;; response body is pass in via the :access-token-parsefn
              access-token ((or (config :access-token-parsefn)
                                extract-access-token)
                            (client/post
                             (:url token-url)
                             {:form-params (:query token-url)}))]

          ;; The auth map for a successful authentication:
          (make-auth (merge {:identity access-token
                             :access_token access-token}
                            (:config-auth config))))

        ;; Step 1: redirect to OAuth2 provider.  Code will be in response.
        (ring.util.response/redirect
         (format-authentication-uri (config :uri-config)))))))
