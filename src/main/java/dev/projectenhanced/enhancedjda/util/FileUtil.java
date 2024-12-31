package dev.projectenhanced.enhancedjda.util;

import dev.projectenhanced.enhancedjda.EnhancedBot;
import lombok.SneakyThrows;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FileUtil {
    /**
     * Method to save resource from "resources" folder to local files
     * @param name Name of resource file
     */
    @SneakyThrows
    public static void saveResource(String name) {
        File file = new File(name);
        if(file.exists()) return;

        InputStream is = EnhancedBot.class.getResourceAsStream("/" + name);
        if(is == null) return;

        Files.copy(is, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        is.close();
    }
}
