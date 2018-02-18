# teamspeak-league-update

A bot that registers every user that joins your Teamspeak 3 server and assigns the users League of Legends solo/duo/flex q tier matching server group.

## Features

- Based on the most powerful Teamspeak 3 api java wrapper
- Assigns matching League of Legends tier server groups for every client
- Easy to set up

## Setup

### Getting started

#### Download
You can download the latest version [3.0.0](https://github.com/stephan-strate/teamspeak-league-update/releases/tag/3.0.0) via GitHub.</br>
Older versions aren't available anymore, due riots api changes.

#### What do I need?
Nothing. You just need a server/computer to run the bot on (most likely the server your ts is running on).

#### Starting the bot
```
$ java -jar teamspeak-league-update.jar
```

### Development

You may use my repository for non-commercial as well as commercial purposes.</br>
But there is no warranty or guaranteed support.

#### Dependencies
- [teamspeak3-api](https://github.com/TheHolyWaffle/TeamSpeak-3-Java-API) by [_TheHolyWaffle_](https://github.com/TheHolyWaffle) at [1.0.14](https://github.com/TheHolyWaffle/TeamSpeak-3-Java-API/releases/tag/v1.0.14)
- [json-simple](https://mvnrepository.com/artifact/com.googlecode.json-simple/json-simple) at [1.1.1](https://mvnrepository.com/artifact/com.googlecode.json-simple/json-simple/1.1.1)
- [sqlite-jdbc](https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc) at [3.20.0](https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc/3.20.0)

## What does it do?

It assigns your highest queue rank (solo/duo or flex).
You can add your name to the database with a simple text message
```
!name [YOUR LEAGUE OF LEGENDS NAME]
```
With the same command, you can change your account.</br>
You don't need to update your ingame name after you changed it, because
the bot stores your account id.

## Questions or bugs?

If you have any questions or you found a bug, just let me know [here](https://github.com/stephan-strate/teamspeak-league-update/issues).