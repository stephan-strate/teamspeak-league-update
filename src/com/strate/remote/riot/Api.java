package com.strate.remote.riot;

import com.strate.remote.Http;
import com.strate.remote.riot.constants.League;
import com.strate.remote.riot.constants.Region;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/* @TODO: Working on Error Logging */

/**
 * <p>[description]</p>
 * @author Stephan Strate
 */
public class Api {

    private String key;
    private Region region;

    /**
     * <p>[description]</p>
     * @param key
     * @param region
     */
    public Api (String key, Region region) {
        this.key = key;
        this.region = region;
    }

    /**
     * <p>[description]</p>
     * @return
     */
    public String getKey () {
        return key;
    }

    /**
     * <p>[description]</p>
     * @return
     */
    public Region getRegion () {
        return region;
    }

    /**
     * <p>[description]</p>
     * @param key
     */
    public void setKey (String key) {
        this.key = key;
    }

    /**
     * <p>[description]</p>
     * @param region
     */
    public void setRegion (Region region) {
        this.region = region;
    }

    /**
     * <p>[description]</p>
     * @param summonerName
     * @return
     */
    public long getIdBySummonerName (String summonerName) {
        try {
            summonerName = URLEncoder.encode(summonerName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println(e);
        }

        Http http = new Http(region.getBaseUrl() + "/lol/summoner/v3/summoners/by-name/" +
                summonerName + "?api_key=" +  key);

        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(http.getResponse());

            return (long) jsonObject.get("id");
        } catch (ParseException e) {
            System.out.println(e);
        }

        return 0;
    }

    /**
     * <p>[description]</p>
     * @param id
     * @return
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
            System.out.println(e);
        }

        return League.UNRANKED;
    }

    /**
     * <p>[description]</p>
     * @param key
     * @return
     */
    public boolean isKeyValid (String key) {
        try {
            Http http = new Http(region.getBaseUrl() + "/lol/summoner/v3/summoners/by-account/test" +
                    "?api_key=" + URLEncoder.encode(key, "UTF-8"));

            int statusCode = http.getStatusCode();
            return statusCode != 403 && statusCode != 0;
        } catch (UnsupportedEncodingException e) {
            System.out.println(e);
        }

        return false;
    }
}