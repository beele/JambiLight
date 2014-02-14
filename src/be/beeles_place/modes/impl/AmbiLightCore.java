package be.beeles_place.modes.impl;

import be.beeles_place.model.ColorModel;
import be.beeles_place.model.SettingsModel;
import be.beeles_place.utils.colorTools.ColorEnhancer;
import be.beeles_place.utils.ScreenCapper;
import be.beeles_place.utils.colorTools.RegionConsolitdator;
import be.beeles_place.utils.logger.LOGGER;

import java.awt.*;
import java.util.Date;

public class AmbiLightCore {

    //Settins params
    private int stepSize;
    private int margin;
    private int horizontalRegionSize;
    private int verticalRegionSize;
    private boolean enhanceColors;

    //Internal variables
    private LOGGER logger;
    private ScreenCapper capper;
    private ColorEnhancer enhancer;
    private RegionConsolitdator consolitdator;
    private ColorModel model;

    private final int width;
    private final int height;
    private int[] pixels;
    private int tempPixelValue;

    private int[][][] regions;
    private float regionWidth;
    private float regionHeight;

    private int x, y;
    private int regionX, regionY;

    public AmbiLightCore(SettingsModel settings, ColorModel colorModel) {
        model = colorModel;
        //Settings.
        margin = settings.getRegionMargin();
        stepSize = settings.getPixelIteratorStepSize();
        enhanceColors = settings.isEnhanceColor();

        //Create the required instances.
        capper = new ScreenCapper();
        enhancer = new ColorEnhancer();

        //Get the screen size and calculate the number of pixels required!
        Dimension size = capper.getScreenDimensions();
        width = (int) size.getWidth();
        height = (int) size.getHeight();
        pixels = new int[(width * height)];

        //Initialize the regions.
        horizontalRegionSize = settings.getHorizontalRegions();
        verticalRegionSize = settings.getVerticalRegions();
        //There are n*m regions, made by the n x m dimensions. The last dimension of 3 is to store r/g/b/#pixels separately.
        regions = new int[this.horizontalRegionSize][this.verticalRegionSize][4];
        regionWidth = (float)width / this.horizontalRegionSize;
        regionHeight = (float)height / this.verticalRegionSize;

        consolitdator = new RegionConsolitdator(this.horizontalRegionSize, this.verticalRegionSize);

        //Get the logger innstance only once.
        logger = LOGGER.getInstance();
        logger.INFO("Init done! There are " + pixels.length + " pixels in " + horizontalRegionSize * verticalRegionSize + " regions");
    }

    public void calculate() {
        long startTime = new Date().getTime();

        //Make a screen capture.
        //Disabling aero themes in windows can easily double or triple performance!
        pixels = capper.capture();

        for(int i = 0 ; i < pixels.length ; i += stepSize) {
            //The pixels in the image are in one long array, we need to get the x and y values of the pixel.
            y = (i / width);        //This is the row the pixel is at.
            x = i - (y * width);    //This is the column the pixel is at.

            //Calculate the correct region for the given x and y coordinate.
            regionX = (int)(x / regionWidth);
            regionY = (int)(y / regionHeight);

            tempPixelValue = pixels[i];
            int[] colors = regions[regionX][regionY];
            colors[0] += (tempPixelValue >>> 16) & 0xFF;
            colors[1] += (tempPixelValue >>> 8) & 0xFF;
            colors[2] += tempPixelValue & 0xFF;
            colors[3] += 1;
        }

        //Go over all the regions and calculate the average color.
        for(int m = 0 ; m < verticalRegionSize ; m++) {
            for(int n = 0; n < horizontalRegionSize ; n++) {
                //Get the average color by dividing the total added color int by the number of pixels!
                int[] colors = regions[n][m];
                int numOfPixelsCounted = colors[3];
                colors[0] /= numOfPixelsCounted;
                colors[1] /= numOfPixelsCounted;
                colors[2] /= numOfPixelsCounted;
                colors[3] = 0;

                //Only process regions that are not ignored or completely black!
                if(colors[0] != 0 || colors[1] != 0 || colors[2] != 0){
                    if(enhanceColors) {
                        regions[n][m] = enhancer.processColor(colors[0], colors[1], colors[2]);
                    } else {
                        //Safety check for color channel values.
                        colors[0] = colors[0] < 256 ? colors[0] : 255;
                        colors[1] = colors[1] < 256 ? colors[1] : 255;
                        colors[2] = colors[2] < 256 ? colors[2] : 255;
                    }
                }
            }
        }

        //It's all about tai-ming (not the vases)
        long endTime = new Date().getTime();
        long difference = endTime - startTime;

        //Set the consolidated regions with colors on the model.
        model.setActionDuration(difference);
        model.setCurrentColors(consolitdator.consolidateRegions(regions));
    }
}