package com.strate.constants;

import java.util.Date;

/**
 * <p>Represents a supported language for this
 * application. You can compare them, get a
 * {@link Language} by language code and return
 * all languages.</p>
 * @author Stephan Strate
 * @since 3.0.0
 */
public enum Language {

    DE (0, "de"),
    EN (1, "en");

    private int id;
    private String code;

    /**
     * <p>Represents a languages the application
     * supports.</p>
     * @param id    order id
     * @param code  language code
     * @since 3.0.0
     */
    Language (int id, String code) {
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
            if (language.getCode().equals((code.toLowerCase()))) {
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
            languages += language.getCode() + "/";
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
        return code;
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
    public String getCode () {
        return code;
    }
}