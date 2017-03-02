package be.beeles_place.jambiLight.utils.logger;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Logger class.
 */
public class LOGGER {

    //Constants.
    private final int DEBUG = 0;
    private final int INFO  = 1;
    private final int ERROR = 2;

    //Private fields.
    private static LOGGER logger;
    private LoggerLevel level;

    private boolean logToSTDOUT;
    private boolean logToFile;
    private String logFileName;
    private Path logFile;

    private DateTimeFormatter timeFormatter;

    /**
     * Private constructor for singleton logic.
     */
    private LOGGER() {
        level = LoggerLevel.ALL;

        LocalDateTime now = LocalDateTime.now();
        logFileName = "JambiLight[" + now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd@HH-mm-ss")) + "].log";
        logFile = Paths.get("./" + logFileName);

        timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    }

    /**
     * Returns an instance of the logger.
     *
     * @return The instance of the logger.
     */
    public static LOGGER getInstance() {
        if (logger == null) {
            logger = new LOGGER();
        }
        return logger;
    }

    /**
     * Sets the level for the logger. This will limit what is printed in the output.
     *
     * @param level The level that should be printed. Use the LoggerLevel enum for meaningful values.
     */
    public void setLoggerLevel(LoggerLevel level) {
        this.level = level;
    }

    /**
     * When set to true will log output to the standard output.
     *
     * @param logToSTDOUT If true logs to to STDOUT, false if not.
     */
    public void setLogToSTDOUT(boolean logToSTDOUT) {
        this.logToSTDOUT = logToSTDOUT;
    }

    /**
     * When set to true will log output to a file named JambiLight[yyyy-MM-dd@HH:mm:ss].
     *
     * @param logToFile If true logs to log file on disk, false if not.
     */
    public void setLogToFile(boolean logToFile) {
        this.logToFile = logToFile;
    }

    /**
     * Return the Path instance that points to the current log (or null if none).
     *
     * @return The Path instance that points to the current log file.
     */
    public Path getCurrentLogFile() {
        return logFile;
    }

    /**
     * Logs the given message at the given level.
     *
     * @param message The message to log.
     * @param level The level to log the message at.
     */
    private void LOG(String message, int level) {
        boolean canPrint = false;

        try {
            //TODO: Add more log levels!
            if (level == DEBUG && (this.level.equals(LoggerLevel.ALL) || (this.level.equals(LoggerLevel.DEBUG)))) {
                canPrint = true;
            } else if (level == INFO && (this.level.equals(LoggerLevel.ALL) || (this.level.equals(LoggerLevel.INFO)))) {
                canPrint = true;
            } else if (level == ERROR && (this.level.equals(LoggerLevel.ALL) || (this.level.equals(LoggerLevel.ERROR)))) {
                canPrint = true;
            }

            if (canPrint) {
                message = "[" + LocalTime.now().format(timeFormatter) + "] \t" + message;

                if(logToSTDOUT) {
                    if(level == ERROR) {
                        System.err.println(message);
                    } else {
                        System.out.println(message);
                    }
                }
                if(logToFile) {
                    Files.write(logFile, (message + "\n").getBytes(), StandardOpenOption.WRITE, StandardOpenOption.APPEND, StandardOpenOption.CREATE);
                }
            }
        } catch (Exception e) {
            System.err.println("FATAL \t=> Cannot log: " + e.getMessage());
        }
    }

    /**
     * Shortcut method, logs as DEBUG.
     *
     * @param message The message to log.
     */
    public void DEBUG(String message) {
        LOG("DEBUG \t=> " + message, DEBUG);
    }

    /**
     * Shortcut method, logs as INFO.
     *
     * @param message The message to log.
     */
    public void INFO(String message) {
        LOG("INFO  \t=> " + message, INFO);
    }

    /**
     * Shortcut method, logs as ERROR.
     *
     * @param message The message to log.
     */
    public void ERROR(String message) {
        LOG("ERROR \t=> " + message, ERROR);
    }

    /**
     * Shortcut method, logs as ERROR and prints specific exception information.
     *
     * @param message The message to log.
     * @param e The Exception instance that was throw.
     */
    public void ERROR(String message, Exception e) {
        LOG("ERROR \t=> " + message + "\n Message:" + e.getMessage() + "\n Cause: " + e.getCause(), ERROR);
    }
}