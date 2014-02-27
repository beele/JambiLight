package be.beeles_place.jambiLight.utils.communication.impl;

import be.beeles_place.jambiLight.utils.communication.ASerialComm;
import be.beeles_place.jambiLight.utils.logger.LOGGER;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class SerialCommMock extends ASerialComm {

    private LOGGER logger;

    private String portName;

    public SerialCommMock() {
        logger = LOGGER.getInstance();
        logger.INFO("COMM => starting serial communication mock service");
    }

    @Override
    public void setUpCommPort(String portName) {
        this.portName = portName;
    }

    @Override
    public void start() {
        try {
            logger.INFO("COMM => Mock color sent!");
            Thread.sleep(250);
        } catch (InterruptedException e) {
            logger.ERROR("COMM => Communications thread interrupted! Closing comm!");
        }
    }

    public void stop() {
        logger.INFO("COMM => Terminating and cleaning up mock serial communication!");
        super.forceQuit = true;
    }

    //Getters & setters.
    public String getCurrentPortName() {
        return portName;
    }

    @Override
    public List<String> getSerialDevicesList() {
        return Arrays.asList(new String[]{"COM1","COM2","COM3"});
    }
}