(ns friend-oauth2.service
  (:require [friend-oauth2.workflow :as oauth2]))

(defn workflow
  ""
  [args service-cfg]
  (oauth2/workflow
    (assoc
      args
      :config
      (merge service-cfg (:config args)))))
