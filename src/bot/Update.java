package bot;

import constants.League;
import constants.Propertie;
import constants.Region;
import requests.Mysql;
import requests.RiotApi;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author  Stephan Strate (development@famstrate.com)
 * @since   1.0.0
 */
public class Update {

    /**
     * Requesting League of Legends Solo/Duo Queue Tier from
     * riot api and returning a unique int identifier.
     *
     * @param id    leagueIdentifier
     *
     * @return  serverTier as League
     */
    public static League serverTier (long id) {
        // initializing connection to riot api
        RiotApi api = new RiotApi(Initialize.configuration.get(Propertie.API), Region.getRegionByName(Initialize.configuration.get(Propertie.REGION)));

        // requesting tier
        return api.getLeagueBySummonerId(id);
    }


    /**
     * Requesting the unique League of Legends account identifier by
     * summoner name.
     *
     * @param name  League of Legends summoner name
     *
     * @return  leagueIdentifier from RiotGames as long
     */
    public static long leagueIdentifier (String name) {
        // initializing connection to riot api
        RiotApi api = new RiotApi(Initialize.configuration.get(Propertie.API), Region.getRegionByName(Initialize.configuration.get(Propertie.REGION)));

        // returning identifier
        return api.getIdBySummonerName(name);
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

                    System.out.println(">> Database update for " + uid + " to League of Legends" +
                            "name " + name);

                    return true;
                }

                // does not exist -> inserting into database
                sql = "INSERT INTO " + Initialize.configuration.get(Propertie.MYSQLDB) + ".account (client, leagueName, leagueId) VALUES ('" +
                        uid + "', '" + name + "', '" + id + "')";
                int update = query.executeUpdate(sql);

                System.out.println(">> Database update for " + uid + " to League of Legends" +
                        "name " + name);

                return true;

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }
}