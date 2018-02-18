package com.strate.setup;

import java.io.*;
import java.util.Properties;

/**
 * <p>Represents the current settings
 * defined in the settings file.
 * Offers methods to access the file and
 * update/create it.</p>
 * @author Stephan Strate
 * @since 3.0.0
 */
public class Settings {

    /**
     * <p>Path to the settings file.</p>
     * @since 3.0.0
     */
    private String path = ".tlu/";

    /**
     * <p>Name of the settings file.</p>
     * @since 3.0.0
     */
    private String ext = "properties.dat";

    /**
     * <p>Properties object that is read
     * from the settings file.</p>
     * @since 3.0.0
     */
    private Properties properties;

    /**
     * <p>Status of properties.</p>
     * @since 3.0.0
     */
    private boolean valid;

    /**
     * <p>Checks if the given path exists, initializes the
     * properties and loads the properties.</p>
     * @since 3.0.0
     */
    public Settings () {
        properties = new Properties();
        valid = false;

        createFolder();
        load();
    }

    /**
     * <p>Reloads/loads the properties into the
     * {@link Settings#properties} object.</p>
     * @since 3.0.0
     */
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

    /**
     * <p>Gets a property from the settings file.</p>
     * @param key   key
     * @return  value
     * @since 3.0.0
     */
    public String getPropertie (String key) {
        return properties.getProperty(key);
    }

    /**
     * <p>Stores a property with key in settings file.</p>
     * @param key   key
     * @param value value
     * @since 3.0.0
     */
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

    /**
     * <p>Returns the status of settings.</p>
     * @return  status
     * @since 3.0.0
     */
    public boolean exists () {
        return valid;
    }

    /**
     * <p>Checks if the given folder exists and
     * creates it, if not.</p>
     * @since 3.0.0
     */
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