package com.strate.console;

public class DefaultConsole extends Console {

    // final console class

    @Method
    protected void update (String[] args) {
        // do something when user calls function
        for (int i = 0; i < args.length; i++) {
            System.out.println(args[i]);
        }
    }

    @Method
    protected void exit (String[] args) {
        System.out.println("Exit program");
        System.exit(0);
    }
}