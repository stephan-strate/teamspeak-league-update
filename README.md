# teamspeak-league-update
This bot registers an **onclientJoin Event**, thats causing the bot to update the users League of Legends Solo/Duo Queue Tier.

## Features

- Based on the most powerful java wrappers for Teamspeak 3 Server Query and
Riot Games
- Easy setup in Config.java

## Getting Started

### Dependencies

- [Teamspeak-3-Java-API](https://github.com/TheHolyWaffle/TeamSpeak-3-Java-API) by _TheHolyWaffle_ at [1.0.14](https://github.com/TheHolyWaffle/TeamSpeak-3-Java-API/releases/tag/v1.0.14)
- [riot-api-java](https://github.com/taycaldwell/riot-api-java) by _taycaldwell_ at [3.9.0](https://github.com/taycaldwell/riot-api-java/releases/tag/v3.9.0)
- [gson](https://github.com/google/gson) at [2.8.0](https://mvnrepository.com/artifact/com.google.code.gson/gson/2.8.0)
- [MySQL Connector](https://mvnrepository.com/artifact/mysql/mysql-connector-java) at [5.1.40](https://mvnrepository.com/artifact/mysql/mysql-connector-java/5.1.40)

### Configuration

1. Install all dependencies listed above.
2. Customize ```Config.java``` with your database and Teamspeak 3 configurations.
3. Setup MySQL database.
4. Run the project.

## Usage

### Nocturne

You can add your name to the database with a simple text message
```!name [LEAGUE OF LEGENDS INGAME NAME]``` into the configured channel.<br />
With the same command, you can change your account.<br />
<p><b>ATTENTION:</b> You don't need to update your ingame name after you changed it, because
the bot stores your account id.</p>


## Questions or bugs?

If you have questions or you found a bug, just let me know [here](https://github.com/stephan-strate/teamspeak-league-update/issues).