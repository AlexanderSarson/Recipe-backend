package utils;

import errorhandling.SettingsException;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Settings {

    public static final String PROPERTY_FILE = "config.properties";
    private static Properties props = null;
    private static final Logger logger = Logger.getLogger(Settings.class.getName());

    private Settings() {}

    private static Properties loadProperties() throws SettingsException {
        Properties allProps = new Properties();
        try {
            allProps.load(Settings.class.getClassLoader().getResourceAsStream(PROPERTY_FILE));
            for (Object key : allProps.keySet()) {
                allProps.setProperty((String) key, allProps.getProperty((String) key).trim());
            }
        } catch (IOException ex) {
            throw new SettingsException("Could not load properties for : " + PROPERTY_FILE);
        }
        return allProps;
    }
    
    /**
     * Returns the property value for the given key or null if it does not exist
     * Key/value must be defined in "config.properties"
     * @param key
     * @return Property value for the given key
     */
    public static String getPropertyValue(String key){
        initializeProperties();
        return props.getProperty(key);
    }
   
    /**
     * Utility method that builds the DEV-connection string using the property values: db.server , db.port and db.testdatabase
     * @return 
     *  a connection string formatted like this: "jdbc:mysql://localhost:3307/startcode"
     */
    public static String getDEVDBConnection(){
        initializeProperties();
        return String.format("jdbc:mysql://%s:%s/%s",props.getProperty("db.server"),props.getProperty("db.port"),props.getProperty("db.database"));
    }
    
    /**
     * Utility method that builds the TEST-connection string using the property values: db.server , db.port and db.database
     * @return 
     *  a connection string formatted like this: "jdbc:mysql://localhost:3307/startcode_test"
     */
    public static String getTESTDBConnection(){
        initializeProperties();
        String server = props.getProperty("dbtest.server") != null ? props.getProperty("dbtest.server") : props.getProperty("db.server");
        String port = props.getProperty("dbtest.port") != null ? props.getProperty("dbtest.port") : props.getProperty("db.port");
        return String.format("jdbc:mysql://%s:%s/%s",server,port,props.getProperty("dbtest.database"));
    }

     private static void initializeProperties() {
         if (props == null) {
             try {
                 props = loadProperties();
             } catch (SettingsException e) {
                 logger.log(Level.SEVERE, e.getMessage());
             }
         }
     }
}
