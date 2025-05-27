package data.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;

public class PropertyLoader {

    public static Properties getPropertiesInstance(String fileName) {
        Properties instance = new Properties();
        try (InputStream resourceStream = PropertyLoader.class.getResourceAsStream(fileName);
             InputStreamReader inputStream = new InputStreamReader(Objects.requireNonNull(resourceStream), StandardCharsets.UTF_8)) {
            instance.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error loading properties from file: " + fileName, e);
        }
        return instance;
    }
}