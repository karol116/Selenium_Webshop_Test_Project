package utils;

import java.util.Properties;

public class ConfigLoader {
    private final Properties properties;
    private static ConfigLoader configLoader;

    public ConfigLoader() {
        properties = PropertyUtils.propertyLoader("src/test/resources/config.properties");
    }

    public static ConfigLoader getInstance() {
        if (configLoader == null) {
            configLoader = new ConfigLoader();
        }
        return configLoader;
    }

    public String getProperty(String propertyName) {
        String properties1 = properties.getProperty(propertyName);
        if (properties1 != null) {
            return properties1;
        } else {
            throw new RuntimeException("propoerty is not specified in the config.prpoerties file");
        }

    }
}
