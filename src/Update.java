import net.rithms.riot.api.RiotApi;
import net.rithms.riot.api.RiotApiException;
import net.rithms.riot.constant.Region;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *  @author   Stephan Strate (development@famstrate.com)
 *  @version  2.0.0 (last update: 15.06.2017)
 */
public class Update {

    /**
     *  Requesting League of Legends Solo/Duo Queue Tier from
     *  riot api and returning a unique int identifier.
     *
     *  @param id  leagueIdentifier
     *
     *  @return serverTier as Integer
     */
    public static int serverTier (long id) {
        try {
            // initializing connection to riot api
            RiotApi api = new RiotApi(Config.LEAGUE_API);

            // requesting tier
            String serverTier = api.getLeagueEntryBySummoner(Region.EUW, id).get(0).getTier();

            // analysing tier and returning int identifier
            switch (serverTier) {
                case "UNRANKED":
                    return LeagueTier.UNRANKED;     // deprecated, can't be reached
                case "BRONCE":
                    return LeagueTier.BRONCE;
                case "SILVER":
                    return LeagueTier.SILVER;
                case "GOLD":
                    return LeagueTier.GOLD;
                case "PLATINUM":
                    return LeagueTier.PLATINUM;
                case "DIAMOND":
                    return LeagueTier.DIAMOND;
                case "MASTER":
                    return LeagueTier.MASTER;
                case "CHALLENGER":
                    return LeagueTier.CHALLENGER;
                default:
                    return LeagueTier.UNKNOWN;
            }
        } catch (RiotApiException e) {}

        /*
         * here we need a little description to understand, why we return UNRANKED instead of UNKNOWN:
         * here we can assume that the League of Legends account, we check, is valid, because we tested it
         * as the user assigned into the database already. So riot gives us a exception, when the player
         * is unranked, or if it isn't reachable.
         */
        return LeagueTier.UNRANKED;
    }


    /**
     *  Requesting the unique League of Legends account identifier by
     *  summoner name.
     *
     *  @param name  League of Legends summoner name
     *
     *  @return leagueIdentifier from RiotGames as long
     */
    public static long leagueIdentifier (String name) {
        try {
            // initializing connection to riot api
            RiotApi api = new RiotApi(Config.LEAGUE_API);

            // returning identifier
            return api.getSummonerByName(Region.EUW, name).getId();

        } catch (RiotApiException e) {
            e.printStackTrace();
        }

        return Config.UNKNOWN;
    }


    /**
     *  Requesting the League of Legends unique identifier out
     *  of our own MySQL database, if exists.
     *
     *  @param id  unique client id
     *
     *  @return leagueIdentifier from our database
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

        return Config.UNKNOWN;
    }


    /**
     *  Updating/inserting the users League of Legends summoner account into
     *  our MySQL database.
     *
     *  @param name  League of Legends summoner name
     *  @param uid   client unique identifier
     *  @param id    league unique identifier
     *
     *  @return return status as boolean
     */
    public static boolean editDatabaseIdentifier (String name, String uid, long id) {
        // calling MySQL instance
        Mysql.con = Mysql.getInstance();

        if (id == Config.UNKNOWN)
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
                sql = "INSERT INTO " + Config.MYSQL_DB + ".account (client, leagueName, leagueId) VALUES ('" +
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