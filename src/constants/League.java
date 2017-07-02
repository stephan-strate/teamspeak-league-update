package constants;

/**
 * Represents the League of a player in League
 * of Legends. You can compare a league with another.
 *
 * @author  Stephan Strate (development@famstrate.com)
 * @since   2.0.0
 */
public enum League {
    UNRANKED(0, "UNRANKED"),
    BRONCE(1, "BRONCE"),
    SILVER(2, "SILVER"),
    GOLD(3, "GOLD"),
    PLATINUM(4, "PLATINUM"),
    DIAMOND(5, "DIAMOND"),
    MASTER(6, "MASTER"),
    CHALLENGER(7, "CHALLENGER");

    private int id;
    private String name;

    League (int id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * You can create a league by a name.
     *
     * @param name  League's name
     *
     * @return  Fitting league to name
     */
    public static League getLeagueByName (String name) {
        for (League league : League.values()) {
            if (league.getName().toLowerCase().equals(name.toLowerCase())) {
                return league;
            }
        }

        System.out.println("League not found.");
        return UNRANKED;
    }

    /**
     * Returns the league's id.
     *
     * @return  League's id
     */
    public int getId () {
        return id;
    }

    /**
     * Returns the league's name.
     *
     * @return  League's name
     */
    public String getName () {
        return name;
    }

    /**
     * Compares a league with another and
     * returns true/false.
     *
     * @param other Other league to compare
     *
     * @return  true if league is the same, false if not
     */
    public boolean equals (League other) {
        return getId() != other.getId();
    }

    /**
     * Returns the league's name.
     *
     * @return  League's name
     */
    @Override
    public String toString () {
        return name;
    }
}