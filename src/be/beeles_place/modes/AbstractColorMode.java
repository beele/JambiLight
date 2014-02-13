package be.beeles_place.modes;

public abstract class AbstractColorMode implements IColorMode, Runnable {

    private boolean runNext = false;
    private boolean forceQuit = false;

    @Override
    public void run() {
        runNext = true;
        if(runNext == true && forceQuit == false) {
            runNext = start();
            run();
        }
    }

    @Override
    public abstract boolean start();

    @Override
    public void stop() {
        forceQuit = true;
    }
}