package com.strate.console;

public class DefaultConsole extends Console {

    @Method
    protected void update (String[] args) {
        // do something when user calls function
        for (int i = 0; i < args.length; i++) {
            System.out.println(args[i]);
        }
    }
}