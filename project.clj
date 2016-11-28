(defproject clojusc/friend-oauth2 "0.1.5"
  :description "OAuth2 workflow for Friend"
  :url "https://github.com/clojusc/friend-oauth2"
  :license {
    :name "MIT License"
    :url "http://dd.mit-license.org"}
  :dependencies [
    [org.clojure/data.json "0.2.6"]
    [com.cemerick/friend "0.2.3"
      :exclusions [org.apache.httpcomponents/httpclient]]
    [ring/ring-core "1.6.0-beta6"]
    [ring/ring-codec "1.0.1"]
    [clj-http "3.4.1"]
    [cheshire "5.6.3"]
    [prismatic/schema "1.1.3"]
    [clojusc/twig "0.3.0"]
    [crypto-random "1.2.0"]]
  :profiles {
    :uber {
      :aot :all}
    :test {
      :dependencies [
        [com.cemerick/url "0.1.1"]
        [compojure "1.6.0-beta1"]
        [midje "1.8.3"]
        [ring-mock "0.1.5"]]
      :plugins [
        [lein-midje "3.2.1"]]}
    :clj15 {
      :dependencies [
        [org.clojure/clojure "1.5.0"]
        [medley "0.6.0" :exclusions [org.clojure/clojure]]]}
    :clj16 {
      :dependencies [
        [org.clojure/clojure "1.6.0"]
        [medley "0.6.0" :exclusions [org.clojure/clojure]]]}
    :clj17 {
      :dependencies [
        [org.clojure/clojure "1.7.0"]]}
    :clj18 {
      :dependencies [
        [org.clojure/clojure "1.8.0"]]}
    :clj19 {
      :dependencies [
        [org.clojure/clojure "1.9.0-alpha14"]]}
    :dev {
      :source-paths ["dev-resources/src"]
      :repl-options {:init-ns friend-oauth2.dev}
      :dependencies [
        [org.clojure/tools.namespace "0.2.11"
          :exclusions [org.clojure/clojure]]]}
    :docs {
      :plugins [[lein-codox "0.10.2"]
                [lein-simpleton "1.3.0"]]
      :codox {
        :project {
          :name "friend-oauth2"
          :description "OAuth2 workflow for Friend"}
        :namespaces [#"^friend-oauth2\.(?!dev)"]
        :output-path "docs/master/current"
        :doc-paths ["docs/source"]
        :metadata {
          :doc/format :markdown
          :doc "Documentation forthcoming"}}}})
