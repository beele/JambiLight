package be.beeles_place.jambiLight.modes;

public abstract class AbstractColorMode implements IColorMode, Runnable {

    private boolean runNext = false;
    private boolean forceQuit = false;

    @Override
    public void run() {
        runNext = true;
        while (runNext && !forceQuit) {
            runNext = start();
        }
    }

    @Override
    public abstract boolean start();

    @Override
    public void stop() {
        forceQuit = true;
    }
}