package com.strate.remote.teamspeak;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.exception.TS3ConnectionFailedException;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;
import com.strate.constants.Ansi;
import java.util.List;

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

            ts3Query = new TS3Query(ts3Config);
            // connect to the server
            ts3Query.connect();

            ts3Api = ts3Query.getApi();
            // choose virtual server (by port)
            ts3Api.selectVirtualServerByPort(port);
            // login in as query
            ts3Api.login(username, password);
            // rename bot
            ts3Api.setNickname("Nocturne");

            valid = true;
        } catch (TS3ConnectionFailedException e) {
            System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Could not connect to the server.");
            valid = false;
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