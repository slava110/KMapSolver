package com.slava_110.kmapsolver.solver;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class GrayCodeTest {

    @Test
    public void testGrayCodes() {
        assertGrayCodeRight(
                2,
                new String[]{
                        "0", "1"
                },
                new String[]{
                        "0", "1"
                }
        );
        assertGrayCodeRight(
                3,
                new String[]{
                        "00", "01", "11", "10"
                },
                new String[]{
                        "0", "1"
                }
        );
        assertGrayCodeRight(
                4,
                new String[]{
                        "00", "01", "11", "10"
                },
                new String[]{
                        "00", "01", "11", "10"
                }
        );
        assertGrayCodeRight(
                5,
                new String[]{
                        "000", "001", "011", "010", "110", "111", "101", "100"
                },
                new String[]{
                        "00", "01", "11", "10"
                }
        );
        assertGrayCodeRight(
                6,
                new String[]{
                        "000", "001", "011", "010", "110", "111", "101", "100"
                },
                new String[]{
                        "000", "001", "011", "010", "110", "111", "101", "100"
                }
        );
    }

    private void assertGrayCodeRight(int varAmount, String[] expectedRows, String[] expectedColumns) {
        GrayCode grayCode = GrayCode.generate(new KMapSize(varAmount));
        assertArrayEquals(expectedRows, grayCode.getRows(), () -> String.format("Rows are wrong for grayCode of size %s!", varAmount));
        assertArrayEquals(expectedColumns, grayCode.getCols(), () -> String.format("Columns are wrong for grayCode of size %s!", varAmount));
    }
}
