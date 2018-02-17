package com.strate.remote.riot.constants;

import java.util.Date;

/**
 * <p>Represents a League of Legends League.
 * You can whether get a {@link League} by name
 * or compare them.</p>
 * @author Stephan Strate
 * @since 2.0.0
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

    private int id;
    private String name;

    /**
     * <p>Represents a League of Legends
     * League with order id and a name.</p>
     * @param id    order id from 0 (lowest) to 7 (highest)
     * @param name  english name (normal case)
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
     * @param other     {@Link League}
     * @return          {@code true} when equals
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
     */
    @Override
    public String toString () {
        return name;
    }

    /**
     * <p>Returns the id.</p>
     * @return  id
     */
    public int getId () {
        return id;
    }

    /**
     * <p>Returns the name.</p>
     * @return  name
     */
    public String getName () {
        return name;
    }
}