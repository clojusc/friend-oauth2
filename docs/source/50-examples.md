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
This can be done with provided `lein` aliases:

* `lein appdotnet`
* `lein facebook`
* `lein github`
* `lein google`
