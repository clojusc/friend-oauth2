(ns friend-oauth2.fixtures
  (:require
   [ring.util.response :refer [response content-type]]
   [friend-oauth2.util :as oauth2-util]))

(def client-config-fixture
  {:client-id "my-client-id"
   :client-secret "my-client-secret"
   :callback {:domain "http://127.0.0.1" :path "/redirect"}})

(def client-config-fixture-invalid
  {:client-id "my-client-id"
   :client-secret "my-client-secret"
   :callback {:domain "http://127.0.0.1" :path-MALFORMED "/redirect"}})

(def client-config-fixture-missing-field
  {:client-id "my-client-id"
   :callback {:domain "http://127.0.0.1" :path "/redirect"}})

(def uri-config-fixture
  {:authentication-uri {:url "http://example.com/authenticate"
                        :query {:client_id (:client-id client-config-fixture)
                                :redirect_uri (oauth2-util/format-config-uri client-config-fixture)}}

   :access-token-uri {:url "http://example.com/get-access-token"
                      :query {:client_id (client-config-fixture :client-id)
                              :client_secret (client-config-fixture :client-secret)
                              :redirect_uri (oauth2-util/format-config-uri client-config-fixture)}}})

;; http://tools.ietf.org/html/draft-ietf-oauth-v2-31#section-5.1
(def access-token-response-fixture
  (-> "{\"access_token\": \"my-access-token\"}"
      response
      (content-type "application/json")))

(def identity-fixture
  {:identity "my-access-token"
   :access_token "my-access-token"})
