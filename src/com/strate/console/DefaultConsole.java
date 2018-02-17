package com.strate.console;

import com.strate.remote.teamspeak.DefaultConnection;

/**
 * <p>Default console application, that's controlling
 * teamspeak-league-update bot.</p>
 * @author Stephan Strate
 * @since 3.0.0
 */
public class DefaultConsole extends Console {

    private DefaultConnection defaultConnection;

    public DefaultConsole (DefaultConnection defaultConnection) {
        this.defaultConnection = defaultConnection;
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
}