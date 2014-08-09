package be.beeles_place.jambiLight.controllers;

import be.beeles_place.jambiLight.model.ColorModel;
import be.beeles_place.jambiLight.utils.communication.ASerialComm;
import be.beeles_place.jambiLight.utils.communication.CommunicationLibraries;
import be.beeles_place.jambiLight.utils.communication.impl.SerialCommJSSC;
import be.beeles_place.jambiLight.utils.communication.impl.SerialCommMock;
import be.beeles_place.jambiLight.utils.communication.impl.SerialCommRXTX;
import be.beeles_place.jambiLight.utils.logger.LOGGER;

import java.util.List;

public class CommunicatorController {

    private LOGGER logger;

    private Thread commThread;

    private ASerialComm comm;
    private ColorModel model;

    public CommunicatorController(ColorModel model, CommunicationLibraries type) {
        logger = LOGGER.getInstance();
        this.model = model;

        switch (type) {
            case MOCK:
                comm = new SerialCommMock();
                break;
            case JSSC:
                comm = new SerialCommJSSC(model);
                break;
            case RXTX:
                comm = new SerialCommRXTX(model);
                break;
        }
    }

    public List<String> getPorts() {
        return comm.getSerialDevicesList();
    }

    public void open(String portName) {
        if(comm != null) {
            comm.setUpCommPort(portName);
            commThread = new Thread(comm);
            commThread.setPriority(Thread.MAX_PRIORITY);
            commThread.setName(comm.getClass().getName());
            commThread.start();
        } else {
            logger.ERROR("COMM => Cannot open comm when no instance has been made!");
        }
    }

    public void close() {
        if(comm == null) {
            logger.ERROR("COMM => Cannot close a non existing comm!");
        } else {
            comm.stop();
        }
    }
}