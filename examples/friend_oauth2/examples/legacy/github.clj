(ns friend-oauth2.examples.legacy.github
  (:require [cemerick.friend :as friend]
            [cemerick.friend [workflows :as workflows]
                             [credentials :as creds]]
            [cemerick.url :as url]
            [clj-http.client :as client]
            [clojure.data.json :as json]
            [clojusc.twig :as logger]
            [compojure.core :as compojure :refer [GET ANY defroutes]]
            [compojure.handler :as handler]
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
  {:authentication-uri {:url "https://github.com/login/oauth/authorize"
                        :query {:client_id (:client-id client-config)
                                :response_type "code"
                                :redirect_uri callback-url
                                :scope "user"}}
   :access-token-uri {:url "https://github.com/login/oauth/access_token"
                      :query {:client_id (:client-id client-config)
                              :client_secret (:client-secret client-config)
                              :grant_type "authorization_code"
                              :redirect_uri callback-url}}})

(defn get-authentications
  [request]
  (get-in request [:session :cemerick.friend/identity :authentications]))

(defn get-token
  ([request]
    (get-token request 0))
  ([request index]
    (let [authentications (get-authentications request)]
      (:access-token (nth (keys authentications) index)))))

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
