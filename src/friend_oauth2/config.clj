(ns friend-oauth2.config
  (:require [clojure.tools.logging :as log]
            [clojusc.twig :as logger]
            [friend-oauth2.util :as util]))

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
  [client-record]
  (log/debug "Converting client-record to client-config ...")
  (log/trace "Record:")
  (log/trace (logger/pprint client-record))
  (->LegacyClient
    (:client-id client-record)
    (:client-secret client-record)
    {:domain (:redirect-domain client-record)
     :path (:redirect-path client-record)}))

(defn ->uri-cfg
  "Convert a `Client` record to a legacy URI configuration map."
  [client-record]
  (log/debug "Converting client-record to uri-config ...")
  (log/trace "Record:")
  (log/trace (logger/pprint client-record))
  (->LegacyURI
    {:url (:auth-uri client-record)
     :query (-> {:client_id (:client-id client-record)
                 :response_type (:response-type client-record)
                 :redirect_uri (:redirect-uri client-record)}
                (update-legacy-map client-record :scope)
                (update-legacy-map client-record :state)
                (update-legacy-map client-record :access-type
                                                 :access_type)
                (update-legacy-map client-record :prompt)
                (update-legacy-map client-record :login-hint
                                                 :login_hint)
                (update-legacy-map client-record :include-granted-scopes
                                                 :include_granted_scopes)
                (update-legacy-map client-record :adnview)
                (update-legacy-map client-record :allow-signup
                                                 :allow_signup))}
     {:url (:token-uri client-record)
      :query {:client_id (:client-id client-record)
              :client_secret (:client-secret client-record)
              :grant_type (:grant-type client-record)
              :redirect_uri (:redirect-uri client-record)}}))
