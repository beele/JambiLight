package be.beeles_place.utils.communication;

import be.beeles_place.model.ColorModel;

import java.util.List;

public class Communicator {

    Thread runner;

    ASerialComm comm;
    ColorModel model;

    public Communicator(ColorModel model, boolean useMock) {
        this.model = model;

        if (useMock) {
            comm = new SerialCommMock();
        } else {
            comm = new SerialCommRXTX();
        }
    }

    public List<String> getPorts() {
        return SerialUtil.getSerialDevicesList();
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

    public void updateColor() {
        comm.setColor(model.getCurrentColor());
    }
}
