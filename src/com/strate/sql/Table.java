package com.strate.sql;

import java.sql.*;
import java.util.Date;

/**
 * <p>Represents sqlite tables that are
 * stored in {@link Database}.</p>
 * @author Stephan Strate
 * @since 3.0.0
 */
public abstract class Table {

    /**
     * <p>Name of table.</p>
     * @since 3.0.0
     */
    private String name;

    /**
     * <p>Database the table is stored in.</p>
     * @since 3.0.0
     */
    private Database database;

    /**
     * <p>Creates/represents sql tables. You can use some
     * default methods, that work for every table. And manage
     * sql connection easily.</p>
     * @param name      table name
     * @param database  sql object
     * @param sql       sql expression
     * @since 3.0.0
     */
    public Table (String name, Database database, String sql) {
        this.name = name;
        this.database = database;

        try {
            // get connection
            Connection connection = database.getConnection();

            // execute statement
            Statement statement = connection.createStatement();
            statement.execute(sql);

            // close connection
            database.closeConnection();
        } catch (SQLException e) {
            System.err.println("[" + new Date().toString() + "][tlu] Can not create sql statement.");
        }
    }

    /**
     * <p>Print all sql rows (can take a
     * while, depending on table size).</p>
     * @since 3.0.0
     */
    public void showAll () {
        String sql =
                "SELECT * FROM " + name;

        try {
            // get connection
            Connection connection = database.getConnection();

            // execute statement
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            // get columns number
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnsNumber = resultSetMetaData.getColumnCount();

            // print whole sql
            System.out.println("Printing " + name + ":");
            while (resultSet.next()) {
                for (int i = 1; i < (columnsNumber + 1); i++) {
                    System.out.print(resultSet.getString(i) + "  ");
                }
                System.out.println();
            }

            // close connection
            database.closeConnection();
        } catch (SQLException e) {
            System.err.println("[" + new Date().toString() + "][tlu] Can not show elements of sql " + name);
        }
    }

    /**
     * <p>Prints the last x rows.</p>
     * @param rows  number of rows
     * @since 3.0.0
     */
    public void showLast (int rows) {
        String sql =
                "SELECT * FROM (SELECT * FROM " + name + " ORDER BY id DESC LIMIT " + rows + ") ORDER BY id ASC";

        try {
            // get connection
            Connection connection = getDatabase().getConnection();

            // execute statement
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            // get columns number
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnsNumber = resultSetMetaData.getColumnCount();

            // print last i rows
            int i = rows;
            while (resultSet.next() && i > 0) {
                for (int x = 1; x < (columnsNumber + 1); x++) {
                    System.out.print(resultSet.getString(x) + "  ");
                }
                System.out.println();

                i--;
            }
        } catch (SQLException e) {
            System.err.println("[" + new Date().toString() + "][tlu] Can not show elements of sql " + name);
        }
    }

    /**
     * <p>Drop whole table.</p>
     * @since 3.0.0
     */
    public void drop () {
        String sql =
                "DROP TABLE " + name;

        try {
            Connection connection = getDatabase().getConnection();
            Statement statement = connection.createStatement();
            statement.execute(sql);
            getDatabase().closeConnection();
        } catch (SQLException e) {
            System.err.println("[" + new Date().toString() + "][tlu] Can not execute.");
        }
    }

    /**
     * <p>Get the sql name.</p>
     * @return  {@code name}
     * @since 3.0.0
     */
    public String getName () {
        return name;
    }

    /**
     * <p>Get the sql object.</p>
     * @return  {@code sql}
     * @since 3.0.0
     */
    public Database getDatabase () {
        return database;
    }
}