package data.constants;

import java.util.Properties;

import static data.utils.PropertyLoader.getPropertiesInstance;

public class TestConstants {
    private static final String PROPERTIES_FILE = "/testConstants.properties";
    private static final Properties PROPERTIES = getPropertiesInstance(PROPERTIES_FILE);

    public static final String PAGE_URI = PROPERTIES.getProperty("pageUri");
    public static final String SEARCH_STRING = PROPERTIES.getProperty("searchString");
}