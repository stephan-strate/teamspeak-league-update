package com.strate.sql.tables.events;

import com.strate.sql.EntryNotFoundException;
import com.strate.sql.Table;
import com.strate.sql.databases.Events;

import java.sql.*;

/**
 * <p>Users table. Used to store informations
 * about a registered user.</p>
 * @author Stephan Strate
 * @since 3.0.0
 */
public class Users extends Table {

    /**
     * <p>Default constructor. Defines the table.</p>
     * @since 3.0.0
     */
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

    /**
     * <p>Inserts a user to the table.</p>
     * @param teamspeakId   teamspeak unique identifier
     * @param teamspeakName teamspeak nickname
     * @param leagueId      league of legends unique identifier
     * @param leagueName    league of legends summoner name
     * @return  did inserting worked
     * @since 3.0.0
     */
    public boolean assign (String teamspeakId, String teamspeakName, long leagueId, String leagueName) {
        // check if valid league id
        if (leagueId != -1) {
            String sql = "SELECT * FROM users WHERE teamspeak_identifier = '" + teamspeakId + "'";
            try {
                Connection connection = getDatabase().getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql);

                if (resultSet.next()) {
                    // user already in database, updating data
                    sql = "UPDATE users SET teamspeak_nickname = '" + teamspeakName + "', league_identifier = " + leagueId + ", league_username = '" + leagueName + "' " +
                            "WHERE teamspeak_identifier = '" + teamspeakId + "'";
                    statement.execute(sql);
                } else {
                    // new user, inserting in database
                    sql = "INSERT INTO users(teamspeak_identifier, teamspeak_nickname, league_identifier, league_username, added, last_modified) " +
                            "VALUES(?, ?, ?, ?, datetime('now', 'localtime'), datetime('now', 'localtime'))";

                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, teamspeakId);
                    preparedStatement.setString(2, teamspeakName);
                    preparedStatement.setLong(3, leagueId);
                    preparedStatement.setString(4, leagueName);

                    preparedStatement.executeUpdate();
                }
                return true;
            } catch (SQLException e) {
                // handle errors
                return false;
            } finally {
                getDatabase().closeConnection();
            }
        }

        return false;
    }

    /**
     * <p>Returns the unique league of legends id.</p>
     * @param uid   unique teamspeak identifier
     * @return  unique league of legends id
     * @since 3.0.0
     */
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
            // do not interrupt user with errors
            return 0;
        } finally {
            getDatabase().closeConnection();
        }

        throw new EntryNotFoundException("User not found in database.");
    }

    public void updateName (String teamspeakId, String teamspeakName) {
        String sql = "UPDATE users SET teamspeak_nickname = '" + teamspeakName + "' WHERE teamspeak_identifier = '" + teamspeakId + "'";
        try {
            Connection connection = getDatabase().getConnection();
            Statement statement = connection.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            // handle errors
        } finally {
            getDatabase().closeConnection();
        }
    }
}