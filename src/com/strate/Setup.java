package com.strate;

import com.strate.constants.Ansi;
import com.strate.constants.Language;
import com.strate.remote.riot.Api;
import com.strate.remote.riot.constants.Region;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * <p>[description]</p>
 * @author Stephan Strate
 */
class Setup {

    /**
     * <p>[description]</p>
     */
    Setup () {

    }

    /**
     * <p>[description]</p>
     * @throws IOException
     */
    void initSetup () throws IOException {
        System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Starting " + Ansi.PURPLE + "teamspeak-league-update" + Ansi.RESET + " setup.");

        // Language
        Language language = language();

        // League of Legends region
        Region region = region();

        // Riot Games api key
        String key = key(region);

        // Notifications
        boolean notifications = notifications();

        System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Finished " + Ansi.PURPLE + "teamspeak-league-update" + Ansi.RESET + " setup.");
    }

    /**
     * <p>[description]</p>
     * @return
     * @throws IOException
     */
    public Language language () throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        Language language = null;
        do {
            System.out.print(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Enter your prefered language (" + Language.getAllLanguages() + "): ");
            language = Language.getLanguageByCode(br.readLine());
        } while (language == null);
        System.out.println(Ansi.BLUE + "[tlu] " + Ansi.GREEN + "Successfully identified language: " + Ansi.RESET + language.getCode());

        return language;
    }

    /**
     * <p>[description]</p>
     * @return
     * @throws IOException
     */
    public Region region () throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        Region region = null;
        do {
            System.out.print(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Enter your region (" + Region.getAllRegions() + "): ");
            region = Region.getRegionByShortcut(br.readLine());
        } while (region == null);
        System.out.println(Ansi.BLUE + "[tlu] " + Ansi.GREEN + "Successfully identified region: " + Ansi.RESET + region.getShortcut());

        return region;
    }

    /**
     * <p>[description]</p>
     * @param region
     * @return
     * @throws IOException
     */
    public String key (Region region) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        /* @TODO: RGAPI-F98DD623-6BE4-41E6-A3A3-7E4A62B7F3B0 */

        String key = "";
        boolean valid = false;
        Api api = new Api(key, region);
        do {
            System.out.print(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Enter your Riot Games api key: ");
            key = br.readLine();

            System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Checking your api key: " + key);
            valid = api.isKeyValid(key);
            if (valid) {
                System.out.println(Ansi.BLUE + "[tlu] " + Ansi.GREEN + "Your api key is valid." + Ansi.RESET);
            } else {
                System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RED + "You api key is not valid." + Ansi.RESET);
            }
        } while (!valid);

        return key;
    }

    /**
     * <p>[description]</p>
     * @return
     * @throws IOException
     */
    public boolean notifications () throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        boolean notifications = false;
        boolean touched = false;
        do {
            System.out.print(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Do you want your users to get notifications? (Y/n) ");
            String temp = br.readLine();
            if (temp.toLowerCase().equals("y")) {
                notifications = true;
                touched = true;
            } else if (temp.toLowerCase().equals("n")) {
                notifications = false;
                touched = true;
            }
        } while (!touched);

        if (notifications) {
            System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Your users will get notifications.");
        } else {
            System.out.println(Ansi.BLUE + "[tlu] " + Ansi.RESET + "Your users will not get notifications.");
        }

        return notifications;
    }
}