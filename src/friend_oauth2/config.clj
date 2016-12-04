(ns friend-oauth2.config)

(defrecord Client
  [client-id
   client-secret
   redirect-uri
   response-type
   scope
   state
   access-type
   prompt
   login-hint
   include-granted-scopes
   code
   grant-type
   adnview
   allow-signup
   auth-uri
   token-uri])

(defn client
  [& {
    :keys [client-id
           client-secret
           redirect-uri
           response-type
           scope
           state
           access-type
           prompt
           login-hint
           include-granted-scopes
           code
           grant-type
           adnview
           allow-signup
           auth-uri
           token-uri]
     :or {client-id (System/getenv "OAUTH2_CLIENT_ID")
          client-secret (System/getenv "OAUTH2_CLIENT_SECRET")
          redirect-uri (System/getenv "OAUTH2_CALLBACK_URL")
          response-type "code"
          grant-type "authorization_code"}}]
  (->Client
    client-id
    client-secret
    redirect-uri
    response-type
    scope
    state
    access-type
    prompt
    login-hint
    include-granted-scopes
    code
    grant-type
    adnview
    allow-signup
    auth-uri
    token-uri))
