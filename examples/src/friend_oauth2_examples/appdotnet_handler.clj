(ns friend-oauth2-examples.appdotnet-handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [cemerick.friend :as friend]
            [friend-oauth2.workflow :as oauth2]
            [cheshire.core :as j]
            (cemerick.friend [workflows :as workflows]
                             [credentials :as creds])))

;; OAuth2 config
(defn access-token-parsefn [body]
  (clojure.walk/keywordize-keys
   (j/parse-string body)))

;; TODO: add a more robust authorization scheme.
(def config-auth {:roles #{::user}})

(def client-config
  {:client-id ""
   :client-secret ""
   :callback {:domain "http://example.com" :path "/app.net.callback"}})

;; TODO: add 'state' parameter for security.
(def uri-config
  {:redirect-uri {:url "https://alpha.app.net/oauth/authenticate"
                  :query {:client_id (:client-id client-config)
                          :response_type "code"
                          :redirect_uri (oauth2/format-config-url client-config)
                          :scope "stream,email"}}

   :access-token-uri {:url "https://alpha.app.net/oauth/access_token"
                      :query {:client_id (:client-id client-config)
                              :client_secret (:client-secret client-config)
                              :grant_type "authorization_code"
                              :redirect_uri (oauth2/format-config-url client-config)
                              :code ""}}})

(defroutes ring-app
  (GET "/" request "open.")
  (GET "/status" request
       (let [count (:count (:session request) 0)
             session (assoc (:session request) :count (inc count))]
         (-> (ring.util.response/response
              (str "<p>We've hit the session page " (:count session)
                   " times.</p><p>The current session: " session "</p>"))
             (assoc :session session))))
  (GET "/authlink" request
       (friend/authorize #{::user} "Authorized page."))
  (GET "/authlink2" request
       (friend/authorize #{::user} "Authorized page 2."))
  (GET "/admin" request
       (friend/authorize #{::admin} "Only admins can see this page."))
  (friend/logout (ANY "/logout" request (ring.util.response/redirect "/"))))

(def app
  (handler/site
   (friend/authenticate
    ring-app
    {:allow-anon? true
     :workflows [(oauth2/workflow
                  {:client-config client-config
                   :uri-config uri-config
                   :access-token-parsefn access-token-parsefn
                   :config-auth config-auth})]})))
