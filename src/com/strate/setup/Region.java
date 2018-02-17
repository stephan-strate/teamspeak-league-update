package com.strate.setup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Region implements Setup {

    /**
     * <p>Default {@link BufferedReader} to read
     * user input. Used to read Region code and
     * match it with available Region codes.</p>
     */
    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    /**
     * <p>{@link com.strate.remote.riot.constants.Region} is the
     * main property and part of the settings.</p>
     */
    private com.strate.remote.riot.constants.Region region;

    public Region () {
        region = null;
    }

    @Override
    public void execute () {
        try {
            do {
                System.out.print("[tlu] Enter your region (" + com.strate.remote.riot.constants.Region.getAllRegions() + "): ");
                region = com.strate.remote.riot.constants.Region.getRegionByShortcut(br.readLine());
            } while (region == null);
            System.out.println("[tlu] Successfully identified region: " + region.getShortcut());
            new Settings().setPropertie("region", region.getShortcut());
        } catch (IOException e) {
            // error handling
        }
    }

    public com.strate.remote.riot.constants.Region get () {
        if (isValid()) {
            return region;
        }

        throw new SetupException("Illegal access of region property in setup. Region not available.");
    }

    public boolean isValid () {
        return region != null;
    }
}