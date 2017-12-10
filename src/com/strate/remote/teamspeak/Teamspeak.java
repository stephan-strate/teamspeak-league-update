package com.strate.remote.teamspeak;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventType;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.exception.TS3ConnectionFailedException;
import com.github.theholywaffle.teamspeak3.api.reconnect.ReconnectStrategy;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;
import com.strate.Init;
import com.strate.constants.Ansi;
import java.util.List;
import java.util.logging.Level;

/**
 * <p>[description]</p>
 * @author Stephan Strate
 * @since 3.0.0
 */
public class Teamspeak {

    private TS3Config ts3Config;
    private TS3Query ts3Query;
    private TS3Api ts3Api;

    boolean valid;

    public Teamspeak (String ip, int port, String username, String password) {
        try {
            ts3Config = new TS3Config();
            // assign teamspeak 3 ip address
            ts3Config.setHost(ip);

            ts3Config.setDebugLevel(Level.WARNING);
            ts3Config.setReconnectStrategy(ReconnectStrategy.exponentialBackoff());
            ts3Config.setConnectionHandler(new com.github.theholywaffle.teamspeak3.api.reconnect.ConnectionHandler() {
                @Override
                public void onConnect(TS3Query ts3Query) {
                    System.out.println(Ansi.BLUE + "[tlu] " + Ansi.GREEN + "Connected to server." + Ansi.RESET);
                    queryLogin(ts3Query.getApi(), username, password, port);
                }

                @Override
                public void onDisconnect(TS3Query ts3Query) {
                    System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RED + "Disconnected from server." + Ansi.RESET);
                    System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Waiting for server to get up again.");
                }
            });

            ts3Query = new TS3Query(ts3Config);
            // connect to the server
            ts3Query.connect();
            valid = true;
        } catch (TS3ConnectionFailedException e) {
            System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Could not connect to the server.");
            valid = false;
        }
    }

    public void queryLogin (TS3Api ts3Api, String username, String password, int port) {
        System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Connecting with query account.");
        // choose virtual server (by port)
        ts3Api.selectVirtualServerByPort(port);
        // login in as query
        ts3Api.login(username, password);
        // rename bot
        ts3Api.setNickname("Nocturne");
        ts3Api.sendChannelMessage("Ready.");
        System.out.println(Ansi.BLUE + "[tlu] " + Ansi.GREEN + "Successfully connected with query account." + Ansi.RESET);
        System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Register events.");
        ts3Api.registerEvent(TS3EventType.SERVER);
        ts3Api.registerEvent(TS3EventType.TEXT_CHANNEL);

        Init.clientId = ts3Api.whoAmI().getId();
        System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Bot ready. Enjoy!");
    }

    private void start (TS3Api ts3Api) {
        ts3Api.addTS3Listeners(new TS3EventAdapter() {
            @Override
            public void onClientJoin (ClientJoinEvent e) {
                handleClientJoin(ts3Api, e);
            }

            @Override
            public void onTextMessage(TextMessageEvent e) {
                // only react to channel messages not sent by the query itself
                if (e.getTargetMode() == TextMessageTargetMode.CHANNEL && e.getInvokerId() != Init.clientId) {
                    String message = e.getMessage().toLowerCase();
                    handleMessage(ts3Api, e, message);
                }
            }
        });
    }

    private void handleClientJoin (TS3Api ts3Api, ClientJoinEvent e) {

    }

    private void handleMessage (TS3Api ts3Api, TextMessageEvent e, String message) {
        /* @TODO: Implement messages and function */
        if (message.equals("!ping")) {
            // simple ping test
            ts3Api.sendChannelMessage("Pong");

        } else if (message.equals("!help")) {
            // a bot can't help
            ts3Api.sendChannelMessage("");

        } else if (message.equals("!name"))  {
            // short explanation for the name system
            ts3Api.sendChannelMessage("");

        } else if (message.startsWith("!name ")) {
            System.out.println("Checking league name...");
            // format !name {League of Legends name}
            String leagueName = message.substring(6);

            // check if name is spelled right, or riot server is down
            if (/* Mysql.editDatabaseIdentifier(leagueName, e.getInvokerUniqueId(), RiotApi.getIdBySummonerName(leagueName))*/true) {
                ts3Api.sendChannelMessage("[b]" + e.getInvokerName() +
                        "[/b] " + "");
            } else {
                ts3Api.sendChannelMessage("");
            }

        } else if (message.startsWith("!")) {
            // after all equals, error message
            ts3Api.sendChannelMessage("");

        }
    }

    public void showChannelList () {
        if (valid) {
            List<Channel> channels = ts3Api.getChannels();
            for (Channel temp : channels) {
                System.out.print(temp.getId() + "  " + temp.getName());
                System.out.println();
            }
        }
    }

    public void disconnect () {
        ts3Query.exit();
    }

    public boolean isValid () {
        return valid;
    }
}