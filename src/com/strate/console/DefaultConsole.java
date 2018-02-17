package com.strate.console;

import com.strate.remote.teamspeak.DefaultConnection;
import com.strate.setup.Settings;

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
    protected void reload (String[] args) {
        settings.load();
        System.out.println("[" + new Date().toString() + "][tlu] Settings reloaded.");
    }

    @Override
    @Method
    public void exit (String[] args) {
        super.exit(args);
        defaultConnection.disconnect();
    }
}