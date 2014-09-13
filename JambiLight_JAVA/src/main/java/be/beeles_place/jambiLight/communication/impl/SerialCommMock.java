package be.beeles_place.jambiLight.communication.impl;

import be.beeles_place.jambiLight.communication.AbstractSerialCommStrategy;
import be.beeles_place.jambiLight.model.ColorModel;

import java.util.Arrays;
import java.util.List;

public class SerialCommMock extends AbstractSerialCommStrategy {

    private String portName;

    public SerialCommMock() {
        logger.INFO("COMM => starting serial communication mock service");
    }

    @Override
    public void setUpCommPort(ColorModel model, String portName) {
        this.model = model;
        this.portName = portName;
    }

    @Override
    public void start() {
        try {
            //Check to see if new colors are available.
            if(model.areNewColorsForCommAvailable()) {
                StringBuilder sb = new StringBuilder();
                sb.append("COMM => Colors: ");
                for (int[] colors : model.getCurrentColorsForComm()) {
                    sb.append("R: ");
                    sb.append(colors[0]);
                    sb.append(" G: ");
                    sb.append(colors[1]);
                    sb.append(" B: ");
                    sb.append(colors[2]);
                    sb.append("\t");
                }
                logger.INFO(sb.toString());
            }
            Thread.sleep(5);
        } catch (InterruptedException e) {
            logger.ERROR("COMM => Communications thread interrupted! Closing comm!");
        }
    }

    public void stop() {
        logger.INFO("COMM => Terminating and cleaning up mock serial communication!");
        forceQuit = true;
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