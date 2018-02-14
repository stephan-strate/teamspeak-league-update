package com.strate.sql.tables;

import com.strate.sql.Table;
import com.strate.sql.databases.Config;

public class Settings extends Table {

    /**
     *
     */
    private String host;

    /**
     *
     */
    private int port;

    /**
     *
     */
    private String name;

    /**
     *
     */
    private String password;

    /**
     *
     */
    private int channelid;

    /**
     *
     */
    private boolean valid;

    /**
     *
     */
    public Settings () {
        super("settings", new Config(), "");
    }

    /**
     *
     */
    public void insert () {

    }

    /**
     *
     * @return
     */
    public boolean exists () {
        return false;
    }

    public String getHost () {
        return host;
    }

    public int getPort () {
        return port;
    }

    @Override
    public String getName () {
        return name;
    }

    public String getPassword () {
        return password;
    }

    public int getChannelid () {
        return channelid;
    }
}