package dev.projectenhanced.enhancedjda.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;

/**
 * Util class to easily manage config
 */
public class EnhancedConfig {
    /**
     * Write object to config
     * @param configName Name of config file
     * @param data Data to write
     * @param <T>
     */
    public static <T> void write(String configName, T data) {
        File configFolder = new File("./config");
        configFolder.mkdirs();

        File file = new File(configFolder,configName + ".yml");
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        try {
            objectMapper.writeValue(file,data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read data from config
     * @param configName Name of config
     * @param expected Class that is expected as output
     * @return Data from config or null
     * @param <T>
     */
    public static <T> T read(String configName, Class<T> expected) {
        File configFolder = new File("./config");
        File file = new File(configFolder,configName + ".yml");
        if(!file.exists()) return null;

        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        try {
            return objectMapper.readValue(file,expected);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
