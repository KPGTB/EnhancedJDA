package dev.projectenhanced.enhancedjda.discord;

import net.dv8tion.jda.api.requests.GatewayIntent;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enable certain intents in your bot
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableIntents {
    GatewayIntent[] value() default {};
}
