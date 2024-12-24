package dev.projectenhanced.enhancedjda.logger;

import org.slf4j.Logger;
import org.slf4j.event.Level;

import java.io.OutputStream;

public class LoggingOutputStream extends OutputStream {
    private static final int BUFFER_SIZE = 2048;
    private final Logger logger;
    private final Level level;
    private final StringBuilder buffer = new StringBuilder(BUFFER_SIZE);

    public LoggingOutputStream(Logger logger, Level level) {
        this.logger = logger;
        this.level = level;
    }

    @Override
    public void write(int b) {
        char c = (char) b;
        if (c == '\n' || c == '\r') {
            flush();
        } else {
            buffer.append(c);
        }
    }

    @Override
    public void flush() {
        if (buffer.length() > 0) {
            String message = buffer.toString();
            buffer.setLength(0);

            // Log the message at the specified level
            switch (level) {
                case ERROR -> logger.error(message);
                case WARN -> logger.warn(message);
                case INFO -> logger.info(message);
                case DEBUG -> logger.debug(message);
                case TRACE -> logger.trace(message);
                default -> throw new IllegalArgumentException("Unsupported log level: " + level);
            }
        }
    }

    @Override
    public void close() {
        flush();
    }
}
