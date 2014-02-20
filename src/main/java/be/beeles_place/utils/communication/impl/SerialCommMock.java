package be.beeles_place.utils.communication.impl;

import be.beeles_place.utils.communication.ASerialComm;
import be.beeles_place.utils.logger.LOGGER;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class SerialCommMock extends ASerialComm {

    private LOGGER logger;

    private boolean isRunning;

    public SerialCommMock() {
        logger = LOGGER.getInstance();
        logger.INFO("COMM => starting serial communication mock service");
        isRunning = false;
    }

    public int initCommPort() {
        return 0;
    }

    public void disposeCommPort() {
        logger.INFO("COMM => Disposing mock serialcomm");
    }

    public void setColor(Color color) {
    }

    @Override
    public void run() {
        isRunning = true;
        while (isRunning) {
            try {
                logger.INFO("COMM => Mock color sent!");
                Thread.sleep(250);
            } catch (InterruptedException e) {
                logger.ERROR("COMM => Thread interrupted! Aborting thread!");
                isRunning = false;
            }
        }
    }

    //Getters & setters.
    public String getPortName() {
        return null;
    }

    public void setPortName(String portName) {
        //Do nothing for now.
    }

    @Override
    public List<String> getSerialDevicesList() {
        return Arrays.asList(new String[]{"COM1","COM2","COM3"});
    }
}