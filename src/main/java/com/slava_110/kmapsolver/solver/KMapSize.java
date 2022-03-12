package com.slava_110.kmapsolver.solver;

public final class KMapSize {
    private final int rowVars;
    private final int colVars;
    private final int rows;
    private final int cols;

    public KMapSize(int varAmount) {
        this(varAmount / 2 + varAmount % 2, varAmount / 2);
    }

    public KMapSize(int rowVars, int colVars) {
        this.rowVars = rowVars;
        this.colVars = colVars;
        this.rows = (int) Math.pow(2, rowVars);
        this.cols = (int) Math.pow(2, colVars);
    }

    public int getRowVars() {
        return rowVars;
    }

    public int getColVars() {
        return colVars;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getTotalSize() {
        return cols * rows;
    }
}
