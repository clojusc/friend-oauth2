# friend-oauth2

friend-oauth2 is an oauth2 workflow for [Friend][1].

[Working examples][2] have been implemented for [app.net's OAuth2](https://github.com/appdotnet/api-spec/blob/master/auth.md), [Facebook's server-side authentication](https://developers.facebook.com/docs/authentication/server-side/), and [Github's OAuth2](http://developer.github.com/v3/oauth/).

## Installation

```clojure
[friend-oauth2 "0.1.0"]
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

4. `access-token-parsefn` is a provider-specific function which parses the access_token response and returns just the access_token. If your OAuth2 provider does not follow the RFC (http://tools.ietf.org/html/draft-ietf-oauth-v2-31#section-5.1, ("in the entity body of the HTTP response using the "application/json" media type as defined by [RFC4627]") then you can pass in a custom function to parse the access_token response.

    Note that there is an alternate function (`get-access-token-from-params`) supplied to handle the common case where an access_token is provided as parameters in the callback request. Simply set the `:access-token-parsefn get-access-token-from-params`   See the [Facebook and Github examples][2] for reference.

5. Because OAuth2 is technically a protocol for obtaining 3rd-party **authorization** of resources, `credential-fn` behaves differently than in other workflows: it allows you to intercept the access-token at the end of the 3rd-party authentication process and inject your own functionality.  This is where you would do something like associate the 3rd-party's authorization with a user or roles in your own system, for example.

## Testing

friend-oauth2 uses Midje (https://github.com/marick/Midje) for testing.  You can run all the tests by starting up a repl, running `use 'midje.repl` and running `autotest`, or run `lein midje :autotest` on the command line.

## To-do:

* Move client_id/client_secret to Authorization header (necessary? Good for security or immaterial? Does FB support this?) (http://tools.ietf.org/html/draft-ietf-oauth-v2-31#section-2.3)

## License

Distributed under the MIT License (http://dd.mit-license.org/)

[1]: https://github.com/cemerick/friend
[2]: https://github.com/ddellacosta/friend-oauth2-examples
