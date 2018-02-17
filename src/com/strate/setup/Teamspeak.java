package com.strate.setup;

import com.strate.remote.teamspeak.DefaultConnection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

    private DefaultConnection defaultConnection;

    public Teamspeak () {
        hostAddress = "";
        port = 9987;

        queryUsername = "";
        queryPassword = "";
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
        defaultConnection.disconnect();
    }

    private String readHostAddress () {
        String hostAddress = "";
        try {
            do {
                System.out.print("[tlu] Teamspeak server IP address (needs to be an address eg. 192.168.0.1): ");
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
                System.out.print("[tlu] Teamspeak server port (default: 9987): ");
                port = br.readLine();

                if (port.equals("")) {
                    touched = true;
                } else {
                    try {
                        parsedPort = Integer.parseInt(port);
                    } catch (NumberFormatException e) {
                        System.out.println("[tlu] You need to parse a valid port.");
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
                System.out.print("[tlu] Server query username: ");
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
                System.out.print("[tlu] Server query password: ");
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
                System.out.print("[tlu] Select a channel id from above: ");
                channelId = br.readLine();

                if (channelId.equals("")) {
                    touched = true;
                } else {
                    try {
                        parsedChannelId = Integer.parseInt(channelId);
                    } catch (NumberFormatException e) {
                        System.out.println("[tlu] You need to parse a valid channel id.");
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
}