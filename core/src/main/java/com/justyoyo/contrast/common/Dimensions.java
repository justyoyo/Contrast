package com.justyoyo.contrast.common;

/**
 * Created by tiberiugolaes on 09/11/2016.
 */

public final class Dimensions {

    private final int minCols;
    private final int maxCols;
    private final int minRows;
    private final int maxRows;

    public Dimensions(int minCols, int maxCols, int minRows, int maxRows) {
        this.minCols = minCols;
        this.maxCols = maxCols;
        this.minRows = minRows;
        this.maxRows = maxRows;
    }

    public int getMinCols() {
        return minCols;
    }

    public int getMaxCols() {
        return maxCols;
    }

    public int getMinRows() {
        return minRows;
    }

    public int getMaxRows() {
        return maxRows;
    }

}
