package requests;

import bot.Initialize;
import constants.League;
import constants.Propertie;
import constants.Region;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Can perform api requests to Riot Games.
 *
 * @author  Stephan Strate (development@famstrate.com)
 * @since   2.0.0
 */
public class RiotApi {
    private String key;
    private Region region;

    public RiotApi (String key, Region region) {
        System.out.println("Creating new Riot api...");
        this.key = key;
        this.region = region;
    }

    /**
     * Returns the unique account id of a summoner name,
     * or 0 if account doesn't exist.
     *
     * @param username  Username to search for
     *
     * @return  Unique account id
     */
    private long idBySummonerName (String username) {
        try {
            // preventing whitespaces in url
            username = URLEncoder.encode(username, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // performing http request
        Http http = new Http(region.getHost() + "/lol/summoner/v3/summoners/by-name/" +
                username + "?api_key=" + key);

        try {
            JSONParser jsonParser = new JSONParser();
            // parsing json
            JSONObject jsonObject = (JSONObject) jsonParser.parse(http.getResponse());
            return (long) jsonObject.get("id");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * Requesting the unique League of Legends account identifier by
     * summoner name.
     *
     * @param name  League of Legends summoner name
     *
     * @return  leagueIdentifier from RiotGames as long
     */
    public static long getIdBySummonerName (String name) {
        // initializing connection to riot api
        RiotApi api = new RiotApi(Initialize.configuration.get(Propertie.API), Region.getRegionByName(Initialize.configuration.get(Propertie.REGION)));

        // returning identifier
        return api.idBySummonerName(name);
    }

    /**
     * Returns the league of a summoner account id, or
     * UNRANKED if account doesn't exist.
     *
     * @param id    Unique account id
     *
     * @return  League of the account id
     */
    private League leagueBySummonerId (long id) {
        // performing http request
        Http http = new Http(region.getHost() + "/lol/league/v3/leagues/by-summoner/" +
                id + "?api_key=" + key);

        try {
            JSONParser jsonParser = new JSONParser();
            // parsing json
            JSONArray jsonArray = (JSONArray) jsonParser.parse(http.getResponse());
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
            return League.getLeagueByName((String) jsonObject.get("tier"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return League.UNRANKED;
    }

    /**
     * Requesting League of Legends Solo/Duo Queue Tier from
     * riot api and returning a unique int identifier.
     *
     * @param id    leagueIdentifier
     *
     * @return  serverTier as League
     */
    public static League getLeagueBySummonerId (long id) {
        // initializing connection to riot api
        RiotApi api = new RiotApi(Initialize.configuration.get(Propertie.API), Region.getRegionByName(Initialize.configuration.get(Propertie.REGION)));

        // requesting tier
        return api.leagueBySummonerId(id);
    }
}