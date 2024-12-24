package dev.projectenhanced.enhancedjda.util;

import java.net.URI;
import java.net.URL;
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
     * This method returns all classes in specified package
     * @param packageName Name of package that have this classes
     * @return Set of classes in package
     */
    public static Set<Class<?>> getAllClassesInPackage(Class<?> mainClazz,String packageName) {
        Set<Class<?>> classes = new HashSet<>();
        try {
            JarFile file = new JarFile(getJarFile(mainClazz));
            for (Enumeration<JarEntry> entry = file.entries(); entry.hasMoreElements();) {
                JarEntry jarEntry = entry.nextElement();
                String name = jarEntry.getName().replace("/", ".");
                if(name.startsWith(packageName) && name.endsWith(".class"))
                    classes.add(Class.forName(name.substring(0, name.length() - 6)));
            }
            file.close();
        } catch(Exception e) {
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

    private static String getJarFile(Class<?> mainClazz) {
        URL classResource = mainClazz.getResource(mainClazz.getSimpleName() + ".class");
        if (classResource == null) {
            throw new RuntimeException("class resource is null");
        }
        String url = classResource.toString();
        if (url.startsWith("jar:file:")) {
            String path = url.replaceAll("^jar:(file:.*[.]jar)!/.*", "$1");
            try {
                return Paths.get(new URI(path)).toString();
            } catch (Exception e) {
                throw new RuntimeException("Invalid Jar File URL String");
            }
        }
        throw new RuntimeException("Invalid Jar File URL String");
    }
}
