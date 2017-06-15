import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *  @author   Stephan Strate (development@famstrate.com)
 *  @version  2.0.0 (last update: 15.06.2017)
 */
public class Mysql {

    public static Connection con = null;

    /**
     *  Initializing a MySQL connection to database
     *  that's defined in {@link Config}.
     */
    public Mysql () {
        try {
            con = DriverManager.getConnection("jdbc:mysql://" + Config.MYSQL_HOST +
                    ":" + Config.MYSQL_PORT + "/" + Config.MYSQL_DB + "?"+"user=" + Config.MYSQL_USER +
                    "&"+"password=" + Config.MYSQL_PASS + "&autoReconnect=true&useSSL=false");
        } catch (SQLException e) {
            System.out.println("Verbindung nicht moglich");
            System.out.println("SQLException: " + e.getMessage());
            System.out.println("SQLState: " + e.getSQLState());
            System.out.println("VendorError: " + e.getErrorCode());
        }
    }

    /**
     *  Calling the MySQL connection, checking if it's still
     *  active or initializing a new connection
     *
     *  @return connection
     */
    public static Connection getInstance () {
        new Mysql();
        return con;
    }
}