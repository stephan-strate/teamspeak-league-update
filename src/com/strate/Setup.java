package com.strate;

import com.strate.constants.Ansi;
import com.strate.constants.Language;
import com.strate.sql.Settings;
import com.strate.remote.riot.Api;
import com.strate.remote.riot.constants.Region;
import com.strate.remote.teamspeak.Teamspeak;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * <p>[description]</p>
 * @author Stephan Strate
 */
class Setup {

    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    /**
     * <p>[description]</p>
     */
    Setup () {

    }

    void initSetup () throws IOException {
        System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Starting " + Ansi.PURPLE + "teamspeak-league-update" + Ansi.RESET + " setup.");

        // non teamspeak related
        Language language = readLanguage();
        Region region = readRegion();
        String key = readKey(region);
        boolean notifications = readNotifications();

        // teamspeak related
        boolean validHost = false;
        Teamspeak teamspeak = null;
        String host = "";
        int port = 9987;
        String username = "";
        String password = "";
        do {
            host = readHost();
            port = readPort();
            username = readUsername();
            password = readPassword();

            // verify teamspeak related data
            teamspeak = new Teamspeak(host, port, username, password);
            validHost = teamspeak.isValid();
        } while (!validHost);
        int channelid = readChannelId(teamspeak);
        teamspeak.disconnect();

        Settings settings = new Settings();
        settings.insert(language.getCode(), region.getShortcut(), key, notifications, host, port, username, password, channelid);

        System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Finished " + Ansi.PURPLE + "teamspeak-league-update" + Ansi.RESET + " setup.");
        System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Please restart the application.");
    }

    public Language readLanguage () throws IOException {
        Language language = null;
        do {
            System.out.print(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Enter your prefered language (" + Language.getAllLanguages() + "): ");
            language = Language.getLanguageByCode(br.readLine());
        } while (language == null);
        System.out.println(Ansi.BLUE + "[tlu] " + Ansi.GREEN + "Successfully identified language: " + Ansi.RESET + language.getCode());

        return language;
    }

    public Region readRegion () throws IOException {
        Region region = null;
        do {
            System.out.print(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Enter your region (" + Region.getAllRegions() + "): ");
            region = Region.getRegionByShortcut(br.readLine());
        } while (region == null);
        System.out.println(Ansi.BLUE + "[tlu] " + Ansi.GREEN + "Successfully identified region: " + Ansi.RESET + region.getShortcut());

        return region;
    }

    public String readKey (Region region) throws IOException {
        String key = "";
        boolean valid = false;
        Api api = new Api(key, region);
        do {
            System.out.print(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Enter your Riot Games api key: ");
            key = br.readLine();

            System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Checking your api key: " + key);
            valid = api.isKeyValid(key);
            if (valid) {
                System.out.println(Ansi.BLUE + "[tlu] " + Ansi.GREEN + "Your api key is valid." + Ansi.RESET);
            } else {
                System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RED + "You api key is not valid." + Ansi.RESET);
            }
        } while (!valid);

        return key;
    }

    public boolean readNotifications () throws IOException {
        boolean notifications = false;
        boolean touched = false;
        do {
            System.out.print(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Do you want your users to get notifications? (Y/n) ");
            String temp = br.readLine();
            if (temp.toLowerCase().equals("y")) {
                notifications = true;
                touched = true;
            } else if (temp.toLowerCase().equals("n")) {
                notifications = false;
                touched = true;
            }
        } while (!touched);

        if (notifications) {
            System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Your users will get notifications.");
        } else {
            System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Your users will not get notifications.");
        }

        return notifications;
    }

    public String readHost () throws IOException {
        String host = "";
        do {
            System.out.print(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Teamspeak server IP address (need to be an address eg. 192.168.0.1): ");
            host = br.readLine();
        } while (host.equals(""));
        return host;
    }

    public int readPort () throws IOException {
        String port = "";
        int temp = 9987;
        boolean touched = false;
        do {
            System.out.print(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Teamspeak server port (default: 9987): ");
            port = br.readLine();

            if (port.equals("")) {
                touched = true;
            } else {
                try {
                    temp = Integer.parseInt(port);
                } catch (NumberFormatException e) {
                    System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RESET + "You need to parse a valid port.");
                    continue;
                }

                touched = true;
            }
        } while (!touched);

        return temp;
    }

    public String readUsername () throws IOException {
        String username = "";
        do {
            System.out.print(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Server query username: ");
            username = br.readLine();
        } while (username.equals(""));
        return username;
    }

    public String readPassword () throws IOException {
        String password = "";
        do {
            System.out.print(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Server query password: ");
            password = br.readLine();
        } while (password.equals(""));
        return password;
    }

    public int readChannelId (Teamspeak teamspeak) throws IOException {
        teamspeak.showChannelList();
        String channelid = "";
        int temp = 1;
        boolean touched = false;
        do {
            System.out.print(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Select a channel id from above: ");
            channelid = br.readLine();

            if (channelid.equals("")) {
                touched = true;
            } else {
                try {
                    temp = Integer.parseInt(channelid);
                } catch (NumberFormatException e) {
                    System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RESET + "You need to parse a valid channel id.");
                    continue;
                }

                touched = true;
            }
        } while (!touched);

        return temp;
    }
}