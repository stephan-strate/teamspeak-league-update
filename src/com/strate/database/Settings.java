package com.strate.database;

import com.strate.constants.Ansi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * <p>[description]</p>
 * @author Stephan Strate
 * @since 3.0.0
 */
public class Settings extends Table {

    private static final String TABLE =
            "CREATE TABLE IF NOT EXISTS settings (\n" +
            " id integer PRIMARY KEY,\n" +
            " language text NOT NULL,\n" +
            " region text NOT NULL,\n" +
            " riot_key text NOT NULL,\n" +
            " notifications boolean NOT NULL,\n" +
            " teamspeak_host text NOT NULL,\n" +
            " teamspeak_port integer NOT NULL,\n" +
            " teamspeak_queryname text NOT NULL,\n" +
            " teamspeak_querypass text NOT NULL,\n" +
            " channel_id integer NOT NULL,\n" +
            ");";

    /**
     * <p>[description]</p>
     * @since 3.0.0
     */
    public Settings () {
        super("settings", TABLE);
    }

    public void insert (String language, String region, String riot_key, boolean notifications, String host, int port, String name, String password, int channelid) {
        String request =
                "INSERT INTO settings(language,region,riot_key,notifications,teamspeak_host,teamspeak_port,teamspeak_queryname,teamspeak_querypass,channel_id) " +
                        "VALUES(?,?,?,?,?,?,?,?,?)";

        Sql sql = new Sql("settings.db");
        Connection con = sql.getCon();
        try {
            PreparedStatement pstmt = con.prepareStatement(request);
            pstmt.setString(1, language);
            pstmt.setString(2, region);
            pstmt.setString(3, riot_key);
            pstmt.setBoolean(4, notifications);
            pstmt.setString(5, host);
            pstmt.setInt(6, port);
            pstmt.setString(7, name);
            pstmt.setString(8, password);
            pstmt.setInt(9, channelid);
            pstmt.execute();
        } catch (SQLException e) {
            System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Could not import settings, please try again.");
            System.out.println(e);
        }
    }
}