# friend-oauth2

friend-oauth2 is an oauth2 workflow for Chas Emerick's [Friend][1] library.

[Working examples][2] have been implemented for [app.net's OAuth2](https://github.com/appdotnet/api-spec/blob/master/auth.md), [Facebook's server-side authentication](https://developers.facebook.com/docs/authentication/server-side/), and [Github's OAuth2](http://developer.github.com/v3/oauth/).

## Installation

```clojure
[friend-oauth2 "0.0.2"]
```

Obviously requires [Friend][1].

## Documentation

For now, the best reference is the [Friend-OAuth2 examples][2]. Also please refer to the [Friend README][1].

Check out the ring-app handlers in the examples for some examples of how authentication and authorization routes are set up per Friend's config.

### Configuring your handler.

(See the one of the [example handlers][2] (appdotnet_handler.clj, facebook_handler.clj or github_handler.clj) for working examples.)

A brief description of the necessary configuration:

1. `client-config` holds the basic information which changes from app-to-app regardless of the provider: client-id, client-secret, and the applications callback url.

2. The `authentication-uri` map holds the provider-specific configuration for the initial redirect to the OAuth2 provider (the user-facing GET request).

3. The `access-token-uri` map holds the provider-specific configuration for the access_token request, after the code is returned from the previous redirect (a server-to-server POST request).

4. `access-token-parsefn` is a provider-specific function which parses the access_token response and returns just the access_token. If your OAuth2 provider does not follow the RFC (http://tools.ietf.org/html/draft-ietf-oauth-v2-31#section-5.1) then you can pass in a custom function to parse the access-token response.  See the [Facebook and Github examples][2] for reference.

5. `config-auth` ...TBD...

## Changelog 0.1.0 -> 0.2.0

* Added tests! Refactored!
* A helper function has been added (`format-config-uri`) to configure the redirect url in the config.
* :redirect-uri in the uri-config has been renamed to :authentication-uri, as it more closely matches the RFC (and it actually makes sense)
* The access-token-parsefn functionality has been tweaked.  If the access-token is returned as defined in the spec (http://tools.ietf.org/html/draft-ietf-oauth-v2-31#section-5.1, as "application/json"), then it will automatically handle that.  Otherwise you can still pass in the access-token-parsefn to override, and it will use that.  See the [Facebook and Github examples][2] for reference.  **Note that this function also now takes the entire response, rather than just the body.**

## To-do:

* Handle exceptions/errors after redirect and access_token request.
* auth-map: should we be using the access-token as identity?  Are there any downsides to this, especially in terms of security?
* Add 'state' parameter by default. (http://tools.ietf.org/html/draft-ietf-oauth-v2-31#section-10.12)
* Move client_id/client_secret to Authorization header (necessary? Good for security or immaterial? Does FB support this?) (http://tools.ietf.org/html/draft-ietf-oauth-v2-31#section-2.3)
* What's that thing I'm getting on the end of my url when I log in via FB ("#_=_")? Fix.

## License

Distributed under the MIT License (http://dd.mit-license.org/)

[1]: https://github.com/cemerick/friend
[2]: https://github.com/ddellacosta/friend-oauth2-examples
