package com.slava_110.kmapsolver.solver;

public class KMapGrayCode {
    private final String[] rows;
    private final String[] cols;

    public KMapGrayCode(String[] rows, String[] cols) {
        this.rows = rows;
        this.cols = cols;
    }

    public String[] getRows() {
        return rows;
    }

    public String[] getCols() {
        return cols;
    }

    private static String pad(String toPad, int size) {
        return String.format("%1$" + size + "s", toPad).replace(' ', '0');
    }

    private static int getGrayCode(int x) {
        return x ^ (x >> 1);
    }
}
