package be.beeles_place.jambiLight.utils.communication;

import be.beeles_place.jambiLight.utils.logger.LOGGER;
import gnu.io.CommPortIdentifier;

import java.util.List;

public abstract class ASerialComm implements ISerialComm, Runnable {

    private LOGGER logger;
    protected boolean forceQuit = false;

    {
        logger = LOGGER.getInstance();
    }

    @Override
    public void run() {
        while (!forceQuit) {
            start();
        }
        if(forceQuit) {
            stop();
        }
    }

    @Override
    public abstract void start();

    public void stop() {
        forceQuit = true;
    }

    //Methods available to all implementations.
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

    private String getPortTypeName(int portType) {
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