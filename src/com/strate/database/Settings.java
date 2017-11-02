package com.strate.database;

/**
 * <p>[description]</p>
 * @author Stephan Strate
 * @since 3.0.0
 */
public class Settings extends Table {

    private static final String TABLE =
            "CREATE TABLE IF NOT EXISTS settings (\n" +
            " id integer PRIMARY KEY, \n" +
            " riot_key text NOT NULL, \n" +
            " region text NOT NULL, \n" +
            " language text NOT NULL, \n" +
            " notifications boolean NOT NULL, \n" +
            " channel_id num NOT NULL, \n" +
            " teamspeak_host text NOT NULL, \n" +
            " teamspeak_port num NOT NULL, \n" +
            " teamspeak_queryname text NOT NULL, \n" +
            " teamspeak_querypass text NOT NULL, \n" +
            " teamspeak_virtualid num NOT NULL\n" +
            ");";

    /**
     * <p>[description]</p>
     * @since 3.0.0
     */
    public Settings () {
        super("settings", TABLE);
    }
}