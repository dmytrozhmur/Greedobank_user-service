package com.griddynamics.internship.userservice.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class PropertiesUtils {
    private static final String FILE_NAME = "application.properties";
    private static Properties properties = new Properties();

    static {
        URL url = PropertiesUtils.class.getClassLoader().getResource(FILE_NAME);
        try {
            properties.load(new FileInputStream(url.getPath()));
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
