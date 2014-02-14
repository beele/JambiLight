package be.beeles_place.utils.communication;

import java.awt.*;

public interface ISerialComm {

    int initCommPort();

    void disposeCommPort();

    void setColor(Color color);

    void setSaveToEeprom(boolean doSave);

    String getPortName();

    void setPortName(String portName);
}
