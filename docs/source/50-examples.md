# friend-oauth2 Examples

The friend-oauth2 project includes the following examples:

 * [Google OAuth 2.0 Login](https://developers.google.com/accounts/docs/OAuth2Login)
 * [Facebook (server-side authentication)](https://developers.facebook.com/docs/authentication/server-side/)
 * [App.net](https://github.com/appdotnet/api-spec/blob/master/auth.md)
 * [Github](http://developer.github.com/v3/oauth/)


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
