package be.beeles_place.jambiLight.communication;

import be.beeles_place.jambiLight.model.ColorModel;
import be.beeles_place.jambiLight.utils.logger.LOGGER;
import gnu.io.CommPortIdentifier;

import java.util.List;

public abstract class AbstractSerialCommStrategy implements ISerialComm, Runnable {

    protected LOGGER logger;
    protected ColorModel model;

    protected boolean forceQuit = false;

    /**
     * Constructor.
     */
    protected AbstractSerialCommStrategy() {
        logger = LOGGER.getInstance();
    }

    /**
     * Executed when the strategy is started as a thread.
     * Will loop the start() method (in a while loop) as long as the forceQuit variable is false.
     * If an exception occurs the logic will be aborted.
     */
    @Override
    public void run() {
        forceQuit = false;
        logger.INFO("COMM => Communication strategy started.");

        while (!forceQuit) {
            start();
        }
        stop();
    }

    @Override
    public abstract void start();

    @Override
    public void stop() {
        forceQuit = true;
    }

    //Methods available to all implementations.
    @Override
    public String getArduinoSerialDeviceName() {
        String name = "";

        List<String> devices = getSerialDevicesList();
        for (String deviceName : devices) {
            //TODO: paramterize values!
            if (deviceName.contains("tty") && deviceName.contains("usbmodem")) {
                name = deviceName;
            } else if (deviceName.contains("COM") && Integer.parseInt(deviceName.replace("COM", "")) == 3) {
                name = deviceName;
            }
        }
        return name;
    }

    @Override
    public String getPortTypeName(int portType) {
        switch (portType) {
            case CommPortIdentifier.PORT_I2C:
                return "I2C";
            case CommPortIdentifier.PORT_PARALLEL:
                return "Parallel";
            case CommPortIdentifier.PORT_RAW:
                return "Raw";
            case CommPortIdentifier.PORT_RS485:
                return "RS485";
            case CommPortIdentifier.PORT_SERIAL:
                return "Serial";
            default:
                return "unknown type";
        }
    }
}