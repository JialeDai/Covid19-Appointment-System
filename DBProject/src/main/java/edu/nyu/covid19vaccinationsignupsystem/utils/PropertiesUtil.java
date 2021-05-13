package edu.nyu.covid19vaccinationsignupsystem.utils;


import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

public class PropertiesUtil {
    public static String getProperty(String key) throws IOException {
        Properties props = new Properties();
        props.load(new java.io.FileInputStream("src/main/resources/application.properties"));
        String value = props.getProperty(key);
        return value;
    }
}
