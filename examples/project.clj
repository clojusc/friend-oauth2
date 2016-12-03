(defproject friend-oauth2-examples "0.0.5"
  :description "Friend Oauth2 Workflow examples, includes App.net, Facebook, Github and Google handlers."

  :url "https://github.com/ddellacosta/friend-oauth2-examples"

  :license {:name "MIT License"
            :url "http://dd.mit-license.org"}

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [compojure "1.1.5" :exclusions [ring/ring-core org.clojure/core.incubator]]
                 [com.cemerick/friend "0.2.0" :exclusions [ring/ring-core]]
                 [friend-oauth2 "0.1.1" :exclusions [org.apache.httpcomponents/httpcore]]
                 [cheshire "5.2.0"]
                 [ring-server "0.3.0" :exclusions [ring]]]

  :profiles {:dev       {:plugins [[lein-ring "0.8.5" :exclusions [org.clojure/clojure]]]}
             :appdotnet {:ring {:handler friend-oauth2-examples.appdotnet-handler/app}}
             :facebook  {:ring {:handler friend-oauth2-examples.facebook-handler/app}}
             :github    {:ring {:handler friend-oauth2-examples.github-handler/app}}
             :google    {:ring {:handler friend-oauth2-examples.google-handler/app}}}

  :aliases  {"facebook"  ["with-profile" "dev,facebook"
                          "do" "ring" "server-headless"]
             "appdotnet" ["with-profile" "dev,appdotnet"
                          "do" "ring" "server-headless"]
             "github"    ["with-profile" "dev,github"
                          "do" "ring" "server-headless"]
             "google"    ["with-profile" "dev,google"
                          "do" "ring" "server-headless"]})
