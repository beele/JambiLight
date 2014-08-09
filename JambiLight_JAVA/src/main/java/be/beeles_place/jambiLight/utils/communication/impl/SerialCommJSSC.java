package be.beeles_place.jambiLight.utils.communication.impl;

import be.beeles_place.jambiLight.model.ColorModel;
import be.beeles_place.jambiLight.utils.communication.ASerialComm;
import be.beeles_place.jambiLight.utils.logger.LOGGER;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class SerialCommJSSC extends ASerialComm {

    private LOGGER logger;
    private ColorModel model;

    private String portName;
    private SerialPort port;

    private boolean started;

    private byte[] buffer;
    private int regionIndex;

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

            stepSize = 48;
            partialSteps = stepSize / 3;
            steps = (int)totalBytes / stepSize;
            buffer = new byte[stepSize];

            currentStep = 0;
            canSendNext = true;

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
    private int partialSteps;
    private int[][] colors = null;

    @Override
    public void start() {
        long startTime = new Date().getTime();

        try {
            if(!started){
                //Wait for 5 seconds to give the Arduino time to reset itself.
                Thread.sleep(5000);
                started = true;
                //Get the initial colors.
                colors = model.getCurrentColorsForComm();
            }

            if(port.getInputBufferBytesCount() > 0) {
                //When the magic continue number has been received from the arduino!
                if(port.readIntArray(1)[0] == 50) {
                    port.purgePort(SerialPort.PURGE_TXCLEAR);
                    port.purgePort(SerialPort.PURGE_RXCLEAR);
                    canSendNext = true;
                    //logger.DEBUG("COMM => New colors to send!");
                }
            } else if(canSendNext == false) {
                //As long as nu input has been received from the arduino sleep for 1ms.
                //logger.DEBUG("COMM => No new colors to send!");
                Thread.sleep(1);
                return;
            }

            if(canSendNext) {
                if(currentStep >= steps || colors == null) {
                    if(model.getNewColorsForCommAvailable()) {
                        //New colors are available and we can send the next series of colors to the arduino.
                        //Get the new colors and reset the currentStep counter.
                        colors = model.getCurrentColorsForComm();
                        currentStep = 0;
                    } else {
                        //No new colors are available to be transmitted to the arduino.
                        //Save cpu time by not letting this run without a small sleep!
                        Thread.sleep(5);
                        return;
                    }
                }

                int bitIndex = 0;
                if(colors != null) {
                    //Sending stepSize bytes per loop means sending stepSize/steps colors (3 bytes per color).
                    for(int i = 0 ; i < partialSteps ; i++) {
                        //Send each color (R/G/B)
                        regionIndex = (currentStep * partialSteps) + i;

                        buffer[bitIndex++] = (byte)colors[regionIndex][0];
                        buffer[bitIndex++] = (byte)colors[regionIndex][1];
                        buffer[bitIndex++] = (byte)colors[regionIndex][2];
                    }
                    port.writeBytes(buffer);

                    currentStep++;
                    canSendNext = false;
                } else {
                    //The colors are still null, the system is still waiting for the screen capture system to produce data.
                    //The sleep saves cpu cycles!
                    Thread.sleep(5);
                }
            }

            long endTime = new Date().getTime();
            long difference = endTime - startTime;
            LOGGER.getInstance().INFO("COMM => serial comm step (" + currentStep + "/" + steps + ") completed in : " + difference + "ms");

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
            if(port != null) {
                port.closePort();
                port = null;
            } else {
                logger.INFO("COMM => No port to close!");
            }
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