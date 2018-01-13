package com.strate;

import com.strate.console.DefaultConsole;
import com.strate.constants.Version;
import com.strate.sql.Settings;
import com.strate.remote.teamspeak.Connection;
import com.strate.remote.teamspeak.DefaultConnection;
import java.io.IOException;

/**
 * <p>Main class.</p>
 * @author Stephan Strate
 * @since 3.0.0
 */
public class Init {

    /* @TODO: Sql requests and inserts */
    /* @TODO: Handle client join event */
    /* @TODO: Handle setup problems with teamspeak auth, cant create sqlite databases */

    /**
     * <p>Main method.</p>
     * @param args  arguments
     * @since 3.0.0
     */
    public static void main (String[] args) throws IOException {
        // create the current version (manually)
        Version version = new Version("2.0.1", 2, "https://github.com/stephan-strate/teamspeak-league-update/releases/download/2.0.1/teamspeak-league-update.jar");

        // searching for new version
        Version latest = new Version();
        latest.update(version);

        Settings settings = new Settings();
        if (!settings.exists()) {
            // starting setup process
            Setup setup = new Setup();
            setup.initSetup();
        } else {
            // open a teamspeak connect
            Connection connection = new DefaultConnection(settings.host, settings.port, settings.name, settings.password, settings.channelid);
            connection.connect();

            // opening console application
            DefaultConsole console = new DefaultConsole();
            console.start();
        }
    }
}