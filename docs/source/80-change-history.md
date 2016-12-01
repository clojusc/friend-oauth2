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

This release integrates the wiki documentation, the Codox docs, and the README.


[0.1.3-transitional]: https://github.com/clojusc/friend-oauth2/releases/tag/0.1.3-transitional
