package com.strate.console;

import com.strate.Init;
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

    /**
     * <p>Default teamspeak connection, to handle
     * connect, disconnect, ..</p>
     * @since 3.0.0
     */
    private DefaultConnection defaultConnection;

    /**
     * <p>Assigning default teamspeak connection.</p>
     * @param defaultConnection teamspeak connection
     * @since 3.0.0
     */
    public DefaultConsole (DefaultConnection defaultConnection) {
        this.defaultConnection = defaultConnection;
    }

    /**
     * <p>Extending the default {@link Console#start()} method
     * with advise for users.</p>
     * @since 3.0.0
     */
    @Override
    public void start() {
        super.start();
        System.out.println("[" + new Date().toString() + "][tlu] Use help for informations.");
    }

    /**
     * <p>Prints some advises about methods for
     * users.</p>
     * @param args  arguments
     * @since 3.0.0
     */
    @Method
    protected void help (String[] args) {
        if (args == null || args.length == 0) {
            System.out.println("[" + new Date().toString() + "][tlu] List of all commands:" +
                    "\n> EXIT: Bot will disconnect from the server and program terminating" +
                    "\n> RECONNECT: Bot disconnects and connects again" +
                    "\n> RELOAD: Reloads the settings, can be used when settings are managed manually" +
                    "\n> UPDATE [option]: Update specific settings, for more informations use UPDATE -help");
        } else {
            System.err.println("[" + new Date().toString() + "][tlu] help does not expect any parameters.");
        }
    }

    /**
     * <p>Extending the default {@link Console#exit(String[])}
     * method with disconnecting from the server.</p>
     * @param args  can be null
     * @since 3.0.0
     */
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

    /**
     * <p>Disconnects and connects to the teamspeak
     * server.</p>
     * @param args  arguments
     * @since 3.0.0
     */
    @Method
    protected void reconnect (String[] args) {
        if (args == null || args.length == 0) {
            System.out.println("[" + new Date().toString() + "][tlu] Reconnecting.");
            defaultConnection.disconnect();
            defaultConnection.connect();
        } else {
            System.err.println("[" + new Date().toString() + "][tlu] reconnect does not expect any parameters.");
        }
    }

    /**
     * <p>Reloads the {@link Settings} object.</p>
     * @param args  arguments
     * @since 3.0.0
     */
    @Method
    protected void reload (String[] args) {
        if (args == null || args.length == 0) {
            Init.s.load();
            System.out.println("[" + new Date().toString() + "][tlu] Settings reloaded.");
        } else {
            System.err.println("[" + new Date().toString() + "][tlu] reload does not expect any parameters.");
        }
    }

    /**
     * <p>Used to update specific settings. Expects
     * an option that needs to be updated.</p>
     * @param args  arguments
     * @since 3.0.0
     */
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
                    new ApiKey(com.strate.remote.riot.constants.Region.getRegionByShortcut(Init.s.getPropertie("region"))).execute();
                    reload(null);
                    break;
                }

                case "teamspeak": {
                    defaultConnection.disconnect();
                    new Teamspeak().execute();
                    reload(null);
                    defaultConnection.connect();
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
}