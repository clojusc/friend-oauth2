# friend-oauth2-examples

Use friend-oauth2 version 0.1.0.

Includes [Google OAuth 2.0 Login](https://developers.google.com/accounts/docs/OAuth2Login), [Facebook (server-side authentication)](https://developers.facebook.com/docs/authentication/server-side/), [App.net](https://github.com/appdotnet/api-spec/blob/master/auth.md) and [Github](http://developer.github.com/v3/oauth/) examples using [friend-oauth2](https://github.com/ddellacosta/friend-oauth2), an OAuth2 workflow for [Friend](https://github.com/cemerick/friend).

## Running

Configure your client id/secret and callback url in the handler code.

```clojure
(def client-config
  {:client-id "<HERE>"
   :client-secret "<HERE>"
   :callback {:domain "http://<HERE>" :path "/<AND HERE>"}})
```

At that point, you should be able to start it up using one of the aliases, which will load ring server in headless mode:

    lein appdotnet
    lein facebook
    lein github
    lein google

## License

Distributed under the MIT License (http://dd.mit-license.org/)

## Authors

* Facebook, App.net, Google API examples by [ddellacosta](https://github.com/ddellacosta)
* Github example by [kanej](https://github.com/kanej)
