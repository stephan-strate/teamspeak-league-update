import java.io.*;
import java.util.Properties;

public class Propertie {
    private Properties prop;

    public String RIOTGAMESAPIKEY;
    public String TEAMSPEAKHOST;
    public String TEAMSPEAKPORT;
    public String TEAMSPEAKQUERYNAME;
    public String TEAMSPEAKQUERYPASS;
    public String TEAMSPEAKVIRTUALID;
    public String BOTNAME;
    public String BOTREADYMSG;
    public String BOTCHANNEL;
    public String MYSQLHOST;
    public String MYSQLPORT;
    public String MYSQLDB;
    public String MYSQLUSER;
    public String MYSQLPASS;


    public Propertie(String path) {
        prop = new Properties();
        loadFile(path);
    }

    private Properties getProp () {
        return prop;
    }

    private void loadFile (String path) {
        try {
            InputStream input = new FileInputStream(path);
            loadProperties(input);
        } catch (FileNotFoundException e) {
            createFile(path);
        }
    }

    private void createFile (String path) {
        File file = new File(path);
        setProperties(file);
        System.exit(0);
    }

    private void loadProperties (InputStream input) {
        try {
            prop.load(input);

            RIOTGAMESAPIKEY = prop.getProperty("RiotGamesApiKey");
            TEAMSPEAKHOST = prop.getProperty("TeamspeakHost");
            TEAMSPEAKPORT = prop.getProperty("TeamspeakPort");
            TEAMSPEAKQUERYNAME = prop.getProperty("TeamspeakQueryName");
            TEAMSPEAKQUERYPASS = prop.getProperty("TeamspeakQueryPass");
            TEAMSPEAKVIRTUALID = prop.getProperty("TeamspeakServerId");
            BOTNAME = prop.getProperty("BotName");
            BOTREADYMSG = prop.getProperty("BotReadyMsg");
            BOTCHANNEL = prop.getProperty("BotChannelId");
            MYSQLHOST = prop.getProperty("MysqlHost");
            MYSQLPORT = prop.getProperty("MysqlPort");
            MYSQLDB = prop.getProperty("MysqlDb");
            MYSQLUSER = prop.getProperty("MysqlUser");
            MYSQLPASS = prop.getProperty("MysqlPass");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setProperties (File file) {
        try {
            Properties properties = new Properties();

            String[] names = { "RiotGamesApiKey", "TeamspeakHost", "TeamspeakPort", "TeamspeakQueryName", "TeamspeakQueryPass",
                "TeamspeakServerId", "BotName", "BotReadyMsg", "BotChannelId", "MysqlHost", "MysqlPort", "MysqlDb", "MysqlUser", "MysqlPass" };
            for (int i = 0; i < names.length; i++) {
                properties.setProperty(names[i], "");
            }

            FileOutputStream output = new FileOutputStream(file);
            properties.store(output, "teamspeak-league-update properties");
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}