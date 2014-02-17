package be.beeles_place.utils.communication.impl;

import be.beeles_place.model.ColorModel;
import be.beeles_place.utils.communication.ASerialComm;
import be.beeles_place.utils.logger.LOGGER;
import gnu.io.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SerialCommRXTX extends ASerialComm {

    private boolean isRunning;
    private String portName;

    private CommPortIdentifier portId;
    private SerialPort port;
    private InputStream input;
    private OutputStream output;

    private ColorModel model;

    public SerialCommRXTX(ColorModel model) {
        this.model = model;
        LOGGER.getInstance().INFO("Initiating serial communication service using RXTX lib");
    }

    public int initCommPort() {
        int status = 0;

        try {
            LOGGER.getInstance().INFO("Opening com port => " + portId.getName());
            port = (SerialPort) portId.open("serial talk", 2000);
            port.setSerialPortParams(100000,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            Thread.sleep(5000);
            input = port.getInputStream();
            output = port.getOutputStream();

        } catch (PortInUseException e) {
            LOGGER.getInstance().ERROR("Serial comm port is already in use!");
            status = -1;
        } catch (UnsupportedCommOperationException e) {
            LOGGER.getInstance().ERROR("Unsupported operation on comm port!");
            status = -1;
        } catch (IOException e) {
            LOGGER.getInstance().ERROR("General IO comm exception!");
            status = -1;
        } catch (Exception e) {
            LOGGER.getInstance().ERROR("An unexpected error occured!\n" + e.getLocalizedMessage());
            status = -1;
        }

        return status;
    }

    public void disposeCommPort() {
        LOGGER.getInstance().INFO("Terminating and cleaning up serial communication!");
        port.close();
        output = null;
        port = null;
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
                LOGGER.getInstance().ERROR("Thread interrupted! Aborting thread!");
                disposeCommPort();
                isRunning = false;
            } catch (IOException e) {
                LOGGER.getInstance().ERROR("Error during serial communication!");
                disposeCommPort();
                isRunning = false;
            }
        }
    }

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
            LOGGER.getInstance().ERROR(nsp.getMessage());
        } catch (Exception exe) {
            LOGGER.getInstance().ERROR("Unexpected error occured \n" + exe.getMessage());
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
