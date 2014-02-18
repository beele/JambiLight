package be.beeles_place.utils.logger;

public class LOGGER {

    private static LOGGER logger;
    private LoggerLevel level;

    private LOGGER() {
        level = LoggerLevel.ALL;
    }

    public static LOGGER getInstance() {
        if (logger == null) {
            logger = new LOGGER();
        }
        return logger;
    }

    public void setLoggerLevel(LoggerLevel level) {
        this.level = level;
    }

    public void DEBUG(String message) {
        LOG("DEBUG \t=> " + message, 0);
    }

    public void INFO(String message) {
        LOG("INFO  \t=> " + message, 1);
    }

    public void ERROR(String message) {
        LOG("ERROR \t=> " + message, 2);
    }

    private void LOG(String message, int type) {
        boolean canPrint = false;

        if (type == 0 && (level.equals(LoggerLevel.ALL) || (level.equals(LoggerLevel.DEBUG)))) {
            canPrint = true;
        } else if (type == 1 && (level.equals(LoggerLevel.ALL) || (level.equals(LoggerLevel.INFO)))) {
            canPrint = true;
        } else if (type == 2 && (level.equals(LoggerLevel.ALL) || (level.equals(LoggerLevel.ERROR)))) {
            canPrint = true;
        }

        if (canPrint) {
            System.out.println(message);
        }
    }
}
