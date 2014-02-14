package be.beeles_place.utils.communication;

import be.beeles_place.utils.logger.LOGGER;

import java.awt.*;

public class SerialCommMock extends ASerialComm {

    private boolean isRunning;

    public SerialCommMock() {
        LOGGER.getInstance().INFO("starting serial communication mock service");
        isRunning = false;
    }

    public int initCommPort() {
        return 0;
    }

    public void disposeCommPort() {
        LOGGER.getInstance().INFO("Disposing mock serialcomm");
    }

    public void setColor(Color color) {
    }

    @Override
    public void setSaveToEeprom(boolean doSave) {
    }

    @Override
    public void run() {
        isRunning = true;
        while(isRunning) {
            try {
                LOGGER.getInstance().INFO("mock color sent!");
                Thread.sleep(250);
            } catch (InterruptedException e) {
                LOGGER.getInstance().ERROR("Thread interrupted! Aborting thread!");
                isRunning = false;
            }
        }
    }

    public String getPortName() {
        return null;
    }

    public void setPortName(String portName) {

    }
}
