package com.strate;

import com.strate.console.DefaultConsole;
import com.strate.constants.Version;
import com.strate.database.Settings;
import com.strate.remote.teamspeak.Teamspeak;
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

    public static volatile int clientId;

    /**
     * <p>Main method.</p>
     * @param args  arguments
     * @since 3.0.0
     */
    public static void main (String[] args) throws IOException {
        /*// create the current version (manually)
        Version version = new Version("2.0.1", 2, "https://github.com/stephan-strate/teamspeak-league-update/releases/download/2.0.1/teamspeak-league-update.jar");

        // searching for new version
        Version latest = new Version();
        latest.update(version);*/

        Settings settings = new Settings();
        if (!settings.exists()) {
            // starting setup process
            Setup setup = new Setup();
            setup.initSetup();
        } else {
            Teamspeak teamspeak = new Teamspeak(settings.host, settings.port, settings.name, settings.password);
        }

        // opening console application
        DefaultConsole console = new DefaultConsole();
        console.start();
    }
}