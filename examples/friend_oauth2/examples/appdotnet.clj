(ns friend-oauth2.examples.appdotnet
  (:require [cemerick.friend :as friend]
            [cemerick.friend.workflows :as workflows]
            [cemerick.friend.credentials :as creds]
            [clojure.tools.logging :as log]
            [clojusc.twig :as logger]
            [compojure.core :as compojure :refer [GET ANY defroutes]]
            [friend-oauth2.service.appdotnet :as appdotnet]
            [friend-oauth2.util :as util]
            [org.httpkit.server :as server]
            [ring.util.response :as response]
            [ring.middleware.defaults :as ring-defaults])
  (:gen-class))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Mini Webapp ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn html
  [content]
  (-> content
      (response/response)
      (response/header "Content-Type" "text/html")))

(def index-content
  (str "<a href=\"/admin\">Admin Pages</a><br />"
       "<a href=\"/authlink\">Authorized page</a><br />"
       "<a href=\"/authlink2\">Authorized page 2</a><br />"
       "<a href=\"/status\">Status</a><br />"
       "<a href=\"/logout\">Log out</a>"))

(defn get-status-content
  [count session]
  (str "<p>We've hit the session page "
       count
       " times.</p><p>The current session: "
       session
       "</p>"))

(defn render-index-page
  [req]
  (html index-content))

(defn render-status-page
  [req]
  (let [count (:count (:session req) 0)
        session (assoc (:session req) :count (inc count))]
    (-> (get-status-content count session)
        (html)
        (assoc :session session))))

(defroutes app-routes
  (GET "/" req
       (render-index-page req))
  (GET "/status" req
       (render-status-page req))
  (GET "/authlink" req
       (friend/authorize
         #{::user}
         (html "Authorized page.")))
  (GET "/authlink2" req
       (friend/authorize
         #{::user}
         (html "Authorized page 2.")))
  (GET "/admin" req
       (friend/authorize
         #{::admin}
         (html "Only admins can see this page.")))
  (friend/logout (ANY "/logout" req (response/redirect "/"))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; OAuth2 Configuration and Integration ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn credential-fn
  [token]
  ;;lookup token in DB or whatever to fetch appropriate :roles
  {:identity token :roles #{::user}})

(def workflow
  (appdotnet/workflow
    {:config {:scope "stream,email"}
     :credential-fn credential-fn}))

(def auth-opts
  {:allow-anon? true
   :workflows [workflow]})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; App Server ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def app
  (-> app-routes
      (friend/authenticate auth-opts)
      (ring-defaults/wrap-defaults ring-defaults/site-defaults)))

(defn -main
  [& args]
  (logger/set-level! '[ring friend friend-oauth2] :info)
  (log/info "Starting example server using App.net OAuth2 ...")
  (server/run-server app {:port 8999}))
