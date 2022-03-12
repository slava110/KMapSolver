package com.slava_110.kmapsolver.solver;

public class GrayCode {
    private final String[] rows;
    private final String[] cols;

    private GrayCode(String[] rows, String[] cols) {
        this.rows = rows;
        this.cols = cols;
    }

    public String[] getRows() {
        return rows;
    }

    public String[] getCols() {
        return cols;
    }

    public static GrayCode generate(KMapSize size) {
        int rows = size.getRows();
        int cols = size.getCols();
        String[] rowsGray = new String[rows];
        for (int i = 0; i < rows; i++) {
            rowsGray[i] = pad(Integer.toBinaryString(getGrayCode(i)), size.getRowVars());
        }
        String[] colsGray = new String[cols];
        for (int i = 0; i < cols; i++) {
            colsGray[i] = pad(Integer.toBinaryString(getGrayCode(i)), size.getColVars());
        }
        return new GrayCode(rowsGray, colsGray);
    }

    private static String pad(String toPad, int size) {
        return String.format("%1$" + size + "s", toPad).replace(' ', '0');
    }

    private static int getGrayCode(int x) {
        return x ^ (x >> 1);
    }
}
