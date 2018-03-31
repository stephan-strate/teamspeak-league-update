package com.strate.setup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Locale;

/**
 * <p>Setup process for preferred language.
 * Reads and stores the language.</p>
 * @author Stephan Strate
 * @since 3.0.0
 */
public class Language implements Setup {

    /**
     * <p>Default {@link BufferedReader} to read
     * user input. Used to read Language code and
     * match it with available Language codes.</p>
     * @since 3.0.0
     */
    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    /**
     * <p>{@link com.strate.constants.Language} is the
     * main property and part of the settings.</p>
     * @since 3.0.0
     */
    private com.strate.constants.Language language;

    /**
     * <p>Default constructor.</p>
     * @since 3.0.0
     */
    public Language () {
        language = null;
    }

    /**
     * <p>Reading and storing the preferred language
     * from console.</p>
     * @since 3.0.0
     */
    @Override
    public void execute () {
        try {
            do {
                System.out.print("[" + new Date().toString() + "][tlu] Enter your prefered language (" + com.strate.constants.Language.getAllLanguages() + "): ");
                language = com.strate.constants.Language.getLanguageByCode(br.readLine());
            } while (language == null);
            System.out.println("[" + new Date().toString() + "][tlu] Successfully identified language: " + language.getCode());
            new Settings().setPropertie("language", language.getCode().toLanguageTag());
            Locale.setDefault(language.getCode());
        } catch (IOException e) {
            // error handling
        }
    }

    /**
     * <p>Returns the language or throws a
     * {@link SetupException}, when language not available.</p>
     * @return  {@link Language#language}
     * @since 3.0.0
     */
    public com.strate.constants.Language get () {
        if (isValid()) {
            return language;
        }

        throw new SetupException("Illegal access of language property in setup. Language not available.");
    }

    /**
     * <p>Check if language object is valid.</p>
     * @return  status
     * @since 3.0.0
     */
    public boolean isValid () {
        return language != null;
    }
}