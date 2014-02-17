package be.beeles_place.utils.communication;

import be.beeles_place.model.ColorModel;
import be.beeles_place.utils.communication.impl.SerialCommJSSC;
import be.beeles_place.utils.communication.impl.SerialCommMock;
import be.beeles_place.utils.communication.impl.SerialCommRXTX;

import java.util.Arrays;
import java.util.List;

public class Communicator {

    private Thread runner;

    private ASerialComm comm;
    private ColorModel model;

    public Communicator(ColorModel model, CommunicationLibraries type) {
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
        comm.setPortName(portName);
        runner = new Thread(comm);
        runner.start();
    }

    public void close() {
        if(runner != null) {
            runner.interrupt();
            runner = null;
        }
    }
}
