package be.beeles_place.jambiLight.utils.communication;

import java.util.List;

public interface ISerialComm {

    public void setUpCommPort(String portName);

    public void start();

    public void stop();

    public String getCurrentPortName();

    public String getArduinoSerialDeviceName();

    public List<String> getSerialDevicesList();
}