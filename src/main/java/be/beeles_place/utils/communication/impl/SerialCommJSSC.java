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

    private LOGGER logger;
    private ColorModel model;

    private boolean isRunning;
    private String portName;

    private SerialPort port;

    public SerialCommJSSC(ColorModel model) {
        this.model = model;

        logger = LOGGER.getInstance();
        logger.INFO("COMM => Initiating serial communication service using JSSC lib");
    }

    @Override
    public int initCommPort() {
        int status = 0;

        try {
            logger.INFO("COMM => Opening com port => " + portName);
            port = new SerialPort(portName);
            port.openPort();
            port.setParams( 100000,
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);
            Thread.sleep(5000);
        } catch (Exception e) {
            logger.ERROR("COMM => An unexpected error occured!\n" + e.getLocalizedMessage());
            status = -1;
        }

        return status;
    }

    @Override
    public void disposeCommPort() {
        logger.INFO("COMM => Terminating and cleaning up serial communication!");
        try {
            port.closePort();
            port = null;
        } catch (SerialPortException e) {
            logger.ERROR("COMM => Could not close comm port!");
        }
    }

    private boolean canSendNext;
    private int currentStep;
    private int stepSize;
    private int totalBytes;
    private int steps;

    @Override
    public void run() {
        isRunning = true;

        //Init variables for serial communication loops.
        totalBytes = model.getNumberOfConsolidatedRegions() * 3;
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
                logger.ERROR("COMM => Thread interrupted! Aborting thread!");
                disposeCommPort();
                isRunning = false;
            } catch (SerialPortException e) {
                logger.ERROR("COMM => Error during serial communication!");
                disposeCommPort();
                isRunning = false;
            }
        }
    }

    //Getters & setters.
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