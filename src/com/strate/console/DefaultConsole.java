package com.strate.console;

import com.strate.remote.teamspeak.DefaultConnection;
import com.strate.setup.*;

import java.util.Date;

/**
 * <p>Default console application, that's controlling
 * teamspeak-league-update bot.</p>
 * @author Stephan Strate
 * @since 3.0.0
 */
public class DefaultConsole extends Console {

    private DefaultConnection defaultConnection;

    private Settings settings;

    public DefaultConsole (DefaultConnection defaultConnection, Settings settings) {
        this.defaultConnection = defaultConnection;
        this.settings = settings;
    }

    @Method
    protected void help (String[] args) {

    }

    @Method
    protected void update (String[] args) {
        if (args != null && args.length == 1) {
            switch (args[0].toLowerCase()) {
                case "language": {
                    new Language().execute();
                    reload(null);
                    break;
                }

                case "notification":
                case "notifications": {
                    new Notification().execute();
                    reload(null);
                    break;
                }

                case "region": {
                    new Region().execute();
                    reload(null);
                    break;
                }

                case "apikey": {
                    new ApiKey(com.strate.remote.riot.constants.Region.getRegionByShortcut(settings.getPropertie("region"))).execute();
                    reload(null);
                    break;
                }

                case "teamspeak": {
                    new Teamspeak().execute();
                    reload(null);
                    break;
                }

                case "--h":
                case "-help": {
                    System.out.println("[" + new Date().toString() + "][tlu] Usage 'update [option]', available options: " +
                            "language, notification, region, apikey, teamspeak");
                    break;
                }

                default: {
                    System.err.println("[" + new Date().toString() + "][tlu] update can not find " + args[0]);
                }
            }
        } else if (args != null && args.length == 0) {
            System.out.println("[" + new Date().toString() + "][tlu] Use 'update -help' or 'update [option]'.");
        } else {
            System.err.println("[" + new Date().toString() + "][tlu] update needs exactly one parameters. Use 'update -help' for more informations.");
        }
    }

    @Method
    protected void reload (String[] args) {
        if (args == null || args.length == 0) {
            settings.load();
            System.out.println("[" + new Date().toString() + "][tlu] Settings reloaded.");
        } else {
            System.err.println("[" + new Date().toString() + "][tlu] reload does not expect any parameters.");
        }
    }

    @Override
    @Method
    public void exit (String[] args) {
        if (args != null && args.length == 0) {
            super.exit(args);
            defaultConnection.disconnect();
        } else {
            System.err.println("[" + new Date().toString() + "][tlu] exit does not expect any parameters.");
        }
    }
}