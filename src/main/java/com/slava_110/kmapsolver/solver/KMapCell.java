package com.slava_110.kmapsolver.solver;

public class KMapCell {
    private final String binary;
    private final int decimal;
    private final int row;
    private final int col;

    public KMapCell(String binary, int decimal, int row, int col) {
        this.binary = binary;
        this.decimal = decimal;
        this.row = row;
        this.col = col;
    }
}
