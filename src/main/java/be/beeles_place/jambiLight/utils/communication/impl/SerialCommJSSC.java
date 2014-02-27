package be.beeles_place.jambiLight.utils.communication.impl;

import be.beeles_place.jambiLight.model.ColorModel;
import be.beeles_place.jambiLight.utils.communication.ASerialComm;
import be.beeles_place.jambiLight.utils.logger.LOGGER;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

import java.util.Arrays;
import java.util.List;

public class SerialCommJSSC extends ASerialComm {

    private LOGGER logger;
    private ColorModel model;

    private String portName;
    private SerialPort port;

    public SerialCommJSSC(ColorModel model) {
        this.model = model;

        logger = LOGGER.getInstance();
        logger.INFO("COMM => Initiating serial communication service using JSSC lib");
    }

    @Override
    public void setUpCommPort(String portName) {
        this.portName = portName;
        try {
            logger.INFO("COMM => Opening com port => " + portName);

            port = new SerialPort(portName);
            port.openPort();
            port.setParams( 100000,
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);

            //Initial state setup.
            totalBytes = model.getNumberOfConsolidatedRegions() * 3;
            //TODO: put the stepsize (aka bytes sent per loop) in the settings model.
            stepSize = 48;
            steps = (int)totalBytes / stepSize;
            currentStep = 0;
            canSendNext = true;

            //Wait for 5 seconds to give the Arduino time to reset itself.
            Thread.sleep(5000);
        } catch (Exception e) {
            logger.ERROR("COMM => Cannot open comport with name: " + portName);
        }
    }

    //Variables used in the start() method:
    private boolean canSendNext;
    private int currentStep;
    private int stepSize;
    private int totalBytes;
    private int steps;

    @Override
    public void start() {
        try {
            if(port.getInputBufferBytesCount() > 0) {
                //When the magic continue number has been received from the arduino!
                if(port.readIntArray(1)[0] == 50) {
                    port.purgePort(SerialPort.PURGE_TXCLEAR);
                    port.purgePort(SerialPort.PURGE_RXCLEAR);
                    canSendNext = true;
                }
            } else {
                //As long as nu input has been received from the arduino sleep for 1ms.
                Thread.sleep(1);
            }

            int[][] colors = null;
            if(canSendNext) {
                if(currentStep >= steps) {
                    if(model.getNewColorsForCommAvailable()) {
                        //New colors are available and we can send the next series of colors to the arduino.
                        //Get the new colors and reset the currentStep counter.
                        colors = model.getCurrentColorsForComm();
                        currentStep = 0;
                    } else {
                        //No new colors are available to be transmitted to the arduino.
                        //Wait some more.
                        return;
                    }
                }

                if(colors != null) {
                    //Sending stepSize bytes per loop means sending stepSize/steps colors (3 bytes per color).
                    for(int i = 0 ; i < stepSize / 3 ; i++) {
                        //Send each color (R/G/B)
                        port.writeByte((byte)colors[(currentStep * stepSize / 3) + i][0]);
                        port.writeByte((byte)colors[(currentStep * stepSize / 3) + i][1]);
                        port.writeByte((byte)colors[(currentStep * stepSize / 3) + i][2]);
                    }
                    currentStep++;
                    canSendNext = false;
                }
            }

        } catch (InterruptedException e) {
            logger.ERROR("COMM => Communications thread interrupted! Closing comm!");
            stop();
        } catch (SerialPortException e) {
            logger.ERROR("COMM => Error during serial communication! Closing comm!");
            stop();
        }
    }

    @Override
    public void stop() {
        logger.INFO("COMM => Terminating and cleaning up serial communication!");

        super.forceQuit = true;
        try {
            port.closePort();
            port = null;
        } catch (SerialPortException e) {
            logger.ERROR("COMM => Could not close comm port!");
        }
    }

    //Getters & setters.
    public String getCurrentPortName() {
        return portName;
    }

    @Override
    public List<String> getSerialDevicesList() {
        return Arrays.asList(SerialPortList.getPortNames());
    }
}