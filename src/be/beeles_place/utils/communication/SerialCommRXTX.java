package be.beeles_place.utils.communication;

import be.beeles_place.model.ColorModel;
import be.beeles_place.utils.logger.LOGGER;
import gnu.io.*;

import java.io.*;

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

    boolean canSendNext = true;
    int currentStep = 0;

    @Override
    public void run() {
        isRunning = true;

        if (initCommPort() != 0) {
            LOGGER.getInstance().ERROR("Cannot start serial communication!");
            isRunning = false;
            return;
        }

        while (isRunning) {
            try {
                Thread.sleep(1);

                if(input.available() > 0) {
                    if(input.read() == 50) {
                        canSendNext = true;
                        //System.out.println("Send next 48 bytes!");
                    }
                } else {
                    //System.out.println("Cannot send next 48 bytes");
                }

                if(canSendNext && model.getCurrentColors() != null) {
                    int[][] colors = model.getCurrentColors();
                    int totalBytes = colors.length * 3;
                    int steps = (int)totalBytes / 48;

                    if(currentStep >= steps) {
                        currentStep = 0;
                    }

                    if(colors != null) {
                        for(int i = 0 ; i < 16 ; i++) {
                            for(int j = 0 ; j < 3 ; j++) {
                                output.write((byte)colors[(currentStep * 16) + i][j]);
                            }
                            /*output.write((byte)255);
                            output.write((byte)181);
                            output.write((byte)135);*/
                        }
                        //System.out.println("48 bytes sent! (Step " + (currentStep + 1) + " out of " + steps + ")");
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
}
