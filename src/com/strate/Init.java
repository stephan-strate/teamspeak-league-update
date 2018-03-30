package com.strate;

import com.strate.console.DefaultConsole;
import com.strate.constants.Version;
import com.strate.remote.teamspeak.DefaultConnection;
import com.strate.setup.*;

import java.util.Date;

/**
 * <p>Main class.</p>
 * @author Stephan Strate
 * @since 3.0.0
 */
public class Init {

    /**
     * <p>Public settings, available for all classes
     * to access the updates properties.</p>
     * @since 3.0.0
     */
    public static Settings s;

    /**
     * <p>Main method.</p>
     * @param args  arguments
     * @since 3.0.0
     */
    public static void main (String[] args) {
        // create the current version (manually)
        Version version = new Version("3.0.2", 5, "https://github.com/stephan-strate/teamspeak-league-update/releases/download/3.0.2/teamspeak-league-update.jar");

        // searching for new version
        Version latest = new Version();
        latest.update(version);

        s = new Settings();
        if (!s.exists()) {
            // reading user language
            new Language().execute();

            // preferred region
            Region region = new Region();
            region.execute();

            // riot games api key
            new ApiKey(region.get()).execute();

            // whether users should get notifications or not
            new Notification().execute();

            // teamspeak settings
            new Teamspeak().execute();

            System.out.println("[" + new Date().toString() + "][tlu] Finished teamspeak-league-update setup.");
            // reload settings
            s.load();
        }

        try {
            // open a teamspeak connection
            DefaultConnection defaultConnection = new DefaultConnection(s.getPropertie("hostAddress"), Integer.parseInt(s.getPropertie("port")),
                    s.getPropertie("queryUsername"), s.getPropertie("queryPassword"), Integer.parseInt(s.getPropertie("channelId")));
            defaultConnection.connect();

            // opening console application
            DefaultConsole console = new DefaultConsole(defaultConnection);
            console.start();
        } catch (NumberFormatException e) {
            System.err.println("[" + new Date().toString() + "][tlu] Parsing error. Check your .tlu/properties.dat properties.");
            System.exit(1);
        }
    }
}