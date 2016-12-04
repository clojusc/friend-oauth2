(ns friend-oauth2.examples.legacy.google
  (:require [cemerick.friend :as friend]
            [cemerick.friend [workflows :as workflows]
                             [credentials :as creds]]
            [cemerick.url :as url]
            [cheshire.core :as json]
            [compojure.core :as compojure :refer [GET ANY defroutes]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [friend-oauth2.workflow :as oauth2]
            [friend-oauth2.util :as util]
            [org.httpkit.server :as server])
  (:gen-class))

(def callback-url (System/getenv "OAUTH2_CALLBACK_URL"))
(def parsed-url (url/url callback-url))

(def client-config
  {:client-id (System/getenv "OAUTH2_CLIENT_ID")
   :client-secret (System/getenv "OAUTH2_CLIENT_SECRET")
   :callback {:domain (format "%s://%s:%s"
                        (:protocol parsed-url)
                        (:host parsed-url)
                        (:port parsed-url))
              :path (:path parsed-url)}})

(def uri-config
  {:authentication-uri {:url "https://accounts.google.com/o/oauth2/auth"
                       :query {:client_id (:client-id client-config)
                               :response_type "code"
                               :redirect_uri callback-url
                               :scope "email"}}
   :access-token-uri {:url "https://accounts.google.com/o/oauth2/token"
                      :query {:client_id (:client-id client-config)
                              :client_secret (:client-secret client-config)
                              :grant_type "authorization_code"
                              :redirect_uri callback-url}}})

(defroutes app-routes
  (GET "/" request
    (str "<a href=\"/admin\">Admin Pages</a><br />"
         "<a href=\"/authlink\">Authorized page</a><br />"
         "<a href=\"/authlink2\">Authorized page 2</a><br />"
         "<a href=\"/status\">Status</a><br />"
         "<a href=\"/logout\">Log out</a>"))
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

(defn credential-fn
  [token]
  ;;lookup token in DB or whatever to fetch appropriate :roles
  {:identity token :roles #{::user}})

(def workflow
  (oauth2/workflow
    {:client-config client-config
     :uri-config uri-config
     :access-token-parsefn util/get-access-token-from-params
     :credential-fn credential-fn}))

(def auth-opts
  {:allow-anon? true
   :workflows [workflow]})

(def app
  (-> app-routes
      (friend/authenticate auth-opts)
      (handler/site)))

(defn -main
  [& args]
  (server/run-server app {:port 8999}))
