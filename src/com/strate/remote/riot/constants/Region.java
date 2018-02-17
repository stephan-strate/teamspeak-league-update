package com.strate.remote.riot.constants;

import java.util.Date;

/**
 * <p>Represents a riot games, league
 * of legends region. You can get a base
 * url depending on region or get all
 * regions.</p>
 * @author Stephan Strate
 * @since 3.0.0
 */
public enum Region {

    BR ("BR1", "br"),
    EUNE ("EUN1", "eune"),
    EUW ("EUW1", "euw"),
    JP ("JP1", "jp"),
    KR ("KR", "kr"),
    LAN ("LA1", "lan"),
    LAS ("LA2", "las"),
    NA ("NA1", "na"),
    OCE ("OC1", "oce"),
    PBE ("PBE1", "pbe"),
    RU ("RU", "ru"),
    TR ("TR1", "tr");

    private String endpoint;
    private String shortcut;

    /**
     * <p>Represents a region defined by
     * riot games.</p>
     * @param endpoint  api endpoint
     * @param shortcut  region shortcut
     * @since 3.0.0
     */
    Region (String endpoint, String shortcut) {
        this.endpoint = endpoint;
        this.shortcut = shortcut;
    }

    /**
     * <p>Fetch a region by shortcut. Static method
     * to get a {@see Region} object.</p>
     * @param shortcut  region shortcut
     * @return  {@see Region}
     * @since 3.0.0
     */
    public static Region getRegionByShortcut (String shortcut) {
        for (Region region : Region.values()) {
            if (region.getShortcut().equals(shortcut.toLowerCase())) {
                return region;
            }
        }

        System.out.println("[" + new Date().toString() + "][tlu] Region not found.");
        return null;
    }

    /**
     * <p>Get the endpoint.</p>
     * @return  {@code endpoint}
     * @since 3.0.0
     */
    public String getEndpoint () {
        return endpoint;
    }

    /**
     * <p>Get the shortcut.</p>
     * @return  {@code shortcut}
     * @since 3.0.0
     */
    public String getShortcut () {
        return shortcut;
    }

    /**
     * <p>Creates the base url for riot games
     * api, depending on region.</p>
     * @return  base url
     * @since 3.0.0
     */
    public String getBaseUrl () {
        return "https://" + getEndpoint().toLowerCase() + ".api.riotgames.com";
    }

    /**
     * <p>Get all regions in a string, for
     * settings as options.</p>
     * @return  all regions
     * @since 3.0.0
     */
    public static String getAllRegions () {
        String regions = "";
        for (Region region : Region.values()) {
            regions += region.getShortcut() + "/";
        }

        regions = regions.substring(0, regions.length() - 1);
        return regions;
    }
}