package com.strate.remote.riot.constants;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>Represents a League of Legends League.
 * You can whether get a {@link League} by name
 * or compare them.</p>
 * @author Stephan Strate
 * @since 3.0.0
 */
public enum League {

    UNRANKED (0, "Unranked"),
    BRONCE (1, "Bronce"),
    SILVER (2, "Silver"),
    GOLD (3, "Gold"),
    PLATINUM (4, "Platinum"),
    DIAMOND (5, "Diamond"),
    MASTER (6, "Master"),
    CHALLENGER (7, "Challenger");

    /**
     * <p>Primary key/id of
     * enum type.</p>
     * @since 3.0.0
     */
    private int id;

    /**
     * <p>League names as string.</p>
     * @since 3.0.0
     */
    private String name;

    /**
     * <p>Represents a League of Legends
     * League with order id and a name.</p>
     * @param id    order id from 0 (lowest) to 7 (highest)
     * @param name  english name (normal case)
     * @since 3.0.0
     */
    League (int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * <p>Returns {@link League} by name. Not case
     * sensitive. Returns {@see League.UNRANKED} when
     * league can not be found.</p>
     * @param name  english name
     * @return      {@link League}
     * @since 3.0.0
     */
    public static League getLeagueByName (String name) {
        for (League league : League.values()) {
            if (league.getName().toLowerCase().equals(name.toLowerCase())) {
                return league;
            }
        }

        System.out.println("[" + new Date().toString() + "][tlu] League not found.");
        return UNRANKED;
    }

    /**
     * <p>Compares order ids of two
     * {@link League} objects and returns
     * {@code true} when they are equal.</p>
     * @param other     {@link League}
     * @return          {@code true} when equals
     * @since 3.0.0
     */
    public boolean equals (League other) {
        return this.getId() == other.getId();
    }

    /**
     * <p>Compares order ids of two
     * {@link League}. Returns 1 when
     * other is lower, 0 when equal and
     * -1 when other is higher.</p>
     * @param other     {@link League}
     * @return          int
     * @since 3.0.0
     */
    public int compare (League other) {
        if (this.getId() > other.getId()) {
            return 1;
        } else if (this.equals(other)) {
            return 0;
        } else {
            return -1;
        }
    }

    /**
     * <p>Returns the name of {@link League}.</p>
     * @return  name as string
     * @since 3.0.0
     */
    @Override
    public String toString () {
        return name;
    }

    /**
     * <p>Returns the id.</p>
     * @return  id
     * @since 3.0.0
     */
    public int getId () {
        return id;
    }

    /**
     * <p>Returns the name.</p>
     * @return  name
     * @since 3.0.0
     */
    public String getName () {
        return name;
    }

    /**
     * <p>Returns all leagues as {@link List}.</p>
     * @return  {@link List}
     * @since 3.0.0
     */
    public static List<League> getAllLeagues () {
        List<League> leagues = new LinkedList<>();
        for (League league : League.values()) {
            leagues.add(league);
        }

        return leagues;
    }
}