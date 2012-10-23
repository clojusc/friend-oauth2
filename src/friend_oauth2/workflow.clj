(ns friend-oauth2.workflow
  (:require [cemerick.friend :as friend]
            [clj-http.client :as client]
            [ring.util.codec :as codec]
            [cheshire.core :as j]))

(defn format-config-url
  "Formats URL from domain and path pairs in a map"
  [client-config]
  (reduce
   #(str %1 (get-in client-config [:callback %2]))
   "" [:domain :path]))

(defn format-token-url
  "Formats the token url with the authorization code"
  [uri-config code]
  (assoc-in uri-config [:query]
            (assoc-in (uri-config :query) [:code] code)))

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
      (cond
       ;; Step 2, 3:
       ;; accept callback and get access_token (via POST)

       ;; (TODO send client_id and client_secret
       ;; parameters via the Authorization header,
       ;; as described in section 2.3.1 of the spec...?)

       ;; TODO: handle exception/error after initial redirect.

       (contains? (:query-params request) "code")
       (let
           [code (last (last (:query-params request)))
            token-url (assoc (:access-token-uri (:uri-config config))
                        :query (assoc-in (:query (:access-token-uri (:uri-config config))) [:code] code))

            ;; Step 4:
            ;; access_token response. Custom function for handling
            ;; response body is passwed in via the :access-token-parsefn
            response-body ((:access-token-parsefn config)
                           (:body
                            (client/post
                             (:url token-url)
                             {:form-params (:query token-url)})))]

         ;; TODO: handle exception/error after access_token request.

         ;; Auth Map, as expected by Friend on a successful authentication:
         (vary-meta
          ;; Identity map
          (merge
           ;; At the least we will have the access-token,
           ;; so we will use that for the identity (for now).
           {:identity (:access_token response-body)
            :access_token (:access_token response-body)}
           (:config-auth config))  ;; config-auth is provider specific auth settings
          ;; Meta-data
          merge
          {:type ::friend/auth}
          {::friend/workflow :oauth2
           ::friend/redirect-on-auth? true}))

       ;; Step 1: redirect to OAuth2 provider.  Code will be in response.
       :else
       (ring.util.response/redirect
        (str (:url (:redirect-uri (:uri-config config))) "?"
             (codec/form-encode (:query (:redirect-uri (:uri-config config)))))))
      ;; If it is not login or callback (if before cond) do...nothing.
      )))
