package com.strate.sql.databases;

import com.strate.sql.Database;

/**
 * <p>Events database.</p>
 * @author Stephan Strate
 * @since 3.0.0
 */
public class Events extends Database {

    /**
     * <p>Default constructor. Assigns database name.</p>
     * @since 3.0.0
     */
    public Events () {
        super("events.db");
    }
}