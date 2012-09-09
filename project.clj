(defproject friend-oauth2 "0.1.0-SNAPSHOT"
  :description "Super-duper pre-release version, OAuth2 workflow for Friend (https://github.com/cemerick/friend)"
  :url "https://github.com/ddellacosta/friend-oauth2"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [ring "1.1.4"]
                 [compojure "1.1.2"]
                 [hiccup "1.0.1"]
                 [com.cemerick/friend "0.1.0"]
                 [com.cemerick/url "0.0.6"]
                 [clj-http "0.5.3"]
                 [org.clojure/core.cache "0.6.2"]
                 [org.clojure/tools.logging "0.2.4"]]
  :plugins [[lein-ring "0.7.3"]]
  :ring {:handler friend-test.handler/app}
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.3"]]}})
