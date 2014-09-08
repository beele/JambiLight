package be.beeles_place.jambiLight.communication;

import java.util.List;

public interface ISerialComm {

    /**
     * Sets up the given port.
     *
     * @param portName The name of the port to set up.
     */
    public void setUpCommPort(String portName);

    /**
     * Starts the communication with the given strategy and on the given port.
     */
    public void start();

    /**
     * Stops the communication.
     */
    public void stop();

    /**
     * Returns the name of the current port.
     *
     * @return The name of the current port.
     */
    public String getCurrentPortName();

    /**
     * Returns a list containing all the ports the given CommunicationStrategy knows.
     *
     * @return A List containing the names of all the ports known.
     */
    public List<String> getSerialDevicesList();

    /**
     * Find the arduino in the serial devices list.
     *
     * @return The name of the device that represents the Arduino.
     */
    public String getArduinoSerialDeviceName();

    /**
     * Returns the name of the port type.
     *
     * @param portType port type.
     * @return The name of the port type.
     */
    public String getPortTypeName(int portType);
}