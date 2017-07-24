# teamspeak-league-update

## Features

- Based on the most powerful Teamspeak 3 java wrapper
- Assigns matching League of Legends tier server groups for every client
- Easy to set up

## Getting Started

### Usage

#### Download
You can download the latest version [2.0.0]() via GitHub.<br />
Older versions aren't available anymore, due riots api changes.

#### What do I need?

1. MySQL database
2. Server or computer the bot can run on (most likely the server your ts is running on)

#### Starting the bot

1. Run `teamspeak-league-update.jar` on your server
2. Fill up the first configuration file `config.properties`
3. Start `teamspeak-league-update.jar` again
4. Fill up the second configuration file `servergroups.properties`
5. Start `teamspeak-league-update.jar` again
6. Fill up the third and last configuration file `messages.properties`
7. Now you are ready to go to start `teamspeak-league-update.jar`

#### Configuration

`config.properties`<br />
In this file, you can configure the main features. You need to fill up all properties.<br />
If you have any questions on how to configure the bot, let me know in _Issues_ section.
```
#teamspeak-league-update properties
#Sat Jul 01 19:48:12 CEST 2017
TEAMSPEAKHOST= { Teamspeak 3 server ip adress }
MYSQLPASS= { MySQL database password }
TEAMSPEAKPORT= { Teamspeak 3 server port }
MYSQLHOST= { MySQL database ip adress }
TEAMSPEAKVIRTUALID= { Teamspeak 3 virtual sever id }
TEAMSPEAKQUERYPASS= { Teamspeak 3 server query password }
MYSQLPORT= { MySQL database port }
RIOTGAMESAPIKEY= { Riot Games api key }
BOTREADYMSG= { Ready message of bot }
BOTNAME= { Name of bot }
MYSQLUSER= { MySQL database username }
TEAMSPEAKQUERYNAME= { Teamspeak 3 server query name }
MYSQLDB= { MySQL database name }
BOTCHANNELID= { Channel where the bot is reachable }
REGION= { Region you want to setup your bot for }
```
We recommend your MySQL database to be only reachable **localy**, to avoid problems.
<br />
<br />

`servergroups.properties`<br />
You need to provide your servergroup id of your representing League of Legends tiers.
```
#teamspeak-league-update properties
#Sat Jul 01 19:50:19 CEST 2017
UNRANKED= { Servergroup id }
MASTER= { Servergroup id }
GOLD= { Servergroup id }
SILVER= { Servergroup id }
CHALLENGER= { Servergroup id }
DIAMOND= { Servergroup id }
BRONCE= { Servergroup id }
PLATINUM= { Servergroup id }
```
<br />

`messages.properties`<br />
Here you can customize all messages of the bot.
```
#teamspeak-league-update properties
#Tue Jul 11 18:04:57 CEST 2017
ERROR= { name could not be updated }
COMMAND= { could not find the command }
SUCCESS= { your name got updated successfully }
NAMEEXPLANATION= { you can change your account name by !name [your name] }
NOTINDATABASE= { reminding message, that name is not in database }
HELP= { can not help you }
REMINDER= { true / false }
```

### Development

You may use my repository for non-commercial as well as commercial purposes.<br />
But there is no warranty or guaranteed support.

#### Dependencies

- [Teamspeak-3-Java-API](https://github.com/TheHolyWaffle/TeamSpeak-3-Java-API) by [_TheHolyWaffle_](https://github.com/TheHolyWaffle) at [1.0.14](https://github.com/TheHolyWaffle/TeamSpeak-3-Java-API/releases/tag/v1.0.14)
- [MySQL Connector](https://mvnrepository.com/artifact/mysql/mysql-connector-java) at [5.1.40](https://mvnrepository.com/artifact/mysql/mysql-connector-java/5.1.40)
- [JSON simple](https://mvnrepository.com/artifact/com.googlecode.json-simple/json-simple) at [1.1.1](https://mvnrepository.com/artifact/com.googlecode.json-simple/json-simple/1.1.1)

## What does it do?

### Nocturne

You can add your name to the database with a simple text message
```!name [LEAGUE OF LEGENDS INGAME NAME]``` into the configured channel.<br />
With the same command, you can change your account.<br />
<p>You don't need to update your ingame name after you changed it, because
the bot stores your account id.</p>


## Questions or bugs?

If you have questions or you found a bug, just let me know [here](https://github.com/stephan-strate/teamspeak-league-update/issues).