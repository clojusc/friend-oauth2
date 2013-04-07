(ns friend-oauth2.workflow-facts
  (:use
   midje.sweet
   friend-oauth2.test-helpers
   friend-oauth2.fixtures)
  (:require
   [cemerick.url :as url]
   [ring.mock.request :as ring-mock]))

(fact
 "A user can authenticate via OAuth2
 (tests 'happy path' for the whole process)."

 (let [authlink-response  (test-app (ring-mock/request :get "/authlink"))
       ring-session-val   (extract-ring-session-val authlink-response)

       login-response     (make-ring-session-get-request
                           "/login" {} ring-session-val)
       state              (extract-state-from-redirect-url login-response)

       ;; refactor to use cemerick.url?
       authentication-url (str
                           "http://example.com/authenticate?redirect_uri=http%3A%2F%2F127.0.0.1%2Fredirect&client_id=my-client-id&state="
                           (clojure.string/replace state #"\+" "%2B"))

       authlink-redirect  (make-ring-session-get-request
                           "/redirect"
                           {:code "my-code" :state state}
                           ring-session-val)
       authlink           (extract-location authlink-redirect)

       authed-response    (make-ring-session-get-request
                           authlink {} ring-session-val)]

   (:status authlink-response)          => 302
   (extract-location authlink-response) => "http://localhost/login"

   (:status login-response)             => 302
   (extract-location login-response)    => authentication-url

   (:status authlink-redirect)          => 303
   (extract-location authlink-redirect) => "/authlink"

   (:status authed-response)            => 200
   (:body authed-response)              => "Authorized page."))

(fact
 "A login request redirects to the authorization uri"
 (let [auth-redirect  (test-app (ring-mock/request :get "/login"))
       location       (extract-location auth-redirect)
       redirect-query (-> location url/url :query clojure.walk/keywordize-keys)]

   (:status auth-redirect)              => 302
   (:redirect_uri redirect-query)       => "http://127.0.0.1/redirect"
   (:client_id redirect-query)          => "my-client-id"
   (not (nil? (:state redirect-query))) => true))

(future-fact
 "access-token-parsefn is used for the token if provided."

 (let [{state :state
        ring-session-val :ring-session-val} (setup-valid-state)
        authlink-redirect  (make-ring-session-get-request
                            "/redirect"
                            {:code "my-code" :state state}
                            ring-session-val)]))

(future-fact
 "If the session state is not the same as the auth-response state, it does not proceed")
