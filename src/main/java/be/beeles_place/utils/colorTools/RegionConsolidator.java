package be.beeles_place.utils.colorTools;

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

    /**
     * Creates a new RegionConsolidater instance.
     *
     * @param horizontalRegions The number of horizontal regions.
     * @param verticalRegions   The number of vertical regions.
     */
    public RegionConsolidator(int horizontalRegions, int verticalRegions, int horizontalMargin, int verticalMargin) {
        width = horizontalRegions;
        height = verticalRegions;

        finalRegionCount = (width * 2) + (height * 2) - 4;

        leftDepth = (int) (width / 2);
        rightDepth = width - leftDepth;

        topDepth = (int) (height / 2);
        bottomDepth = height - topDepth;

        this.horizontalMargin = horizontalMargin;
        this.verticalMargin = verticalMargin;
    }

    /**
     * Will consolidate all regions in to the wanted amount of pixels.
     *
     * @param regions The multidimensional regions array containing all calculated regions.
     * @return An array of int where each side of the screen is appended in the array. starting Top (vertical), Right(side), Bottom(vertical) and Left(side).
     * The second dimension contains the R/G/B values.
     */
    //TODO: give the closes regions more importance than those that are further away!
    //TODO: optimize for speed!
    public int[][] consolidateRegions(int[][][] regions) {
        cRegions = new int[finalRegionCount][3];

        //Collect all regions per column into one pixel.
        for (int i = 0; i < width; i++) {
            //Reset color value variables.
            r = g =b = rr = gg = bb = 0;
            //Loop from column 0 + margin to half of the columns for the left side. (start column + margin <===> center column)
            for (int j = horizontalMargin; j < topDepth; j++) {
                r += regions[i][j][0];
                g += regions[i][j][1];
                b += regions[i][j][2];
            }
            //Calculate the average color values. (total added color values / number of loops)
            r /= (topDepth - horizontalMargin);
            g /= (topDepth - horizontalMargin);
            b /= (topDepth - horizontalMargin);

            //Loop from the center column to the right end column - margin. (center column <===> end column - margin)
            for (int j = topDepth; j < (height - horizontalMargin); j++) {
                rr += regions[i][j][0];
                gg += regions[i][j][1];
                bb += regions[i][j][2];
            }
            //Calculate the average color values. (total added color values / number of loops)
            rr /= (bottomDepth - horizontalMargin);
            gg /= (bottomDepth - horizontalMargin);
            bb /= (bottomDepth - horizontalMargin);

            //Top consolidated region.
            cRegions[i] = new int[]{r, g, b};
            //Bottom consolidated region.
            cRegions[width + height - 2 + (width - i - 1)] = new int[]{rr, gg, bb};
        }

        //Collect all regions per row into one pixel.
        for (int m = 0; m < height; m++) {
            //Reset color value variables.
            r = g =b = rr = gg = bb = 0;
            //Loop from row 0 + margin to the half of the rows for the top side. (start row + margin <===> center row)
            for (int n = verticalMargin; n < leftDepth; n++) {
                r += regions[n][m][0];
                g += regions[n][m][1];
                b += regions[n][m][2];
            }
            //Calculate the average color values. (total added color values / number of loops)
            r /= (leftDepth - verticalMargin);
            g /= (leftDepth - verticalMargin);
            b /= (leftDepth - verticalMargin);

            //Loop from the center row to the bottom end row - margin. (center row <===> end row - margin)
            for (int n = leftDepth; n < (width - verticalMargin); n++) {
                rr += regions[n][m][0];
                gg += regions[n][m][1];
                bb += regions[n][m][2];
            }
            //Calculate the average color values. (total added color values / number of loops)
            rr /= (rightDepth - verticalMargin);
            gg /= (rightDepth - verticalMargin);
            bb /= (rightDepth - verticalMargin);

            //Left consolidated region.
            int tempIndex = width + (height - 2) + width + (height - 2) - m;
            tempIndex = tempIndex == finalRegionCount ? 0 : tempIndex;
            cRegions[tempIndex] = new int[]{r, g, b};
            //Right consolidated region.
            cRegions[m + width - 1] = new int[]{rr, gg, bb};
        }

        //Recalculate the corner regions. The loop logic above have some strange behaviour for the corners.
        int[] c1, c2;
        //TOP-LEFT CORNER
        c1 = cRegions[1];
        c2 = cRegions[cRegions.length - 1];
        cRegions[0] = averageRegions(c1,c2);
        //TOP-RIGHT CORNER
        c1 = cRegions[width - 2];
        c2 = cRegions[width];
        cRegions[width - 1] = averageRegions(c1,c2);
        //BOTTOM-RIGHT CORNER
        c1 = cRegions[width + height - 3];
        c2 = cRegions[width + height - 1];
        cRegions[width + height - 2] = averageRegions(c1,c2);
        //BOTTOM-LEFT CORNER
        c1 = cRegions[width + height + width - 4];
        c2 = cRegions[width + height + width - 2];
        cRegions[width + height + width - 3] = averageRegions(c1,c2);;

        return cRegions;
    }

    /**
     * Calculates the average of two given regions/
     * @param regionA Array of int containing the R/G/B colors for the first region.
     * @param regionB Array of int containing the R/G/B colors for the second region.
     * @return An array of int containing the averaged R/G/B values.
     */
    private int[] averageRegions(int[] regionA, int[] regionB) {
        regionA[0] = (regionA[0] + regionB[0]) / 2;
        regionA[1] = (regionA[1] + regionB[1]) / 2;
        regionA[2] = (regionA[2] + regionB[2]) / 2;
        return regionA;
    }
}