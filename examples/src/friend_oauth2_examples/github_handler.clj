(ns friend-oauth2-examples.github-handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [cemerick.friend :as friend]
            [clj-http.client :as client]
            [friend-oauth2.workflow :as oauth2]
            [friend-oauth2.util :refer [format-config-uri get-access-token-from-params]]
            [cheshire.core :as j]
            (cemerick.friend [workflows :as workflows]
                             [credentials :as creds])))

(declare render-status-page)
(declare render-repos-page)
(declare get-github-repos)

(def config-auth {:roles #{::user}})

(def client-config
  {:client-id ""
   :client-secret ""
   :callback {:domain "http://example.com" :path "/github.callback"}})

(def uri-config
  {:authentication-uri {:url "https://github.com/login/oauth/authorize"
                        :query {:client_id (:client-id client-config)
                                :response_type "code"
                                :redirect_uri (format-config-uri client-config)
                                :scope "user"}}

   :access-token-uri {:url "https://github.com/login/oauth/access_token"
                      :query {:client_id (:client-id client-config)
                              :client_secret (:client-secret client-config)
                              :grant_type "authorization_code"
                              :redirect_uri (format-config-uri client-config)}}})

(defroutes ring-app
  (GET "/" request "<a href=\"/repos\">My Github Repositories</a><br><a href=\"/status\">Status</a>")
  (GET "/status" request
       (render-status-page request))
  (GET "/repos" request
       (friend/authorize #{::user} (render-repos-page request)))
  (friend/logout (ANY "/logout" request (ring.util.response/redirect "/"))))

(def app
  (handler/site
   (friend/authenticate
    ring-app
    {:allow-anon? true
     :workflows [(oauth2/workflow
                  {:client-config client-config
                   :uri-config uri-config
                   :access-token-parsefn get-access-token-from-params
                   :config-auth config-auth})]})))

(defn render-status-page [request]
  (let [count (:count (:session request) 0)
        session (assoc (:session request) :count (inc count))]
    (-> (ring.util.response/response
           (str "<p>We've hit the session page " (:count session)
                " times.</p><p>The current session: " session "</p>"))
         (assoc :session session))))

(defn render-repos-page 
  "Shows a list of the current users github repositories by calling the github api
   with the OAuth2 access token that the friend authentication has retrieved."
  [request]
  (let [authentications (get-in request [:session :cemerick.friend/identity :authentications])
        access-token (:access_token (second (first authentications)))
        repos-response (get-github-repos access-token)]
    (str (vec (map :name repos-response)))))

(defn get-github-repos 
  "Github API call for the current authenticated users repository list."
  [access-token]
  (let [url (str "https://api.github.com/user/repos?access_token=" access-token)
        response (client/get url {:accept :json})
        repos (j/parse-string (:body response) true)]
    repos))
