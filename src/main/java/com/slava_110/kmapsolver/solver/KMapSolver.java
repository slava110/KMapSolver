package com.slava_110.kmapsolver.solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class KMapSolver {

    @NotNull
    public static List<List<KMapCell>> solve(int varAmount, List<Integer> terms, List<Integer> dontCares, boolean mknf) {
        KMapSize size = new KMapSize(varAmount);
        KMapCell[][] kMap = generateKMap(size);

        List<List<KMapCell>> groups = new ArrayList<>();

        if(mknf) {
            List<Integer> finalTerms = terms;
            terms = IntStream.range(0, size.getTotalSize())
                    .filter(num -> !finalTerms.contains(num))
                    .boxed()
                    .collect(Collectors.toList());
        }

        Queue<Integer> termQueue = terms.stream()
                .filter(term -> term >= 0 && term < size.getTotalSize())
                .collect(Collectors.toCollection(LinkedList::new));

        List<Integer> termsToGroup = new ArrayList<>(terms);
        if(!dontCares.isEmpty())
            termsToGroup.addAll(dontCares);

        while (!termQueue.isEmpty()) {
            //List<KMapCell> group = findGroup(kMap, size, termQueue.peek(), terms, termQueue);

            List<KMapCell> group = findGroup(kMap, size, termQueue.peek(), termsToGroup, termQueue);

            if(group == null) {
                termQueue.poll();
                continue;
            }

            groups.add(group);

            /*termQueue = termQueue.stream()
                    .filter(term -> !group.stream().anyMatch(cell -> cell.decimal == term))
                    .collect(Collectors.toCollection(LinkedList::new));*/

            for (KMapCell cell : group) {
                termQueue.removeIf(term -> cell.decimal == term);
            }
        }

        ListIterator<List<KMapCell>> iter = groups.listIterator();
        while (iter.hasNext()) {
            List<KMapCell> group = iter.next();
            if(!group.stream()
                    .filter(
                            cell -> !groups.stream()
                                    .filter(group1 -> group != group1)
                                    .anyMatch(
                                            group1 -> group1.contains(cell)
                                    )
                    )
                    .findAny()
                    .isPresent()
            ) {
                iter.remove();
                continue;
            }
        }

        return groups;
    }

    @Nullable
    private static List<KMapCell> findGroup(KMapCell[][] kMap, KMapSize size, int decimal, List<Integer> terms, Queue<Integer> termQueue) {
        @Nullable
        final Pos pos = findDecimal(kMap, decimal);
        if(pos == null) {
            StringBuilder errorBuilder = new StringBuilder("Pos is null!\n=== KMap ===\n");

            for (int x = 0; x < kMap.length; x++) {
                for (int y = 0; y < kMap[x].length; y++) {
                    errorBuilder.append(kMap[x][y].decimal + " ");
                }
                errorBuilder.append("\n");
            }
            errorBuilder.append(String.format("Find decimal %s: %s", decimal, pos != null));
            throw new IllegalArgumentException(errorBuilder.toString());
        }

        final List<Region> regions = Region.generateRegions(size);

        List<List<KMapCell>> composedGroups = new ArrayList<>(1);
        composedGroups.add(Collections.singletonList(kMap[pos.row][pos.col]));

        for (Region region : regions) {
            final List<KMapCell> cells = checkRegion(kMap, size, terms, region, pos);
            if(cells.isEmpty())
                continue;

            if(cells.size() > composedGroups.get(0).size()) {
                composedGroups = new ArrayList<>();
                composedGroups.add(cells);
            } else if(cells.size() == composedGroups.get(0).size()) {
                composedGroups.add(cells);
            }
        }

        List<List<KMapCell>> groupsThatContainCellsGrouppedBefore = new ArrayList<>();

        for (List<KMapCell> group : composedGroups) {
            if(group.stream().anyMatch(cell -> !termQueue.contains(cell.decimal)))
                groupsThatContainCellsGrouppedBefore.add(group);
        }

        if(groupsThatContainCellsGrouppedBefore.size() > 0 && groupsThatContainCellsGrouppedBefore.size() != composedGroups.size()) {
            return composedGroups.stream()
                    .filter(group ->
                            !groupsThatContainCellsGrouppedBefore.contains(group)
                    )
                    .findAny()
                    .orElse(null);
        }
        return composedGroups.get(0);
    }

    @NotNull
    private static List<KMapCell> checkRegion(KMapCell[][] kMap, KMapSize size, List<Integer> terms, Region region, Pos pos) {
        final List<KMapCell> res = new ArrayList<>();

        int r = 0;

        while (r != region.getHeight()) {
            int curRow = (pos.row + r) % size.getRows();
            if(curRow < 0)
                curRow += size.getRows();

            int c = 0;
            while (c != region.getWidth()) {
                int curCol = (pos.col + c) % size.getCols();
                if(curCol < 0)
                    curCol += size.getCols();

                KMapCell cell = kMap[curRow][curCol];

                if(!terms.contains(cell.decimal))
                    return Collections.emptyList();

                res.add(cell);

                if(region.getWidth() < 0)
                    c--;
                else
                    c++;
            }

            if(region.getWidth() < 0)
                r--;
            else
                r++;
        }

        return res;
    }

    @Nullable
    private static Pos findDecimal(KMapCell[][] kMap, int decimal) {
        for (int row = 0; row < kMap.length; row++) {
            for (int col = 0; col < kMap[row].length; col++) {
                if(kMap[row][col].decimal == decimal)
                    return new Pos(row, col);
            }
        }
        return null;
    }

    private static KMapCell[][] generateKMap(KMapSize size) {
        KMapCell[][] kMap = new KMapCell[size.getRows()][size.getCols()];

        GrayCode grayCode = GrayCode.generate(size);

        for (int row = 0; row < size.getRows(); row++) {
            for (int col = 0; col < size.getCols(); col++) {
                String binary = grayCode.getRows()[row] + grayCode.getCols()[col];
                kMap[row][col] = new KMapCell(binary, Integer.parseInt(binary, 2), row, col);
            }
        }

        return kMap;
    }

    public static class KMapCell {
        public String binary;
        public int decimal;
        public int row;
        public int col;

        public KMapCell(String binary, int decimal, int row, int col) {
            this.binary = binary;
            this.decimal = decimal;
            this.row = row;
            this.col = col;
        }
    }
}
