package be.beeles_place.jambiLight;

import be.beeles_place.jambiLight.model.SettingsModel;
import be.beeles_place.jambiLight.utils.colorTools.RegionConsolidator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.fail;

public class RegionConsolidatorTest {

    private int width;
    private int height;

    @Before
    public void init() {
        //Number of regions!
        width = 16;
        height = 9;
    }

    @Test
    public void testConsolidation() {
        //Without margins
        System.out.println("=> Consolidator, no margins");
        RegionConsolidator rc = new RegionConsolidator(generateSettings(width,height,0,0,true,2));
        testColors(rc);

        //With horizontal margin
        System.out.println("=> Consolidator, horizontal margin");
        rc = new RegionConsolidator(generateSettings(width,height,2,0,true,2));
        testColors(rc);

        //With vertical margin
        System.out.println("=> Consolidator, vertical margin");
        rc = new RegionConsolidator(generateSettings(width,height,0,2,true,2));
        testColors(rc);

        //With both margins
        System.out.println("=> Consolidator, both margins");
        rc = new RegionConsolidator(generateSettings(width,height,2,2,true,2));
        testColors(rc);
    }

    private void testColors(RegionConsolidator rc) {
        //Full white.
        System.out.println("==> White");
        consolidate(rc, generateFullRegions(255, 255, 255), 255, 255, 255);

        //Full red.
        System.out.println("==> Red");
        consolidate(rc, generateFullRegions(255,0,0),255,0,0);

        //Full green.
        System.out.println("==> Green");
        consolidate(rc, generateFullRegions(0,255,0),0,255,0);

        //Full blue.
        System.out.println("==> Blue");
        consolidate(rc, generateFullRegions(0,0,255),0,0,255);

        //Full cyan.
        System.out.println("==> Cyan");
        consolidate(rc, generateFullRegions(0,255,255),0,255,255);

        //Full purple.
        System.out.println("==> Purple");
        consolidate(rc, generateFullRegions(255,0,255),255,0,255);

        //Full yellow.
        System.out.println("==> Yellow");
        consolidate(rc, generateFullRegions(255,255,0),255,255,0);

        System.out.println("------------------------------------------------");
    }

    private SettingsModel generateSettings(int regionsX, int regionsY, int marginX, int marginY, boolean doWeighing, int weighFactor) {
        SettingsModel settings = new SettingsModel();
        settings.setHorizontalRegions(regionsX);
        settings.setVerticalRegions(regionsY);
        settings.setHorizontalMargin(marginX);
        settings.setVerticalMargin(marginY);
        settings.setWeighColor(doWeighing);
        settings.setWeighFactor(weighFactor);
        return settings;
    }

    private int[][][] generateFullRegions(int r, int g, int b) {
        int[][][] regions = new int[width][height][3];
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++){
                regions[i][j][0] = r;
                regions[i][j][1] = g;
                regions[i][j][2] = b;
            }
        }
        return regions;
    }

    private void consolidate(RegionConsolidator rc, int[][][] fullRegions, int r, int g, int b) {
        int[][] consolidatedRegions = rc.consolidateRegions(fullRegions);
        mainLoop:for(int m = 0; m < consolidatedRegions.length ; m++) {
            for(int n = 0; n < consolidatedRegions[m].length; n++) {
                //Red
                int color = consolidatedRegions[m][0];
                if(color > 255) {
                    fail("Fault at: [" + m + "][r]" + consolidatedRegions[m][n] + " value was too large (>255)");
                } else if(color < 0) {
                    fail("Fault at: [" + m + "][r]" + consolidatedRegions[m][n] + " value was too small (<0)");
                } else if(color != r) {
                    fail("Red color was different from expected value : " + r + ", was: " + color);
                }

                //Green
                color = consolidatedRegions[m][1];
                if(color > 255) {
                    fail("Fault at: [" + m + "][g]" + consolidatedRegions[m][n] + " value was too large (>255)");
                } else if(color < 0) {
                    fail("Fault at: [" + m + "][g]" + consolidatedRegions[m][n] + " value was too small (<0)");
                } else if(color != g) {
                    fail("Green color was different from expected value : " + g + ", was: " + color);
                }

                //Blue
                color = consolidatedRegions[m][2];
                if(color > 255) {
                    fail("Fault at: [" + m + "][b]" + consolidatedRegions[m][n] + " value was too large (>255)");
                } else if(color < 0) {
                    fail("Fault at: [" + m + "][b]" + consolidatedRegions[m][n] + " value was too small (<0)");
                } else if(color != b) {
                    fail("Blue color was different from expected value : " + b + ", was: " + color);
                }
            }
        }
    }
}
