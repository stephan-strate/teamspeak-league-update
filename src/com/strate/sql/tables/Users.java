package com.strate.sql.tables;

import com.strate.sql.Table;
import com.strate.sql.databases.Events;

public class Users extends Table {

    public Users () {
        super("users", new Events(), "");
    }

    public long getSummonerId (String uid) {
        return 0;
    }
}