package dev.projectenhanced.enhancedjda.discord;

import dev.projectenhanced.enhancedjda.discord.type.MemberCachePolicyType;
import dev.projectenhanced.enhancedjda.discord.type.ChunkingFilterType;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enable and Setup caching for your bot
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableCaching {
    MemberCachePolicyType policy();
    ChunkingFilterType filter();
    CacheFlag[] flags() default {};
}
