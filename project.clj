(defproject friend-oauth2 "0.0.3"
  :description "OAuth2 workflow for Friend (https://github.com/cemerick/friend). (Bug reports/contributions welcome!)"
  :url "https://github.com/ddellacosta/friend-oauth2"
  :license {:name "MIT License"
            :url "http://dd.mit-license.org"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [com.cemerick/friend "0.1.4"]
                 [ring "1.1.8"]
                 [clj-http "0.6.5"]
                 [cheshire "5.0.2"]]
  :plugins [[lein-ring "0.8.3"]
            [lein-midje "3.0.0"]
            [codox "0.6.4"]]
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.3"]
                        [ring "1.2.0-beta2"]
                        [midje "1.5.0"]]}})
