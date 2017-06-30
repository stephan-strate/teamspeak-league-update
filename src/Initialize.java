import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.*;
import com.github.theholywaffle.teamspeak3.api.reconnect.ConnectionHandler;
import com.github.theholywaffle.teamspeak3.api.reconnect.ReconnectStrategy;
import requests.RiotApi;
import settings.Configuration;

import java.util.logging.Level;

/**
 *  This bot registers an onclientJoin Event, thats
 *  causing the bot to update the users League of Legends Solo/Duo Queue Tier.
 *
 *  @author   Stephan Strate (development@famstrate.com)
 *  @version  2.0.0 (last update: 15.06.2017)
 */
public class Initialize {

    // client id !important
    private static volatile int clientId;

    /**
     *  Starting the bot and calling the two main functions
     *  in {@link Initialize}.
     *
     *  @param args  no description needed
     */
    public static void main (String[] args) {
        String[] names = { "RIOTGAMESAPIKEY", "TEAMSPEAKHOST", "TEAMSPEAKPORT", "TEAMSPEAKQUERYNAME", "TEAMSPEAKQUERYPASS",
                "TEAMSPEAKVIRTUALID", "BOTNAME", "BOTREADYMSG", "BOTCHANNELID", "MYSQLHOST", "MYSQLPORT", "MYSQLDB", "MYSQLUSER", "MYSQLPASS" };
        Configuration configuration = new Configuration("config.properties", names);

        RiotApi riotApi = new RiotApi(configuration.get("RIOTGAMESAPIKEY"), "euw1");
        System.out.println(riotApi.getLeagueBySummonerId(riotApi.getIdBySummonerName("HDG metrickstar")));

        final TS3Config config = new TS3Config();
        // host (ip-adress !important)
        config.setHost(Config.TS_HOST);
        // debug level, displays messages in console
        config.setDebugLevel(Level.ALL);

        // reconnecting after server restarts
        config.setReconnectStrategy(ReconnectStrategy.exponentialBackoff());
        config.setConnectionHandler(new ConnectionHandler() {
            @Override
            public void onConnect(TS3Query ts3Query) {
                // server query login at every reconnect
                queryLogin(ts3Query.getApi());
            }

            @Override
            public void onDisconnect(TS3Query ts3Query) {
                // we don't do anything
            }
        });

        // connecting
        final TS3Query query = new TS3Query(config);
        query.connect();

        // functionality
        start(query.getApi());
    }


    /**
     *  Login into the server query account
     *  thats defined in {@link Config}.
     *
     *  @param api  Teamspeak 3 api
     */
    private static void queryLogin (TS3Api api) {
        api.login(Config.TS_QUERYNAME, Config.TS_QUERYPASS);                // username, password
        api.selectVirtualServerById(Config.TS_VIRTUALSERVERID);             // virtualserver id
        api.setNickname(Config.BOT_NAME);                                   // nickname
        api.sendChannelMessage(Config.BOT_CHANNEL, Config.BOT_READY_MSG);   // channel + message

        // register "Server" and "Text-Channel" events
        api.registerEvent(TS3EventType.SERVER);
        api.registerEvent(TS3EventType.TEXT_CHANNEL);

        // update clientId
        clientId = api.whoAmI().getId();
    }


    /**
     *  onClientJoin and onTextMessage Event register. Merging
     *  all functions together.
     *
     *  @param api  Teamspeak 3 api
     */
    private static void start (final TS3Api api) {
        // working with events
        api.addTS3Listeners(new TS3EventAdapter() {

            /**
             * description: Requesting and comparing server groups and at last
             * assign and remove them.
             *
             * @param e (event)
             */
            @Override
            public void onClientJoin (ClientJoinEvent e) {
                // requesting League of Legends unique identifier from MySQL databse
                long leagueIdentifier = Update.databaseIdentifier(e.getUniqueClientIdentifier());

                // initializing serverTier with UNKNOWN
                int serverTier = LeagueTier.UNKNOWN;
                // if unique identifier isn't UNKNOWN, requesting League of Legends Solo/Duo Queue Tier from riot api
                if (leagueIdentifier != Config.UNKNOWN) {
                    serverTier = Update.serverTier(leagueIdentifier);
                } else {
                    // if someone isn't in database
                    api.pokeClient(e.getClientId(), "Du hast deinen Summoner Namen noch nicht hinterlegt!");
                    api.pokeClient(e.getClientId(), "Dazu musst du in den Channel 'Wer ist Nocturne?' und !name deinSummonerName in den Chat schreiben.");
                }

                // initializing variable
                int serverServerGroup;

                // filter server group out of String
                if (serverTier == LeagueTier.UNRANKED) {
                    serverServerGroup = ServerGroup.UNRANKED;
                } else if (serverTier == LeagueTier.BRONCE) {
                    serverServerGroup = ServerGroup.BRONCE;
                } else if (serverTier == LeagueTier.SILVER) {
                    serverServerGroup = ServerGroup.SILVER;
                } else if (serverTier == LeagueTier.GOLD) {
                    serverServerGroup = ServerGroup.GOLD;
                } else if (serverTier == LeagueTier.PLATINUM) {
                    serverServerGroup = ServerGroup.PLATINUM;
                } else if (serverTier == LeagueTier.DIAMOND) {
                    serverServerGroup = ServerGroup.DIAMOND;
                } else if (serverTier == LeagueTier.MASTER) {
                    serverServerGroup = ServerGroup.MASTER;
                } else if (serverTier == LeagueTier.CHALLENGER) {
                    serverServerGroup = ServerGroup.CHALLENGER;
                } else { serverServerGroup = ServerGroup.UNKNOWN; }


                // initializing some variables
                int clientTier;
                int clientServerGroup;

                // getting clients server groups as a string
                String clientServerGroups = e.getClientServerGroups();

                // string analysis, assign server group (delete later) and tier (comparison)
                if (clientServerGroups.contains(ServerGroup.UNRANKED + "")) {
                    clientTier = LeagueTier.UNRANKED;
                    clientServerGroup = ServerGroup.UNRANKED;
                } else if (clientServerGroups.contains(ServerGroup.BRONCE + "")) {
                    clientTier = LeagueTier.BRONCE;
                    clientServerGroup = ServerGroup.BRONCE;
                } else if (clientServerGroups.contains(ServerGroup.SILVER + "")) {
                    clientTier = LeagueTier.SILVER;
                    clientServerGroup = ServerGroup.SILVER;
                } else if (clientServerGroups.contains(ServerGroup.GOLD + "")) {
                    clientTier = LeagueTier.GOLD;
                    clientServerGroup = ServerGroup.GOLD;
                } else if (clientServerGroups.contains(ServerGroup.PLATINUM + "")) {
                    clientTier = LeagueTier.PLATINUM;
                    clientServerGroup = ServerGroup.PLATINUM;
                } else if (clientServerGroups.contains(ServerGroup.DIAMOND + "")) {
                    clientTier = LeagueTier.DIAMOND;
                    clientServerGroup = ServerGroup.DIAMOND;
                } else if (clientServerGroups.contains(ServerGroup.MASTER + "")) {
                    clientTier = LeagueTier.MASTER;
                    clientServerGroup = ServerGroup.MASTER;
                } else if (clientServerGroups.contains(ServerGroup.CHALLENGER + "")) {
                    clientTier = LeagueTier.CHALLENGER;
                    clientServerGroup = ServerGroup.CHALLENGER;
                } else { clientTier = ServerGroup.UNKNOWN; clientServerGroup = ServerGroup.UNKNOWN; }


                // assign/remove new/old server group (if it's different)
                if (clientTier != serverTier) {
                    int ivoker = e.getClientDatabaseId();

                    api.removeClientFromServerGroup(clientServerGroup, ivoker);
                    api.addClientToServerGroup(serverServerGroup, ivoker);

                    System.out.println(">> League update for " + e.getClientNickname() +
                            " from groupID " + clientServerGroup + " to " + serverServerGroup);
                }
            }


            /**
             *  register text messages in bot's channel and answering
             *  to specific types of messages. Updating League of Legends Summoner
             *  name on "!name [SUMMONER]" in MySQL database.
             *
             *  @param e  event track
             */
            @Override
            public void onTextMessage(TextMessageEvent e) {
                // only react to channel messages not sent by the query itself
                if (e.getTargetMode() == TextMessageTargetMode.CHANNEL && e.getInvokerId() != clientId) {
                    String message = e.getMessage().toLowerCase();

                    if (message.equals("!ping")) {
                        // simple ping test
                        api.sendChannelMessage("Anwesend");

                    } else if (message.equals("!help")) {
                        // a bot can't help
                        api.sendChannelMessage("Ich kann dir nicht helfen!");

                    } else if (message.equals("!name"))  {
                        // short explanation for the name system
                        api.sendChannelMessage("Um deinen League of Legends in unser System einzutragen, schreibe " +
                                "einfach '!name [League of Legends Name]' und ersetzt [League of Legends Name] durch deinen Namen.");

                    } else if (message.startsWith("!name ")) {
                        // format !name {League of Legends name}
                        String leagueName = message.substring(6);

                        // check if name is spelled right, or riot server is down
                        if (Update.editDatabaseIdentifier(leagueName, e.getInvokerUniqueId(), Update.leagueIdentifier(leagueName))) {
                            api.sendChannelMessage("[b]" + e.getInvokerName() +
                                    "[/b] dein League of Legends Name [i]" + leagueName + "[/i] wurde erfolgreich aktualisiert!");
                        } else {
                            api.sendChannelMessage("Es gab ein Problem bei der Aktualisierung! Entweder du hast deinen Namen " +
                                    "falsch geschrieben oder die Riot Server sind nicht zu erreichen.");
                        }

                    } else if (message.startsWith("!")) {
                        // after all equals, error message
                        api.sendChannelMessage("Konnte diesen Befehl nicht finden.");

                    }
                }
            }
        });
    }
}