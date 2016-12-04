# Examples

The friend-oauth2 project includes the following examples:

 * [App.net][app.net]
 * [Facebook][facebook]
 * [Github][github]
 * [Google OAuth 2.0 Login][google]


## Configuration

The examples make use of the following environment variables:

* `OAUTH2_CLIENT_ID`
* `OAUTH2_CLIENT_SECRET`
* `OAUTH2_CALLBACK_URL`


## Running

Once the environment variables are set, you will be ready to run the examples.
There are two sets of examples, one set for each type of configuration support
`friend-oauth2` provides (see [configuration documentation][config docs] for
more details).


### Record-based Configuration

The examples configured using the new records support can be run with the
following `lein` aliases:

* `lein appdotnet`
* `lein facebook`
* `lein github`
* `lein google`

Note that, under the hood, the record-based configuration is
backwards-compatible with the legacy configuration.


### Legacy Configuration

The examples configured using the original configuration mechanism can be run
with the  following `lein` aliases:

* `lein legacy-appdotnet`
* `lein legacy-facebook`
* `lein legacy-github`
* `lein legacy-google`


## Source Code

We keep the example source code as part of the code in this repository:
 * [examples/friend_oauth2/examples][example source code]

However, we also recognize that it's nice to see an example in the
documentation itself, so we'll take on the burden of maintaining the following
text in the effort to provide a good docs experience for you :-)


Here's the Github `friend-oauth2` example, runable from the commandline
(with `lein`, `java`, etc.):

```clj
(ns friend-oauth2.examples.github
  (:require [cemerick.friend :as friend]
            [cemerick.friend.workflows :as workflows]
            [cemerick.friend.credentials :as creds]
            [clj-http.client :as client]
            [clojure.data.json :as json]
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
    :scope "user"
    :auth-uri "https://github.com/login/oauth/authorize"
    :token-uri "https://github.com/login/oauth/access_token"))

(def client-config (config/->client-cfg cfg))
(def uri-config (config/->uri-cfg cfg))

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
```

[app.net]: https://developers.app.net/reference/authentication/
[facebook]: https://developers.facebook.com/docs/facebook-login/manually-build-a-login-flow/
[github]: https://developer.github.com/v3/oauth/
[google]: https://developers.google.com/accounts/docs/OAuth2Login
[config docs]: http://clojusc.github.io/friend-oauth2/current/20-configurtion.html
[example source code]: https://github.com/clojusc/friend-oauth2/tree/master/examples/friend_oauth2/examples
