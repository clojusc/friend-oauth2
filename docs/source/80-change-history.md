# Change History


## 0.1.3 & 0.1.3-transitional

The Clojusc 0.1.3 release of `friend-oauth2` is exactly the same as
Dave Della Costa's 0.1.3 release, with the exception that the group ID has been
changed to `clojusc`. All the code, all of the deps, etc., are exactly the
same. This is tagged in Github as [0.1.3-transitional][0.1.3-transitional].
This is provided as a convenience to developers that wish to switch to the new,
supported org for `friend-oauth2`, but don't want to take on any burdens of
upgrade maintenance at this time.


## 0.1.4

Dave made a series of changes and brought in PRs when he was still maintaining
the project that hadn't gotten a release -- those are included in 0.1.4. Also,
there were some community contributions made after that time that have also
been pulled in.


## 0.1.5

When Clojusc took over maintenance of `friend-oauth2`, one of the things we
wanted to do was run CI tasks against several versions of the JVM as well as
Clojure itself. 0.1.5 introduces this capability in Travis CI with a new build
matrix.


## 0.2.0

The 0.2.0 release integrated the wiki documentation, the Codox docs, the
README, and the examples from the previously separate `friend-oauth2-examples`
repo.


## 0.3.0

This release introduces a new (and fully backwards-compatible) way of
configuring OAuth2 clients using records. Also note that a new dependency was
introdued that, due to its use of reader conditionals, only supports Clojure
version 1.7 and higher.


[0.1.3-transitional]: https://github.com/clojusc/friend-oauth2/releases/tag/0.1.3-transitional
