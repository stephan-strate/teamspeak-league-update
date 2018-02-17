package com.strate;

import com.strate.console.DefaultConsole;
import com.strate.constants.Version;
import com.strate.remote.teamspeak.DefaultConnection;
import com.strate.setup.*;

/**
 * <p>Main class.</p>
 * @author Stephan Strate
 * @since 3.0.0
 */
public class Init {

    /**
     * <p>Main method.</p>
     * @param args  arguments
     * @since 3.0.0
     */
    public static void main (String[] args) {
        // create the current version (manually)
        Version version = new Version("2.0.1", 2, "https://github.com/stephan-strate/teamspeak-league-update/releases/download/2.0.1/teamspeak-league-update.jar");

        // searching for new version
        Version latest = new Version();
        latest.update(version);

        Settings s = new Settings();
        if (!s.exists()) {
            // reading user language
            Language language = new Language();
            language.execute();

            // preferred region
            Region region = new Region();
            region.execute();

            // riot games api key
            ApiKey apiKey = new ApiKey(region.get());
            apiKey.execute();

            // whether users should get notifications or not
            Notification notification = new Notification();
            notification.execute();

            // teamspeak settings
            Teamspeak teamspeak = new Teamspeak();
            teamspeak.execute();

            System.out.println("[tlu] Finished teamspeak-league-update setup.\n" +
                    "[tlu] Please restart the application.");
            System.exit(0);
        } else {
            // open a teamspeak connection
            DefaultConnection defaultConnection = new DefaultConnection(s.getPropertie("hostAddress"), Integer.parseInt(s.getPropertie("port")),
                    s.getPropertie("queryUsername"), s.getPropertie("queryPassword"), Integer.parseInt(s.getPropertie("channelId")));
            defaultConnection.connect();

            // opening console application
            DefaultConsole console = new DefaultConsole(defaultConnection, s);
            console.start();
        }
    }
}