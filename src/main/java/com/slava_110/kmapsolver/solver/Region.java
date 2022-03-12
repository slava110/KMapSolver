package com.slava_110.kmapsolver.solver;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class Region {
    private final int width;
    private final int height;

    public Region(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getArea() {
        return Math.abs(width * height);
    }

    public static List<Region> generateRegions(KMapSize size) {
        List<Region> res = new ArrayList<>();

        for (int w = 1; w <= size.getCols(); w *= 2) {
            for (int h = 1; h <= size.getRows(); h *= 2) {
                res.add(new Region(w, h));

                if((w == 1 && h == 1) || (w == size.getCols() && h == size.getRows()))
                    continue;

                if(w == h) {
                    res.add(new Region(-w, h));
                    res.add(new Region(w, -h));
                    res.add(new Region(-w, -h));
                } else if(w > h) {
                    res.add(new Region(-w, h));

                    if(h != -1) {
                        res.add(new Region(w, -h));
                        res.add(new Region(-w, -h));
                    }
                } else if(w < h) {
                    res.add(new Region(w, -h));

                    if(w != 1) {
                        res.add(new Region(-w, h));
                        res.add(new Region(-w, -h));
                    }
                }
            }
        }

        res.sort(Comparator.comparingInt(Region::getArea)); //
        return res;
    }
}
