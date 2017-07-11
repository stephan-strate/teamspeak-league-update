package bot;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.*;
import com.github.theholywaffle.teamspeak3.api.reconnect.ConnectionHandler;
import com.github.theholywaffle.teamspeak3.api.reconnect.ReconnectStrategy;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;
import constants.League;
import constants.Propertie;
import requests.Mysql;
import requests.RiotApi;
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

    // public configurations
    public static Configuration configuration;
    public static Configuration servergroups;
    public static Configuration messages;

    /**
     *  Starting the bot and calling the two main functions
     *  in {@link Initialize}.
     *
     *  @param args  no description needed
     */
    public static void main (String[] args) {
        // all available configurations
        Propertie[] configProperties = { Propertie.API, Propertie.TSHOST, Propertie.TSPORT, Propertie.TSQUERYNAME, Propertie.TSQUERYPASS,
                Propertie.TSVIRTUALID, Propertie.BOTNAME, Propertie.BOTMSG, Propertie.BOTCHANNEL, Propertie.MYSQLHOST, Propertie.MYSQLPORT, Propertie.MYSQLDB,
                Propertie.MYSQLUSER, Propertie.MYSQLPASS, Propertie.REGION };
        // loading configurations
        configuration = new Configuration("config.properties", configProperties);

        // all available servergroups
        Propertie[] servergroupProperties = { Propertie.UNRANKED, Propertie.BRONCE, Propertie.SILVER, Propertie.GOLD, Propertie.PLATINUM, Propertie.DIAMOND,
                Propertie.MASTER, Propertie.CHALLENGER };
        // loading servergroups
        servergroups = new Configuration("servergroups.properties", servergroupProperties);

        // all available messages
        Propertie[] messageProperties = { Propertie.REMINDER, Propertie.NOTINDATABASE, Propertie.HELP, Propertie.NAME, Propertie.SUCCESS, Propertie.ERROR,
                Propertie.COMMAND };
        // loading messages
        messages = new Configuration("messages.properties", messageProperties);


        final TS3Config config = new TS3Config();
        // host (ip-adress !important)
        config.setHost(configuration.get(Propertie.TSHOST));
        // debug level, displays messages in console
        config.setDebugLevel(Level.WARNING);

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
        System.out.println("Connecting with query account...");
        api.login(configuration.get(Propertie.TSQUERYNAME), configuration.get(Propertie.TSQUERYPASS));                          // username, password
        api.selectVirtualServerById(Integer.parseInt(configuration.get(Propertie.TSVIRTUALID)));                                // virtualserver id
        api.setNickname(configuration.get(Propertie.BOTNAME));                                                                  // nickname
        api.sendChannelMessage(Integer.parseInt(configuration.get(Propertie.BOTCHANNEL)), configuration.get(Propertie.BOTMSG)); // channel + message

        // register "Server" and "Text-Channel" events
        System.out.println("Register events...");
        api.registerEvent(TS3EventType.SERVER);
        api.registerEvent(TS3EventType.TEXT_CHANNEL);

        // update clientId
        clientId = api.whoAmI().getId();
        System.out.println("Bot ready...");
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
                System.out.println();
                // requesting League of Legends unique identifier from MySQL databse
                long clientAccountId = Mysql.databaseIdentifier(e.getUniqueClientIdentifier());

                // initializing serverTier with UNKNOWN
                League accountLeague;
                // if unique identifier isn't UNKNOWN, requesting League of Legends Solo/Duo Queue Tier from riot api
                if (clientAccountId != 0) {
                    accountLeague = RiotApi.getLeagueBySummonerId(clientAccountId);
                    System.out.println("Users league from account: " + accountLeague.getName());
                } else {
                    if (Boolean.parseBoolean(messages.get(Propertie.REMINDER))) {
                        // if someone isn't in database
                        api.pokeClient(e.getClientId(), messages.get(Propertie.NOTINDATABASE));
                    }
                    return;
                }

                // initializing some variables
                League clientLeague;
                // getting client
                ClientInfo client = api.getClientInfo(e.getClientId());

                try {
                    if (client.isInServerGroup(Integer.parseInt(servergroups.get(Propertie.BRONCE)))) {
                        clientLeague = League.BRONCE;
                    } else if (client.isInServerGroup(Integer.parseInt(servergroups.get(Propertie.SILVER)))) {
                        clientLeague = League.SILVER;
                    } else if (client.isInServerGroup(Integer.parseInt(servergroups.get(Propertie.GOLD)))) {
                        clientLeague = League.GOLD;
                    } else if (client.isInServerGroup(Integer.parseInt(servergroups.get(Propertie.PLATINUM)))) {
                        clientLeague = League.PLATINUM;
                    } else if (client.isInServerGroup(Integer.parseInt(servergroups.get(Propertie.DIAMOND)))) {
                        clientLeague = League.DIAMOND;
                    } else if (client.isInServerGroup(Integer.parseInt(servergroups.get(Propertie.MASTER)))) {
                        clientLeague = League.MASTER;
                    } else if (client.isInServerGroup(Integer.parseInt(servergroups.get(Propertie.CHALLENGER)))) {
                        clientLeague = League.CHALLENGER;
                    } else {
                        clientLeague = League.UNRANKED;
                    }

                    System.out.println("Users league from teamspeak: " + clientLeague.getName());

                    // assign/remove new/old server group (if it's different)
                    if (!clientLeague.equals(accountLeague)) {
                        System.out.println("Leagues are different, changing it...");
                        int ivoker = e.getClientDatabaseId();

                        api.removeClientFromServerGroup(Integer.parseInt(servergroups.get(Propertie.getPropertieByName(clientLeague.getName()))), ivoker);
                        api.addClientToServerGroup(Integer.parseInt(servergroups.get(Propertie.getPropertieByName(accountLeague.getName()))), ivoker);

                        System.out.println("League update for " + e.getClientNickname() +
                                " from group " + clientLeague.getName() + " to " + accountLeague.getName() + "\n");
                    }
                } catch (NumberFormatException error) {
                    error.printStackTrace();
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
                        api.sendChannelMessage("Pong");

                    } else if (message.equals("!help")) {
                        // a bot can't help
                        api.sendChannelMessage(messages.get(Propertie.HELP));

                    } else if (message.equals("!name"))  {
                        // short explanation for the name system
                        api.sendChannelMessage(messages.get(Propertie.NAME));

                    } else if (message.startsWith("!name ")) {
                        System.out.println("Checking league name...");
                        // format !name {League of Legends name}
                        String leagueName = message.substring(6);

                        // check if name is spelled right, or riot server is down
                        if (Mysql.editDatabaseIdentifier(leagueName, e.getInvokerUniqueId(), RiotApi.getIdBySummonerName(leagueName))) {
                            api.sendChannelMessage("[b]" + e.getInvokerName() +
                                    "[/b] " + messages.get(Propertie.SUCCESS));
                        } else {
                            api.sendChannelMessage(messages.get(Propertie.ERROR));
                        }

                    } else if (message.startsWith("!")) {
                        // after all equals, error message
                        api.sendChannelMessage(messages.get(Propertie.COMMAND));

                    }
                }
            }
        });
    }
}