(ns friend-oauth2.helper-facts
  (:use
   midje.sweet
   friend-oauth2.fixtures)
  (:require
   [friend-oauth2.workflow :as friend-oauth2]
   [cemerick.friend :as friend]
   [ring.mock.request :as ring-mock]))

(fact
 "Extracts the access token from a JSON access token response"
 (friend-oauth2/extract-access-token access-token-response-fixture)
 => "my-access-token")

(fact
 "Returns nil if there is no code in the request"
 ;; No longer necessary since ring params/keyword-params handles this for us.
 ;; Not sure if this test is necessary anymore either, but leaving in for now.
 ;; (friend-oauth2/extract-code (ring-mock/request :get default-redirect))
 (-> (ring-mock/request :get "/redirect") :params :code)
 => nil)

(fact
 "Formats URI from domain and path pairs in a map"
 (friend-oauth2/format-config-uri client-config-fixture)
 => "http://127.0.0.1/redirect")

(fact
 "Formats the client authentication uri"
 (friend-oauth2/format-authentication-uri uri-config-fixture "anti-forgery-token")
 => "http://example.com/authenticate?redirect_uri=http%3A%2F%2F127.0.0.1%2Fredirect&client_id=my-client-id&state=anti-forgery-token")

(fact
 "Replaces the authorization code"
 ((friend-oauth2/replace-authorization-code (uri-config-fixture :access-token-uri) "my-code") :code)
 => "my-code")

(fact
 "Creates the auth-map for Friend with proper meta-data"
 (meta (friend-oauth2/make-auth identity-fixture))
 =>
 {:type ::friend/auth
  ::friend/workflow :email-login
  ::friend/redirect-on-auth? true})
