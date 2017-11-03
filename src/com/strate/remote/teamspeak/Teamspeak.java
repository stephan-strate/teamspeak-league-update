package com.strate.remote.teamspeak;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.exception.TS3ConnectionFailedException;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;
import com.strate.constants.Ansi;

import java.util.List;

public class Teamspeak {

    private TS3Config ts3Config;
    private TS3Query ts3Query;
    private TS3Api ts3Api;

    boolean valid;

    public Teamspeak (String ip, int port) {
        try {
            ts3Config = new TS3Config();
            ts3Config.setHost(ip);

            ts3Query = new TS3Query(ts3Config);
            ts3Query.connect();

            ts3Api = ts3Query.getApi();
            ts3Api.selectVirtualServerByPort(port);
            ts3Api.setNickname("Nocturne");

            valid = true;
        } catch (TS3ConnectionFailedException e) {
            System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Could not connect to the server.");
            valid = false;
        }
    }

    public boolean isValid () {
        return valid;
    }

    public void showChannelList () {
        if (valid) {
            List<Channel> channels = ts3Api.getChannels();
            System.out.println(channels);
        }
    }
}