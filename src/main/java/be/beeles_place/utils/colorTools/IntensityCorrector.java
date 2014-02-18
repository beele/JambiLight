package be.beeles_place.utils.colorTools;

public class IntensityCorrector {

    public int[][] correctIntensity(int[][] regions) {

        //Loop over each region.
        for (int[] region : regions) {
            int r = region[0];
            int g = region[1];
            int b = region[2];

            //TODO: tweak and improve color detection!

            //Dominant red
            if (r > (b + 10) && r > (g + 10)) {
                g /= 2;
                b /= 2;
            }
            //Dominant green
            else if (g > (r + 10) && g > (b + 10)) {
                r /= 2;
                b /= 2;
            }
            //Dominant blue
            else if (b > (r + 10) && b > (g + 10)) {
                r /= 2;
                g /= 2;
            }
            //Dominant yellow
            else if ((r + g) > ((b * 2) + 20)) {
                b /= 4;
            }
            //Dominant cyan
            else if ((g + b) > ((r * 2) + 20)) {
                r /= 4;
            }
            //Dominant purple
            else if ((r + b) > ((g * 2) + 20)) {
                g /= 4;
            }
            //Gray, white or black.
            else {
                r /= 2;
                g /= 2;
                b /= 2;
            }

            region[0] = r;
            region[1] = g;
            region[2] = b;
        }

        return regions;
    }
}
