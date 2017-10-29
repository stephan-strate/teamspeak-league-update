package com.strate.remote.riot.constants;

/* @TODO: Finish descriptions */

/**
 * <p>[description]</p>
 * @author Stephan Strate
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
     * <p>[description]</p>
     * @param id
     * @param name
     */
    League (int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * <p>[description]</p>
     * @param name
     * @return
     */
    public static League getLeagueByName (String name) {
        for (League league : League.values()) {
            if (league.getName().toLowerCase().equals(name.toLowerCase())) {
                return league;
            }
        }

        /* @TODO: Creating custom exception for "League not found" */

        System.out.println("League not found.");
        return UNRANKED;
    }

    /**
     * <p>[description]</p>
     * @return
     */
    public int getId () {
        return id;
    }

    /**
     * <p>[description]</p>
     * @return
     */
    public String getName () {
        return name;
    }

    /**
     * <p>[description]</p>
     * @param other
     * @return
     */
    public boolean equals (League other) {
        return this.getId() == other.getId();
    }

    /**
     * <p>[description]</p>
     * @param other
     * @return
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
     * <p>[description]</p>
     * @return
     */
    @Override
    public String toString () {
        return name;
    }
}