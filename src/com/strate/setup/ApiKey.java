package com.strate.setup;

import com.strate.remote.riot.Api;
import com.strate.remote.riot.constants.Region;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ApiKey implements Setup {

    /**
     * <p>Default {@link BufferedReader} to read
     * user input. Used to read the riot api key.</p>
     */
    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    private String apiKey;

    private Region region;

    public ApiKey (Region region) {
        apiKey = "";
        this.region = region;
    }

    @Override
    public void execute () {
        try {
            boolean valid = false;
            Api api = new Api(apiKey, region);
            do {
                System.out.print("[tlu] Enter your Riot Games api key for " + region.getShortcut() + ": ");
                apiKey = br.readLine();

                System.out.println("[tlu] Checking your api key: " + apiKey);
                valid = api.isKeyValid(apiKey);
                if (valid) {
                    System.out.println("[tlu] Your api key is valid.");
                } else {
                    System.out.println("[tlu] You api key is not valid.");
                }
            } while (!valid);
            new Settings().setPropertie("apikey", apiKey);
        } catch (IOException e) {
            // handle errors
        }
    }
}