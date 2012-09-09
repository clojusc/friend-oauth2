# friend-oauth2

friend-oauth2 is an oauth2 workflow for Chas Emerick's [Friend][1] library.

This has been tested with app.net's [OAuth2](https://github.com/appdotnet/api-spec/blob/master/auth.md) as well as [Facebook's server-side authentication](https://developers.facebook.com/docs/authentication/server-side/).

## Prerequisites

* [Friend][1]
* [Leiningen.][2]
* [Compojure][3] (for handler examples).

[1]: https://github.com/cemerick/friend
[2]: https://github.com/technomancy/leiningen
[3]: https://github.com/weavejester/compojure

## Running the example handlers

Set up a application for app.net or Facebook, and in the appropriate handler file, set the client-id, client-secret, and callback per your application's configuration.  Tweak the project.clj.

To start a web server for the application, run:

    lein ring server(-headless)

That should be it.  Checkout the ring-app handlers for some examples of how authentication and authorization routes are set up per Friend's config.

## TODO:

* WRITE SOME F#*$ING TESTS
* Handle exceptions/errors after redirect and access_token request.
* Fix namespace, it is stupid as it is.
* Add a better authorization scheme, preferably one which integrates Friend's credential-fn when the access_token is received.
* Add 'state' parameter by default in redirect/access_token parameters.
* Move client_id/client_secret to Authorization header (necessary? Good for security or immaterial? Does FB support this?)
* What's that thing I'm getting on the end of my url when I log in via FB ("#_=_")? Fix.

## License

Distributed under the MIT License (http://dd.mit-license.org/)
