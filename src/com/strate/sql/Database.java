package com.strate.sql;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

/**
 * <p>Helps to create several databases in different
 * folders. Easy to use/implements abstract class.
 * Just extend it and call the super class. You can
 * get connections and close them again with this class.</p>
 * @author Stephan Strate
 * @since 3.0.0
 */
public abstract class Database {

    /**
     * <p>Path to databases.</p>
     * @since 3.0.0
     */
    private final String path = ".tlu/";

    /**
     * <p>Current connection.</p>
     * @since 3.0.0
     */
    private Connection con = null;

    /**
     * <p>Current url.</p>
     * @since 3.0.0
     */
    private String url;

    /**
     * <p>Creates a Sqlite sql without
     * an extended path.</p>
     * @param database  sql name
     * @since 3.0.0
     */
    public Database (String database) {
        this(database, "");
    }

    /**
     * <p>Creates a Sqlite sql, creating
     * the needed path and sql itself.</p>
     * @param database  sql name
     * @param path      path extension
     * @since 3.0.0
     */
    public Database (String database, String path) {
        try {
            createFolder(path);
            // prepare url
            url = "jdbc:sqlite:" + this.path + path + database;

            // get sql connection (create sql if needed)
            con = DriverManager.getConnection(url);
            // close connection
            con.close();
        } catch (SQLException e) {
            System.err.println("[" + new Date().toString() + "][tlu] Can not create sql " + this.path + path + database + ".");
        }
    }

    /**
     * <p>Creates the folders, that are given
     * to store the databases in it.</p>
     * @param ext   path extension
     * @since 3.0.0
     */
    private void createFolder (String ext) {
        // create path as file
        File file = new File(path + ext);

        // if file not exists
        if (!file.exists()) {
            // create folder
            file.mkdir();
        }
    }

    /**
     * <p>Opens a new sql connection.</p>
     * @return  sql connection
     * @since 3.0.0
     */
    public Connection getConnection () {
        // reset connection
        con = null;
        try {
            // open connection
            return con = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.err.println("[" + new Date().toString() + "][tlu] Can not open sql connection " + url + ".");
        }

        // return null when error occurs
        return con;
    }

    /**
     * <p>Closes a open sql connection.</p>
     * @since 3.0.0
     */
    public void closeConnection () {
        try {
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            System.err.println("[" + new Date().toString() + "][tlu] Can not close sql connection " + url + ".");
        }
    }
}