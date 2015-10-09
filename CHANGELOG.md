## Changelog 0.1.3 -> 0.1.4

* apply patch suggested in https://github.com/ddellacosta/friend-oauth2/issues/36 to prevent infinite redirects

## Changelog 0.1.0 -> 0.1.1

* (this time for reals) adds credential-fn for injecting your own functionality in the post-3rd-party-authentication stage. Thanks go to Kevin Lynagh (https://github.com/lynaghk) for this feature.

## Changelog 0.0.4 -> 0.1.0

* More refactoring of entire codebase.  Tests and code re-written to be more idiomatic Clojure.

## Changelog 0.0.3 -> 0.0.4

* Added default state parameter for CSRF protection.
* Refactoring of tests to use dummy app rather than directly call workflow function.
* Refactoring of code overall to be more idiomatic.

## Changelog 0.0.2 -> 0.0.3

* Nothing much, just updated versions of dependencies, and ensured tests are still passing.

## Changelog 0.0.1 -> 0.0.2

* Added tests! Refactored!
* A helper function has been added (`format-config-uri`) to configure the redirect url in the config.
* :redirect-uri in the uri-config has been renamed to :authentication-uri, as it more closely matches the RFC (and it actually makes sense)
* The access-token-parsefn functionality has been tweaked.  If the access-token is returned as defined in the spec (http://tools.ietf.org/html/draft-ietf-oauth-v2-31#section-5.1, as "application/json"), then it will automatically handle that.  Otherwise you can still pass in the access-token-parsefn to override, and it will use that.  See the [Facebook and Github examples][2] for reference.  **Note that this function also now takes the entire response, rather than just the body.**
