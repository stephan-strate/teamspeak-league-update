package com.strate.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * <p>Console class that's make it possible to create
 * simple, but powerful console applications.
 * Just extend from it and use {@code Method} annotations
 * to create methods for the console.</p>
 * @author Stephan Strate
 * @since 3.0.0
 */
abstract class Console {

    private boolean active = true;
    private ArrayList<Method> methods = new ArrayList<>();
    private Class current = getClass();
    private Object instance;

    /**
     * <p>Create a new console.</p>
     * @since 3.0.0
     */
    Console () {
        for (Method method : current.getDeclaredMethods()) {
            if (method.isAnnotationPresent(com.strate.console.Method.class)) {
                // get all methods that have annotation
                methods.add(method);

                /* @TODO: Methods get iterate one time to much, when creating the console */
                // debugging iteration bug
                // System.out.println(method);
            }
        }
    }

    /**
     * <p>Logical part of console application.
     * Reading new lines and invoking the correct methods.</p>
     * @since 3.0.0
     */
    private void process () {
        // init reader
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (isActive()) {
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
                System.out.println("Error. Input/Output error.");
            } catch (IllegalAccessException e) {
                System.out.println("Internal error. No permission to call method.");
            } catch (InvocationTargetException e) {
                System.out.println("Internal error.");
            }
        }
    }

    /**
     * <p>Start the console application.</p>
     * @since 3.0.0
     */
    public void start () {
        process();
    }

    /**
     * <p>Singleton pattern. Returns a instance
     * of current object, to invoke methods on it.</p>
     * @return  {@code instance}
     * @since 3.0.0
     */
    public Object getInstance () {
        // check if instance is present
        if (instance == null) {
            try {
                // create new instance
                instance = current.newInstance();
            } catch (InstantiationException e) {
                System.out.println("Internal error. Can not create a new instance of console.");
            } catch (IllegalAccessException e) {
                System.out.println("Internal error.");
            }

            return instance;
        } else {
            return instance;
        }
    }

    /**
     * <p>Get active attribute.</p>
     * @return  {@code active}
     * @since 3.0.0
     */
    public boolean getActive () {
        return active;
    }

    /**
     * <p>Check if console application is active.</p>
     * @return  {@code boolean}
     * @since 3.0.0
     */
    public boolean isActive () {
        try {
            // get active method
            Method method = getClass().getMethod("getActive");
            // calling method on instance object
            return (boolean) method.invoke(getInstance());
        } catch (NoSuchMethodException e) {
            System.out.println("Internal error. getActive method could not be found.");
        } catch (IllegalAccessException e) {
            System.out.println("Internal error. No permission to call getActive method.");
        } catch (InvocationTargetException e) {
            System.out.println("Internal error.");
        }

        // application can not run in this state
        return false;
    }

    /**
     * <p>Default exit method for user. Can be
     * overwritten.</p>
     * @param args  can be null
     * @since 3.0.0
     */
    @com.strate.console.Method
    public void exit (String[] args) {
        active = false;
    }
}