package dev.projectenhanced.enhancedjda.util;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Util to get classes from package
 */
public class ReflectionUtil {
    /**
     * This method returns all classes in the specified package.
     * @param mainClazz The main class of the application.
     * @param packageName Name of the package containing the classes.
     * @return Set of classes in the package.
     */
    public static Set<Class<?>> getAllClassesInPackage(Class<?> mainClazz, String packageName) {
        Set<Class<?>> classes = new HashSet<>();
        try {
            File codeSource = new File(mainClazz.getProtectionDomain().getCodeSource().getLocation().toURI());

            if (codeSource.isFile() && codeSource.getName().endsWith(".jar")) {
                // Running from JAR
                try (JarFile jarFile = new JarFile(codeSource)) {
                    for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements(); ) {
                        JarEntry jarEntry = entries.nextElement();
                        String name = jarEntry.getName().replace("/", ".");
                        if (name.startsWith(packageName) && name.endsWith(".class")) {
                            classes.add(Class.forName(name.substring(0, name.length() - 6)));
                        }
                    }
                }
            } else if (codeSource.isDirectory()) {
                // Running from IDE or unpacked directory
                File packageDir = new File(codeSource, packageName.replace(".", File.separator));
                if (packageDir.exists() && packageDir.isDirectory()) {
                    Files.walk(packageDir.toPath())
                            .filter(path -> path.toString().endsWith(".class"))
                            .forEach(path -> {
                                String className = packageName + "." +
                                        packageDir.toPath().relativize(path).toString()
                                                .replace(File.separator, ".")
                                                .replace(".class", "");
                                try {
                                    classes.add(Class.forName(className));
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                            });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }

    /**
     * This method returns all classes that extends specified class in specified package
     * @param packageName Name of package that have this classes
     * @param abstractClass Class that need to be extended
     * @return Set of classes in package
     */
    public static Set<Class<?>> getAllClassesInPackage(Class<?> mainClazz, String packageName, Class<?> abstractClass) {
        Set<Class<?>> classes = getAllClassesInPackage(mainClazz,packageName);
        Set<Class<?>> result = new HashSet<>();

        classes.forEach(clazz -> {
            if(clazz.getSuperclass().equals(abstractClass) ) {
                result.add(clazz);
            }
        });

        return result;
    }
}
