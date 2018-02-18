package com.strate.setup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

/**
 * <p>Setup process for the notification value.
 * Reads and stores the notification value.</p>
 * @author Stephan Strate
 * @since 3.0.0
 */
public class Notification implements Setup {

    /**
     * <p>Default {@link BufferedReader} to read
     * user input. Used to read notification status.</p>
     * @since 3.0.0
     */
    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    /**
     * <p>Notification is the
     * main property and part of the settings.</p>
     * @since 3.0.0
     */
    private boolean notification;

    /**
     * <p>Default constructor.</p>
     * @since 3.0.0
     */
    public Notification () {
        notification = false;
    }

    /**
     * <p>Reads and stores the notification value
     * from console.</p>
     * @since 3.0.0
     */
    @Override
    public void execute () {
        try {
            boolean touched = false;
            do {
                System.out.print("[" + new Date().toString() + "][tlu] Do you want your users to get notifications? (Y/n) ");
                String temp = br.readLine();
                if (temp.toLowerCase().equals("y")) {
                    notification = true;
                    touched = true;
                } else if (temp.toLowerCase().equals("n")) {
                    notification = false;
                    touched = true;
                }
            } while (!touched);
            new Settings().setPropertie("notification", notification + "");

            if (notification) {
                System.out.println("[" + new Date().toString() + "][tlu] Your users will get notifications.");
            } else {
                System.out.println("[" + new Date().toString() + "][tlu] Your users will not get notifications.");
            }
        } catch (IOException e) {
            // error handling
        }
    }

    /**
     * <p>Gets the notification status.</p>
     * @return  {@link Notification#notification}
     * @since 3.0.0
     */
    public boolean get () {
        return notification;
    }
}