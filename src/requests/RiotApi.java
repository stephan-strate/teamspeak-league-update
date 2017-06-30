package requests;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class RiotApi {
    private String key;
    private String region;

    private final String type = "https://";
    private final String endpoint = ".api.riotgames.com/";

    public RiotApi (String key, String region) {
        this.key = key;
        this.region = region;
    }

    public long getIdBySummonerName (String username) {
        try {
            username = URLEncoder.encode(username, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Http http = new Http(type + region + endpoint + "lol/summoner/v3/summoners/by-name/" +
                username + "?api_key=" + key);

        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(http.getResponse());
            return (long) jsonObject.get("id");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int parseLeagueToId (String league) {
        switch (league) {
            case "BRONCE":
                return 0;

            case "SILVER":
                return 1;

            case "GOLD":
                return 2;

            case "PLATINUM":
                return 3;

            case "DIAMOND":
                return 4;

            case "MASTER":
                return 5;

            case "CHALLENGER":
                return 6;

            default:
                return -1;
        }
    }

    public int getLeagueBySummonerId (long id) {
        Http http = new Http(type + region + endpoint + "lol/league/v3/leagues/by-summoner/" +
                id + "?api_key=" + key);

        try {
            JSONParser jsonParser = new JSONParser();
            JSONArray jsonArray = (JSONArray) jsonParser.parse(http.getResponse());
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
            return parseLeagueToId((String) jsonObject.get("tier"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return -1;
    }
}