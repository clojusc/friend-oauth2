(ns friend-oauth2.examples.github
  (:require [cemerick.friend :as friend]
            [cemerick.friend.workflows :as workflows]
            [cemerick.friend.credentials :as creds]
            [clj-http.client :as client]
            [clojure.data.json :as json]
            [clojure.tools.logging :as log]
            [clojusc.twig :as logger]
            [compojure.core :as compojure :refer [GET ANY defroutes]]
            [friend-oauth2.service.github :as github]
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
       "<a href=\"/repos\">My Github Repositories</a><br />"
       "<a href=\"/authlink2\">Authorized page 2</a><br />"
       "<a href=\"/status\">Status</a><br />"
       "<a href=\"/logout\">Log out</a>"))

(defn get-status-content
  [count session]
  (str "<p>We've hit the session page "
       count
       " times.</p><p>The current session:</p><p>"
       session
       "</p>"))

(defn get-github-repos
  "Github API call for the current authenticated users repository list."
  [access-token]
  (let [url (str "https://api.github.com/user/repos?access_token=" access-token)
        response (client/get url {:accept :json})
        repos (json/read-str (:body response) :key-fn keyword)]
    repos))

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

(defn render-repos-page
  "Shows a list of the current users github repositories by calling the github api
   with the OAuth2 access token that the friend authentication has retrieved."
  [req]
  (let [access-token (github/get-token req)
        repos-response (get-github-repos access-token)]
    (log/debug "Got token:" access-token)
    (log/trace "Got repos:" (logger/pprint repos-response))
    (html (->> repos-response
               (map :name)
               (vec)
               (str)))))

(defroutes app-routes
  (GET "/" req
       (render-index-page req))
  (GET "/status" req
       (render-status-page req))
  (GET "/repos" req
       (friend/authorize
         #{::user}
         (render-repos-page req)))
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

(derive ::admin ::user)

(defn credential-fn
  [token]
  ;;lookup token in DB or whatever to fetch appropriate :roles
  {:identity token :roles #{::admin}})

(def workflow
  (github/workflow
    {:config {:scope "user"}
     :access-token-parsefn util/get-access-token-from-params
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
  (log/info "Starting example server using Github OAuth2 ...")
  (server/run-server app {:port 8999}))
