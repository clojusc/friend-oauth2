(ns friend-oauth2.util
  (:require [clojure.data.json :as json]
            [clojure.string :as string]
            [clojure.tools.logging :as log]
            [crypto.random :as random]
            [ring.util.codec :as ring-codec]))

(defn format-config-uri
  "Formats URI from domain and path pairs in a map"
  [{{:keys [domain path]} :callback}]
  (str domain path))

(defn format-authn-uri
  "Formats the client authentication uri"
  [{{:keys [query url]} :authentication-uri} anti-forgery-token]
  (log/error "query:" query)
  (log/error "url:" url)
  (log/error "anti-forgery-token:" anti-forgery-token)
  (->> (assoc query :state anti-forgery-token)
       ring-codec/form-encode
       (str url "?")))

(defn replace-authz-code
  "Formats the token uri with the authorization code"
  [{:keys [query]} code]
  (assoc-in query [:code] code))

;; http://tools.ietf.org/html/draft-ietf-oauth-v2-31#section-5.1
(defn extract-access-token
  "Returns the access token from a JSON response body"
  [{body :body}]
  (-> body
      (json/read-str :key-fn keyword)
      :access_token))

(defn get-access-token-from-params
  "Alternate function to allow retrieve
   access_token when passed in as form params."
  [{body :body}]
  (-> body
      ring.util.codec/form-decode
      (get "access_token")))

(defn extract-anti-forgery-token
  "Extracts the anti-csrf state key from the response"
  [{session :session}]
  (:state session))

(defn generate-anti-forgery-token
  "Generates random string for anti-forgery-token."
  []
  (string/replace (random/base64 60) #"[\+=/]" "-"))
