(ns friend-oauth2.fixtures
  (:require
   [friend-oauth2.workflow :as friend-oauth2]))

(def client-config-fixture
  {:client-id "my-client-id"
   :client-secret "my-client-secret"
   :callback {:domain "http://127.0.0.1" :path "/redirect"}})

(def uri-config-fixture
  {:authentication-uri {:url "http://example.com/authenticate"
                        :query {:client_id (:client-id client-config-fixture)
                                :redirect_uri (friend-oauth2/format-config-uri client-config-fixture)}}

   :access-token-uri {:url "http://example.com/get-access-token"
                      :query {:client_id (client-config-fixture :client-id)
                              :client_secret (client-config-fixture :client-secret)
                              :redirect_uri (friend-oauth2/format-config-uri client-config-fixture)
                              :code ""}}})

;; http://tools.ietf.org/html/draft-ietf-oauth-v2-31#section-5.1
(def access-token-response-fixture
  (ring.util.response/content-type
   (ring.util.response/response
    "{\"access_token\": \"my-access-token\"}")
   "application/json"))

(def identity-fixture
  {:identity "my-access-token", :access_token "my-access-token"})
