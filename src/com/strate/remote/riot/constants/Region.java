package com.strate.remote.riot.constants;

import com.strate.constants.Ansi;

/* @TODO: Finish descriptions */
/* @TODO: Working on Error Logging */

/**
 * <p>[description]</p>
 * @author Stephan Strate
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
     * <p>[description]</p>
     * @param endpoint
     * @param shortcut
     */
    Region (String endpoint, String shortcut) {
        this.endpoint = endpoint;
        this.shortcut = shortcut;
    }

    /**
     * <p>[description]</p>
     * @param shortcut
     * @return
     */
    public static Region getRegionByShortcut (String shortcut) {
        for (Region region : Region.values()) {
            if (region.getShortcut().equals(shortcut.toLowerCase())) {
                return region;
            }
        }

        System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RED + "Region not found." + Ansi.RESET);
        return null;
    }

    /**
     * <p>[description]</p>
     * @return
     */
    public String getEndpoint () {
        return endpoint;
    }

    /**
     * <p>[description]</p>
     * @return
     */
    public String getShortcut () {
        return shortcut;
    }

    /**
     * <p>[description]</p>
     * @return
     */
    public String getBaseUrl () {
        return "https://" + getEndpoint().toLowerCase() + ".api.riotgames.com";
    }

    /**
     * <p>[description]</p>
     * @return
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