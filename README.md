# friend-oauth2

Current version on [clojars](https://clojars.org/friend-oauth2):

```clojure
[friend-oauth2 "0.1.3"]
```

friend-oauth2 is an oauth2 workflow for [Friend][1].

[Working examples][2] have been implemented for [app.net's OAuth2](https://github.com/appdotnet/api-spec/blob/master/auth.md), [Facebook's server-side authentication](https://developers.facebook.com/docs/authentication/server-side/), and [Github's OAuth2](http://developer.github.com/v3/oauth/).

## Installation and Usage

Please see the [detailed documentation in the wiki](https://github.com/ddellacosta/friend-oauth2/wiki) that [pjlegato](https://github.com/pjlegato) wrote for details on configuration.

For some more examples, please check out the  [friend-oauth2 examples][2]. Also please refer to the [Friend README][1].

Check out the ring-app handlers in the examples for some other examples of how authentication and authorization routes are set up per Friend's config.

## Contributing/Testing

Bug reports and pull requests are most welcome.  There are outstanding issues that I could use development help with if you are interested in contributing.  If you find a critical bug I will do my best to take care of it quickly, and of course in this case as well a pull request is most welcome.

friend-oauth2 uses Midje (https://github.com/marick/Midje) for testing.  You can run all the tests by starting up a repl, running `use 'midje.repl` and running `autotest`, or run `lein midje :autotest` on the command line.

## License

Distributed under the MIT License (http://dd.mit-license.org/)

[1]: https://github.com/cemerick/friend
[2]: https://github.com/ddellacosta/friend-oauth2-examples
