package com.strate;

import com.strate.constants.Version;

import java.io.IOException;

/**
 * <p>[description]</p>
 * @author Stephan Strate
 */
public class Init {

    /* @TODO: Sql requests and inserts */
    /* @TODO: Setup */
    /* @TODO: Handle client join event */

    public static volatile int clientId;

    /**
     * <p>[description]</p>
     * @param args
     */
    public static void main (String[] args) throws IOException {
        // create the current version (manually)
        Version version = new Version("2.0.0", 12, "https://api.harddestiny.de/v1/bot/download/2.0.0");

        // searching for new version
        Version latest = new Version();
        latest.update(version);

        // starting setup process
        Setup setup = new Setup();
        setup.initSetup();
    }
}