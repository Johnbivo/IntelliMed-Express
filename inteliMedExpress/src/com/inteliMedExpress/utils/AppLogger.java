package com.inteliMedExpress.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Logger utility class for InteliMedExpress application.
 * Provides centralized logging capabilities with both console and file output.
 */
public class AppLogger {
    private static final Logger LOGGER = Logger.getLogger("InteliMedExpress");
    private static final String LOG_DIRECTORY = "logs";
    private static FileHandler fileHandler;
    private static boolean initialized = false;

    /**
     * Log levels available for the application
     */
    public enum LogLevel {
        INFO, WARNING, ERROR, DEBUG
    }

    /**
     * Initialize the logger with console and file handlers.
     * Creates a log directory if it doesn't exist.
     */
    public static synchronized void initialize() {
        if (initialized) {
            return;
        }

        try {
            // Create logs directory if it doesn't exist
            File logDir = new File(LOG_DIRECTORY);
            if (!logDir.exists()) {
                logDir.mkdir();
            }

            // Get current date for log file name
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String logFileName = LOG_DIRECTORY + "/app_" + dateFormat.format(new Date()) + ".log";

            // Configure logger
            LOGGER.setUseParentHandlers(false);
            LOGGER.setLevel(Level.ALL);

            // Create and add console handler
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setFormatter(new LogFormatter());
            consoleHandler.setLevel(Level.ALL);
            LOGGER.addHandler(consoleHandler);

            // Create and add file handler
            fileHandler = new FileHandler(logFileName, true);
            fileHandler.setFormatter(new LogFormatter());
            fileHandler.setLevel(Level.ALL);
            LOGGER.addHandler(fileHandler);

            initialized = true;
            LOGGER.info("Logger initialized. Log file: " + logFileName);
        } catch (IOException e) {
            System.err.println("Failed to initialize logger: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Log a message with the specified level and class name.
     *
     * @param level     The log level
     * @param className The name of the class generating the log message
     * @param message   The message to log
     */
    public static void log(LogLevel level, String className, String message) {
        if (!initialized) {
            initialize();
        }

        Level javaLevel;
        switch (level) {
            case INFO:
                javaLevel = Level.INFO;
                break;
            case WARNING:
                javaLevel = Level.WARNING;
                break;
            case ERROR:
                javaLevel = Level.SEVERE;
                break;
            case DEBUG:
                javaLevel = Level.FINE;
                break;
            default:
                javaLevel = Level.INFO;
        }

        LOGGER.logp(javaLevel, className, null, message);
    }

    /**
     * Log an exception with ERROR level.
     *
     * @param className The name of the class generating the log message
     * @param message   The message to log
     * @param exception The exception to log
     */
    public static void logException(String className, String message, Exception exception) {
        if (!initialized) {
            initialize();
        }

        StringBuilder sb = new StringBuilder(message);
        sb.append("\nException: ").append(exception.getClass().getName())
                .append("\nMessage: ").append(exception.getMessage())
                .append("\nStack Trace: ");

        for (StackTraceElement element : exception.getStackTrace()) {
            sb.append("\n    at ").append(element.toString());
        }

        LOGGER.logp(Level.SEVERE, className, null, sb.toString());
    }

    /**
     * Convenience method for logging INFO level messages.
     */
    public static void info(String className, String message) {
        log(LogLevel.INFO, className, message);
    }

    /**
     * Convenience method for logging WARNING level messages.
     */
    public static void warning(String className, String message) {
        log(LogLevel.WARNING, className, message);
    }

    /**
     * Convenience method for logging ERROR level messages.
     */
    public static void error(String className, String message) {
        log(LogLevel.ERROR, className, message);
    }

    /**
     * Convenience method for logging DEBUG level messages.
     */
    public static void debug(String className, String message) {
        log(LogLevel.DEBUG, className, message);
    }

    /**
     * Custom formatter for log records.
     */
    private static class LogFormatter extends Formatter {
        private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        @Override
        public String format(LogRecord record) {
            StringBuilder sb = new StringBuilder();

            sb.append(DATE_FORMAT.format(new Date(record.getMillis())))
                    .append(" [").append(record.getLevel()).append("] ")
                    .append(record.getSourceClassName()).append(": ")
                    .append(record.getMessage())
                    .append(System.lineSeparator());

            return sb.toString();
        }
    }

    /**
     * Close any open handlers when the application is shutting down.
     */
    public static void shutdown() {
        if (initialized) {
            for (Handler handler : LOGGER.getHandlers()) {
                handler.close();
            }
        }
    }
}