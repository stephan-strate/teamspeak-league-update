package bot;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.*;
import com.github.theholywaffle.teamspeak3.api.reconnect.ConnectionHandler;
import com.github.theholywaffle.teamspeak3.api.reconnect.ReconnectStrategy;
import constants.League;
import constants.Propertie;
import settings.Configuration;

import java.util.logging.Level;

/**
 * This bot registers an onclientJoin Event, thats
 * causing the bot to update the users League of Legends Solo/Duo Queue Tier.
 *
 * @author  Stephan Strate (development@famstrate.com)
 * @version 2.0.0 (last update: 15.06.2017)
 */
public class Initialize {

    // client id !important
    private static volatile int clientId;

    public static Configuration configuration;
    public static Configuration servergroups;

    public Configuration getConfiguration () {
        return configuration;
    }

    public Configuration getServergroups () {
        return servergroups;
    }

    /**
     *  Starting the bot and calling the two main functions
     *  in {@link Initialize}.
     *
     *  @param args  no description needed
     */
    public static void main (String[] args) {
        Propertie[] configProperties = { Propertie.API, Propertie.TSHOST, Propertie.TSPORT, Propertie.TSQUERYNAME, Propertie.TSQUERYPASS,
                Propertie.TSVIRTUALID, Propertie.BOTNAME, Propertie.BOTMSG, Propertie.BOTCHANNEL, Propertie.MYSQLHOST, Propertie.MYSQLPORT, Propertie.MYSQLDB,
                Propertie.MYSQLUSER, Propertie.MYSQLPASS, Propertie.REGION };
        configuration = new Configuration("config.properties", configProperties);

        Propertie[] servergroupProperties = { Propertie.UNRANKED, Propertie.BRONCE, Propertie.SILVER, Propertie.GOLD, Propertie.PLATINUM, Propertie.DIAMOND,
                Propertie.MASTER, Propertie.CHALLENGER };
        servergroups = new Configuration("servergroups.properties", servergroupProperties);


        final TS3Config config = new TS3Config();
        // host (ip-adress !important)
        config.setHost(configuration.get(Propertie.TSHOST));
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
     *  Login into the server query account.
     *
     *  @param api  Teamspeak 3 api
     */
    private static void queryLogin (TS3Api api) {
        api.login(configuration.get(Propertie.TSQUERYNAME), configuration.get(Propertie.TSQUERYPASS));                          // username, password
        api.selectVirtualServerById(Integer.parseInt(configuration.get(Propertie.TSVIRTUALID)));                                // virtualserver id
        api.setNickname(configuration.get(Propertie.BOTNAME));                                                                  // nickname
        api.sendChannelMessage(Integer.parseInt(configuration.get(Propertie.BOTCHANNEL)), configuration.get(Propertie.BOTMSG)); // channel + message

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
                League serverTier = League.UNRANKED;
                // if unique identifier isn't UNKNOWN, requesting League of Legends Solo/Duo Queue Tier from riot api
                if (leagueIdentifier != 0) {
                    serverTier = Update.serverTier(leagueIdentifier);
                    System.out.println("Users league: " + serverTier.getName());
                    System.out.println(League.SILVER.getName() + "");
                } else {
                    // if someone isn't in database
                    api.pokeClient(e.getClientId(), "Du hast deinen Summoner Namen noch nicht hinterlegt!");
                    api.pokeClient(e.getClientId(), "Dazu musst du in den Channel 'Wer ist Nocturne?' und !name deinSummonerName in den Chat schreiben.");
                }

                // initializing some variables
                int serverServerGroup = 0;
                try {
                    String temp = configuration.get(Propertie.getPropertieByName(serverTier.getName()));
                    serverServerGroup = Integer.parseInt(temp);
                } catch (NumberFormatException x) {
                    System.out.println("FAIL");
                }
                League clientTier;
                int clientServerGroup;

                // getting clients server groups as a string
                String clientServerGroups = e.getClientServerGroups();

                // string analysis, assign server group (delete later) and tier (comparison)
                if (clientServerGroups.contains(League.UNRANKED.getName() + "")) {
                    clientTier = League.UNRANKED;
                    clientServerGroup = Integer.parseInt(servergroups.get(Propertie.UNRANKED));
                } else if (clientServerGroups.contains(League.BRONCE.getName() + "")) {
                    clientTier = League.BRONCE;
                    clientServerGroup = Integer.parseInt(servergroups.get(Propertie.BRONCE));
                } else if (clientServerGroups.contains(League.SILVER.getName() + "")) {
                    clientTier = League.SILVER;
                    clientServerGroup = Integer.parseInt(servergroups.get(Propertie.SILVER));
                } else if (clientServerGroups.contains(League.GOLD.getName() + "")) {
                    clientTier = League.GOLD;
                    clientServerGroup = Integer.parseInt(servergroups.get(Propertie.GOLD));
                } else if (clientServerGroups.contains(League.PLATINUM.getName() + "")) {
                    clientTier = League.PLATINUM;
                    clientServerGroup = Integer.parseInt(servergroups.get(Propertie.PLATINUM));
                } else if (clientServerGroups.contains(League.DIAMOND.getName() + "")) {
                    clientTier = League.DIAMOND;
                    clientServerGroup = Integer.parseInt(servergroups.get(Propertie.DIAMOND));
                } else if (clientServerGroups.contains(League.MASTER.getName() + "")) {
                    clientTier = League.MASTER;
                    clientServerGroup = Integer.parseInt(servergroups.get(Propertie.MASTER));
                } else if (clientServerGroups.contains(League.CHALLENGER.getName() + "")) {
                    clientTier = League.CHALLENGER;
                    clientServerGroup = Integer.parseInt(servergroups.get(Propertie.CHALLENGER));
                } else {
                    clientTier = League.UNRANKED;
                    clientServerGroup = Integer.parseInt(servergroups.get(Propertie.UNRANKED));
                }


                // assign/remove new/old server group (if it's different)
                if (!clientTier.equals(serverTier)) {
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
                        System.out.println("Checking league name...");
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