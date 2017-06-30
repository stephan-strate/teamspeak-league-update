package settings;

import java.io.*;
import java.util.Properties;

/**
 * Can create a configuration file with given specs.
 *
 * @author  Stephan Strate (development@famstrate.com)
 * @since   2.0.0
 */
public class Configuration {
    private Properties prop;
    private String[] names = null;

    public Configuration(String path, String[] names) {
        // Setting the names
        this.names = names;

        // Initialize propertie attribute
        prop = new Properties();

        // Loading the given propertie file
        loadFile(path);
    }

    public Configuration(String path) {
        // Initialize propertie attribute
        prop = new Properties();
        // Loading the given propertie file
        loadFile(path);
    }

    /**
     * Gives you a specific propertie. If propertie isn't load
     * an exception is thrown.
     *
     * @param propertie Propertie name
     * @return          Propertie value
     */
    public String get (String propertie) {
        try {
            if (prop != null) {
                return prop.getProperty(propertie);
            }

            throw new IllegalAccessException();
        } catch (IllegalAccessException e) {
            System.out.println("Propertie " + propertie + " not found or propertie file not loaded.");
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Can return the private attribute "prop"
     * to another class.
     *
     * @return  Prop attribute of class
     */
    public Properties getProp () {
        return prop;
    }

    /**
     * Loading the given file, can only be
     * called by own class.
     *
     * @param path  Path to the file
     */
    private void loadFile (String path) {
        try {
            // Opening the file
            InputStream input = new FileInputStream(path);

            // Loading properties of the file
            loadProperties(input);
        } catch (FileNotFoundException e) {
            // Is called when file could not be found
            createFile(path);
        }
    }

    /**
     * Generates the given properties file,
     * can only be called by own class.
     *
     * @param path  Path to the file
     */
    private void createFile (String path) {
        // Generate the file
        File file = new File(path);

        // Setting the given properties
        setProperties(file);

        // Exit the program
        System.out.println("Please fill the generated " + path + " file and" +
                " start the program again.");
        System.exit(0);
    }

    /**
     * Loading the properties into attribute
     * from loaded file.
     *
     * @param input Opened file
     */
    private void loadProperties (InputStream input) {
        try {
            prop.load(input);
            if (names != null) {
                for (String name : names) {
                    if (prop.getProperty(name).equals("")) {
                        throw new IllegalAccessException();
                    }
                }
            }
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            System.out.println("You need to fill all properties before you start" +
                    " the program. Please try again.");
            System.exit(0);
        }
    }

    /**
     * Setting the given propertie names to
     * the given file.
     *
     * @param file  Path to the file
     */
    private void setProperties (File file) {
        try {
            // Setting the given propertie names
            Properties properties = new Properties();
            if (names != null) {
                for (String name : names) {
                    properties.setProperty(name, "");
                }
            }

            // Save it to the file
            FileOutputStream output = new FileOutputStream(file);
            properties.store(output, "teamspeak-league-update properties");
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}