package be.beeles_place.utils.colorTools;

public class RegionConsolitdator {

    private int width;
    private int height;

    private int leftDepth;
    private int rightDepth;

    private int topDepth;
    private int bottomDepth;

    private int consolidatedRegionSize;
    private int[][] cRegions;

    private int r,g,b;
    private int rr,gg,bb;

    /**
     * Constructor for the RegionConsolidator class.
     * @param horizontalRegions The number of horizontal regions.
     * @param verticalRegions The number of vertical regions.
     */
    public RegionConsolitdator(int horizontalRegions, int verticalRegions) {
        width = horizontalRegions;
        height = verticalRegions;

        leftDepth = (int)(width / 2);
        rightDepth = width - leftDepth;

        topDepth = (int)(height / 2);
        bottomDepth = height - topDepth;

        consolidatedRegionSize = ((width + height) * 2 - 4);
    }

    /**
     * Will consolidate all regions in to the wanted amount of pixels.
     * @param regions The multidimensional regions array containing all calculated regions. The second dimension contains the R/G/B values.
     * @return An array of int each side of the screen is appended in the array. starting Top (vertical), Right(side), Bottom(vertical) and Left(side).
     */
    public int[][] consolidateRegions(int[][][] regions) {
        cRegions = new int[consolidatedRegionSize][3];

        //Collect all regions per column into one pixel.
        for(int i = 0 ; i < width ; i++) {
            for(int j = 0 ; j < topDepth ; j++) {
                r += regions[i][j][0];
                g += regions[i][j][1];
                b += regions[i][j][2];
            }
            r /= topDepth;
            g /= topDepth;
            b /= topDepth;

            for(int j = 0 ; j < bottomDepth ; j++) {
                rr += regions[i][j][0];
                gg += regions[i][j][1];
                bb += regions[i][j][2];
            }
            rr /= bottomDepth;
            gg /= bottomDepth;
            bb /= bottomDepth;

            cRegions[i] = new int[]{r,g,b};
            cRegions[width + height - 2 + (width - i - 1)] = new int[]{rr,gg,bb};
            //TODO: if at all possible condense the two loops in to one loop for more performance.
        }

        //Collect all regions per row into one pixel.
        for(int m = 0 ; m < height - 2 ; m++) {
            for(int n = 0 ; n < leftDepth ; n++) {
                r += regions[n][m][0];
                g += regions[n][m][1];
                b += regions[n][m][2];
            }
            r /= leftDepth;
            g /= leftDepth;
            b /= leftDepth;

            for(int n = 0 ; n < rightDepth ; n++) {
                rr += regions[n][m][0];
                gg += regions[n][m][1];
                bb += regions[n][m][2];
            }
            rr /= rightDepth;
            gg /= rightDepth;
            bb /= rightDepth;

            cRegions[m + width - 1] = new int[]{r,g,b};
            cRegions[width + height - 2 + width + m] = new int[]{rr,gg,bb};
            //TODO: if at all possible condense the two loops in to one loop for more performance.
        }

        return cRegions;
    }
}