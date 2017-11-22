package com.strate.console;

import java.lang.reflect.Method;
import java.util.ArrayList;

class Console {

    boolean actvie = false;
    ArrayList<Method> methods = new ArrayList<>();

    Console () {
        for (Method method : getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(com.strate.console.Command.class)) {
                // get all methods that have annotation
                methods.add(method);
            }
        }
    }

    protected void process () {

    }

    public void start () {
        actvie = true;
        process();
    }

    public void end () {
        actvie = false;
        System.exit(0);
    }
}