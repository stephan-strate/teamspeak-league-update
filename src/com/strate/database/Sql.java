package com.strate.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * <p>Represents a sqlite database.</p>
 * @author Stephan Strate
 * @since 3.0.0
 */
class Sql {

    private static final String path = ".tlu/";

    private Connection con = null;
    private String url;

    /**
     * <p>Represents a sqlite database connection.</p>
     * @param database  database name
     * @since 3.0.0
     */
    Sql (String database) {
        create(database);
    }

    /**
     * <p>Connect to a sqlite database and
     * create folder if necessary.</p>
     * @param database  database name
     * @since 3.0.0
     */
    private void create (String database) {
        try {
            checkFolder();
            url = "jdbc:sqlite:" + path + database;

            con = DriverManager.getConnection(url);
            con.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }


    public Connection connect () {
        con = null;
        try {
            return con = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e);
        }

        return null;
    }

    public void disconnect () {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * <p>Create folder if necessary.</p>
     * @since 3.0.0
     */
    private void checkFolder () {
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
    }

    /**
     * <p>Returns the connection.</p>
     * @return  {@link Connection}
     * @since 3.0.0
     */
    Connection getCon () {
        return con;
    }
}