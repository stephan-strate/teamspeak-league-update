package com.strate.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

abstract class Console {

    boolean actvie = false;
    ArrayList<Method> methods = new ArrayList<>();
    private Class obj = getClass();
    private Object o;

    Console () {
        /* @TODO: Methods get iterate one time to much, when creating the console */
        for (Method method : obj.getDeclaredMethods()) {
            if (method.isAnnotationPresent(com.strate.console.Method.class)) {
                // get all methods that have annotation
                methods.add(method);
                System.out.println(method);
            }
        }
    }

    private void process () {
        // init reader
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (actvie) {
            try {
                // read line
                String command = br.readLine();
                // split inputs from eg. "update database test" to
                // ["update", "database", "test"]
                String[] parts = command.split(" ");
                // if command is available
                if (parts.length > 0) {
                    // get command/endpoint
                    String endpoint = parts[0];
                    // get the attributes
                    String[] args = Arrays.copyOfRange(parts, 1, parts.length);

                    // iterate all methods
                    for (Method method : methods) {
                        // check if any method has the name of endpoint
                        if (method.getName().equals(endpoint)) {
                            // get singleton instance
                            Object temp = getInstance();
                            // prepare arguments for method
                            Object[] params = new Object[1];
                            params[0] = args;
                            // invoke method
                            method.invoke(temp, params);
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("");
            } catch (IllegalAccessException e) {
                System.out.println("");
            } catch (InvocationTargetException e) {
                System.out.println("");
            }
        }
    }

    private Object getInstance () {
        if (o == null) {
            try {
                o = obj.newInstance();
            } catch (InstantiationException e) {

            } catch (IllegalAccessException e) {

            }

            return o;
        } else {
            return o;
        }
    }

    public void start () {
        actvie = true;
        process();
    }

    public void end () {
        actvie = false;
    }

    @com.strate.console.Method
    public void exit (String[] args) {
        System.out.println("Exit program");
        actvie = false;
    }
}