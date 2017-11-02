package com.strate.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * <p>[description]</p>
 * @author Stephan Strate
 * @since 3.0.0
 */
public class Users extends Table {

    private static final String TABLE =
            "CREATE TABLE IF NOT EXISTS users (\n" +
            " id integer PRIMARY KEY, \n" +
            " original_summoner text NOT NULL, \n" +
            " summoner_id num NOT NULL, \n" +
            " teamspeak_id num NOT NULL, \n" +
            " created date NOT NULL, \n" +
            " changed date NOT NULL, \n" +
            " restricted boolean NOT NULL\n" +
            ");";

    /**
     * <p>[description]</p>
     * @since 3.0.0
     */
    public Users () {
        super("users", TABLE);
    }

    /**
     * <p>[description]</p>
     * @param id
     * @return
     * @since 3.0.0
     */
    public long getSummonerIdByTeamspeakId (String id) {
        String request =
                "SELECT summoner_id FROM users WHERE teamspeak_id = " + id;

        try {
            Statement stmt = getCon().createStatement();
            ResultSet rs = stmt.executeQuery(request);

            if (rs.next()) {
                return rs.getInt("summoner_id");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return 0;
    }
}