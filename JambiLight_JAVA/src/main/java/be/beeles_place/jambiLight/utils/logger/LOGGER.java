package be.beeles_place.jambiLight.utils.logger;

/**
 * Logger class.
 */
public class LOGGER {

    //Private fields.
    private static LOGGER logger;
    private LoggerLevel level;

    /**
     * Private constructor for singleton logic.
     */
    private LOGGER() {
        level = LoggerLevel.ALL;
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
     * Logs the given message at the given level.
     *
     * @param message The message to log.
     * @param level The level to log the message at.
     */
    private void LOG(String message, int level) {
        boolean canPrint = false;

        if (level == 0 && (this.level.equals(LoggerLevel.ALL) || (this.level.equals(LoggerLevel.DEBUG)))) {
            canPrint = true;
        } else if (level == 1 && (this.level.equals(LoggerLevel.ALL) || (this.level.equals(LoggerLevel.INFO)))) {
            canPrint = true;
        } else if (level == 2 && (this.level.equals(LoggerLevel.ALL) || (this.level.equals(LoggerLevel.ERROR)))) {
            canPrint = true;
        }

        if (canPrint) {
            System.out.println(message);
        }
    }

    /**
     * Shortcut method, logs as DEBUG.
     *
     * @param message The message to log.
     */
    public void DEBUG(String message) {
        LOG("DEBUG \t=> " + message, 0);
    }

    /**
     * Shortcut method, logs as INFO.
     *
     * @param message The message to log.
     */
    public void INFO(String message) {
        LOG("INFO  \t=> " + message, 1);
    }

    /**
     * Shortcut method, logs as ERROR.
     *
     * @param message The message to log.
     */
    public void ERROR(String message) {
        LOG("ERROR \t=> " + message, 2);
    }
}
