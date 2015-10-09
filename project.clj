(defproject vita-io/friend-oauth2 "0.1.4"
  :description "OAuth2 workflow for Friend (https://github.com/cemerick/friend). (Bug reports/contributions welcome!)"
  :url "https://github.com/clojusc/friend-oauth2"
  :license {:name "MIT License"
            :url "http://dd.mit-license.org"}

  :dependencies [[org.clojure/clojure "1.6.0"]
                 [com.cemerick/friend "0.2.1" :exclusions [org.apache.httpcomponents/httpclient]]
                 [ring "1.3.2"]
                 [ring/ring-codec "1.0.0"]
                 [clj-http "1.0.1"]
                 [cheshire "5.4.0"]
                 [crypto-random "1.2.0"]]

  :plugins [[lein-midje "3.1.3"]
            [codox "0.8.10"]]

  :profiles {:dev
             {:dependencies [[ring-mock "0.1.5"]
                             [org.clojure/tools.nrepl "0.2.5"]
                             [midje "1.6.3"]
                             [com.cemerick/url "0.1.1"]
                             [compojure "1.3.1"]]}})
