# Configuration Reference

There are two ways in which one may configure use of `friend-oauth2`:

* via two custom maps (`client-config` and `uri-config`), or
* via a single configuration record

The latter is a new method and should not be considered for production use
yet. For those applications that can take a chance on this new feature, we'd
be greatly appreciative for usage, testing in the wild, and any bug reports!


## Legacy Configuration

The map passed to `oauth2/workflow` accepts various optional arguments,
described below. See also the code in `examples/src` for working examples.
Here's some code from the Github example:

```clj
(def callback-url (System/getenv "OAUTH2_CALLBACK_URL"))
(def parsed-url (url/url callback-url))

(def client-config
  {:client-id (System/getenv "OAUTH2_CLIENT_ID")
   :client-secret (System/getenv "OAUTH2_CLIENT_SECRET")
   :callback {:domain (format "%s://%s:%s"
                        (:protocol parsed-url)
                        (:host parsed-url)
                        (:port parsed-url))
              :path (:path parsed-url)}})

(def uri-config
  {:authentication-uri {:url "https://github.com/login/oauth/authorize"
                        :query {:client_id (:client-id client-config)
                                :response_type "code"
                                :redirect_uri callback-url
                                :scope "user"}}
   :access-token-uri {:url "https://github.com/login/oauth/access_token"
                      :query {:client_id (:client-id client-config)
                              :client_secret (:client-secret client-config)
                              :grant_type "authorization_code"
                              :redirect_uri callback-url}}})
```

* `client-config` holds the basic information which changes from app to app,
  regardless of the provider: `:client-id`, `:client-secret`, and the
  application's callback url.
* The `authentication-uri` map holds the provider-specific configuration for
  the initial redirect to the OAuth2 provider (the user-facing GET request).
* The `access-token-uri` map holds the provider-specific configuration for
  the access_token request, after the code is returned from the previous
  redirect (a server-to-server POST request).
* `access-token-parsefn` is a provider-specific function which parses the
  access_token response and returns just the access_token (see below).

If your OAuth2 provider does not follow the RFC
(http://tools.ietf.org/html/draft-ietf-oauth-v2-31#section-5.1, ("in
the entity body of the HTTP response using the "application/json"
media type as defined by [RFC4627]") then you can pass in a custom
function to parse the access_token response.  Note that there is an
alternate function (`get-access-token-from-params`) supplied to handle
the common case where an access_token is provided as parameters in the
callback request. Simply set the `:access-token-parsefn
get-access-token-from-params` See the
Facebook and Github examples for reference.


## New Configuration

The new way of configuring `friend-oauth2` in an application makes use of the
`friend-oauth2.config/Client` record. Here is some example usage for creating
a new client configuration:

```clj
(require '[friend-oauth2.config :as config]
(config/client
  :scope "user"
  :auth-uri "https://github.com/login/oauth/authorize"
  :token-uri "https://github.com/login/oauth/access_token")
```

Note that if the `:client-id`, `:client-secret`, and `:redirect-uri` arguments
are not proivded, they are taken from the `OAUTH2_CLIENT_ID`,
`OAUTH2_CLIENT_SECRET`, and `OAUTH2_CALLBACK_URL` environment variables.

The `Client` record defines the following authentication fields:

* `client-id`
* `redirect-uri`
* `response-type`
* `scope`
* `state`
* `access-type`
* `prompt`
* `login-hint`
* `include-granted-scopes`
* `adnview`
* `allow-signup`

combined with the following token access fields:

* `client-secret`
* `code`
* `grant-type`

and the following service configuration fields:

* `auth-uri`
* `token-uri`

We recommend using the constructor `friend-oauth2.config/client` which will
populate a client configuration record with default values when not specified
in the constructor.

If they are not provided, they will be looked up in the system environment,
supplying those values instead.

and these are the defaults:

* `client-id`: `(System/getenv "OAUTH2_CLIENT_ID")`
* `client-secret`: `(System/getenv "OAUTH2_CLIENT_SECRET")`
* `redirect-uri`: `(System/getenv "OAUTH2_CALLBACK_URL")`
* `response-type`: `"code"`
* `grant-type`: `"authorization_code"`

and these are set to `nil` by default:

* `scope`
* `state`
* `access-type`
* `prompt`
* `login-hint`
* `include-granted-scopes`
* `adnview`
* `allow-signup`
* `auth-uri`
* `token-uri`

Backwards compatibility with the legacy `friend-oauth2` configuration is
facilitated by several configuration utility functions that perform appropriate
tranformations from the record-based approach.


## Service Configuration

If, instead of creating a service configuration from scratch, you chose to use
one of the [predefined `friend-oauth2` services][predefined services], the
steps required are fewer and a little bit different.

First of all, you won't call `friend-oauth2.workflow/workflow`; you'll call
`friend-oauth2.service.<name>/workflow` (e.g.,
`friend-oauth2.service.github/workflow`).

Secondly, you won't pass a map with `:config` set to an instance of
`friend-oauth2.config/Client`; you'll pass a map witb `:config` set to a simple
map data structure with just the bits you need (e.g., `{:scope "user"}`).

This is the approach used in the non-legacy examples.


## Configuration Sources

The fields for the record above are the combination of the fields taken from
the OAuth2 services supported by `friend-oauth2`. Each is covered below in its
own sub-section.


### App.net

[App.net's OAuth2 service][app.net service] supports the following query
string parameters for the authorization phase:

* `client_id`
* `response_type` (value `code`)
* `redirect_uri`
* `scope`
* `state`
* `adnview`

And it supports the following for access code exchange:

* `code`
* `client_id`
* `client_secret`
* `redirect_uri`
* `grant_type` (value `authorization_code`)


### Facebook

[Facebook's OAuth2 service][facebook service] supports the following query
string parameters for the authorization phase:

* `client_id`
* `response_type` (values `code`, `token`, `code%20token`, or
  `granted_scopes`)
* `redirect_uri`
* `scope`
* `state`

And it supports the following for access code exchange:

* `code`
* `client_id`
* `client_secret`
* `redirect_uri`


### Github

[Github's OAuth2 service][github service] supports the following query
string parameters for the authorization phase:

* `client_id`
* `redirect_uri`
* `scope`
* `state`
* `allow_signup` (values `true` or `false`)

And it supports the following for access code exchange:

* `code`
* `client_id`
* `client_secret`
* `redirect_uri`
* `state`


### Google

[Google's OAuth2 service][google oauth2 service] supports the following query
string parameters for the authorization phase:

* `response_type` (value `code`)
* `client_id`
* `redirect_uri`
* `scope`
* `state`
* `access_type` (values `online` and `offline`)
* `prompt` (values `none`, `consent`, or `select_account`)
* `login_hint`
* `include_granted_scopes` (values `true` or `false`)


And it supports the following for access code exchange:

* `code`
* `client_id`
* `client_secret`
* `redirect_uri`
* `grant_type` (value `authorization_code`)


[app.net service]: https://developers.app.net/reference/authentication/flows/web/#server-side-flow
[facebook service]: https://developers.facebook.com/docs/facebook-login/manually-build-a-login-flow
[github service]: https://developer.github.com/v3/oauth/
[google oauth2 service]: https://developers.google.com/identity/protocols/OAuth2WebServer
[predefined services]: https://github.com/clojusc/friend-oauth2/tree/master/src/friend_oauth2/service
