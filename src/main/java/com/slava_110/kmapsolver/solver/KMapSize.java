package com.slava_110.kmapsolver.solver;

public class KMapSize {
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

    public KMapGrayCode generateGrayCode() {
        String[] rowsGray = new String[rows];
        for (int i = 0; i < rows; i++) {
            rowsGray[i] = pad(Integer.toBinaryString(getGrayCode(i)), rowVars);
        }
        String[] colsGray = new String[cols];
        for (int i = 0; i < cols; i++) {
            colsGray[i] = pad(Integer.toBinaryString(getGrayCode(i)), colVars);
        }
        return new KMapGrayCode(rowsGray, colsGray);
    }

    private static String pad(String toPad, int size) {
        return String.format("%1$" + size + "s", toPad).replace(' ', '0');
    }

    private static int getGrayCode(int x) {
        return x ^ (x >> 1);
    }
}
