package com.strate.sql.tables;

import com.strate.sql.Table;
import com.strate.sql.databases.Config;

public class Settings extends Table {

    public Settings () {
        super("settings", new Config(), "");
    }

    public void insert () {

    }
}