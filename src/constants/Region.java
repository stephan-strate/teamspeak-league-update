package constants;

/**
 * Represents the Riot Games' League of Legends regions
 * and can return things like the endpoint of a region.
 *
 * @author  Stephan Strate (development@famstrate.com)
 * @since   2.0.0
 */
public enum Region {
    BR("BR1", "br"),
    EUNE("EUN1", "eune"),
    EUW("EUW1", "euw"),
    JP("JP1", "jp"),
    KR("KR", "kr"),
    LAN("LA1", "lan"),
    LAS("LA2", "las"),
    NA("NA1", "na"),
    OCE("OC1", "oce"),
    PBE("PBE1", "pbe"),
    RU("RU", "ru"),
    TR("TR1", "tr");

    private String id;
    private String name;

    Region (String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * You can create a region by a name.
     *
     * @param name  Region's name
     *
     * @return  Fitting region to name
     */
    public static Region getRegionByName (String name) {
        for (Region region : Region.values()) {
            if (region.getName().equals(name.toLowerCase())) {
                return region;
            }
        }

        System.out.println("Region not found.");
        return EUW;
    }

    /**
     * Returns the region's id.
     *
     * @return  Region's id
     */
    public String getId () {
        return id;
    }

    /**
     * Returns the region's name.
     *
     * @return  Region's name
     */
    public String getName () {
        return name;
    }

    /**
     * Returns the region's host.
     *
     * @return  Region's host
     */
    public String getHost () {
        return "https://" + getId().toLowerCase() + ".api.riotgames.com";
    }

    /**
     * Returns the region's name.
     *
     * @return  Region's name
     */
    @Override
    public String toString () {
        return name;
    }
}