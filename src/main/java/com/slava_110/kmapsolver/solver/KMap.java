package com.slava_110.kmapsolver.solver;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Nullable;

public class KMap {
    private final KMapSize size;
    private final KMapCell[][] cells;

    public KMap(int varAmount, boolean mknf) {
        size = new KMapSize(varAmount);
        cells = new KMapCell[size.getRows()][size.getCols()];
        generateCells();
    }

    private void generateCells() {
        KMapGrayCode grayCode = size.generateGrayCode();

        for (int row = 0; row < size.getRows(); row++) {
            for (int col = 0; col < size.getCols(); col++) {
                String binary = grayCode.getRows()[row] + grayCode.getCols()[col];
                cells[row][col] = new KMapCell(binary, Integer.parseInt(binary, 2), row, col);
            }
        }
    }

    public void findGroups(List<Integer> terms, List<Integer> dontCares) {
        List<KMapGroup> groups = new ArrayList<>();

        Queue<Integer> termsQueue = terms.stream().collect(Collectors.toCollection(LinkedList::new));

        while (!termsQueue.isEmpty()) {
            KMapGroup group = findGroup();
            if(group == null)
                continue;

            groups.add(group);
        }
    }

    @Nullable
    private KMapGroup findGroup() {
        for(int i = 0; i <= size.getCols(); i *= 2) {

        }
    }
}
