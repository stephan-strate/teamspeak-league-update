package com.strate.setup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

public class Language implements Setup {

    /**
     * <p>Default {@link BufferedReader} to read
     * user input. Used to read Language code and
     * match it with available Language codes.</p>
     */
    private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    /**
     * <p>{@link com.strate.constants.Language} is the
     * main property and part of the settings.</p>
     */
    private com.strate.constants.Language language;

    public Language () {
        language = null;
    }

    @Override
    public void execute () {
        try {
            do {
                System.out.print("[" + new Date().toString() + "][tlu] Enter your prefered language (" + com.strate.constants.Language.getAllLanguages() + "): ");
                language = com.strate.constants.Language.getLanguageByCode(br.readLine());
            } while (language == null);
            System.out.println("[" + new Date().toString() + "][tlu] Successfully identified language: " + language.getCode());
            new Settings().setPropertie("language", language.getCode());
        } catch (IOException e) {
            // error handling
        }
    }

    public com.strate.constants.Language get () {
        if (isValid()) {
            return language;
        }

        throw new SetupException("Illegal access of language property in setup. Language not available.");
    }

    public boolean isValid () {
        return language != null;
    }
}