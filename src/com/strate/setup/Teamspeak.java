package com.strate.setup;

import com.strate.remote.riot.constants.League;
import com.strate.remote.teamspeak.DefaultConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Teamspeak implements Setup {

    /**
     * <p>Default {@link BufferedReader} to read
     * user input. Used to read teamspeak related
     * data.</p>
     */
    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    private String hostAddress;

    private int port;

    private String queryUsername;

    private String queryPassword;

    private int channelId;

    private HashMap<League, String> serverGroups;

    private DefaultConnection defaultConnection;

    public Teamspeak () {
        hostAddress = "";
        port = 9987;

        queryUsername = "";
        queryPassword = "";

        channelId = 0;
        serverGroups = null;
    }

    @Override
    public void execute () {
        hostAddress = readHostAddress();
        port = readPort();

        queryUsername = readQueryUsername();
        queryPassword = readQueryPassword();

        // verify teamspeak related data
        defaultConnection = new DefaultConnection(hostAddress, port, queryUsername, queryPassword, 1);

        defaultConnection.connect();
        channelId = readChannelId();
        serverGroups = readServerGroups();
        defaultConnection.disconnect();

        Settings settings = new Settings();
        settings.setPropertie("hostAddress", hostAddress);
        settings.setPropertie("port", port + "");
        settings.setPropertie("queryUsername", queryUsername);
        settings.setPropertie("queryPassword", queryPassword);
        settings.setPropertie("channelId", channelId + "");

        Iterator it = serverGroups.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            settings.setPropertie(pair.getKey().toString(), pair.getValue().toString());
        }
    }

    private String readHostAddress () {
        String hostAddress = "";
        try {
            do {
                System.out.print("[" + new Date().toString() + "][tlu] Teamspeak server IP address/hostname: ");
                hostAddress = br.readLine();
            } while (hostAddress.equals(""));
        } catch (IOException e) {
            // handle errors
        }

        return hostAddress;
    }

    private int readPort () {
        String port = "";
        int parsedPort = 9987;
        try {
            boolean touched = false;
            do {
                System.out.print("[" + new Date().toString() + "][tlu] Teamspeak server port (default: 9987): ");
                port = br.readLine();

                if (port.equals("")) {
                    touched = true;
                } else {
                    try {
                        parsedPort = Integer.parseInt(port);
                    } catch (NumberFormatException e) {
                        System.out.println("[" + new Date().toString() + "][tlu] You need to parse a valid port.");
                        continue;
                    }

                    touched = true;
                }
            } while (!touched);
        } catch (IOException e) {
            // handle errors
        }

        return parsedPort;
    }

    private String readQueryUsername () {
        String queryUsername = "";
        try {
            do {
                System.out.print("[" + new Date().toString() + "][tlu] Server query username: ");
                queryUsername = br.readLine();
            } while (queryUsername.equals(""));
        } catch (IOException e) {
            // handle errors
        }

        return queryUsername;
    }

    private String readQueryPassword () {
        String queryPassword = "";
        try {
            do {
                System.out.print("[" + new Date().toString() + "][tlu] Server query password: ");
                queryPassword = br.readLine();
            } while (queryPassword.equals(""));
        } catch (IOException e) {
            // handle errors
        }

        return queryPassword;
    }

    private int readChannelId () {
        // show channel list
        defaultConnection.showChannelList();

        String channelId = "";
        int parsedChannelId = 1;
        try {
            boolean touched = false;
            do {
                System.out.print("[" + new Date().toString() + "][tlu] Select a channel id from above: ");
                channelId = br.readLine();

                if (channelId.equals("")) {
                    touched = false;
                } else {
                    try {
                        parsedChannelId = Integer.parseInt(channelId);
                    } catch (NumberFormatException e) {
                        System.out.println("[" + new Date().toString() + "][tlu] You need to parse a valid channel id.");
                        continue;
                    }

                    touched = true;
                }
            } while (!touched);
        } catch (IOException e) {
            // handle errors
        }

        return parsedChannelId;
    }

    private HashMap<League, String> readServerGroups () {
        // show list of server groups
        defaultConnection.showServerGroups();
        System.out.println("[" + new Date().toString() + "][tlu] Select your server groups.");

        HashMap<League, String> serverGroups = new HashMap<>();

        for (League league : League.getAllLeagues()) {
            String serverGroup = "";
            try {
                boolean touched = false;
                do {
                    System.out.print("[" + new Date().toString() + "][tlu] Server group for " + league.getName() + ": ");
                    serverGroup = br.readLine();

                    if (serverGroup.equals("")) {
                        touched = false;
                    } else {
                        try {
                            Integer.parseInt(serverGroup);
                        } catch (NumberFormatException e) {
                            System.out.println("[" + new Date().toString() + "][tlu] You need to parse a valid server group id.");
                            continue;
                        }

                        serverGroups.put(league, serverGroup);
                        touched = true;
                    }
                } while (!touched);
            } catch (IOException e) {
                // handle errors
            }
        }

        return serverGroups;
    }
}