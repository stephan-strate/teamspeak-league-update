package com.strate.setup;

import java.io.*;
import java.util.Properties;

public class Settings {

    private String path = ".tlu/";

    private String ext = "properties.dat";

    private Properties properties;

    private boolean valid;

    public Settings () {
        createFolder();
        properties = new Properties();
        valid = false;
        load();
    }

    public void load () {
        try {
            InputStream input = new FileInputStream(path + ext);
            properties.load(input);
            input.close();
            if (!properties.isEmpty()) {
                valid = true;
            }
        } catch (FileNotFoundException e) {
            try {
                File file = new File(path + ext);
                FileOutputStream output = new FileOutputStream(file);
                properties.store(output, "teamspeak-league-update properties");
                output.close();
                load();
            } catch (FileNotFoundException x) {
                // internal error
            } catch (IOException x) {
                // internal error
            }
        } catch (IOException e) {
            // internal error
        }
    }

    public String getPropertie (String key) {
        return properties.getProperty(key);
    }

    public void setPropertie (String key, String value) {
        try {
            properties.setProperty(key, value);
            File file = new File(path + ext);
            FileOutputStream output = new FileOutputStream(file);
            properties.store(output, "teamspeak-league-update properties");
            output.close();
        } catch (FileNotFoundException e) {
            // handle error
        } catch (IOException e) {
            // handle error
        }
    }

    public boolean exists () {
        return valid;
    }

    private void createFolder () {
        // create path as file
        File file = new File(path);

        // if file not exists
        if (!file.exists()) {
            // create folder
            file.mkdir();
        }
    }
}