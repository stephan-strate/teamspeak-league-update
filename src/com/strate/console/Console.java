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
        for (Method method : obj.getDeclaredMethods()) {
            if (method.isAnnotationPresent(com.strate.console.Method.class)) {
                // get all methods that have annotation
                methods.add(method);
            }
        }
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

                    for (Method method : methods) {
                        if (method.getName().equals(endpoint)) {
                            o = obj.newInstance();
                            Object[] params = new Object[1];
                            params[0] = args;
                            method.invoke(o, params);
                        }
                    }
                }
            } catch (IOException e) {
                System.out.println("");
            } catch (IllegalAccessException e) {
                System.out.println("");
            } catch (InvocationTargetException e) {
                System.out.println("");
            } catch (InstantiationException e) {

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