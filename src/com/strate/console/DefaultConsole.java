package com.strate.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class DefaultConsole extends Console {

    private static final Console obj = new DefaultConsole();

    @Override
    protected void process () {
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
                            Object[] params = new Object[1];
                            params[0] = args;
                            method.invoke(getInstance(), params);
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

    public Object getInstance () {
        return obj;
    }

    @Command
    protected void update (String[] args) {
        // do something when user calls function
        for (int i = 0; i < args.length; i++) {
            System.out.println(args[i]);
        }
    }

    @Command
    protected void exit (String[] args) {
        System.out.println("Exit program");
        end();
    }
}