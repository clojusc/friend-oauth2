(ns friend-oauth2.service.github
  (:require [clojure.tools.logging :as log]
            [clojusc.twig :as logger]
            [friend-oauth2.config :as config]
            [friend-oauth2.service :as service]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; Supporting functions ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn get-authentications
  [request]
  (get-in request [:session :cemerick.friend/identity :authentications]))

(defn get-token
  ([request]
    (get-token request 0))
  ([request index]
    (let [authentications (get-authentications request)
          auths-keys (keys authentications)]
      (log/debug "Got authentications:" authentications)
      (log/debug "Got auths-keys:" auths-keys)
      (:access-token (nth auths-keys index)))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;; OAuth2 Configuration and Integration ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(def service-cfg
  (config/client
    :auth-uri "https://github.com/login/oauth/authorize"
    :token-uri "https://github.com/login/oauth/access_token"))

(defn workflow
  ""
  [args]
  (service/workflow args service-cfg))
