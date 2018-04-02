# Changelog

## [3.1.0](https://github.com/stephan-strate/teamspeak-league-update/releases/tag/3.1.0)

### Features

* **Database commands:** You will be able to print all users in console and get information about
specific users.
* **Language support:** The bot speaks your language *(currently en_US and de_DE)*.
You can contribute and add your translations (*src/lang_en_GB.properties* for example).
* **Database actuality:** The teamspeak nickname will be updated on every connect, so you can
easily identify your users.

### Bug Fixes

* Updating **teamspeak** settings on the fly causes a java error
* Missing properties in **properties.dat** causing unsolvable errors

## [3.0.2](https://github.com/stephan-strate/teamspeak-league-update/releases/tag/3.0.2) (30.03.2018)

### Bug Fixes

* **database connection:** closed databases too early, caused weird behaviour, closes [#3](https://github.com/stephan-strate/teamspeak-league-update/issues/3)

## [3.0.1](https://github.com/stephan-strate/teamspeak-league-update/releases/tag/3.0.1) (21.03.2018)

### Upgrading Instructions

To get this update, you need to manually download 3.0.1, because of a small bug in our upgrading process.

### Bug Fixes

* **database problems:** as usual some database problems got fixed, that caused weird behaviour
* **upgrading bug:** you now can upgrade to a newer version automatically

## [3.0.0](https://github.com/stephan-strate/teamspeak-league-update/releases/tag/3.0.0) (18.02.2018)

**Refactor release.** Everything is new, rewritten and rethought. Have fun with first usable production.

## Legacy versions

All versions **< 3.0.0** are not supported anymore.
Please use the newest version available to ensure most stability.