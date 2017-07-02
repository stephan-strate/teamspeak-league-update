package requests;

import bot.Initialize;
import constants.Propertie;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
            con = DriverManager.getConnection("jdbc:mysql://" + Initialize.configuration.get(Propertie.MYSQLHOST) +
                    ":" + Initialize.configuration.get(Propertie.MYSQLPORT) + "/" + Initialize.configuration.get(Propertie.MYSQLDB) + "?"+"user=" +
                    Initialize.configuration.get(Propertie.MYSQLUSER) + "&"+"password=" + Initialize.configuration.get(Propertie.MYSQLPASS) + "&autoReconnect=true&useSSL=false");
        } catch (SQLException e) {
            System.out.println("No connection to database possible.");
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }

    /**
     * Calling the MySQL connection, checking if it's still
     * active or initializing a new connection.
     *
     * @return  connection
     */
    public static Connection getInstance () {
        new Mysql();
        return con;
    }
}