package com.strate.database;

import com.strate.constants.Ansi;
import java.sql.*;

/**
 * <p>Represents sqlite database table. You can
 * execute requests.</p>
 * @author Stephan Strate
 * @since 3.0.0
 */
abstract class Table {

    static final String TABLE = "";

    private Sql sql = null;
    private Connection con = null;

    private String database;

    /**
     * <p>Represents sqlite database table.</p>
     * @param database  database name
     * @param request   request string
     * @since 3.0.0
     */
    Table (String database, String request) {
        // assigning com.strate.database name, to make it available
        this.database = database;

        // make sure, that com.strate.database exists
        create(database, request);
    }

    /**
     * <p>Creating new database if needed.</p>
     * @param database  database name
     * @param request   request string
     * @since 3.0.0
     */
    private void create (String database, String request) {
        sql = new Sql(database + ".db");

        try {
            con = sql.connect();
            Statement stmt = con.createStatement();
            stmt.execute(request);
            sql.disconnect();

        } catch (SQLException e) {
            System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Database could not be created.");
        }
    }

    /**
     * <p>Shows database table for debug purposes.
     * Not used in production.</p>
     * @since 3.0.0
     */
    @Deprecated
    public void showAll () {
        String request =
                "SELECT * FROM " + database;

        try {
            con = sql.connect();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(request);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();

            System.out.println("Printing " + database + ":");
            while (rs.next()) {
                System.out.println();
                for (int i = 1; i <= columnsNumber; i++) {
                    System.out.print(rs.getString(i) + "  ");
                }
            }
            sql.disconnect();
        } catch (SQLException e) {
            System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Could not access database.");
        }
    }

    /**
     * <p>Returns the {@link Sql}.</p>
     * @return  {@link Sql}
     * @since 3.0.0
     */
    public Sql getSql () {
        return sql;
    }

    /**
     * <p>Returns the {@link Connection}.</p>
     * @return  {@link Connection}
     * @since 3.0.0
     */
    public Connection getCon () {
        return con;
    }

    /**
     * <p>Returns the database name.</p>
     * @return  database name
     * @since 3.0.0
     */
    public String getDatabase () {
        return database;
    }
}