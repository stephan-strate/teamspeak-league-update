package com.strate.constants;

import java.util.Date;
import java.util.Locale;

/**
 * <p>Represents a supported language for this
 * application. You can compare them, get a
 * {@link Language} by language code and return
 * all languages.</p>
 * @author Stephan Strate
 * @since 3.0.0
 */
public enum Language {

    EN (0, new Locale("en", "US")),
    DE (1, new Locale("de", "DE"));

    /**
     * <p>Primary key/id of
     * enum type.</p>
     * @since 3.0.0
     */
    private int id;

    /**
     * <p>Language code as string.</p>
     * @since 3.0.0
     */
    private Locale code;

    /**
     * <p>Represents a languages the application
     * supports.</p>
     * @param id    order id
     * @param code  language code
     * @since 3.0.0
     */
    Language (int id, Locale code) {
        this.id = id;
        this.code = code;
    }

    /**
     * <p>Get a {@link Language} object by language
     * code.</p>
     * @param code  language code
     * @return      {@link Language}
     * @since 3.0.0
     */
    public static Language getLanguageByCode (String code) {
        for (Language language : Language.values()) {
            if (language.toString().toLowerCase().equals(code.toLowerCase())) {
                return language;
            }
        }

        System.out.println("[" + new Date().toString() + "][tlu] Language not found.");
        return null;
    }

    /**
     * <p>Concat all language codes with '/'.</p>
     * @return  all languages concat with '/'
     * @since 3.0.0
     */
    public static String getAllLanguages () {
        String languages = "";
        for (Language language : Language.values()) {
            languages += language.toString() + "/";
        }

        languages = languages.substring(0, languages.length() - 1);
        return languages;
    }

    /**
     * <p>Returns {@code true} when order id
     * of both elements is equal.</p>
     * @param other     {@link Language}
     * @return          {@code true} when order is equal
     * @since 3.0.0
     */
    public boolean equals (Language other) {
        return this.getId() == other.getId();
    }

    /**
     * <p>Returns the language code of
     * a {@link Language} object.</p>
     * @return  language code
     * @since 3.0.0
     */
    @Override
    public String toString () {
        return code.toLanguageTag();
    }

    /**
     * <p>Returns the order id.</p>
     * @return  order id
     * @since 3.0.0
     */
    public int getId () {
        return id;
    }

    /**
     * <p>Returns the language code.</p>
     * @return  language code
     * @since 3.0.0
     */
    public Locale getCode () {
        return code;
    }
}