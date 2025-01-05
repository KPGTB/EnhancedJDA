package eu.kpgtb;

import dev.projectenhanced.enhancedjda.EnhancedBot;
import dev.projectenhanced.enhancedjda.config.EnhancedConfig;
import dev.projectenhanced.enhancedjda.controller.PackageMapping;
import dev.projectenhanced.enhancedjda.discord.EnableCaching;
import dev.projectenhanced.enhancedjda.discord.EnableIntents;
import dev.projectenhanced.enhancedjda.discord.type.ChunkingFilterType;
import dev.projectenhanced.enhancedjda.discord.type.MemberCachePolicyType;
import dev.projectenhanced.enhancedjda.logger.EnhancedLogger;
import eu.kpgtb.config.ExampleConfig;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.util.Date;

@EnableIntents({GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT,GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES})
@EnableCaching(policy = MemberCachePolicyType.ALL, filter = ChunkingFilterType.ALL, flags = {CacheFlag.CLIENT_STATUS})
@PackageMapping(
        listeners = "listener",
        commands = "command",
        contexts = "context",
        tables = "data"
)
public class Main extends EnhancedBot {

    public static void main(String[] args) throws ReflectiveOperationException {
        runBot(Main.class);
    }

    @Override
    public void preBuild(DefaultShardManagerBuilder builder) {
        builder.setStatus(OnlineStatus.DO_NOT_DISTURB);
        builder.setActivity(Activity.customStatus("\uD83D\uDEE0\uFE0F Working on EnhancedJDA"));
    }

    @Override
    public void postBuild(ShardManager shardManager) {}

    @Override
    public void onReady(ReadyEvent event) {
        EnhancedLogger.getLogger().info("Logged as {}", event.getJDA().getSelfUser().getAsTag());

        ExampleConfig data = EnhancedConfig.read("something", ExampleConfig.class);
        if(data != null) System.out.println(data);
        EnhancedConfig.write("something", new ExampleConfig("Mark", new Date().toLocaleString()));
    }

    @Override
    public void onShutdown() {}
}