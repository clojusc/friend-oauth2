(defproject friend-oauth2-examples "0.0.2"
  :description "Friend Oauth2 Workflow examples, includes App.net, Facebook and Github handlers."
  :url "https://github.com/ddellacosta/friend-oauth2-examples"
  :license {:name "MIT License"
            :url "http://dd.mit-license.org"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [compojure "1.1.5"]
                 [com.cemerick/friend "0.1.4"]
                 [friend-oauth2 "0.0.3"]
                 [cheshire "5.0.2"]]
  :plugins [[lein-ring "0.8.3"]]
  :ring {:handler friend-oauth2-examples.appdotnet-handler/app}
;;  :ring {:handler friend-oauth2-examples.facebook-handler/app}
;;  :ring {:handler friend-oauth2-examples.github-handler/app}
  :profiles
  {:dev {:dependencies [[ring-mock "0.1.3"]]}})
