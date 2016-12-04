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
`friend-oauth2.config/Client` record. The `Client` record defines the following
fields:

* `client-id`
* `client-secret`
* `response-type`
* `redirect-uri`
* `scope`
* `state`
* `access-type`
* `prompt`
* `login-hint`
* `include-granted-scopes`
* `code`
* `grant-type`
* `adnview`
* `allow-signup`

We recommend using the constructor `friend-oauth2.config/new` which will
populate a client configuration record with default values when not specified.

Backwards compatibility with the legacy `friend-oauth2` configuration is
facilitated by several configuration utility functions that perform appropriate
tranformations from the record-based approach.


### Sources

The fields for the record above are the combination of the fields taken from
the OAuth2 services supported by `friend-oauth2`. Each is covered below in its
own sub-section.

#### App.net

[App.net's OAuth2 service][app.net service] supports the following query
string parameters for the authorization phase:

* `client_id`
* `response_type`
* `redirect_uri`
* `scope`
* `state`
* `adnview`

And it supports the following for access code exchange:

* `code`
* `client_id`
* `client_secret`
* `redirect_uri`
* `grant_type`


#### Facebook

[Facebook's OAuth2 service][facebook service] supports the following query
string parameters for the authorization phase:

* `client_id`
* `response_type`
* `redirect_uri`
* `scope`
* `state`

And it supports the following for access code exchange:

* `code`
* `client_id`
* `client_secret`
* `redirect_uri`


#### Github

[Github's OAuth2 service][github service] supports the following query
string parameters for the authorization phase:

* `client_id`
* `redirect_uri`
* `scope`
* `state`
* `allow_signup`

And it supports the following for access code exchange:

* `code`
* `client_id`
* `client_secret`
* `redirect_uri`
* `state`


#### Google

[Google's OAuth2 service][google oauth2 service] supports the following query
string parameters for the authorization phase:

* `response_type`
* `client_id`
* `redirect_uri`
* `scope`
* `state`
* `access_type`
* `prompt`
* `login_hint`
* `include_granted_scopes`


And it supports the following for access code exchange:

* `code`
* `client_id`
* `client_secret`
* `redirect_uri`
* `grant_type`


[app.net service]: https://developers.app.net/reference/authentication/flows/web/#server-side-flow
[facebook service]: https://developers.facebook.com/docs/facebook-login/manually-build-a-login-flow
[github service]: https://developer.github.com/v3/oauth/
[google oauth2 service]: https://developers.google.com/identity/protocols/OAuth2WebServer
