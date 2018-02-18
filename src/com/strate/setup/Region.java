package com.strate.setup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

/**
 * <p>Setup process for region.
 * Reads and stores the region.</p>
 * @author Stephan Strate
 * @since 3.0.0
 */
public class Region implements Setup {

    /**
     * <p>Default {@link BufferedReader} to read
     * user input. Used to read Region code and
     * match it with available Region codes.</p>
     * @since 3.0.0
     */
    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    /**
     * <p>{@link com.strate.remote.riot.constants.Region} is the
     * main property and part of the settings.</p>
     * @since 3.0.0
     */
    private com.strate.remote.riot.constants.Region region;

    /**
     * <p>Default constructor.</p>
     * @since 3.0.0
     */
    public Region () {
        region = null;
    }

    /**
     * <p>Reading and storing the region from
     * console.</p>
     * @since 3.0.0
     */
    @Override
    public void execute () {
        try {
            do {
                System.out.print("[" + new Date().toString() + "][tlu] Enter your region (" + com.strate.remote.riot.constants.Region.getAllRegions() + "): ");
                region = com.strate.remote.riot.constants.Region.getRegionByShortcut(br.readLine());
            } while (region == null);
            System.out.println("[" + new Date().toString() + "][tlu] Successfully identified region: " + region.getShortcut());
            new Settings().setPropertie("region", region.getShortcut());
        } catch (IOException e) {
            // error handling
        }
    }

    /**
     * <p>Returns the region or throw a
     * {@link SetupException}, when region is not available.</p>
     * @return  {@link Region#region}
     * @since 3.0.0
     */
    public com.strate.remote.riot.constants.Region get () {
        if (isValid()) {
            return region;
        }

        throw new SetupException("Illegal access of region property in setup. Region not available.");
    }

    /**
     * <p>Check if region object is valid.</p>
     * @return  status
     * @since 3.0.0
     */
    public boolean isValid () {
        return region != null;
    }
}