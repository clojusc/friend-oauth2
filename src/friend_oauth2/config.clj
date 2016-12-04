(ns friend-oauth2.config
  (:require [friend-oauth2.util :as util]))

(defrecord Client
  [;; authentication fields
   client-id
   redirect-uri
   redirect-domain
   redirect-path
   response-type
   scope
   state
   access-type
   prompt
   login-hint
   include-granted-scopes
   adnview
   allow-signup
   ;; token access fields
   client-secret
   code
   grant-type
   ;; service fields
   auth-uri
   token-uri])

(defrecord LegacyClient
  [client-id
   client-secret
   callback])

(defrecord LegacyURI
  [authentication-uri
   access-token-uri])

(defn client
  "A `Client` record constructor that provides default values for fields."
  [& {
    :keys [client-id
           redirect-uri
           response-type
           scope
           state
           access-type
           prompt
           login-hint
           include-granted-scopes
           adnview
           allow-signup
           client-secret
           code
           grant-type
           auth-uri
           token-uri]
     :or {client-id (System/getenv "OAUTH2_CLIENT_ID")
          client-secret (System/getenv "OAUTH2_CLIENT_SECRET")
          redirect-uri (System/getenv "OAUTH2_CALLBACK_URL")
          response-type "code"
          grant-type "authorization_code"}}]
  (let [[redirect-domain
         redirect-path] (util/parse-url redirect-uri)]
    (->Client
      ;; authentication fields
      client-id
      redirect-uri
      redirect-domain
      redirect-path
      response-type
      scope
      state
      access-type
      prompt
      login-hint
      include-granted-scopes
      adnview
      allow-signup
      ;; token access fields
      client-secret
      code
      grant-type
      ;; service fields
      auth-uri
      token-uri)))

(defn update-legacy-map
  ([map-cfg record-cfg k]
    (update-legacy-map map-cfg record-cfg k k))
  ([map-cfg record-cfg record-k map-k]
    (let [v (record-k record-cfg)]
      (if v
        (assoc map-cfg map-k v)
        map-cfg))))

(defn ->client-cfg
  "Convert a `Client` record to a legacy client configuration map."
  [cfg]
  (->LegacyClient
    (:client-id cfg)
    (:client-secret cfg)
    {:domain (:redirect-domain cfg)
     :path (:redirect-path cfg)}))

(defn ->uri-cfg
  "Convert a `Client` record to a legacy URI configuration map."
  [cfg]
  (->LegacyURI
    {:url (:auth-uri cfg)
     :query (-> {:client_id (:client-id cfg)
                 :response_type (:response-type cfg)
                 :redirect_uri (:redirect-uri cfg)}
                (update-legacy-map cfg :scope)
                (update-legacy-map cfg :state)
                (update-legacy-map cfg :access-type
                                       :access_type)
                (update-legacy-map cfg :prompt)
                (update-legacy-map cfg :login-hint
                                       :login_hint)
                (update-legacy-map cfg :include-granted-scopes
                                       :include_granted_scopes)
                (update-legacy-map cfg :adnview)
                (update-legacy-map cfg :allow-signup
                                       :allow_signup))}
     {:url (:token-uri cfg)
      :query {:client_id (:client-id cfg)
              :client_secret (:client-secret cfg)
              :grant_type (:grant-type cfg)
              :redirect_uri (:redirect-uri cfg)}}))
