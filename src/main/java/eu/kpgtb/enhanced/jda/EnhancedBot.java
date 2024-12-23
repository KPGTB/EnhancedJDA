package eu.kpgtb.enhanced.jda;

import eu.kpgtb.enhanced.jda.annotation.EnableCaching;
import eu.kpgtb.enhanced.jda.annotation.EnableDatabase;
import eu.kpgtb.enhanced.jda.annotation.EnableIntents;
import eu.kpgtb.enhanced.jda.annotation.EnableListeners;
import eu.kpgtb.enhanced.jda.controller.DataController;
import eu.kpgtb.enhanced.jda.controller.ListenerController;
import eu.kpgtb.enhanced.jda.listener.ReadyListener;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class EnhancedBot {
    private static EnhancedBot bot;

    private final String enhancedPackage;
    private final String botPackage;

    private final ListenerController listenerController;
    private final DataController dataController;

    private final Dotenv dotenv;
    private final ShardManager shardManager;
    private final Logger logger;

    protected static void runBot(Class<? extends EnhancedBot> botClass) throws ReflectiveOperationException {
        bot = botClass.getDeclaredConstructor().newInstance();
    }

    protected EnhancedBot() {
        this.dotenv = Dotenv.configure().load();
        this.logger = JDALogger.getLog("EnhancedBot");

        DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(dotenv.get("TOKEN"));
        enableIntents(builder);
        enableCaching(builder);
        preBuild(builder);

        this.shardManager = builder.build();
        postBuild(shardManager);

        Runtime.getRuntime().addShutdownHook(new Thread(this::handleShutdown));
        Runtime.getRuntime().addShutdownHook(new Thread(this::onShutdown));

        this.enhancedPackage = EnhancedBot.class.getPackageName();
        this.botPackage = getClass().getPackageName();

        this.dataController = new DataController(this);
        enableDatabase();

        this.listenerController = new ListenerController(this);
        enableListeners();

    }

    private void enableListeners() {
        List<String> listenerPackages = new ArrayList<>();
        listenerPackages.add(this.enhancedPackage + ".listener");

        EnableListeners annotation = getClass().getDeclaredAnnotation(EnableListeners.class);
        if(annotation != null) {
            for (String pkg : annotation.value()) {
                listenerPackages.add(this.botPackage + "." + pkg);
            }
        }

        listenerPackages.forEach(this.listenerController::registerListeners);
    }

    private void enableIntents(DefaultShardManagerBuilder builder) {
        EnableIntents annotation = getClass().getDeclaredAnnotation(EnableIntents.class);
        if(annotation != null) {
            builder.enableIntents(Arrays.stream(annotation.value()).toList());
        }
    }

    private void enableCaching(DefaultShardManagerBuilder builder) {
        EnableCaching annotation = getClass().getDeclaredAnnotation(EnableCaching.class);
        if(annotation != null) {
            builder.setMemberCachePolicy(annotation.policy().getJdaMemberCachePolicy());
            builder.setChunkingFilter(annotation.filter().getJdaChunkingFilter());
            builder.enableCache(Arrays.stream(annotation.flags()).toList());
        }
    }

    private void enableDatabase() {
        EnableDatabase annotation = getClass().getDeclaredAnnotation(EnableDatabase.class);
        if(annotation != null) {
            this.dataController.enable();

            List<String> persisterPackages = new ArrayList<>();
            persisterPackages.add(this.enhancedPackage + ".controller.data.persister.base");
            persisterPackages.addAll(Arrays.stream(annotation.persisters()).map(pkg -> this.botPackage + "." + pkg).toList());
            persisterPackages.forEach(this.dataController::registerPersisters);

            Arrays.stream(annotation.tables()).map(pkg -> this.botPackage + "." + pkg).forEach(this.dataController::registerTables);
        }
    }

    private void handleShutdown() {
        if(this.dataController != null) this.dataController.close();
    }

    public void preBuild(DefaultShardManagerBuilder builder) {}
    public void postBuild(ShardManager shardManager) {}

    public void onReady(ReadyEvent event) {}
    public void onShutdown() {}
}
