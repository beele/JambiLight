package be.beeles_place.utils.communication;

import java.awt.*;

public interface ISerialComm {

    int initCommPort();

    void disposeCommPort();

    void setColor(Color color);

    String getPortName();

    void setPortName(String portName);
}
