package com.strate.setup;

/**
 * <p>Used to show an error in setup objects.</p>
 * @author Stephan Stare
 * @since 3.0.0
 */
class SetupException extends RuntimeException {

    /**
     * <p>Default constructor.</p>
     * @param message   message
     * @since 3.0.0
     */
    SetupException (String message) {
        super(message);
    }
}