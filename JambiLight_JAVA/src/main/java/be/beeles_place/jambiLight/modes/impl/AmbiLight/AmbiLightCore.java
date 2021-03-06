package be.beeles_place.jambiLight.modes.impl.AmbiLight;

import be.beeles_place.jambiLight.model.ColorModel;
import be.beeles_place.jambiLight.model.SettingsModel;
import be.beeles_place.jambiLight.utils.colorTools.ColorEnhancer;
import be.beeles_place.jambiLight.utils.colorTools.IntensityCorrector;
import be.beeles_place.jambiLight.utils.colorTools.RegionConsolidator;
import be.beeles_place.jambiLight.utils.logger.LOGGER;
import be.beeles_place.jambiLight.utils.screenCapture.IScreenCapper;

import java.awt.*;
import java.util.Date;

public class AmbiLightCore {

    //Settings params
    private int stepSize;
    private int horizontalRegionSize;
    private int verticalRegionSize;
    private boolean enhanceColors;
    private boolean doCorrection;

    //Internal variables
    private LOGGER logger;
    private IScreenCapper capper;
    private RegionConsolidator consolidator;
    private ColorEnhancer enhancer;
    private IntensityCorrector corrector;
    private ColorModel model;

    private int width;
    private int height;

    private int[][][] regions;
    private float regionWidth;
    private float regionHeight;

    private int x, y;
    private int regionX, regionY;

    /**
     * Creates a new AmbiLightCore instance.
     *
     * @param settings   The SettingsModel instance.
     * @param colorModel The ColorModel instance.
     * @param screenCapper The IScreenCapper instance that will facilitate the capture of input data.
     */
    public AmbiLightCore(SettingsModel settings, ColorModel colorModel, IScreenCapper screenCapper) {
        model = colorModel;
        capper = screenCapper;

        //Settings.
        stepSize = settings.getPixelIteratorStepSize();
        enhanceColors = settings.isEnhanceColor();
        doCorrection = settings.isCorrectIntensity();

        horizontalRegionSize = settings.getHorizontalRegions();
        verticalRegionSize = settings.getVerticalRegions();

        //Update size of regions
        checkDimensionsAndRegionSize();

        //Create the required instances.
        enhancer = new ColorEnhancer(settings);
        corrector = new IntensityCorrector(settings);
        consolidator = new RegionConsolidator(settings);

        //Get the logger instance only once.
        logger = LOGGER.getInstance();
        logger.INFO("==========================================================================");
        logger.INFO("==========================================================================");
        logger.INFO("AMBILIGHT-CORE => Init complete!");
        logger.INFO("AMBILIGHT-CORE => There are " + (width * height) + " pixels in " + horizontalRegionSize * verticalRegionSize + " regions.");
        logger.INFO("AMBILIGHT-CORE => There will be " + colorModel.getNumberOfConsolidatedRegions() + " consolidated regions.");
        logger.INFO("==========================================================================");
        logger.INFO("==========================================================================");
    }

    /**
     * Will capture the screen and split it up into the predefined region count.
     * Afterwards it will consolidate the regions into the (to be mapped) pixel regions.
     * (Each consolidated region will be mapped to a single LED.)
     */
    public void calculate() {
        long startTime = new Date().getTime();

        //Make a screen capture.
        //Disabling aero themes in windows can easily double or triple performance!
        int[] pixels = capper.capture();
        checkDimensionsAndRegionSize();
        model.setRawImageData(pixels.clone());

        /*for (int i = 0; i < pixels.length; i += stepSize) {
            //The pixels in the image are in one long array, we need to get the x and y values of the pixel.
            y = (i / width);        //This is the row the pixel is at.
            x = i - (y * width);    //This is the column the pixel is at.

            //Calculate the correct region for the given x and y coordinate.
            regionX = (int) (x / regionWidth);
            regionY = (int) (y / regionHeight);

            tempPixelValue = pixels[i];
            //Add the R/G/B to the current region.
            int[] colors = regions[regionX][regionY];
            colors[0] += (tempPixelValue >>> 16) & 0xFF;
            colors[1] += (tempPixelValue >>> 8) & 0xFF;
            colors[2] += tempPixelValue & 0xFF;
            colors[3] += 1;
        }*/

        int redactedLength = pixels.length / 4;
        int offset = redactedLength - 1;
        for (int i = 0; i < redactedLength; i += stepSize) {
            pixelCalc(regions, i, pixels[i]);
            pixelCalc(regions, i + offset, pixels[i + offset]);
            pixelCalc(regions, i + offset * 2, pixels[i + offset * 2]);
            pixelCalc(regions, i + offset * 3, pixels[i + offset * 3]);
        }

        //Go over all the regions and calculate the average color.
        for (int m = 0; m < verticalRegionSize; m++) {
            for (int n = 0; n < horizontalRegionSize; n++) {
                //Get the average color by dividing the total added color int by the number of pixels!
                int[] colors = regions[n][m];
                int numOfPixelsCounted = colors[3];
                colors[0] /= numOfPixelsCounted;
                colors[1] /= numOfPixelsCounted;
                colors[2] /= numOfPixelsCounted;
                colors[3] = 0;

                //Only process regions that are not ignored or completely black!
                if (enhanceColors && (colors[0] != 0 || colors[1] != 0 || colors[2] != 0)) {
                    regions[n][m] = enhancer.processColor(colors[0], colors[1], colors[2]);
                }
            }
        }

        //Set the consolidated regions with colors on the model.
        int[][] cRegions = consolidator.consolidateRegions(regions);
        //Correct the intensity if enabled.
        if(doCorrection) {
            cRegions = corrector.correctIntensity(cRegions);
        }
        model.setCurrentColors(cRegions);

        //It's all about tai-ming (not the vases)
        long endTime = new Date().getTime();
        model.setActionDuration(endTime - startTime);
        LOGGER.getInstance().INFO("AMBILIGHT-CORE => Pixel processing completed in : " + model.getActionDuration() + "ms");

        //Everything has been updated!
        model.publishModelUpdate();
        
        cRegions = null;
        pixels = null;
    }

    private void pixelCalc(int[][][] regions, int i, int pixel) {
        //The pixels in the image are in one long array, we need to get the x and y values of the pixel.
        y = (i / width);        //This is the row the pixel is at.
        x = i - (y * width);    //This is the column the pixel is at.

        //Calculate the correct region for the given x and y coordinate.
        regionX = (int) (x / regionWidth);
        regionY = (int) (y / regionHeight);

        //Add the R/G/B to the current region.
        int[] colors = regions[regionX][regionY];
        colors[0] += (pixel >>> 16) & 0xFF;
        colors[1] += (pixel >>> 8) & 0xFF;
        colors[2] += pixel & 0xFF;
        colors[3] += 1;
    }

    /**
     * This method checks if the dimensions of the captured area have changed, if so the required changes are made.
     * In principle this will allow each captured frame to have different dimensions and still be used for the core logic.
     */
    private void checkDimensionsAndRegionSize() {
        //Fix for null pointer exception that could occur when restarting the app (after settings have changed).
        if(capper == null) {
            return;
        }

        Dimension size = capper.getScreenDimensions();

        if(width != size.getWidth() || height != size.getHeight()) {
            //Get the screen size and calculate the number of pixels required!
            width = (int) size.getWidth();
            height = (int) size.getHeight();
            model.setRawWidth(width);
            model.setRawHeight(height);

            //There are (n x m) regions, made by the n and m dimensions. The last dimension of 3 is to store r/g/b/#pixels separately.
            regions = new int[this.horizontalRegionSize][this.verticalRegionSize][4];
            regionWidth = (float) width / this.horizontalRegionSize;
            regionHeight = (float) height / this.verticalRegionSize;
        }
    }

    /**
     * Call this method when stopping the AmbiLightMode!
     */
    public void dispose() {
        if(capper != null) {
            capper.dispose();
            capper = null;
        }
    }
}