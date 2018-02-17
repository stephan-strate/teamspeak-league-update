package com.strate.console;

import com.strate.remote.teamspeak.DefaultConnection;
import com.strate.setup.Settings;

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

    /**
     * <p>Dummy method. Output all arguments.</p>
     * @param args  arguments
     * @since 3.0.0
     */
    @Method
    protected void update (String[] args) {
        // do something when user calls function
        for (int i = 0; i < args.length; i++) {
            System.out.println(args[i]);
        }
    }

    protected void reload (String[] args) {
        settings.load();
        System.out.println("[tlu] Settings reloaded.");
    }
}