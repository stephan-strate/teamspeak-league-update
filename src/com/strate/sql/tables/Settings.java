package com.strate.sql.tables;

import com.strate.constants.Language;
import com.strate.remote.riot.constants.Region;
import com.strate.sql.Table;
import com.strate.sql.databases.Config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Settings extends Table {

    private Language language;

    private Region region;

    private String apiKey;

    private boolean notification;

    private String hostAddress;

    private int port;

    private String queryUsername;

    private String queryPassword;

    private int channelId;

    private boolean valid;

    public Settings () {
        super("settings", new Config(),
                "CREATE TABLE IF NOT EXISTS settings (\n" +
                        "id integer PRIMARY KEY,\n" +
                        "language text NOT NULL,\n" +
                        "region text NOT NULL,\n" +
                        "apiKey text NOT NULL,\n" +
                        "notification boolean NOT NULL,\n" +
                        "hostAddress text NOT NULL,\n" +
                        "port integer NOT NULL,\n" +
                        "queryUsername text NOT NULL,\n" +
                        "queryPassword text NOT NULL,\n" +
                        "channelId integer NOT NULL\n" +
                        ");");
    }

    public void update (String key, String value) {
        if (!exists()) {
            try {
                Connection connection = getDatabase().getConnection();
                Statement statement = connection.createStatement();

                String sql = "INSERT INTO settings(language,region,apiKey,notification,hostAddress,port,queryUsername,queryPassword,channelId) VALUES(?,?,?,?,?,?,?,?,?)";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, "en");
                preparedStatement.setString(2, "euw");
                preparedStatement.setString(3, "");
                preparedStatement.setBoolean(4, true);
                preparedStatement.setString(5, "localhost");
                preparedStatement.setInt(6, 9987);
                preparedStatement.setString(7, "serveradmin");
                preparedStatement.setString(8, "");
                preparedStatement.setInt(9, 1);
            } catch (SQLException e) {
                // handle errors
            }
        }

        try {
            // get database connection
            Connection connection = getDatabase().getConnection();
            Statement statement = connection.createStatement();

            // execute sql statement
            String sql = "UPDATE settings SET " + key + " = " + value + " WHERE id = 0";
            statement.execute(sql);

            // close database connection
            getDatabase().closeConnection();
        } catch (SQLException e) {
            // handle errors
        }

    }

    public boolean exists () {
        return false;
    }
}