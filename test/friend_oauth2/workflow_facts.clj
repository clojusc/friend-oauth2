(ns friend-oauth2.workflow-facts
  (:use midje.sweet)
  (:require [friend-oauth2.workflow :as friend-oauth2]
            [cemerick.friend :as friend]
            [clj-http.client :as client]
            [ring.mock.request :as ring-mock]
            [cheshire.core :as j]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Configuration fixtures
;;

(def client-config-fixture
  {:client-id "my-client-id"
   :client-secret "my-client-secret"
   :callback {:domain "http://127.0.0.1" :path "/redirect"}})

(def uri-config-fixture
  {:access-token-uri {:url "http://example.com"
                      :query {:client_id (client-config-fixture :client-id)
                              :client_secret (client-config-fixture :client-secret)
                              :redirect_uri (friend-oauth2/format-config-url client-config-fixture)
                              :code ""}}})

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Stub for http post
;;

(background
 (client/post {:url "" :form-params {}}) =>
 (fn [& anything] {}))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Dummy responses/requests for various OAuth2 endpoints
;;

;; http://tools.ietf.org/html/draft-ietf-oauth-v2-31#section-4.1.2
(defn redirect-request-fixture
  [redirect-uri]
  (ring-mock/content-type
   (ring-mock/query-string
              (ring-mock/request :get redirect-uri)
              {:code "my-code"})
   "application/x-www-form-urlencoded"))

(def default-redirect "/redirect")

(defn redirect-with-default-redirect []
  (redirect-request-fixture default-redirect))
  
;; http://tools.ietf.org/html/draft-ietf-oauth-v2-31#section-5.1
(def access-token-response-fixture
  (ring.util.response/content-type
   (ring.util.response/response
    "{\"access_token\": \"my-access-token\"}")
   "application/json"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;;
;; Facts
;;

(fact
 "Extracts the access token from a JSON access token response"
 (friend-oauth2/extract-access-token access-token-response-fixture)
 => "my-access-token")

(fact
 "Extracts the code from the initial authorization request"
 (friend-oauth2/extract-code (redirect-with-default-redirect))
 => "my-code")

(fact
 "Returns nil if there is no code in the request"
 (friend-oauth2/extract-code (ring-mock/request :get default-redirect))
 => nil)

(fact
 "If there is a code in the request it posts to the token-url"
 ((friend-oauth2/workflow) redirect-with-default-redirect)
 (provided
  (client/post {:url "" :form-params {}}) => 1 :times 1))

(fact
 "Formats URL from domain and path pairs in a map"
 (friend-oauth2/format-config-url client-config-fixture)
 => "http://127.0.0.1/redirect")

(fact
 "Formats the token url with the authorization code"
 (get-in
  (friend-oauth2/format-token-url (uri-config-fixture :access-token-uri) "my-code")
  [:query :code])
 => "my-code")
