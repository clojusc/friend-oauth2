(ns friend-oauth2.examples.facebook
  (:require [cemerick.friend :as friend]
            [cemerick.friend.workflows :as workflows]
            [cemerick.friend.credentials :as creds]
            [clojure.tools.logging :as log]
            [clojusc.twig :as logger]
            [compojure.core :as compojure :refer [GET ANY defroutes]]
            [compojure.handler :as handler]
            [friend-oauth2.config :as config]
            [friend-oauth2.workflow :as oauth2]
            [friend-oauth2.util :as util]
            [org.httpkit.server :as server])
  (:gen-class))

(def cfg
  (config/client
    :auth-uri "https://www.facebook.com/dialog/oauth"
    :token-uri "https://graph.facebook.com/oauth/access_token"))

(def client-config (config/->client-cfg cfg))
(def uri-config (config/->uri-cfg cfg))

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
  (logger/set-level! '[ring friend friend-oauth2] :info)
  (log/info "Starting example server using Facebook OAuth2 ...")
  (server/run-server app {:port 8999}))
