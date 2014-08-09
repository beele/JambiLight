package be.beeles_place.jambiLight.modes;

public interface IColorMode {

    /**
     * This method should be called initially.
     */
    public void init();

    /**
     * This method will be called multiple times and should execute a single unit of work.
     */
    public void start();

    /**
     * This method will stop the mode after the current execution of the start method!
     */
    public void stop();
}
