package com.strate.sql.tables.events;

import com.strate.sql.Table;
import com.strate.sql.databases.Events;

import java.sql.*;

public class Users extends Table {

    public Users () {
        super("users", new Events(),
                "CREATE TABLE IF NOT EXISTS users (\n" +
                        "id integer PRIMARY KEY,\n" +
                        "teamspeak_identifier text NOT NULL,\n" +
                        "teamspeak_nickname text NOT NULL,\n" +
                        "league_identifier long NOT NULL,\n" +
                        "league_username text NOT NULL,\n" +
                        "added text NOT NULL,\n" +
                        "last_modified text NOT NULL\n" +
                        ");");
    }

    public boolean assign (String teamspeakId, String teamspeakName, long leagueId, String leagueName) {
        if (leagueId != -1) {
            String sql = "SELECT * FROM users WHERE teamspeak_identifier = '" + teamspeakId + "'";
            try {
                Connection connection = getDatabase().getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);

                if (resultSet.next()) {
                    sql = "UPDATE users SET teamspeak_nickname = '" + teamspeakName + "', league_identifier = " + leagueId + ", league_username = '" + leagueName + "' " +
                            "WHERE teamspeak_identifier = '" + teamspeakId + "'";
                    statement.execute(sql);
                } else {
                    sql = "INSERT INTO users(teamspeak_identifier, teamspeak_nickname, league_identifier, league_username, added, last_modified) " +
                            "VALUES(?, ?, ?, ?, datetime('now', 'localtime'), datetime('now', 'localtime'))";

                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, teamspeakId);
                    preparedStatement.setString(2, teamspeakName);
                    preparedStatement.setLong(3, leagueId);
                    preparedStatement.setString(4, leagueName);

                    preparedStatement.executeUpdate();
                }

                getDatabase().closeConnection();
                return true;
            } catch (SQLException e) {
                // handle errors
                return false;
            }
        }

        return false;
    }

    public long getSummonerId (String uid) {
        String sql = "SELECT * FROM users WHERE teamspeak_identifier = '" + uid + "'";
        try {
            Connection connection = getDatabase().getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                return resultSet.getLong("league_identifier");
            }
        } catch (SQLException e) {
            return 0;
        }

        return 0;
    }
}