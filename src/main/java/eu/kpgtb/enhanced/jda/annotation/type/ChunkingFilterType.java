package eu.kpgtb.enhanced.jda.annotation.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dv8tion.jda.api.utils.ChunkingFilter;

@Getter @AllArgsConstructor
public enum ChunkingFilterType {
    ALL(ChunkingFilter.ALL),
    NONE(ChunkingFilter.NONE);

    private final ChunkingFilter jdaChunkingFilter;
}
