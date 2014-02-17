package be.beeles_place.utils.communication.impl;

import be.beeles_place.model.ColorModel;
import be.beeles_place.utils.communication.ASerialComm;
import be.beeles_place.utils.logger.LOGGER;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

import java.util.Arrays;
import java.util.List;

public class SerialCommJSSC extends ASerialComm {

    private boolean isRunning;
    private String portName;

    private SerialPort port;

    private ColorModel model;

    public SerialCommJSSC(ColorModel model) {
        this.model = model;
        LOGGER.getInstance().INFO("Initiating serial communication service using JSSC lib");
    }

    @Override
    public int initCommPort() {
        int status = 0;

        try {
            LOGGER.getInstance().INFO("Opening com port => " + portName);
            port = new SerialPort(portName);
            port.setParams( 100000,
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);
            Thread.sleep(5000);
        } catch (Exception e) {
            LOGGER.getInstance().ERROR("An unexpected error occured!\n" + e.getLocalizedMessage());
            status = -1;
        }

        return status;
    }

    @Override
    public void disposeCommPort() {
        LOGGER.getInstance().INFO("Terminating and cleaning up serial communication!");
        try {
            port.closePort();
            port = null;
        } catch (SerialPortException e) {
            LOGGER.getInstance().ERROR("Could not close comm port!");
        }
    }

    boolean canSendNext;
    int currentStep;
    int stepSize;
    int totalBytes;
    int steps;

    @Override
    public void run() {
        isRunning = true;

        //Init variables for serial communication loops.
        totalBytes = model.getNumberOfColorsProcessed() * 3;
        //TODO: put the stepsize (aka bytes sent per loop) in the settings model.
        stepSize = 48;
        steps = (int) totalBytes / stepSize;
        currentStep = 0;
        canSendNext = true;

        if (initCommPort() != 0) {
            LOGGER.getInstance().ERROR("Cannot start serial communication!");
            isRunning = false;
            return;
        }

        while (isRunning) {
            try {
                if(port.getInputBufferBytesCount() > 0) {
                    if(port.readIntArray(1)[0] == 50) {
                        canSendNext = true;
                    }
                } else {
                    Thread.sleep(1);
                }

                //TODO: add in a system so each color is sent only once!
                if(canSendNext && model.getCurrentColors() != null) {
                    if(currentStep >= steps) {
                        currentStep = 0;
                    }

                    int[][] colors = model.getCurrentColors();
                    if(colors != null) {
                        //Sending stepSize bytes per loop means sending stepSize/steps colors (3 bytes per color).
                        for(int i = 0 ; i < stepSize / 3 ; i++) {
                            //Loop over each color (R/G/B)
                            for(int j = 0 ; j < 3 ; j++) {
                                port.writeByte((byte)colors[(currentStep * stepSize / 3) + i][j]);
                            }
                        }
                        currentStep++;
                        canSendNext = false;
                    }
                }

            } catch (InterruptedException e) {
                LOGGER.getInstance().ERROR("Thread interrupted! Aborting thread!");
                disposeCommPort();
                isRunning = false;
            } catch (SerialPortException e) {
                LOGGER.getInstance().ERROR("Error during serial communication!");
                disposeCommPort();
                isRunning = false;
            }
        }
    }

    @Override
    public String getPortName() {
        return portName;
    }

    @Override
    public void setPortName(String portName) {
        this.portName = portName;
    }

    @Override
    public List<String> getSerialDevicesList() {
        return Arrays.asList(SerialPortList.getPortNames());
    }
}
