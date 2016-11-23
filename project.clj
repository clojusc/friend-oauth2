(defproject clojusc/friend-oauth2 "0.1.4"
  :description "OAuth2 workflow for Friend"
  :url "https://github.com/clojusc/friend-oauth2"
  :license {:name "MIT License"
            :url "http://dd.mit-license.org"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [com.cemerick/friend "0.2.3" :exclusions [org.apache.httpcomponents/httpclient]]
                 [ring "1.5.0"]
                 [ring/ring-codec "1.0.1"]
                 [clj-http "3.4.1"]
                 [cheshire "5.6.3"]
                 [prismatic/schema "1.1.3"]
                 [crypto-random "1.2.0"]]
  :plugins [[lein-midje "3.2.1"]
            [codox "0.10.2"]]
  :profiles {:dev
             {:dependencies [[ring-mock "0.1.5"]
                             [org.clojure/tools.nrepl "0.2.12"]
                             [midje "1.8.3"]
                             [com.cemerick/url "0.1.1"]
                             [compojure "1.5.1"]]}})
