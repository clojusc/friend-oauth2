(ns friend-oauth2.examples.github
  (:require [cemerick.friend :as friend]
            [cemerick.friend.workflows :as workflows]
            [cemerick.friend.credentials :as creds]
            [clj-http.client :as client]
            [clojure.data.json :as json]
            [clojure.tools.logging :as log]
            [clojusc.twig :as logger]
            [compojure.core :as compojure :refer [GET ANY defroutes]]
            [compojure.handler :as handler]
            [friend-oauth2.config :as config]
            [friend-oauth2.workflow :as oauth2]
            [friend-oauth2.util :as util]
            [ring.util.response :as response]
            [org.httpkit.server :as server])
  (:gen-class))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Supporting functions ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-authentications
  [request]
  (get-in request [:session :cemerick.friend/identity :authentications]))

(defn get-token
  ([request]
    (get-token request 0))
  ([request index]
    (let [authentications (get-authentications request)
          auths-keys (keys authentications)]
      (log/debug "Got authentications:" authentications)
      (log/debug "Got auths-keys:" auths-keys)
      (:access-token (nth auths-keys index)))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Mini Webapp ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn render-status-page [request]
  (let [count (:count (:session request) 0)
        session (assoc (:session request) :count (inc count))]
    (-> (str "<p>We've hit the session page "
             (:count session)
             " times.</p><p>The current session: "
             session
             "</p>")
        (ring.util.response/response)
        (assoc :session session))))

(defn get-github-repos
  "Github API call for the current authenticated users repository list."
  [access-token]
  (let [url (str "https://api.github.com/user/repos?access_token=" access-token)
        response (client/get url {:accept :json})
        repos (json/read-str (:body response) :key-fn keyword)]
    repos))

(defn render-repos-page
  "Shows a list of the current users github repositories by calling the github api
   with the OAuth2 access token that the friend authentication has retrieved."
  [request]
  (let [access-token (get-token request)
        repos-response (get-github-repos access-token)]
    (log/debug "Got token:" access-token)
    (log/trace "Got repos:" (logger/pprint repos-response))
    (->> repos-response
         (map :name)
         (vec)
         (str))))

(defroutes app-routes
  (GET "/" request
    (str "<a href=\"/repos\">My Github Repositories</a><br />"
         "<a href=\"/status\">Status</a><br />"
         "<a href=\"/logout\">Log out</a>"))
  (GET "/status" request
       (render-status-page request))
  (GET "/repos" request
       (friend/authorize #{::user} (render-repos-page request)))
  (friend/logout (ANY "/logout" request (ring.util.response/redirect "/"))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; OAuth2 Configuration and Integration ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def cfg
  (config/client
    :scope "user"
    :auth-uri "https://github.com/login/oauth/authorize"
    :token-uri "https://github.com/login/oauth/access_token"))

(defn credential-fn
  [token]
  ;;lookup token in DB or whatever to fetch appropriate :roles
  {:identity token :roles #{::user}})

(def workflow
  (oauth2/workflow
    {:config cfg
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
  (log/info "Starting example server using Github OAuth2 ...")
  (server/run-server app {:port 8999}))
