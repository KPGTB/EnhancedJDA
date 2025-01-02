package dev.projectenhanced.enhancedjda;

import dev.projectenhanced.enhancedjda.controller.PackageMapping;
import dev.projectenhanced.enhancedjda.controller.command.CommandController;
import dev.projectenhanced.enhancedjda.controller.command.component.ComponentController;
import dev.projectenhanced.enhancedjda.discord.EnableCaching;
import dev.projectenhanced.enhancedjda.discord.EnableIntents;
import dev.projectenhanced.enhancedjda.controller.data.DataController;
import dev.projectenhanced.enhancedjda.controller.listener.ListenerController;
import dev.projectenhanced.enhancedjda.logger.EnhancedLogger;
import dev.projectenhanced.enhancedjda.logger.LoggingOutputStream;
import dev.projectenhanced.enhancedjda.util.FileUtil;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.slf4j.event.Level;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

@Getter
public class EnhancedBot {
    private static EnhancedBot bot;

    private final String enhancedPackage;
    private final String botPackage;

    private final ComponentController componentController;
    private final ListenerController listenerController;
    private final CommandController commandController;
    private final DataController dataController;

    private final Dotenv dotenv;
    private final ShardManager shardManager;

    private final PackageMapping packageMapping;

    /**
     * Method used to run the bot
     * @param botClass Main class of bot. It needs to extend EnhancedBot class
     * @throws ReflectiveOperationException
     */
    protected static void runBot(Class<? extends EnhancedBot> botClass) throws ReflectiveOperationException {
        bot = botClass.getDeclaredConstructor().newInstance();
    }

    protected EnhancedBot() {
        System.setErr(new PrintStream(new LoggingOutputStream(EnhancedLogger.getErrorLogger(),Level.ERROR)));
        this.packageMapping = getClass().getDeclaredAnnotation(PackageMapping.class);

        File envFile = new File(".env");
        if(!envFile.exists()) {
            FileUtil.saveResource(".env");
            EnhancedLogger.getLogger().error("Created file .env ("+envFile.getAbsolutePath()+") - please configure it!");
            new Scanner(System.in).nextLine();
            System.exit(0);
        }
        this.dotenv = Dotenv.configure().load();

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

        this.componentController = new ComponentController();

        this.dataController = new DataController(this);
        enableDatabase();

        this.listenerController = new ListenerController(this);
        enableListeners();

        this.commandController = new CommandController(this);
        enableCommands();
        enableContexts();
    }

    private void enableListeners() {
        List<String> listenerPackages = new ArrayList<>();
        listenerPackages.add(this.enhancedPackage + ".listener");

        if(packageMapping != null) {
            for (String pkg : packageMapping.listeners()) {
                listenerPackages.add(this.botPackage + "." + pkg);
            }
        }

        listenerPackages.forEach(this.listenerController::registerListeners);
    }

    private void enableCommands() {
        if(packageMapping != null) {
            Arrays.stream(packageMapping.commands()).map(pkg -> this.botPackage + "." + pkg).forEach(this.commandController::registerCommands);
        }
    }

    private void enableContexts() {
        if(packageMapping != null) {
            Arrays.stream(packageMapping.contexts()).map(pkg -> this.botPackage + "." + pkg).forEach(this.commandController::registerContexts);
        }
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
        if(packageMapping != null) {
            this.dataController.enable();

            List<String> persisterPackages = new ArrayList<>();
            persisterPackages.add(this.enhancedPackage + ".controller.data.persister.base");
            persisterPackages.addAll(Arrays.stream(packageMapping.persisters()).map(pkg -> this.botPackage + "." + pkg).toList());
            persisterPackages.forEach(this.dataController::registerPersisters);

            Arrays.stream(packageMapping.tables()).map(pkg -> this.botPackage + "." + pkg).forEach(this.dataController::registerTables);
        }
    }

    private void handleShutdown() {
        if(this.dataController != null) this.dataController.close();
    }

    /**
     * Get main class from package name
     * @param packageName Name of package
     */
    public Class<?> getPackageClass(String packageName) {
        return packageName.startsWith(this.enhancedPackage) ? this.getClass().getSuperclass() : this.getClass();
    }

    /**
     * Method invoked just before building shard manager. You can modify builder
     * @param builder
     */
    public void preBuild(DefaultShardManagerBuilder builder) {}

    /**
     * Method invoked just after building shard manager. You can modify it
     * @param shardManager
     */
    public void postBuild(ShardManager shardManager) {}

    /**
     * Method invoked when bot is ready
     * @param event ReadyEvent
     */
    public void onReady(ReadyEvent event) {}

    /**
     * Method invoked just before shutdown
     */
    public void onShutdown() {}
}
