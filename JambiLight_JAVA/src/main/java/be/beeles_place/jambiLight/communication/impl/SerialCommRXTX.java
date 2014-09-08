package be.beeles_place.jambiLight.communication.impl;

import be.beeles_place.jambiLight.communication.AbstractSerialCommStrategy;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Please do not longer use this implementation!
 * It should still function but the JSSC implementation is better in every way!
 * May be removed in the future.
 */
@SuppressWarnings("unchecked")
@Deprecated
public class SerialCommRXTX extends AbstractSerialCommStrategy {

    private String portName;

    private CommPortIdentifier portId;
    private SerialPort port;
    private InputStream input;
    private OutputStream output;

    private boolean started;

    public SerialCommRXTX() {
        logger.INFO("COMM => Initiating serial communication service using RXTX lib");
    }

    @Override
    public void setUpCommPort(String portName) {
        this.portName = portName;
        try {
            logger.INFO("COMM => Opening com port => " + portName);

            portId = CommPortIdentifier.getPortIdentifier(portName);
            //portId = CommPortIdentifier.getPortIdentifier(SerialUtil.getArduinoSerialDeviceName());
            //portId = CommPortIdentifier.getPortIdentifier("/dev/tty.usbmodem1421");

            port = (SerialPort) portId.open("serial talk", 2000);
            port.setSerialPortParams(100000,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            //Initial state setup.
            totalBytes = model.getNumberOfConsolidatedRegions() * 3;
            stepSize = 48;
            steps = (int) totalBytes / stepSize;
            currentStep = 0;
            canSendNext = true;

        } catch (PortInUseException e) {
            logger.ERROR("COMM => Serial comm port is already in use!");
        } catch (UnsupportedCommOperationException e) {
            logger.ERROR("COMM => Unsupported operation on comm port!");
        } catch (Exception e) {
            logger.ERROR("COMM => An unexpected error occured!\n" + e.getLocalizedMessage());
        }
    }

    @Override
    public void start() {
        try {
            if(!started){
                //Wait for 5 seconds to give the Arduino time to reset itself.
                Thread.sleep(5000);
                input = port.getInputStream();
                output = port.getOutputStream();
                started = true;
            }

            if(input.available() > 0) {
                if(input.read() == 50) {
                    canSendNext = true;
                }
            } else {
                Thread.sleep(1);
            }

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
            logger.ERROR("COMM => Communications thread interrupted! Closing comm!");
            stop();
        } catch (IOException e) {
            logger.ERROR("COMM => Error during serial communication! Closing comm!");
            stop();
        }
    }

    public void stop() {
        logger.INFO("COMM => Terminating and cleaning up serial communication!");
        forceQuit = true;
        port.close();
        output = null;
        port = null;
    }

    private boolean canSendNext;
    private int currentStep;
    private int stepSize;
    private int totalBytes;
    private int steps;

    //Getters & setters.
    @Override
    public String getCurrentPortName() {
        return portName;
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