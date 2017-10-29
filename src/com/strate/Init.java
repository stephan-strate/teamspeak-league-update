package com.strate;

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
        Setup setup = new Setup();
        setup.initSetup();
    }
}