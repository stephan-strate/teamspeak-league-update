package constants;

public enum Propertie {
    API("RIOTGAMESAPIKEY"),
    TSHOST("TEAMSPEAKHOST"),
    TSPORT("TEAMSPEAKPORT"),
    TSQUERYNAME("TEAMSPEAKQUERYNAME"),
    TSQUERYPASS("TEAMSPEAKQUERYPASS"),
    TSVIRTUALID("TEAMSPEAKVIRTUALID"),
    BOTNAME("BOTNAME"),
    BOTMSG("BOTREADYMSG"),
    BOTCHANNEL("BOTCHANNELID"),
    MYSQLHOST("MYSQLHOST"),
    MYSQLPORT("MYSQLPORT"),
    MYSQLDB("MYSQLDB"),
    MYSQLUSER("MYSQLUSER"),
    MYSQLPASS("MYSQLPASS"),
    REGION("REGION"),

    UNRANKED("UNRANKED"),
    BRONCE("BRONCE"),
    SILVER("SILVER"),
    GOLD("GOLD"),
    PLATINUM("PLATINUM"),
    DIAMOND("DIAMOND"),
    MASTER("MASTER"),
    CHALLENGER("CHALLENGER");

    private String name;

    Propertie (String name) {
        this.name = name;
    }

    public static Propertie getPropertieByName (String name) {
        for (Propertie propertie : Propertie.values()) {
            if (propertie.getName().toLowerCase().equals(name.toLowerCase())) {
                return propertie;
            }
        }

        return null;
    }

    public String getName () {
        return name;
    }
}