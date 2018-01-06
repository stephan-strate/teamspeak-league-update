package requests;

import bot.Initialize;
import constants.Propertie;

import java.sql.*;

/**
 * Can perform mysql requests.
 *
 * @author  Stephan Strate (development@famstrate.com)
 * @since   1.0.0
 */
public class Mysql {

    public static Connection con = null;

    /**
     * Initializing a MySQL connection to database.
     */
    public Mysql () {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection("jdbc:mysql://" + Initialize.configuration.get(Propertie.MYSQLHOST) +
                    ":" + Initialize.configuration.get(Propertie.MYSQLPORT) + "/" + Initialize.configuration.get(Propertie.MYSQLDB) + "?"+"user=" +
                    Initialize.configuration.get(Propertie.MYSQLUSER) + "&"+"password=" + Initialize.configuration.get(Propertie.MYSQLPASS) + "&autoReconnect=true&useSSL=false");
        } catch (SQLException e) {
            System.out.println("No connection to database possible.");
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        } catch (Exception e) {

        }
    }

    /**
     * Calling the MySQL connection, checking if it's still
     * active or initializing a new connection.
     *
     * @return  connection
     */
    private static Connection getInstance () {
        new Mysql();
        return con;
    }

    /**
     * Requesting the League of Legends unique identifier out
     * of our own MySQL database, if exists.
     *
     * @param id    unique client id
     *
     * @return  leagueIdentifier from our database
     */
    public static long databaseIdentifier (String id) {
        // calling MySQL instance
        Mysql.con = Mysql.getInstance();

        // if MySQL connection is valid
        if (Mysql.con != null) {
            Statement query;
            try {
                // executing a MySQL request
                query = Mysql.con.createStatement();
                String sql = "SELECT * FROM account WHERE client = '" + id + "'";
                ResultSet result = query.executeQuery(sql);
                if (result.next()) {
                    // returning the unique identifier
                    return result.getLong("leagueId");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return 0;
    }

    /**
     * Updating/inserting the users League of Legends summoner account into
     * our MySQL database.
     *
     * @param name  League of Legends summoner name
     * @param uid   client unique identifier
     * @param id    league unique identifier
     *
     * @return  return status as boolean
     */
    public static boolean editDatabaseIdentifier (String name, String uid, long id) {
        // calling MySQL instance
        Mysql.con = Mysql.getInstance();

        if (id == 0)
            return false;

        // if MySQL connection is valid
        if (Mysql.con != null) {
            Statement query;
            try {                // executing a MySQL request

                query = Mysql.con.createStatement();
                String sql = "SELECT * FROM account WHERE client = '" + uid + "'";
                ResultSet result = query.executeQuery(sql);

                // exist already -> updating information
                if (result.next()) {

                    // restricted user
                    if (result.getBoolean("restricted")) {
                        return false;
                    }
                    sql = "UPDATE account SET leagueName = '" + name +
                            "',leagueId = '" + id + "' WHERE client = '" + uid + "'";
                    int update = query.executeUpdate(sql);

                    System.out.println("Database update for " + uid + " to League of Legends" +
                            "name " + name + "\n");

                    return true;
                }

                // does not exist -> inserting into database
                sql = "INSERT INTO " + Initialize.configuration.get(Propertie.MYSQLDB) + ".account (client, leagueName, leagueId) VALUES ('" +
                        uid + "', '" + name + "', '" + id + "')";
                int update = query.executeUpdate(sql);

                System.out.println("Database update for " + uid + " to League of Legends" +
                        "name " + name + "\n");

                return true;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }
}