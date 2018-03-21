package com.strate.remote.teamspeak;

import com.github.theholywaffle.teamspeak3.api.TextMessageTargetMode;
import com.github.theholywaffle.teamspeak3.api.event.*;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;
import com.github.theholywaffle.teamspeak3.api.wrapper.ServerGroup;
import com.strate.Init;
import com.strate.remote.riot.Api;
import com.strate.remote.riot.constants.League;
import com.strate.remote.riot.constants.Region;
import com.strate.sql.EntryNotFoundException;
import com.strate.sql.tables.events.Users;

import java.util.*;

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
     * <p>Extending {@link Connection#init()} method with
     * some EventListeners.</p>
     * @since 3.0.0
     */
    @Override
    protected void init () {
        // client join events
        addEventListener(TS3EventType.SERVER, new TS3EventAdapter() {
            @Override
            public void onClientJoin(ClientJoinEvent e) {
                super.onClientJoin(e);
                // parse event to handler
                clientJoin(e);
            }
        });

        // channel messages
        addEventListener(TS3EventType.TEXT_CHANNEL, new TS3EventAdapter() {
            @Override
            public void onTextMessage(TextMessageEvent e) {
                super.onTextMessage(e);
                // filter message
                if (e.getTargetMode() == TextMessageTargetMode.CHANNEL && e.getInvokerId() != getTs3Api().whoAmI().getId()) {
                    String message = e.getMessage().toLowerCase();
                    textMessage(e, message);
                }
            }
        });

        // private messages
        addEventListener(TS3EventType.TEXT_PRIVATE, new TS3EventAdapter() {
            @Override
            public void onTextMessage(TextMessageEvent e) {
                super.onTextMessage(e);
                if (e.getTargetMode() == TextMessageTargetMode.CLIENT && e.getInvokerId() != getTs3Api().whoAmI().getId()) {
                    // filter message
                    String message = e.getMessage().toLowerCase();
                    if (message.equals("!mute")) {
                        new Users().assign(e.getInvokerUniqueId(), e.getInvokerName(), 0, "");
                    } else {
                        textMessage(e, message);
                    }
                }
            }
        });
    }

    /**
     * <p>Client join handler.</p>
     * @param e on client join event
     * @since 3.0.0
     */
    private void clientJoin (ClientJoinEvent e) {
        try {
            System.out.println("[" + new Date().toString() + "][tlu] '" + e.getClientNickname() + "' joined the server.");
            // look up account id, 0 when not found
            long accountId = new Users().getSummonerId(e.getUniqueClientIdentifier());

            if (accountId != 0) {
                // current riot game account
                Api api = new Api(Init.s.getPropertie("apikey"), Region.getRegionByShortcut(Init.s.getPropertie("region")));
                League account = api.getLeagueById(accountId);

                // old teamspeak league
                int clientId = e.getClientId();
                ClientInfo clientInfo = getTs3Api().getClientInfo(clientId);
                League teamspeak = League.UNRANKED;

                // get all server groups with id
                HashMap<League, String> serverGroupsByLeague = new HashMap<>();
                HashMap<String, League> serverGroupsById = new HashMap<>();
                for (League league : League.getAllLeagues()) {
                    String id = Init.s.getPropertie(league.getName());
                    serverGroupsByLeague.put(league, id);
                    serverGroupsById.put(id, league);
                }

                // get all assigned league server groups
                List<League> teamspeakLeagues = new LinkedList<>();
                for (int group : clientInfo.getServerGroups()) {
                    if (serverGroupsById.containsKey(group + "")) {
                        teamspeakLeagues.add(serverGroupsById.get(group + ""));
                    }
                }

                // get the highest rank
                for (League league : teamspeakLeagues) {
                    if (league.compare(teamspeak) > 0) {
                        teamspeak = league;
                    }
                }

                int invoker = e.getClientDatabaseId();
                if (!teamspeak.equals(account)) {
                    System.out.println("[" + new Date().toString() + "][tlu] Assigning new league " + account.getName() + " and removing " + teamspeak.getName() + ".");

                    // account league is higher
                    if (teamspeakLeagues.size() > 1) {
                        // more than one league is assigned, remove all
                        for (League league : teamspeakLeagues) {
                            getTs3Api().removeClientFromServerGroup(Integer.parseInt(serverGroupsByLeague.get(league)), invoker);
                        }
                    } else if (teamspeakLeagues.size() == 1) {
                        // remove the single league
                        getTs3Api().removeClientFromServerGroup(Integer.parseInt(serverGroupsByLeague.get(teamspeak)), invoker);
                    }

                    // assign new league
                    getTs3Api().addClientToServerGroup(Integer.parseInt(serverGroupsByLeague.get(account)), invoker);
                } else if (teamspeakLeagues.size() > 1) {
                    // account league is not higher, but more than one league is assigned
                    for (League league : teamspeakLeagues) {
                        // remove all leagues that are not the highest
                        if (!league.equals(teamspeak)) {
                            getTs3Api().removeClientFromServerGroup(Integer.parseInt(serverGroupsByLeague.get(league)), invoker);
                        }
                    }
                }
            }
        } catch (EntryNotFoundException error) {
            // when user is not registered in database, you will be thrown here
            if (Boolean.parseBoolean(Init.s.getPropertie("notification"))) {
                getTs3Api().sendPrivateMessage(e.getClientId(), "Welcome to the server. You can assign your League of Legends summoner name " +
                        "by using [b]!name [Your League of Legends name][/b]. If you do not want to receive any messages of Nocturne, just use [b]!mute[/b].");
            }
        }
    }

    /**
     * <p>Text message handler.</p>
     * @param e         text message event
     * @param message   message
     * @since 3.0.0
     */
    private void textMessage (TextMessageEvent e, String message) {
        // switching through message types
        if (message.equals("!ping")) {
            // simple ping test
            String response = "Pong";
            if (e.getTargetMode() == TextMessageTargetMode.CHANNEL) {
                getTs3Api().sendChannelMessage(response);
            } else if (e.getTargetMode() == TextMessageTargetMode.CLIENT) {
                getTs3Api().sendPrivateMessage(e.getInvokerId(), response);
            }
        } else if (message.equals("!help")) {
            // a bot can't help
            String response = "I can't help you.";
            if (e.getTargetMode() == TextMessageTargetMode.CHANNEL) {
                getTs3Api().sendChannelMessage(response);
            } else if (e.getTargetMode() == TextMessageTargetMode.CLIENT) {
                getTs3Api().sendPrivateMessage(e.getInvokerId(), response);
            }
        } else if (message.equals("!name"))  {
            // short explanation for the name system
            String response = "Usage: !name [Your League of Legends name]";
            if (e.getTargetMode() == TextMessageTargetMode.CHANNEL) {
                getTs3Api().sendChannelMessage(response);
            } else if (e.getTargetMode() == TextMessageTargetMode.CLIENT) {
                getTs3Api().sendPrivateMessage(e.getInvokerId(), response);
            }
        } else if (message.startsWith("!name ")) {
            // format !name {League of Legends name}
            String leagueName = message.substring(6);
            System.out.println("[" + new Date().toString() + "][tlu] '" + e.getInvokerName() + "' wants to assign '" + leagueName + "' as League of Legends name.");

            // check if name is spelled right, or riot server is down
            Users users = new Users();
            if (users.assign(e.getInvokerUniqueId(), e.getInvokerName(), new Api(Init.s.getPropertie("apikey"),
                    Region.getRegionByShortcut(Init.s.getPropertie("region"))).getIdBySummonerName(leagueName), leagueName)) {

                String response = "[b]" + leagueName +
                        "[/b] " + "added to database. Reconnect to get your league assigned.";
                if (e.getTargetMode() == TextMessageTargetMode.CHANNEL) {
                    getTs3Api().sendChannelMessage(response);
                } else if (e.getTargetMode() == TextMessageTargetMode.CLIENT) {
                    getTs3Api().sendPrivateMessage(e.getInvokerId(), response);
                }
                System.out.println("[" + new Date().toString() + "][tlu] '" + e.getInvokerName() + "' assigned '" + leagueName + "' as League of Legends name.");
            } else {
                getTs3Api().sendChannelMessage("Error while adding your League of Legends name, please try again later.");
            }
        } else if (message.startsWith("!")) {
            // after all equals, error message
            String response = "Command not found.";
            if (e.getTargetMode() == TextMessageTargetMode.CHANNEL) {
                getTs3Api().sendChannelMessage(response);
            } else if (e.getTargetMode() == TextMessageTargetMode.CLIENT) {
                getTs3Api().sendPrivateMessage(e.getInvokerId(), response);
            }
        }
    }

    /**
     * <p>Prints all channels to the console.</p>
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
     * <p>Prints all server groups to the console.</p>
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