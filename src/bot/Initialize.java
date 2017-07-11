package bot;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.*;
import constants.Propertie;
import settings.Configuration;

/**
 * This bot registers an onclientJoin Event, thats
 * causing the bot to update the users League of Legends Solo/Duo Queue Tier.
 *
 * @author  Stephan Strate (development@famstrate.com)
 * @version 2.0.0 (last update: 15.06.2017)
 */
public class Initialize {

    // client id !important
    public static volatile int clientId;

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

        // opening connection to server
        bot.ConnectionHandler connectionHandler = new bot.ConnectionHandler();

        // functionality
        start(connectionHandler.query.getApi());
    }


    /**
     *  onClientJoin and onTextMessage Event register. Merging
     *  all functions together.
     *
     *  @param api  Teamspeak 3 api
     */
    private static void start (final TS3Api api) {
        ClientJoinHandler clientJoinHandler = new ClientJoinHandler();
        MessageHandler messageHandler = new MessageHandler();

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
                // checking the event and react
                clientJoinHandler.check(api, e);
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
                    // checking the message and react
                    messageHandler.check(api, e, message);
                }
            }
        });
    }
}