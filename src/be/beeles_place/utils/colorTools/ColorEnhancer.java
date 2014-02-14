package be.beeles_place.utils.colorTools;

public class ColorEnhancer {

    /**
     * Enhances the color by checking what type of color it is (8 base types).
     * The detected color range will then be amplified.
     *
     * @param r The red color value from 0 to 255.
     * @param g The green color value from 0 to 255.
     * @param b The blue color value from 0 to 255.
     * @return An array of int containing 4 dimensions r/g/b/unused.
     */
    public int[] processColor(int r, int g, int b) {
        //Values of 0 are not optimal, and could crash the app (div by zero!)
        r = r != 0 ? r : 1;
        g = g != 0 ? g : 1;
        b = b != 0 ? b : 1;

        float ratioR = r / 256.0f;
        float ratioG = g / 256.0f;
        float ratioB = b / 256.0f;
        float ratioTotal = ratioR + ratioG + ratioB;

        ratioR /= ratioTotal;
        ratioG /= ratioTotal;
        ratioB /= ratioTotal;

        //Dominant red
        if (r > (b + 10) && r > (g + 10)) {
            r += (int) (r * ratioR * 2.5);
        }
        //Dominant green
        else if (g > (r + 10) && g > (b + 10)) {
            g += (int) (g * ratioG * 2.5);
        }
        //Dominant blue
        else if (b > (r + 10) && b > (g + 10)) {
            b += (int) (b * ratioB * 2.5);
        }
        //Dominant yellow
        else if ((r + g) > ((b * 2) + 20)) {
            r += (int) (r * ratioR * 2.5);
            g += (int) (g * ratioG * 2.5);
        }
        //Dominant turquiose
        else if ((g + b) > ((r * 2) + 20)) {
            g += (int) (g * ratioG * 2.5);
            b += (int) (b * ratioB * 2.5);
        }
        //Dominant purple
        else if ((r + b) > ((g * 2) + 20)) {
            r += (int) (r * ratioR * 2.5);
            b += (int) (b * ratioB * 2.5);
        } else {
            r += (int) (r * ratioR * 2.5);
            g += (int) (g * ratioG * 2.5);
            b += (int) (b * ratioB * 2.5);
        }

        //For safety, values that were amplified too much will be toned down again!
        r = r < 256 ? r : 255;
        g = g < 256 ? g : 255;
        b = b < 256 ? b : 255;

        return new int[]{r, g, b, 0};
    }
}
