package dev.projectenhanced.enhancedjda.util;

import lombok.SneakyThrows;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FileUtil {
    @SneakyThrows
    public static void saveResource(String name) {
        File file = new File(name);
        if(file.exists()) return;

        InputStream is = FileUtil.class.getResourceAsStream(name);
        if(is == null) return;

        Files.copy(is, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        is.close();
    }
}
