package com.strate.remote.teamspeak;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventType;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.reconnect.ReconnectStrategy;
import com.strate.Init;
import com.strate.constants.Ansi;

import java.util.logging.Level;

/**
 * <p>[description]</p>
 * @author Stephan Strate
 */
public class ConnectionHandler {

    public TS3Query query;

    /**
     * <p>[description]</p>
     */
    public ConnectionHandler () {
        final TS3Config config = new TS3Config();

        /* @TODO: Get host from database */

        config.setHost("ip");
        config.setDebugLevel(Level.WARNING);

        config.setReconnectStrategy(ReconnectStrategy.exponentialBackoff());
        config.setConnectionHandler(new com.github.theholywaffle.teamspeak3.api.reconnect.ConnectionHandler() {
            @Override
            public void onConnect(TS3Query ts3Query) {
                System.out.println(Ansi.BLUE + "[tlu] " + Ansi.GREEN + "Connected to server." + Ansi.RESET);
                queryLogin(ts3Query.getApi());
            }

            @Override
            public void onDisconnect(TS3Query ts3Query) {
                System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RED + "Disconnected from server." + Ansi.RESET);
                System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Waiting for server to get up again.");
            }
        });

        query = new TS3Query(config);
        query.connect();

        start(query.getApi());
    }

    /**
     * <p>[description]</p>
     * @param api
     */
    private void queryLogin (TS3Api api) {
        System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Connecting with query account.");
        api.login("name", "pass");
        api.selectVirtualServerById(0);
        api.setNickname("Nocturne");
        api.sendChannelMessage("Ready.");
        System.out.println(Ansi.BLUE + "[tlu] " + Ansi.GREEN + "Successfully connected with query account." + Ansi.RESET);

        System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Register events.");
        api.registerEvent(TS3EventType.SERVER);
        api.registerEvent(TS3EventType.TEXT_CHANNEL);

        Init.clientId = api.whoAmI().getId();
        System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Bot ready. Enjoy!");
    }

    /**
     * <p>[description]</p>
     * @param api
     */
    private void start (TS3Api api) {
        api.addTS3Listeners(new TS3EventAdapter() {
            @Override
            public void onClientJoin (ClientJoinEvent e) {
                handleClientJoin(api, e);
            }

            @Override
            public void onTextMessage(TextMessageEvent e) {
                // only react to channel messages not sent by the query itself
                if (e.getTargetMode() == TextMessageTargetMode.CHANNEL && e.getInvokerId() != Init.clientId) {
                    String message = e.getMessage().toLowerCase();
                    handleMessage(api, e, message);
                }
            }
        });
    }

    /**
     * <p>[description]</p>
     * @param api
     * @param e
     */
    private void handleClientJoin (TS3Api api, ClientJoinEvent e) {

    }

    /**
     * <p>[description]</p>
     * @param api
     * @param e
     * @param message
     */
    private void handleMessage (TS3Api api, TextMessageEvent e, String message) {

        /* @TODO: Implement messages and function */

        if (message.equals("!ping")) {
            // simple ping test
            api.sendChannelMessage("Pong");

        } else if (message.equals("!help")) {
            // a bot can't help
            api.sendChannelMessage("");

        } else if (message.equals("!name"))  {
            // short explanation for the name system
            api.sendChannelMessage("");

        } else if (message.startsWith("!name ")) {
            System.out.println("Checking league name...");
            // format !name {League of Legends name}
            String leagueName = message.substring(6);

            // check if name is spelled right, or riot server is down
            if (/* Mysql.editDatabaseIdentifier(leagueName, e.getInvokerUniqueId(), RiotApi.getIdBySummonerName(leagueName))*/true) {
                api.sendChannelMessage("[b]" + e.getInvokerName() +
                        "[/b] " + "");
            } else {
                api.sendChannelMessage("");
            }

        } else if (message.startsWith("!")) {
            // after all equals, error message
            api.sendChannelMessage("");

        }
    }
}