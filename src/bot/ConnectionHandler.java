package bot;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventType;
import com.github.theholywaffle.teamspeak3.api.reconnect.ReconnectStrategy;
import constants.Propertie;

import java.util.logging.Level;

public class ConnectionHandler {

    public TS3Query query;

    public ConnectionHandler () {
        final TS3Config config = new TS3Config();
        // host (ip-adress !important)
        config.setHost(Initialize.configuration.get(Propertie.TSHOST));
        // debug level, displays messages in console
        config.setDebugLevel(Level.WARNING);

        // reconnecting after server restarts
        config.setReconnectStrategy(ReconnectStrategy.exponentialBackoff());
        config.setConnectionHandler(new com.github.theholywaffle.teamspeak3.api.reconnect.ConnectionHandler() {
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
        query = new TS3Query(config);
        query.connect();
    }

    /**
     *  Login into the server query account.
     *
     *  @param api  Teamspeak 3 api
     */
    private static void queryLogin (TS3Api api) {
        System.out.println("Connecting with query account...");
        api.login(Initialize.configuration.get(Propertie.TSQUERYNAME), Initialize.configuration.get(Propertie.TSQUERYPASS));                          // username, password
        api.selectVirtualServerById(Integer.parseInt(Initialize.configuration.get(Propertie.TSVIRTUALID)));                                // virtualserver id
        api.setNickname(Initialize.configuration.get(Propertie.BOTNAME));                                                                  // nickname
        api.sendChannelMessage(Integer.parseInt(Initialize.configuration.get(Propertie.BOTCHANNEL)), Initialize.configuration.get(Propertie.BOTMSG)); // channel + message

        // register "Server" and "Text-Channel" events
        System.out.println("Register events...");
        api.registerEvent(TS3EventType.SERVER);
        api.registerEvent(TS3EventType.TEXT_CHANNEL);

        // update clientId
        Initialize.clientId = api.whoAmI().getId();
        System.out.println("Bot ready...");
    }
}