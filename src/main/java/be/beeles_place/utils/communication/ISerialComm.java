package be.beeles_place.utils.communication;

import java.util.List;

public interface ISerialComm {

    int initCommPort();

    void disposeCommPort();

    String getPortName();

    void setPortName(String portName);

    String getArduinoSerialDeviceName();

    List<String> getSerialDevicesList();
}
