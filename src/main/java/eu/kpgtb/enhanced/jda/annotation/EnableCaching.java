package eu.kpgtb.enhanced.jda.annotation;

import eu.kpgtb.enhanced.jda.annotation.type.ChunkingFilterType;
import eu.kpgtb.enhanced.jda.annotation.type.MemberCachePolicyType;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableCaching {
    MemberCachePolicyType policy();
    ChunkingFilterType filter();
    CacheFlag[] flags() default {};
}
