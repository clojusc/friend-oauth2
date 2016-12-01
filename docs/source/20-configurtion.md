# Configuration Reference

The map passed to `oauth2/workflow` accepts various optional arguments, described below. See also the [example handlers][2] for working examples.


* `client-config` holds the basic information which changes from app to app, regardless of the provider: `:client-id`, `:client-secret`, and the application's callback url.
* The `authentication-uri` map holds the provider-specific configuration for the initial redirect to the OAuth2 provider (the user-facing GET request).
* The `access-token-uri` map holds the provider-specific configuration for the access_token request, after the code is returned from the previous redirect (a server-to-server POST request).
* `access-token-parsefn` is a provider-specific function which parses the access_token response and returns just the access_token (see below.)

If your OAuth2 provider does not follow the RFC
(http://tools.ietf.org/html/draft-ietf-oauth-v2-31#section-5.1, ("in
the entity body of the HTTP response using the "application/json"
media type as defined by [RFC4627]") then you can pass in a custom
function to parse the access_token response.  Note that there is an
alternate function (`get-access-token-from-params`) supplied to handle
the common case where an access_token is provided as parameters in the
callback request. Simply set the `:access-token-parsefn
get-access-token-from-params` See the
[Facebook and Github examples][2] for reference.
