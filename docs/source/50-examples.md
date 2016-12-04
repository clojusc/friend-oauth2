# Examples

The friend-oauth2 project includes the following examples:

 * [App.net](https://developers.app.net/reference/authentication/)
 * [Facebook (server-side authentication)](https://developers.facebook.com/docs/facebook-login/manually-build-a-login-flow/)
 * [Github](https://developer.github.com/v3/oauth/)
 * [Google OAuth 2.0 Login](https://developers.google.com/accounts/docs/OAuth2Login)


## Configuration

The examples make use of the following environment variables:

* `OAUTH2_CLIENT_ID`
* `OAUTH2_CLIENT_SECRET`
* `OAUTH2_CALLBACK_URL`


## Running

Once the environment variables are set, you will be ready to run the examples.
There are two sets of examples, one set for each type of configuration support
`friend-oauth2` provides (see [configuration documentation][config docs] for
more details).

### Record-based Configuration

The examples configured using the new records support can be run with the
following `lein` aliases:

* `lein appdotnet`
* `lein facebook`
* `lein github`
* `lein google`

Note that, under the hood, the record-based configuration is
backwards-compatible with the legacy configuration.


### Legacy Configuration

The examples configured using the original configuration mechanism can be run
with the  following `lein` aliases:

* `lein legacy-appdotnet`
* `lein legacy-facebook`
* `lein legacy-github`
* `lein legacy-google`

[config docs]: http://clojusc.github.io/friend-oauth2/current/20-configurtion.html
