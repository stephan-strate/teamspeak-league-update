package requests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Open a connection to any domain and
 * returning the response to another class.
 *
 * @author  Stephan Strate (development@famstrate.com)
 * @since   2.0.0
 */
public class Http {
    private final String USER_AGENT = "Mozilla/5.0";
    private URL url;
    private HttpURLConnection con;

    public Http (String url) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returning the set URL.
     *
     * @return  Formated URL object
     */
    public URL getUrl () {
        return url;
    }

    /**
     * Gives back the connection as a String and
     * closes the connection after the response.
     *
     * @return  Response
     */
    public String getResponse () {
        try {
            // Open connection
            con = (HttpURLConnection) url.openConnection();

            // Set method
            con.setRequestMethod("GET");

            // Set user agent
            con.setRequestProperty("User-Agent", USER_AGENT);

            // Get response code
            int responseCode = con.getResponseCode();
            System.out.println("Sending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            // Read response
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            // Close connection
            in.close();
            return response.toString();
        } catch (IOException e) {
            System.out.println("Not able to open a connection.");
            e.printStackTrace();
        }

        return null;
    }
}