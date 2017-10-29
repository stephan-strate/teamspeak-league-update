package com.strate.constants;

/**
 * <p>[description]</p>
 * @author Stephan Strate
 */
public enum Language {

    DE (0, "de"),
    EN (1, "en");

    private int id;
    private String code;

    /**
     * <p>[description]</p>
     * @param id
     * @param code
     */
    Language (int id, String code) {
        this.id = id;
        this.code = code;
    }

    /**
     * <p>[description]</p>
     * @param code
     * @return
     */
    public static Language getLanguageByCode (String code) {
        for (Language language : Language.values()) {
            if (language.getCode().equals((code.toLowerCase()))) {
                return language;
            }
        }

        System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RED + "Language not found." + Ansi.RESET);
        return null;
    }

    /**
     * <p>[description]</p>
     * @return
     */
    public int getId () {
        return id;
    }

    /**
     * <p>[description]</p>
     * @return
     */
    public String getCode () {
        return code;
    }

    /**
     * <p>[description]</p>
     * @param other
     * @return
     */
    public boolean equals (Language other) {
        return this.getId() == other.getId();
    }

    /**
     * <p>[description]</p>
     * @return
     */
    @Override
    public String toString () {
        return code;
    }

    /**
     * <p>[description]</p>
     * @return
     */
    public static String getAllLanguages () {
        String languages = "";
        for (Language language : Language.values()) {
            languages += language.getCode() + "/";
        }

        languages = languages.substring(0, languages.length() - 1);
        return languages;
    }
}