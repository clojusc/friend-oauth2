# friend-oauth2

[![Build Status][travis-badge]][travis]
[![Dependencies Status][deps-badge]][deps]
[![Clojars Project][clojars-badge]][clojars]
[![Clojure version][clojure-v]][project]

*OAuth2 workflow for Clojure/Ring friend framework*

[![][logo]][logo-large]


#### Contents

* [Latest Release](#latest-release-)
* [Introduction](#introduction-)
* [Documentation](#documentation-)
* [License](#license-)


## Latest Release [&#x219F;](#contents)

Current version on [clojars][clojars]:

```clojure
[clojusc/friend-oauth2 "0.2.0"]
```


## Introduction [&#x219F;](#contents)

friend-oauth2 is an OAuth2 ([site][oauth2 site], [RFC][oauth2 rfc]) workflow
for [Friend][friend url]. [Working examples][friend oauth2 examples] have been
implemented for [app.net's OAuth2][app.net's OAuth2],
[Facebook's server-side authentication][Facebook's auth], and
[Github's OAuth2][Github's OAuth2].


### New Maintainer!

**Where is [Dave Della Costa's version][Dave Della Costa's version]?** It's here!
The clojusc org is the new maintainer for `friend-oauth2`. However, there is
more to that answer, if you are asking about versions of releases:

* Dave's releases of `friend-oauth2` are here, everything 0.1.3 and below:
  https://github.com/clojusc/friend-oauth2/releases
* Clojusc releases of `friend-oauth2`: https://clojars.org/clojusc/friend-oauth2


### 0.1.3 & 0.1.3-transitional

What's more, the Clojuse 0.1.3 release of `friend-oauth2` is exactly the same as
Dave's 0.1.3 release, with the exception that the group ID has been changed to
`clojusc`. All the code, all of the deps, etc., are exactly the same. This is
tagged in Github as [0.1.3-transitional][0.1.3-transitional]. This is provided as
a convenience to developers that wish to switch to the new, supported org for
`friend-oauth2`, but don't want to take on any burdens of upgrade maintenance at
this time.


### 0.2.0

This release integrates the wiki documentation, the Codox docs, and the README.


## Documentation [&#x219F;](#contents)

Published `friend-oauth2` documentation:
 * [current version](http://clojusc.github.io/friend-oauth2/current/)

Other versions are also availabel there (see the "Other Versions" topic).

In additoin to generated documentation, the docs at that link also include
usasge, configuration, and testing instructions, among other topics.


## License [&#x219F;](#contents)

Copyright © 2012-2016, Dave Della Costa

Copyright © 2016, Clojure-Aided Enrinchment Center

Distributed under the MIT License (http://dd.mit-license.org/)

[oauth2 site]: https://oauth.net/2/
[oauth2 rfc]: https://tools.ietf.org/html/rfc6749
[friend url]: https://github.com/cemerick/friend
[friend oauth2 examples]: https://github.com/clojusc/friend-oauth2-examples
[app.net's OAuth2]: https://github.com/appdotnet/api-spec/blob/master/auth.md
[Facebook's auth]: https://developers.facebook.com/docs/authentication/server-side/
[Github's OAuth2]: http://developer.github.com/v3/oauth/
[docs]: https://github.com/clojusc/friend-oauth2/wiki
[Dave Della Costa's version]: https://github.com/ddellacosta/friend-oauth2/
[0.1.3-transitional]: https://github.com/clojusc/friend-oauth2/releases/tag/0.1.3-transitional

[travis]: https://travis-ci.org/clojusc/friend-oauth2
[travis-badge]: https://travis-ci.org/clojusc/friend-oauth2.png?branch=master
[deps]: http://jarkeeper.com/clojusc/friend-oauth2
[deps-badge]: http://jarkeeper.com/clojusc/friend-oauth2/status.svg
[logo]: resources/images/friend-oauth-logo-x250.png
[logo-large]: resources/images/friend-oauth-logo-x1000.png
[tag-badge]: https://img.shields.io/github/tag/clojusc/friend-oauth2.svg
[tag]: https://github.com/clojusc/friend-oauth2/tags
[clojure-v]: https://img.shields.io/badge/Clojure-1.5+-blue.svg
[project]: https://github.com/clojusc/friend-oauth2/blob/master/project.clj
[clojars]: https://clojars.org/clojusc/friend-oauth2
[clojars-badge]: https://img.shields.io/clojars/v/friend-oauth2.svg
