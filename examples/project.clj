(defproject friend-oauth2-examples "0.0.1"
  :description "Friend Oauth2 Workflow examples, includes App.net and Facebook handlers."
  :url "https://github.com/ddellacosta/friend-oauth2-examples"
  :license {:name "MIT License"
            :url "http://dd.mit-license.org"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [compojure "1.1.3"]
                 [com.cemerick/friend "0.1.0"]
                 [friend-oauth2 "0.0.1"]
                 [cheshire "4.0.2"]]
  :plugins [[lein-ring "0.7.5"]]
  :ring {:handler friend-oauth2-examples.facebook-handler/app}
;;  :ring {:handler friend-oauth2-examples.appdotnet-handler/app}
;;  :ring {:handler friend-oauth2-examples.github-handler/app}
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.3"]]}})
