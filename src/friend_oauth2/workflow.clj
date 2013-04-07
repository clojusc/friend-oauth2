(ns friend-oauth2.workflow
  (:require [cemerick.friend :as friend]
            [clj-http.client :as client]
            [ring.util.codec :as ring-codec]
            [ring.util.request :as ring-request]
            [cheshire.core :as j]
            [crypto.random :as random]))

(defn format-config-uri
  "Formats URI from domain and path pairs in a map"
  [client-config]
  (reduce
   #(str %1 (get-in client-config [:callback %2]))
   "" [:domain :path]))

(defn format-authentication-uri
  "Formats the client authentication uri"
  [{:keys [authentication-uri]} anti-forgery-token]
  (str (:url authentication-uri) "?"
       (ring-codec/form-encode
        (merge
         {:state anti-forgery-token}
         (:query authentication-uri)))))

(defn replace-authorization-code
  "Formats the token uri with the authorization code"
  [uri-config code]
  (assoc-in (:query uri-config) [:code] code))

;; http://tools.ietf.org/html/draft-ietf-oauth-v2-31#section-5.1
(defn extract-access-token
  "Returns the access token from a JSON response body"
  [response]
  (:access_token
   (clojure.walk/keywordize-keys
    (j/parse-string (:body response)))))

(defn extract-anti-forgery-token
  "Extracts the anti-csrf state key from the response"
  [response]
  (if-let [state-pairs (first (filter
                               #(= (second %1) "state")
                               (:session response)))]
    (-> state-pairs first name)
    nil))

(defn generate-anti-forgery-token []
  (clojure.string/join
   (clojure.string/split (random/base64 60) #"/")))

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
    (if (or (= (ring-request/path-info request)
               (-> config :client-config :callback :path))
            (= (ring-request/path-info request)
               (or (:login-uri config) (-> request ::friend/auth-config :login-uri))))

      ;; Steps 2 and 3:
      ;; accept auth code callback, get access_token (via POST)

      ;; http://tools.ietf.org/html/draft-ietf-oauth-v2-31#section-4.1.2
      (let [params         (:params request)
            code           (:code params)
            response-state (:state params)
            session-state  (extract-anti-forgery-token request)]

        (if (and (not (nil? code))
                 (= response-state session-state))

          (let [access-token-uri (-> config :uri-config :access-token-uri)
                token-url (assoc-in access-token-uri [:query]
                                    (merge {:grant_type "authorization_code"}
                                           (replace-authorization-code access-token-uri code)))
                token-response (client/post
                                (:url token-url)
                                {:form-params (:query token-url)})

                ;; Step 4:
                ;; access_token response. Custom function for handling
                ;; response body is passed in via the :access-token-parsefn

                access-token ((or (:access-token-parsefn config)
                                  extract-access-token)
                              token-response)]

            ;; The auth map for a successful authentication:
            (make-auth (merge {:identity access-token
                               :access_token access-token}
                              (:config-auth config))))

          ;; Step 1: redirect to OAuth2 provider.  Code will be in response.
          (let [anti-forgery-token    (generate-anti-forgery-token)
                session-with-af-token (assoc (:session request)
                                        (keyword anti-forgery-token) "state")]
            (assoc
                (ring.util.response/redirect
                 (format-authentication-uri (:uri-config config) anti-forgery-token))
              :session session-with-af-token)))))))
