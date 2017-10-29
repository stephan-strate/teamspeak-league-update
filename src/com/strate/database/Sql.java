package com.strate.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * <p>[description]</p>
 * @author Stephan Strate
 */
class Sql {

    private static final String path = ".tlu/";

    private Connection con = null;

    /**
     * <p>[description]</p>
     * @param database
     */
    Sql (String database) {
        connect(database);
    }

    /**
     * <p>[description]</p>
     * @return
     */
    Connection getCon () {
        return con;
    }

    /**
     * <p>[description]</p>
     * @param database
     */
    private void connect (String database) {
        try {
            checkFolder();
            String url = "jdbc:sqlite:" + path + database;

            con = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * <p>[description]</p>
     */
    private void checkFolder () {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
    }
}