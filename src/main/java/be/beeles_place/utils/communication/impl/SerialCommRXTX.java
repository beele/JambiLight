package be.beeles_place.utils.communication.impl;

import be.beeles_place.model.ColorModel;
import be.beeles_place.utils.communication.ASerialComm;
import be.beeles_place.utils.logger.LOGGER;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class SerialCommRXTX extends ASerialComm {

    private LOGGER logger;
    private ColorModel model;

    private boolean isRunning;
    private String portName;

    private CommPortIdentifier portId;
    private SerialPort port;
    private InputStream input;
    private OutputStream output;

    public SerialCommRXTX(ColorModel model) {
        this.model = model;

        logger = LOGGER.getInstance();
        logger.INFO("COMM => Initiating serial communication service using RXTX lib");
    }

    public int initCommPort() {
        int status = 0;

        try {
            logger.INFO("COMM => Opening com port => " + portId.getName());
            port = (SerialPort) portId.open("serial talk", 2000);
            port.setSerialPortParams(100000,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            Thread.sleep(5000);
            input = port.getInputStream();
            output = port.getOutputStream();

        } catch (PortInUseException e) {
            logger.ERROR("COMM => Serial comm port is already in use!");
            status = -1;
        } catch (UnsupportedCommOperationException e) {
            logger.ERROR("COMM => Unsupported operation on comm port!");
            status = -1;
        } catch (IOException e) {
            logger.ERROR("COMM => General IO comm exception!");
            status = -1;
        } catch (Exception e) {
            logger.ERROR("COMM => An unexpected error occured!\n" + e.getLocalizedMessage());
            status = -1;
        }

        return status;
    }

    public void disposeCommPort() {
        logger.INFO("COMM => Terminating and cleaning up serial communication!");
        port.close();
        output = null;
        port = null;
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
                if(input.available() > 0) {
                    if(input.read() == 50) {
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
                                output.write((byte)colors[(currentStep * stepSize / 3) + i][j]);
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
            } catch (IOException e) {
                logger.ERROR("COMM => Error during serial communication!");
                disposeCommPort();
                isRunning = false;
            }
        }
    }

    //Getters & setters.
    public String getPortName() {
        return portName;
    }

    public void setPortName(String portName) {
        this.portName = portName;
        isRunning = false;

        try {
            portId = CommPortIdentifier.getPortIdentifier(portName);
            //portId = CommPortIdentifier.getPortIdentifier(SerialUtil.getArduinoSerialDeviceName());
            //portId = CommPortIdentifier.getPortIdentifier("/dev/tty.usbmodem1421");

        } catch (gnu.io.NoSuchPortException nsp) {
            logger.ERROR("COMM => " + nsp.getMessage());
        } catch (Exception exe) {
            logger.ERROR("COMM => Unexpected error occured \n" + exe.getMessage());
        }
    }

    //Utils methods
    public List<String> getSerialDevicesList() {
        ArrayList<String> deviceList = new ArrayList<String>();

        java.util.Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier portIdentifier = portEnum.nextElement();
            deviceList.add(portIdentifier.getName());
        }

        return deviceList;
    }
}