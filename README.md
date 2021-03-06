# teamspeak-league-update

A bot that registers every user that joins your Teamspeak 3 server and assigns the users League of Legends solo/duo/flex q tier matching server group.

## Features

- Based on the most powerful Teamspeak 3 api java wrapper
- Assigns matching League of Legends tier server groups for every client
- Easy to set up

## Setup

### Getting started

#### Download
You can download the latest version [3.0.2](https://github.com/stephan-strate/teamspeak-league-update/releases/tag/3.0.2) via GitHub.</br>
Older versions aren't available anymore, due riots api changes.

#### What do I need?
Nothing. You just need a server/computer to run the bot on (most likely the server your ts is running on).

#### Starting the bot
```
$ java -jar teamspeak-league-update.jar
```

#### Configuration

| Parameter | Value | Description |
| :--- | :--- | :--- |
| language | string <i>(de/en)</i> | Select the language that is used to notify your users (if enabled). <div style="color: red;">Currently not available!</div> |
| notification | boolean | Users that not added their League of Legends name to your database, will be notified with a private message on every reconnect. |
| hostAddress | string | Your server ip address or hostname. |
| port | int <i>(default: 9987)</i> | Your server port. |
| queryUsername | string | Server query username. |
| queryPassword | string | Server query password. |
| channelId | int | The bot needs a channel to rest. You can contact the bot in private chat with him or in this specific channel. |
| serverGroups | int | You will need to create Server groups like "Bronce", "Silver", .. that represent the League of Legends Tiers. |
| region | string | This is the region most of your users are placed in. Only accounts placed in this region will be considered. |
| apikey | string | This will be probably the hardest thing to get at the moment. You need to request a Riot Games api key at: https://developer.riotgames.com/ |

Use the following command to get more informations on how to configure your bot:
```
$ help
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