package be.beeles_place.utils.communication;

import be.beeles_place.utils.logger.LOGGER;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;

public class SerialCommRXTX extends ASerialComm {

    private boolean isRunning;
    private String portName;

    private CommPortIdentifier portId;
    private SerialPort port;
    private OutputStream output;

    private Color color;
    private boolean updateRequired;

    public SerialCommRXTX() {
        LOGGER.getInstance().INFO("initiating serial communication service using RXTX lib");
    }

    public void setColor(Color color) {
        if(this.color == null) {
            this.color = color;
        }

        if(color.getRGB() != this.color.getRGB()) {
            this.color = color;
            updateRequired = true;
        } else {
            updateRequired = false;
        }
    }

    public int initCommPort() {
        int status = 0;

        try{
            port = (SerialPort)portId.open("serial talk", 4000);
            output = port.getOutputStream();
            port.setSerialPortParams(   9600,
                                        SerialPort.DATABITS_8,
                                        SerialPort.STOPBITS_1,
                                        SerialPort.PARITY_NONE);
        } catch (PortInUseException e) {
            LOGGER.getInstance().ERROR("Serial comm port is already in use!");
            status = -1;
        } catch (UnsupportedCommOperationException e) {
            LOGGER.getInstance().ERROR("Unsupported operation on comm port!");
            status = -1;
        } catch (IOException e) {
            LOGGER.getInstance().ERROR("General IO comm exception!");
            status = -1;
        } catch (Exception e){
            LOGGER.getInstance().ERROR("An unexpected error occured!\n" + e.getLocalizedMessage());
            status = -1;
        }

        return status;
    }

    public void disposeCommPort(){
        LOGGER.getInstance().INFO("Terminating and cleaning up serial communication!");
        port.close();
        output = null;
        port = null;
    }

    @Override
    public void run() {
        isRunning = true;

        if(initCommPort() != 0) {
            LOGGER.getInstance().ERROR("Cannot start serial communication!");
            isRunning = false;
            return;
        }

        while(isRunning) {
            try {
                Thread.sleep(10);
                if(updateRequired) {
                    //oxee to save the next color, oxff to just display it!
                    LOGGER.getInstance().DEBUG("sending new color over comm! " + color.toString());
                    output.write(0xff);

                    output.write(color.getRed());
                    output.write(color.getGreen());
                    output.write(color.getBlue());
                    updateRequired = false;
                }
            } catch (InterruptedException e) {
                LOGGER.getInstance().ERROR("Thread interrupted! Aborting thread!");
                disposeCommPort();
                isRunning = false;
            }  catch (IOException e) {
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
        updateRequired = true;
        color = new Color(255,255,255);

        try {
            portId = CommPortIdentifier.getPortIdentifier(portName);
            //portId = CommPortIdentifier.getPortIdentifier(SerialUtil.getArduinoSerialDeviceName());
            //portId = CommPortIdentifier.getPortIdentifier("/dev/tty.usbmodem1421");

        } catch(gnu.io.NoSuchPortException nsp)  {
            LOGGER.getInstance().ERROR(nsp.getMessage());
        }
        catch(Exception exe) {
            LOGGER.getInstance().ERROR("Unexpected error occured \n" + exe.getMessage());
        }
    }
}
