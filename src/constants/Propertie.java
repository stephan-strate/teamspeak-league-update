package constants;

public enum Propertie {
    API("RIOTGAMESAPIKEY", ""),
    TSHOST("TEAMSPEAKHOST", "localhost"),
    TSPORT("TEAMSPEAKPORT", "9987"),
    TSQUERYNAME("TEAMSPEAKQUERYNAME", "serveradmin"),
    TSQUERYPASS("TEAMSPEAKQUERYPASS", ""),
    TSVIRTUALID("TEAMSPEAKVIRTUALID", "1"),
    BOTNAME("BOTNAME", "Nocturne"),
    BOTMSG("BOTREADYMSG", "Ready."),
    BOTCHANNEL("BOTCHANNELID", "1"),
    MYSQLHOST("MYSQLHOST", "localhost"),
    MYSQLPORT("MYSQLPORT", "3306"),
    MYSQLDB("MYSQLDB", "ts3_data"),
    MYSQLUSER("MYSQLUSER", "bot"),
    MYSQLPASS("MYSQLPASS", ""),
    REGION("REGION", "euw"),

    UNRANKED("UNRANKED", "10"),
    BRONCE("BRONCE", "11"),
    SILVER("SILVER", "12"),
    GOLD("GOLD", "13"),
    PLATINUM("PLATINUM", "14"),
    DIAMOND("DIAMOND", "15"),
    MASTER("MASTER", "16"),
    CHALLENGER("CHALLENGER", "17"),

    REMINDER("REMINDER", "true"),
    NOTINDATABASE("NOTINDATABASE", "You can give us your summoner name and we update your league."),
    HELP("HELP", "I can't help you."),
    NAME("NAMEEXPLANATION", "You can update your name with !name [summoner name]"),
    SUCCESS("SUCCESS", "Your name got updated."),
    ERROR("ERROR", "Your name couldn't be updated."),
    COMMAND("COMMAND", "Can't find command.");

    private String name;
    private String value;

    Propertie (String name, String value) {
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

    public String getValue() {
        return value;
    }
}