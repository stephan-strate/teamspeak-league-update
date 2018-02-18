package com.strate.remote.teamspeak;

import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventType;
import com.github.theholywaffle.teamspeak3.api.event.TextMessageEvent;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroup;
import com.strate.Init;
import com.strate.remote.riot.Api;
import com.strate.remote.riot.constants.League;
import com.strate.remote.riot.constants.Region;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * <p></p>
 * @author Stephan Strate
 * @since 3.0.0
 */
public class DefaultConnection extends Connection {

    /**
     * <p>Default constructor.</p>
     * @param ip        ip address
     * @param port      port
     * @param username  query username
     * @param password  query password
     * @param channel   channel id
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
        /*Api api = new Api(Init.s.getPropertie("apikey"), Region.getRegionByShortcut(Init.s.getPropertie("region")));

        // current riot game account
        long accountId = 0;
        League league = api.getLeagueById(accountId);

        // old teamspeak league
        int clientId = e.getClientId();
        ClientInfo clientInfo = getTs3Api().getClientInfo(clientId);
        League teamspeak = League.UNRANKED;
        int groupId = -1;

        HashMap<String, League> leagues = League.getAllLeagues();
        for (ServerGroup serverGroup : getTs3Api().getServerGroups()) {
            if (leagues.containsKey(serverGroup.getName().toLowerCase())) {
                teamspeak = League.getLeagueByName(serverGroup.getName());
                groupId = serverGroup.getId();
                break;
            }
        }

        if (!teamspeak.equals(league)) {
            int invoker = e.getClientDatabaseId();
            getTs3Api().removeClientFromServerGroup(groupId, invoker);
            getTs3Api().addClientToServerGroup(groupId, invoker);
        }*/
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
        System.out.println("[" + new Date().toString() + "][tlu] Your channels:");
        List<Channel> channels = getTs3Api().getChannels();
        for (Channel channel : channels) {
            System.out.println("> " + channel.getId() + "  " + channel.getName());
        }
    }

    /**
     * <p></p>
     * @since 3.0.0
     */
    public void showServerGroups () {
        System.out.println("[" + new Date().toString() + "][tlu] Your server groups:");
        List<ServerGroup> serverGroups = getTs3Api().getServerGroups();
        for (ServerGroup serverGroup : serverGroups) {
            System.out.println("> " + serverGroup.getId() + "  " + serverGroup.getName());
        }
    }
}