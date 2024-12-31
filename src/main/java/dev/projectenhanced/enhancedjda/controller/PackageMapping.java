package dev.projectenhanced.enhancedjda.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Map packages for commands, contexts, listeners, tables and persisters
 * Use relative paths from class which extends EnhancedBot
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PackageMapping {
    String[] commands() default {};
    String[] contexts() default {};
    String[] listeners() default {};
    String[] tables() default {};
    String[] persisters() default {};
}
