# friend-oauth2

Current version on [clojars][clojars]:

```clojure
[friend-oauth2 "0.1.3"]
```

## Introduction

friend-oauth2 is an OAuth2 ([site][oauth2 site], [RFC][oauth2 rfc]) workflow
for [Friend][friend url]. [Working examples][friend oauth2 examples] have been
implemented for [app.net's OAuth2][app.net's OAuth2],
[Facebook's server-side authentication][Facebook's auth], and
[Github's OAuth2][Github's OAuth2].


## Installation and Usage

Please see the [detailed documentation in the wiki][docs] that
[pjlegato](https://github.com/pjlegato) wrote for details on configuration.

For some more examples, please check out the
[friend-oauth2 examples][friend oauth2 examples]. Also please refer to the
[Friend README][friend url].

Check out the ring-app handlers in the examples for some other examples of
how authentication and authorization routes are set up per friend's config.


## Contributing/Testing

Bug reports and pull requests are most welcome.  There are outstanding issues
that I could use development help with if you are interested in contributing.
If you find a critical bug I will do my best to take care of it quickly, and
of course in this case as well a pull request is most welcome.

friend-oauth2 uses Midje (https://github.com/marick/Midje) for testing.  You
can run all the tests by starting up a repl, running `use 'midje.repl` and
running `autotest`, or run `lein midje :autotest` on the command line.


## License

Copyright © 2012-2016, Dave Della Costa

Distributed under the MIT License (http://dd.mit-license.org/)

[oauth2 site]: https://oauth.net/2/
[oauth2 rfc]: https://tools.ietf.org/html/rfc6749
[friend url]: https://github.com/cemerick/friend
[friend oauth2 examples]: https://github.com/ddellacosta/friend-oauth2-examples
[app.net's OAuth2]: https://github.com/appdotnet/api-spec/blob/master/auth.md
[Facebook's auth]: https://developers.facebook.com/docs/authentication/server-side/
[Github's OAuth2]: http://developer.github.com/v3/oauth/
[docs]: https://github.com/ddellacosta/friend-oauth2/wiki
[clojars]: https://clojars.org/friend-oauth2
