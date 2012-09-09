(defproject friend-oauth2 "0.0.1-SNAPSHOT"
  :description "Super-duper pre-release version, OAuth2 workflow for Friend (https://github.com/cemerick/friend)"
  :url "https://github.com/ddellacosta/friend-oauth2"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [compojure "1.1.3"]
                 [com.cemerick/friend "0.1.0"]
                 [clj-http "0.5.3"]]
  :plugins [[lein-ring "0.7.3"]]
  ;; app.net
  ;;:ring {:handler friend-oauth2.appnet-handler/app}
  ;; facebook
  :ring {:handler friend-oauth2.facebook-handler/app}
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.3"]]}})
