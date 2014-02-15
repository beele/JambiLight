package be.beeles_place.utils.communication;

public interface ISerialComm {

    int initCommPort();

    void disposeCommPort();

    String getPortName();

    void setPortName(String portName);
}
