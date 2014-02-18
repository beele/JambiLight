package be.beeles_place.utils.colorTools;

public class IntensityCorrector {

    public IntensityCorrector() {

    }

    public void correctIntensity() {

        int r = 0;
        int g = 0;
        int b = 0;

        //Dominant red
        if (r > (b + 10) && r > (g + 10)) {

        }
        //Dominant green
        else if (g > (r + 10) && g > (b + 10)) {

        }
        //Dominant blue
        else if (b > (r + 10) && b > (g + 10)) {

        }
        //Dominant yellow
        else if ((r + g) > ((b * 2) + 20)) {

        }
        //Dominant cyan
        else if ((g + b) > ((r * 2) + 20)) {

        }
        //Dominant purple
        else if ((r + b) > ((g * 2) + 20)) {

        }
        //Gray, white or black.
        else {

        }

        //For safety, values that were amplified too much will main.java.be toned down again!
        r = r < 256 ? r : 255;
        g = g < 256 ? g : 255;
        b = b < 256 ? b : 255;
    }
}
