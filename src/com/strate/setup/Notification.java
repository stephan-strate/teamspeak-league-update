package com.strate.setup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Notification implements Setup {

    /**
     * <p>Default {@link BufferedReader} to read
     * user input. Used to read notification status.</p>
     */
    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    /**
     * <p>Notification is the
     * main property and part of the settings.</p>
     */
    private boolean notification;

    public Notification () {
        notification = false;
    }

    @Override
    public void execute () {
        try {
            boolean touched = false;
            do {
                System.out.print("[tlu] Do you want your users to get notifications? (Y/n) ");
                String temp = br.readLine();
                if (temp.toLowerCase().equals("y")) {
                    notification = true;
                    touched = true;
                } else if (temp.toLowerCase().equals("n")) {
                    notification = false;
                    touched = true;
                }
            } while (!touched);

            if (notification) {
                System.out.println("[tlu] Your users will get notifications.");
            } else {
                System.out.println("[tlu] Your users will not get notifications.");
            }
        } catch (IOException e) {
            // error handling
        }
    }

    public boolean get () {
        return notification;
    }
}