package dev.projectenhanced.enhancedjda.logger;

import lombok.Getter;
import net.dv8tion.jda.internal.utils.JDALogger;
import org.slf4j.Logger;

/**
 * Util class to create loggers
 */
public class EnhancedLogger {
    @Getter private final static Logger logger;
    @Getter private final static Logger errorLogger;

    static {
        logger = JDALogger.getLog("EnhancedBot");
        errorLogger = JDALogger.getLog("System Error");
    }

}
