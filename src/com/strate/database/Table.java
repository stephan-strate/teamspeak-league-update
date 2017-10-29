package com.strate.database;

import java.sql.*;

/* @TODO: Connecting at every interaction and closing connection after it. */
/* @TODO: Finish descriptions */
/* @TODO: Looking up best practices for "Statements" */
/* @TODO: Working on Error Logging */

/**
 * <p>[description]</p>
 * @author Stephan Strate
 */
abstract class Table {

    static final String TABLE = "";

    private Sql sql = null;
    private Connection con = null;

    private String database;

    /**
     * <p>[description]</p>
     * @param database
     * @param request
     */
    Table (String database, String request) {
        // assigning com.strate.database name, to make it available
        this.database = database;

        // make sure, that com.strate.database exists
        create(database, request);
    }

    /**
     * <p>[description]</p>
     * @param database
     * @param request
     */
    private void create (String database, String request) {
        sql = new Sql(database + ".db");

        try {
            con = sql.getCon();
            Statement stmt = con.createStatement();
            stmt.execute(request);

        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * <p>[description]</p>
     */
    public void connect () {

    }

    /**
     * <p>[description]</p>
     * @param params
     */
    public void insert (String[] params) {

    }

    /**
     * <p>[description]</p>
     */
    public void update () {

    }

    /**
     * <p>[description]</p>
     */
    public void delete () {

    }

    /**
     * <p>[description]</p>
     */
    public void showAll () {
        String request =
                "SELECT * FROM " + database;

        try {
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
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    /**
     * <p>[description]</p>
     * @return
     */
    public Sql getSql () {
        return sql;
    }

    /**
     * <p>[description]</p>
     * @return
     */
    public Connection getCon () {
        return con;
    }
}