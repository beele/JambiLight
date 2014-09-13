package be.beeles_place.jambiLight.controllers;

import be.beeles_place.jambiLight.model.ColorModel;
import be.beeles_place.jambiLight.model.SettingsModel;
import be.beeles_place.jambiLight.communication.AbstractSerialCommStrategy;
import be.beeles_place.jambiLight.communication.CommunicationStrategy;
import be.beeles_place.jambiLight.utils.logger.LOGGER;

import java.util.List;

public class CommunicationController {

    private LOGGER logger;

    private Thread commThread;

    private AbstractSerialCommStrategy comm;
    private ColorModel model;
    private SettingsModel settings;

    /**
     * Creates a CommunicationController instance.
     *
     * @param model The object containing the settings.
     * @param settings The object containing the model.
     */
    public CommunicationController(ColorModel model, SettingsModel settings) {
        logger = LOGGER.getInstance();

        this.model = model;
        this.settings = settings;
    }

    /**
     * Initializes the communication controller logic.
     * Will automatically open the communications on the port in the settings, if isAutoConnect is true in the settings
     * and the port is not null.
     *
     * @param strategy The CommunicationStrategies enumerated name to use. The given strategy will be used
     *                 to open the communication.
     */
    public void init(CommunicationStrategy strategy) {
        if(strategy == null) {
            strategy = CommunicationStrategy.JSSC;
        } else {
            comm = (AbstractSerialCommStrategy) strategy.getCommStrategy();
        }
        settings.setPorts(getPorts());

        //Only open the port is we can and may do so.
        if(settings.isAutoConnect()){
            open();
        }
    }

    /**
     * Opens the serial communication on the port in the settings model.
     * Uses the strategy given with the init() method.
     */
    public void open() {
        if(comm != null && settings.getPort() != null) {
            comm.setUpCommPort(model, settings.getPort());
            commThread = new Thread(comm);
            commThread.setPriority(Thread.MAX_PRIORITY);
            commThread.setName(comm.getClass().getName());
            commThread.start();
        } else {
            logger.ERROR("COMM => Cannot open comm when no instance has been made or no port has been given!");
        }
    }

    /**
     * Closes the communication that is currently open.
     */
    public void close() {
        if(comm == null) {
            logger.ERROR("COMM => Cannot close a non existing comm!");
        } else {
            comm.stop();
        }
    }

    //Getters & setters.
    /**
     * Returns a list containing all the ports the given CommunicationStrategy knows.
     *
     * @return A List containing the names of all the ports known.
     */
    public List<String> getPorts() {
        if(comm == null) {
            logger.ERROR("COMM => Cannot enumerate ports when comm is not initialized!");
            return null;
        } else {
            return comm.getSerialDevicesList();
        }
    }
}