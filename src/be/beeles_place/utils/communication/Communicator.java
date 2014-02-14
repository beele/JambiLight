package be.beeles_place.utils.communication;

import be.beeles_place.model.ColorModel;

import java.util.List;

public class Communicator {

    Thread runner;

    ASerialComm comm;
    ColorModel model;

    public Communicator(ColorModel model) {
        this.model = model;

        comm = new SerialCommRXTX();
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
        runner.interrupt();
        runner = null;
    }

    public void updateColor(){
        comm.setColor(model.getCurrentColor());
    }
}
