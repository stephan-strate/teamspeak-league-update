package com.strate.remote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/* @TODO: Working on Error Logging */

/**
 * <p>[description]</p>
 * @author Stephan Strate
 */
public class Http {

    private static final String USER_AGENT = "Mozilla/5.0";

    private URL url;
    private HttpURLConnection con;

    /**
     * <p>[description]</p>
     * @param url
     */
    public Http (String url) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            System.out.println(e);
        }
    }

    /**
     * <p>[description]</p>
     * @return
     */
    public String getUserAgent () {
        return USER_AGENT;
    }

    /**
     * <p>[description]</p>
     * @return
     */
    public URL getUrl () {
        return url;
    }

    /**
     * <p>[description]</p>
     * @return
     */
    public HttpURLConnection getCon() {
        return con;
    }

    /**
     * <p>[description]</p>
     * @return
     */
    public String getResponse () {
        try {

            /* @TODO: Working on more fail safe request */

            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();
            return response.toString();
        } catch (IOException e) {
            System.out.println(e);
        }

        return null;
    }

    /**
     * <p>[description]</p>
     * @return
     */
    public int getStatusCode () {
        try {
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);

            return con.getResponseCode();
        } catch (IOException e) {
            System.out.println(e);
        }

        return 0;
    }
}