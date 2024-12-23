package eu.kpgtb.enhanced.jda.annotation.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

@Getter @AllArgsConstructor
public enum MemberCachePolicyType {
    NONE(MemberCachePolicy.NONE),
    ALL(MemberCachePolicy.ALL),
    OWNER(MemberCachePolicy.OWNER),
    ONLINE(MemberCachePolicy.ONLINE),
    VOICE(MemberCachePolicy.VOICE),
    BOOSTER(MemberCachePolicy.BOOSTER),
    PENDING(MemberCachePolicy.PENDING),
    DEFAULT(MemberCachePolicy.DEFAULT);

    private final MemberCachePolicy jdaMemberCachePolicy;
}
