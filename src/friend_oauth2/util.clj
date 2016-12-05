(ns friend-oauth2.util
  (:require [cemerick.url :as url]
            [clojure.data.json :as json]
            [clojure.string :as string]
            [clojure.tools.logging :as log]
            [crypto.random :as random]
            [ring.util.codec :as ring-codec]))

(defn get-domain
  "From a parsed URL object, construct a domain string."
  [parsed-url]
  (let [port (:port parsed-url)]
    (if port
      (format "%s://%s:%s"
                (:protocol parsed-url)
                (:host parsed-url)
                port)
      (format "%s://%s"
                (:protocol parsed-url)
                (:host parsed-url)))))

(defn parse-url
  "Parse a URL into the two parts needed by legacy `friend-oauth2` code."
  [uri]
  (let [parsed-url (url/url uri)
        domain (get-domain parsed-url)]
    [domain (:path parsed-url)]))

(defn format-config-uri
  "Formats URI from domain and path pairs in a map"
  [{{:keys [domain path]} :callback}]
  (str domain path))

(defn format-authn-uri
  "Formats the client authentication uri"
  [{{:keys [query url]} :authentication-uri} anti-forgery-token]
  (log/trace "query:" query)
  (log/trace "url:" url)
  (log/trace "anti-forgery-token:" anti-forgery-token)
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
  (log/debug "Got body:\n" body)
  (-> body
      (json/read-str :key-fn keyword)
      :access_token))

(defn get-access-token-from-params
  "Alternate function to allow retrieve
   access_token when passed in as form params."
  [{body :body}]
  (log/debug "Got body:\n" body)
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
