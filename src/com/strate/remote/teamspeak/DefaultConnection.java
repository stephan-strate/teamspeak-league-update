package com.strate.remote.teamspeak;

import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventType;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;

import java.util.List;

/**
 * <p></p>
 * @author Stephan Strate
 * @since 3.0.0
 */
public class DefaultConnection extends Connection {

    /**
     * <p></p>
     * @param ip
     * @param port
     * @param username
     * @param password
     * @param channel
     * @since 3.0.0
     */
    public DefaultConnection (String ip, int port, String username, String password, int channel) {
        super(ip, port, username, password, channel);
    }

    /**
     * <p></p>
     * @since 3.0.0
     */
    @Override
    protected void init () {
        addEventListener(TS3EventType.SERVER, new TS3EventAdapter() {
            @Override
            public void onClientJoin(ClientJoinEvent e) {
                super.onClientJoin(e);
                clientJoin(e);
            }
        });

        addEventListener(TS3EventType.TEXT_CHANNEL, new TS3EventAdapter() {
            @Override
            public void onTextMessage(TextMessageEvent e) {
                super.onTextMessage(e);
                if (e.getTargetMode() == TextMessageTargetMode.CHANNEL && e.getInvokerId() != getTs3Api().whoAmI().getId()) {
                    String message = e.getMessage().toLowerCase();
                    textMessage(e, message);
                }
            }
        });
    }

    /**
     * <p></p>
     * @param e
     * @since 3.0.0
     */
    private void clientJoin (ClientJoinEvent e) {

    }

    /**
     * <p></p>
     * @param e
     * @since 3.0.0
     */
    private void textMessage (TextMessageEvent e, String message) {
        if (message.equals("!ping")) {
            // simple ping test
            getTs3Api().sendChannelMessage("Pong");

        } else if (message.equals("!help")) {
            // a bot can't help
            getTs3Api().sendChannelMessage("");

        } else if (message.equals("!name"))  {
            // short explanation for the name system
            getTs3Api().sendChannelMessage("");

        } else if (message.startsWith("!name ")) {
            System.out.println("Checking league name...");
            // format !name {League of Legends name}
            String leagueName = message.substring(6);

            // check if name is spelled right, or riot server is down
            if (/* Mysql.editDatabaseIdentifier(leagueName, e.getInvokerUniqueId(), RiotApi.getIdBySummonerName(leagueName))*/true) {
                getTs3Api().sendChannelMessage("[b]" + e.getInvokerName() +
                        "[/b] " + "");
            } else {
                getTs3Api().sendChannelMessage("");
            }
        } else if (message.startsWith("!")) {
            // after all equals, error message
            getTs3Api().sendChannelMessage("");
        }
    }

    /**
     * <p></p>
     * @since 3.0.0
     */
    public void showChannelList () {
        List<Channel> channels = getTs3Api().getChannels();
        for (Channel channel : channels) {
            System.out.println(channel.getId() + "  " + channel.getName() + "\n");
        }
    }
}