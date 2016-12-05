(ns friend-oauth2.service.appdotnet
  (:require [clojure.tools.logging :as log]
            [clojusc.twig :as logger]
            [friend-oauth2.config :as config]
            [friend-oauth2.service :as service]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Supporting functions ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; TBD

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; OAuth2 Configuration and Integration ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def service-cfg
  (config/client
    :auth-uri "https://account.app.net/oauth/authenticate"
    :token-uri "https://account.app.net/oauth/access_token"))

(defn workflow
  ""
  [args]
  (service/workflow args service-cfg))

