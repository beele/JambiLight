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
    public int[][] consolidateRegions(int[][][] regions) {
        cRegions = new int[finalRegionCount][3];

        //Collect all regions per column into one pixel.
        for (int i = 0; i < width; i++) {
            r = 0;
            g = 0;
            b = 0;
            rr = 0;
            gg = 0;
            bb = 0;
            for (int j = horizontalMargin; j < topDepth; j++) {
                r += regions[i][j][0];
                g += regions[i][j][1];
                b += regions[i][j][2];
            }
            r /= (topDepth - horizontalMargin);
            g /= (topDepth - horizontalMargin);
            b /= (topDepth - horizontalMargin);

            for (int j = topDepth; j < (height - horizontalMargin); j++) {
                rr += regions[i][j][0];
                gg += regions[i][j][1];
                bb += regions[i][j][2];
            }
            rr /= (bottomDepth - horizontalMargin);
            gg /= (bottomDepth - horizontalMargin);
            bb /= (bottomDepth - horizontalMargin);

            //Top
            cRegions[i] = new int[]{r, g, b};
            //Bottom
            cRegions[width + height - 2 + (width - i - 1)] = new int[]{rr, gg, bb};
        }

        //Collect all regions per row into one pixel.
        for (int m = 0; m < height; m++) {
            r = 0;
            g = 0;
            b = 0;
            rr = 0;
            gg = 0;
            bb = 0;
            for (int n = verticalMargin; n < leftDepth; n++) {
                r += regions[n][m][0];
                g += regions[n][m][1];
                b += regions[n][m][2];
            }
            r /= (leftDepth - verticalMargin);
            g /= (leftDepth - verticalMargin);
            b /= (leftDepth - verticalMargin);

            for (int n = leftDepth; n < (width - verticalMargin); n++) {
                rr += regions[n][m][0];
                gg += regions[n][m][1];
                bb += regions[n][m][2];
            }
            rr /= (rightDepth - verticalMargin);
            gg /= (rightDepth - verticalMargin);
            bb /= (rightDepth - verticalMargin);

            //Left
            int tempIndex = width + (height - 2) + width + (height - 2) - m;
            tempIndex = tempIndex == finalRegionCount ? 0 : tempIndex;
            cRegions[tempIndex] = new int[]{r, g, b};
            //Right
            cRegions[m + width - 1] = new int[]{rr, gg, bb};
        }

        return cRegions;
    }
}