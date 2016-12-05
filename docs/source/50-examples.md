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


Here's the Google `friend-oauth2` example, runable from the commandline
(e.g., with `lein`):

```clj
(ns friend-oauth2.examples.google
  (:require [cemerick.friend :as friend]
            [cemerick.friend.workflows :as workflows]
            [cemerick.friend.credentials :as creds]
            [clojure.tools.logging :as log]
            [clojusc.twig :as logger]
            [compojure.core :as compojure :refer [GET ANY defroutes]]
            [friend-oauth2.service.google :as google]
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
  (google/workflow
    {:config {:scope "email"}
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
  (log/info "Starting example server using Google OAuth2 ...")
  (server/run-server app {:port 8999}))
```

[app.net]: https://developers.app.net/reference/authentication/
[facebook]: https://developers.facebook.com/docs/facebook-login/manually-build-a-login-flow/
[github]: https://developer.github.com/v3/oauth/
[google]: https://developers.google.com/accounts/docs/OAuth2Login
[config docs]: http://clojusc.github.io/friend-oauth2/current/20-configurtion.html
[example source code]: https://github.com/clojusc/friend-oauth2/tree/master/examples/friend_oauth2/examples
