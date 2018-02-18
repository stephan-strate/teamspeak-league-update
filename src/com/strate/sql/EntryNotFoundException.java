package com.strate.sql;

/**
 * <p>Represents the status, that the requested
 * entry could not be found.</p>
 * @author Stephan Strate
 * @since 3.0.0
 */
public class EntryNotFoundException extends RuntimeException {

    /**
     * <p>Default constructor.</p>
     * @param message   message
     * @since 3.0.0
     */
    public EntryNotFoundException (String message) {
        super(message);
    }
}