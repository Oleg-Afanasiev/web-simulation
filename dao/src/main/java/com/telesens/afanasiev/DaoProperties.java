package com.telesens.afanasiev;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This class loads the DAO properties file 'dao.properties'
 *
 * @author Afanasiev Oleg <oleg.kh81@gmail.com>
 * @version 0.1
 */
public class DaoProperties {
    private static final String PROPERTIES_FILE = "dao.properties";
    private static final Properties PROPERTIES = new Properties();

    static {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream propertiesFile = classLoader.getResourceAsStream(PROPERTIES_FILE);

        if (propertiesFile == null) {
            throw new DaoException(
                    "Properties file '" + PROPERTIES_FILE + "' is missing in classpath.");
        }

        try {
            PROPERTIES.load(propertiesFile);
        } catch (IOException e) {
            throw new DaoException(
                    "Cannot load properties file '" + PROPERTIES_FILE + "'.", e);

        }
    }
    public String getProperty(String key) throws DaoException {
        String property = PROPERTIES.getProperty(key);

        if (property == null || property.trim().length() == 0) {

            throw new DaoException("Required property '" + key + "'"
                    + " is missing in properties file '" + PROPERTIES_FILE + "'.");

        }

        return property;
    }
}
