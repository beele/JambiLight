package be.beeles_place.jambiLight.utils.colorTools;

public class RegionConsolidator {

    private int width;
    private int height;
    private int finalRegionCount;

    private int leftDepth;
    private int rightDepth;

    private int topDepth;
    private int bottomDepth;

    private int horizontalMargin;
    private int verticalMargin;

    private int[][] cRegions;

    private int r, g, b;
    private int rr, gg, bb;

    private boolean weighColors;
    private int weighFactor = 2;
    private int weight = 1;
    private int totalWeight = 1;
    private int tempWeight;

    /**
     * Creates a new RegionConsolidator instance.
     * @param horizontalRegions The number of horizontal regions.
     * @param verticalRegions The number of vertical regions.
     * @param horizontalMargin An int representing the horizontal margin.
     * @param verticalMargin An int representing the vertical margin.
     * @param weighColors The SettingsModel containing the application settings.
     * @param weighFactor The factor which the weight is calculated with. A higher number will result in bigger weight steps.
     */
    public RegionConsolidator(int horizontalRegions, int verticalRegions, int horizontalMargin, int verticalMargin, boolean weighColors, int weighFactor) {
        //The number of horizontal and vertical regions so the final amount of consolidated regions can be calculated.
        width = horizontalRegions;
        height = verticalRegions;

        //Calculate the number of consolidated regions.
        //The minus 4 is because the sides are 2 shorter than the top and bottom (they overlap the sides).
        finalRegionCount = (width * 2) + (height * 2) - 4;

        //Four quadrants are needed.
        //Left and right.
        leftDepth = (int) (width / 2);
        rightDepth = width - leftDepth;
        //Top and bottom.
        topDepth = (int) (height / 2);
        bottomDepth = height - topDepth;

        //The margins (from 0 to n) are the regions at the outside that will be ignored.
        this.horizontalMargin = horizontalMargin;
        this.verticalMargin = verticalMargin;
        //Settings value that determines if the regions should we weighed when consolidating.
        this.weighColors = weighColors;
        this.weighFactor = weighFactor;
    }

    /**
     * Will consolidate all regions in to the wanted amount of pixels.
     *
     * @param regions The multidimensional regions array containing all calculated regions.
     * @return An array of int where each side of the screen is appended in the array. starting Top (vertical), Right(side), Bottom(vertical) and Left(side).
     * The second dimension contains the R/G/B values.
     */
    public int[][] consolidateRegions(int[][][] regions) {
        cRegions = new int[finalRegionCount][3];

        //Collect all regions per column into one pixel.
        for (int i = 0; i < width; i++) {
            //Reset color value variables.
            r = g = b = rr = gg = bb = 0;
            //Loop from column 0 + margin to half of the columns for the left side. (start column + margin <===> center column)
            for (int j = horizontalMargin; j < topDepth; j++) {
                weight = getWeight(j - horizontalMargin);
                r += (regions[i][j][0] * weight);
                g += (regions[i][j][1] * weight);
                b += (regions[i][j][2] * weight);
            }
            //Calculate the average color values. (total added color values / calculated total weight)
            totalWeight = getTotalWeight(topDepth - horizontalMargin);
            r /= totalWeight;
            g /= totalWeight;
            b /= totalWeight;

            //Loop from the center column to the right end column - margin. (center column <===> end column - margin)
            for (int j = topDepth; j < (height - horizontalMargin); j++) {
                weight = getWeight((height - horizontalMargin) - j - 1);
                rr += (regions[i][j][0] * weight);
                gg += (regions[i][j][1] * weight);
                bb += (regions[i][j][2] * weight);
            }
            //Calculate the average color values. (total added color values / calculated total weight)
            totalWeight = getTotalWeight(bottomDepth - horizontalMargin);
            rr /= totalWeight;
            gg /= totalWeight;
            bb /= totalWeight;

            //Top consolidated region.
            cRegions[i] = new int[]{r, g, b};
            //Bottom consolidated region.
            cRegions[width + height - 2 + (width - i - 1)] = new int[]{rr, gg, bb};
        }

        //Collect all regions per row into one pixel.
        for (int m = 0; m < height; m++) {
            //Reset color value variables.
            r = g = b = rr = gg = bb = 0;
            //Loop from row 0 + margin to the half of the rows for the top side. (start row + margin <===> center row)
            for (int n = verticalMargin; n < leftDepth; n++) {
                weight = getWeight(n - verticalMargin);
                r += (regions[n][m][0] * weight);
                g += (regions[n][m][1] * weight);
                b += (regions[n][m][2] * weight);
            }
            //Calculate the average color values. (total added color values / calculated total weight)
            totalWeight = getTotalWeight(leftDepth - verticalMargin);
            r /= totalWeight;
            g /= totalWeight;
            b /= totalWeight;

            //Loop from the center row to the bottom end row - margin. (center row <===> end row - margin)
            for (int n = leftDepth; n < (width - verticalMargin); n++) {
                weight = getWeight((width - verticalMargin) - n - 1);
                rr += (regions[n][m][0] * weight);
                gg += (regions[n][m][1] * weight);
                bb += (regions[n][m][2] * weight);
            }
            //Calculate the average color values. (total added color values / calculated total weight)
            totalWeight = getTotalWeight(rightDepth - verticalMargin);
            rr /= totalWeight;
            gg /= totalWeight;
            bb /= totalWeight;

            //Left consolidated region.
            int tempIndex = width + (height - 2) + width + (height - 2) - m;
            tempIndex = tempIndex == finalRegionCount ? 0 : tempIndex;
            cRegions[tempIndex] = new int[]{r, g, b};
            //Right consolidated region.
            cRegions[m + width - 1] = new int[]{rr, gg, bb};
        }

        //TODO: this does not work correctly with margins enabled!
        //Recalculate the corner regions. The loop logic above have some strange behaviour for the corners.
        int[] c1, c2;
        //TOP-LEFT CORNER
        c1 = cRegions[1 + horizontalMargin];
        c2 = cRegions[cRegions.length - 1 - verticalMargin];
        cRegions[0] = averageRegions(c1,c2);
        //TOP-RIGHT CORNER
        c1 = cRegions[width - 2 - horizontalMargin];
        c2 = cRegions[width - verticalMargin];
        cRegions[width - 1] = averageRegions(c1,c2);
        //BOTTOM-RIGHT CORNER
        c1 = cRegions[width + height - 3 - verticalMargin];
        c2 = cRegions[width + height - 1 + horizontalMargin];
        cRegions[width + height - 2] = averageRegions(c1,c2);
        //BOTTOM-LEFT CORNER
        c1 = cRegions[width + height + width - 4 - horizontalMargin];
        c2 = cRegions[width + height + width - 2 + verticalMargin];
        cRegions[width + height + width - 3] = averageRegions(c1,c2);

        return cRegions;
    }

    /**
     * Calculates the average of two given regions.
     * @param regionA Array of int containing the R/G/B colors for the first region.
     * @param regionB Array of int containing the R/G/B colors for the second region.
     * @return An array of int containing the averaged R/G/B values.
     */
    private int[] averageRegions(int[] regionA, int[] regionB) {
        int[] newRegion = new int[3];
        newRegion[0] = (regionA[0] + regionB[0]) / 2;
        newRegion[1] = (regionA[1] + regionB[1]) / 2;
        newRegion[2] = (regionA[2] + regionB[2]) / 2;
        return newRegion;
    }

    /**
     * Gets the weight for the given index. There are n steps in this method. After the first 5 steps (index = 0,1,2,3,4) the minimum weight is reached.
     * @param index An int that represents the index to be weighed. 0 is maximum weight. n = minimum weight.
     * @return A weighed int representing the index.
     */
    private int getWeight(int index) {
        if(weighColors && index < 5) {
            tempWeight = 10 - (index * weighFactor);
            return tempWeight < 0 ? 1 : tempWeight;
        } else {
            return 1;
        }
    }

    /**
     * Gets the total weight for n loops.
     * @param totalLoops The total number of iterations for which to calculate the total weight.
     * @return An int representing the total calculated weight.
     */
    private int getTotalWeight(int totalLoops) {
        int weight = 0;
        for(int i = 0; i < totalLoops; i++) {
            weight += getWeight(i);
        }
        return weight > 0 ? weight : 1;
    }
}