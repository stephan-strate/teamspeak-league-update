package com.strate.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Arrays;

abstract class Console {

    boolean actvie = true;

    Console () {

    }

    private void process () {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (actvie) {
            try {
                String command = br.readLine();
                String[] parts = command.split(" ");
                if (parts.length > 0) {
                    String endpoint = parts[0];
                    String[] args = Arrays.copyOfRange(parts, 1, parts.length);
                }

                Method[] methods = {};
                Class obj = getClass().getEnclosingClass();
                for (Method method : obj.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(com.strate.console.Method.class)) {
                        // get all methods that have annotation
                    }
                }
            } catch (IOException e) {
                System.out.println("");
            }
        }
    }

    public void start () {
        actvie = true;
        process();
    }

    public void end () {
        actvie = false;
    }
}