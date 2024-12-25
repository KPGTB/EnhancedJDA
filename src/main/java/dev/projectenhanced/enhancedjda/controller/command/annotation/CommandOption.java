package dev.projectenhanced.enhancedjda.controller.command.annotation;

import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(value = CommandOptions.class)
public @interface CommandOption {
    OptionType type();
    String name();
    String description();
    boolean required() default false;
    boolean autoComplete() default false;
}
