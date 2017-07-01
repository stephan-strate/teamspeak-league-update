package requests;

import constants.League;
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
    public long getIdBySummonerName (String username) {
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
     * Returns the league of a summoner account id, or
     * UNRANKED if account doesn't exist.
     *
     * @param id    Unique account id
     *
     * @return  League of the account id
     */
    public League getLeagueBySummonerId (long id) {
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
}