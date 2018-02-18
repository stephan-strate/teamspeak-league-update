package com.strate.setup;

import com.strate.remote.riot.Api;
import com.strate.remote.riot.constants.Region;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

/**
 * <p>Setup process for the riot game api
 * key. Reads, validates and stores the api key.</p>
 * @author Stephan Strate
 * @since 3.0.0
 */
public class ApiKey implements Setup {

    /**
     * <p>Default {@link BufferedReader} to read
     * user input. Used to read the riot api key.</p>
     * @since 3.0.0
     */
    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    /**
     * <p>Main property of this api key setup.</p>
     * @since 3.0.0
     */
    private String apiKey;

    /**
     * <p>Dependency to validate the api key.</p>
     * @since 3.0.0
     */
    private Region region;

    /**
     * <p>Assigning default api key and given region.</p>
     * @param region    region
     * @since 3.0.0
     */
    public ApiKey (Region region) {
        apiKey = "";
        this.region = region;
    }

    /**
     * <p>Reading the api key from console, checking if
     * it is valid and store it in properties file.</p>
     * @since 3.0.0
     */
    @Override
    public void execute () {
        try {
            boolean valid = false;
            Api api = new Api(apiKey, region);
            do {
                System.out.print("[" + new Date().toString() + "][tlu] Enter your Riot Games api key for " + region.getShortcut() + ": ");
                apiKey = br.readLine();

                System.out.println("[" + new Date().toString() + "][tlu] Checking your api key: " + apiKey);
                valid = api.isKeyValid(apiKey);
                if (valid) {
                    System.out.println("[" + new Date().toString() + "][tlu] Your api key is valid.");
                } else {
                    System.out.println("[" + new Date().toString() + "][tlu] You api key is not valid.");
                }
            } while (!valid);
            new Settings().setPropertie("apikey", apiKey);
        } catch (IOException e) {
            // handle errors
        }
    }
}