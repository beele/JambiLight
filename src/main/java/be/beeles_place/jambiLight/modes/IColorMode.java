package be.beeles_place.jambiLight.modes;

public interface IColorMode {

    /**
     * This method will be called multiple times and should execute a single unit of work.
     *
     * @return If this task completed successfully (and may or should be repeated) return true.
     *         Otherwise return false!
     */
    public boolean start();

    /**
     * This method will stop the mode after the current execution of the start method!
     */
    public void stop();
}
