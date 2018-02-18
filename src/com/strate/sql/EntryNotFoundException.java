package com.strate.sql;

public class EntryNotFoundException extends RuntimeException {

    public EntryNotFoundException (String message) {
        super(message);
    }
}