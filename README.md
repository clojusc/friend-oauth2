# friend-oauth2

friend-oauth2 is an oauth2 workflow for Chas Emerick's [Friend][1] library.

[Examples][2] have been implemented for [app.net's OAuth2](https://github.com/appdotnet/api-spec/blob/master/auth.md) as well as [Facebook's server-side authentication](https://developers.facebook.com/docs/authentication/server-side/).

## Installation

```clojure
[friend-oauth2 "0.0.2"]
```

Obviously requires [Friend][1].

## Documentation

For now, the best reference is the [Friend-OAuth2 examples][2]. Also please refer to the [Friend README][1].

Check out the ring-app handlers in the examples for some examples of how authentication and authorization routes are set up per Friend's config.

### Writing your own handler.

See the one of the [example handlers][2] (appdotnet_handler.clj or facebook_handler.clj) for an example.

A brief description of the necessary configuration:

1. `client-config` holds the basic information which changes from app-to-app regardless of the provider: client-id, client-secret, and the applications callback url.

2. The `redirect-uri` map holds the provider-specific configuration for the initial redirect to the OAuth2 provider (the user-facing GET request).

3. The `access-token-uri` map holds the provider-specific configuration for the access_token request, after the code is returned from the previous redirect (a server-to-server POST request).

4. `access-token-parsefn` is a provider-specific function which parses the body of the access_token response.  This is necessary because different providers return different formats; for example, app.net returns the access_token in JSON format, whereas Facebook provides this as a query string.

5. `config-auth` ...incomplete, TODO.

## Changelog 0.2.0

* Added tests! Refactored!
* Made access-token-parsefn optional, as set up to follow spec ()
* tweaked naming scheme for config ()

## TODO:

* Handle exceptions/errors after redirect and access_token request.
* Add a better authorization scheme (in terms of authorization and auth-map settings), preferably one which integrates Friend's credential-fn when the access_token is received.
* Add 'state' parameter by default in redirect/access_token parameters.
* Move client_id/client_secret to Authorization header (necessary? Good for security or immaterial? Does FB support this?)
* What's that thing I'm getting on the end of my url when I log in via FB ("#_=_")? Fix.

## License

Distributed under the MIT License (http://dd.mit-license.org/)

[1]: https://github.com/cemerick/friend
[2]: https://github.com/ddellacosta/friend-oauth2-examples
