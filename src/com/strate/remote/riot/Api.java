package com.strate.remote.riot;

import com.strate.constants.Ansi;
import com.strate.remote.Http;
import com.strate.remote.riot.constants.League;
import com.strate.remote.riot.constants.Region;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * <p>Perform api requests to riot games api. Only specific
 * api calls are defined, which are used in this application.
 * It does not cover all api calls possible.</p>
 * @author Stephan Strate
 * @since 3.0.0
 */
public class Api {

    private String key;
    private Region region;

    /**
     * <p>Create a new {@link Api} object with defined region and
     * api key. There is no default validation of the api key. You can
     * do this by using {@see isKeyValid}.</p>
     * @param key       Riot Games api key
     * @param region    League of Legends region
     * @since 3.0.0
     */
    public Api (String key, Region region) {
        this.key = key;
        this.region = region;
    }

    /**
     * <p>Returns {@code true} when passed api key is valid or an internal
     * error occurs.</p>
     * @param key   Riot Games api key
     * @return  {@code true} when api key is valid
     * @since 3.0.0
     */
    public boolean isKeyValid (String key) {
        try {
            Http http = new Http(region.getBaseUrl() + "/lol/summoner/v3/summoners/by-account/" +
                    "?api_key=" + URLEncoder.encode(key, "UTF-8"));

            int statusCode = http.getStatusCode() != null ? http.getStatusCode().getCode() : 0;
            // 403: forbidden => api key not valid, 0: request failed => can not check if key is valid
            return statusCode != 403 && statusCode != 0;
        } catch (UnsupportedEncodingException e) {
            System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Can not verify key, caused by an internal error. Continue setup.");
        }

        // still returning true, to not bother the user
        return true;
    }

    /**
     * <p>Gets the summoner id of a League of Legends summoner name
     * to identify the user permanently, even if he/she changes the summoner
     * name. Returns {@code -1} when something went wrong.</p>
     * @param summonerName  summoner name
     * @return  summoner id
     * @since 3.0.0
     */
    public long getIdBySummonerName (String summonerName) {
        try {
            summonerName = URLEncoder.encode(summonerName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Summoner name " + summonerName + " could not be parsed.");
        }

        Http http = new Http(region.getBaseUrl() + "/lol/summoner/v3/summoners/by-name/" +
                summonerName + "?api_key=" + key);

        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(http.getResponse());

            return (long) jsonObject.get("id");
        } catch (ParseException e) {
            System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Summoner name could not be checked. Wrong data.");
        }

        return -1;
    }

    /**
     * <p>Gets the summoners highest 5x5 {@link League}. If something went wrong it
     * will return {@code League.UNRANKED}.</p>
     * @param id    summoner id (can be get with {@see getIdBySummonerName}
     * @return  summoners highest {@link League}
     * @since 3.0.0
     */
    public League getLeagueById (long id) {
        Http http = new Http(region.getBaseUrl() + "/lol/league/v3/leagues/by-summoner/" +
                id + "?api_key=" + key);

        try {
            JSONParser jsonParser = new JSONParser();
            JSONArray jsonArray = (JSONArray) jsonParser.parse(http.getResponse());
            JSONObject jsonObject;

            int i = 0;
            League solo = null, flex = null;
            while ((jsonObject = (JSONObject) jsonArray.get(i)) != null) {
                Object temp = jsonObject.get("queue");
                if (temp.equals("RANKED_SOLO_5x5")) {
                    solo = League.getLeagueByName((String) temp);
                } else if (temp.equals("RANKED_FLEX_5x5")) {
                    flex = League.getLeagueByName((String) temp);
                }

                i = i + 1;
            }

            if (solo != null && flex != null) {
                switch (solo.compare(flex)) {
                    case 1: return solo;
                    case 0: return solo;
                    case -1: return flex;
                }
            } else if (solo != null) {
                return solo;
            } else if (flex != null) {
                return flex;
            } else {
                return League.UNRANKED;
            }
        } catch (ParseException e) {
            System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Summoner league could not be checked. Wrong data.");
        }

        return League.UNRANKED;
    }

    /**
     * <p>Returns the api key.</p>
     * @return  api key
     * @since 3.0.0
     */
    public String getKey () {
        return key;
    }

    /**
     * <p>Returns the {@link Region}.</p>
     * @return  {@link Region}
     * @since 3.0.0
     */
    public Region getRegion () {
        return region;
    }

    /**
     * <p>Sets the api key.</p>
     * @param key   api key
     * @since 3.0.0
     */
    public void setKey (String key) {
        this.key = key;
    }

    /**
     * <p>Set the {@link Region}.</p>
     * @param region    {@link Region}
     * @since 3.0.0
     */
    public void setRegion (Region region) {
        this.region = region;
    }
}