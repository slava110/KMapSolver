package com.slava_110.kmapsolver.component;

import static com.slava_110.kmapsolver.util.ComponentDrawingUtils.getTableCellHeight;
import static com.slava_110.kmapsolver.util.ComponentDrawingUtils.getTableCellWidth;
import static com.slava_110.kmapsolver.util.ComponentDrawingUtils.getTableX;
import static com.slava_110.kmapsolver.util.ComponentDrawingUtils.getTableY;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.InstanceData;
import com.cburch.logisim.instance.InstanceFactory;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.instance.Port;
import com.slava_110.kmapsolver.solver.GrayCode;
import com.slava_110.kmapsolver.solver.KMapSize;
import com.slava_110.kmapsolver.solver.KMapSolver;
import com.slava_110.kmapsolver.solver.KMapSolver.KMapCell;
import com.slava_110.kmapsolver.util.ComponentDrawingUtils;
import com.slava_110.kmapsolver.util.ComponentUtils;

public class LKarnaughMap extends InstanceFactory {

    public LKarnaughMap() {
        super("karnaugh_map", () -> "Karnaugh Map");
        setOffsetBounds(Bounds.create(-180, -180, 180, 180));

        Port[] ports = new Port[2];
        ports[0] = new Port(-180, -90, Port.INPUT, 16);
        ports[0].setToolTip(() -> "Function Data");
        ports[1] = new Port(-180, -60, Port.INPUT, 1);
        ports[1].setToolTip(() -> "MKNF?");
        setPorts(ports);
    }

    @Override
    public void paintInstance(InstancePainter painter) {
        painter.drawBounds();
        painter.drawPorts();

        Graphics g = painter.getGraphics();
        Bounds bounds = painter.getBounds();
        LKarnaughMapData data = ComponentUtils.getData(painter);

        if(data != null) {
            GrayCode grayCode = GrayCode.generate(new KMapSize(4));

            ComponentDrawingUtils.drawTable(g, bounds, 5, 5, 20, (x, y) -> {
                if(y == 0 && x > 0) {
                    return grayCode.getCols()[x - 1];
                } else if(x == 0 && y > 0) {
                    return grayCode.getRows()[y - 1];
                } else if(x > 0 && y > 0) {
                    return getDisplayForValue(data, x - 1, y - 1);
                } else {
                    return "";
                }
            });

            drawIntervals(g, bounds, data);
        }
    }

    private String getDisplayForValue(LKarnaughMapData data, int x, int y) {
        if(x > 1)
            x = x == 2 ? 3 : 2;
        if(y > 1)
            y = y == 2 ? 3 : 2;

        int index = y * 4 + x;
        if(data.terms.contains(index))
            return "1";
        else if(data.dontcares.contains(index))
            return "X";
        else if(data.errored.contains(index))
            return "!";
        else
            return "0";
    }

    private static final Color[] INTERVAL_COLORS = new Color[]{
            Color.BLUE,
            Color.CYAN,
            Color.GREEN,
            Color.MAGENTA,
            Color.ORANGE,
            Color.PINK,
            Color.RED,
            Color.YELLOW
    };

    private void drawIntervals(Graphics g, Bounds bounds, LKarnaughMapData data) {
        if(data.getGroups() != null && !data.getGroups().isEmpty()) {
            Graphics2D g1 = (Graphics2D) g.create();

            g1.setStroke(new BasicStroke(2));
            g1.clipRect(
                    bounds.getX() + getTableCellWidth(bounds, 5),
                    bounds.getY() + getTableCellHeight(bounds, 5),
                    bounds.getWidth() - getTableCellWidth(bounds, 5),
                    bounds.getHeight() - getTableCellHeight(bounds, 5)
            );

            int lastColor = 0;
            for (List<KMapCell> interval : data.getGroups()) {
                g1.setColor(INTERVAL_COLORS[lastColor++]);

                KMapCell begin = interval.get(0);
                KMapCell end = interval.get(interval.size() - 1);

                boolean yCont = end.row - begin.row == 3 && !interval.stream().anyMatch(c -> c.row == 1);
                boolean xCont = end.col - begin.col == 3 && !interval.stream().anyMatch(c -> c.col == 1);

                if(xCont && yCont) {
                    drawInterval(g1, bounds, begin.col + 1, begin.row + 1, 0, 0);
                    drawInterval(g1, bounds, begin.col + 1, end.row + 1, 0, 5);
                    drawInterval(g1, bounds, end.col + 1, begin.row + 1, 5, 0);
                    drawInterval(g1, bounds, end.col+ 1, end.row + 1, 5, 5);
                } else if(yCont) {
                    drawInterval(g1, bounds, begin.col + 1, begin.row + 1, end.col + 1, 0);
                    drawInterval(g1, bounds, end.col + 1, end.row + 1, begin.col + 1, 5);
                } else if(xCont) {
                    drawInterval(g1, bounds, begin.col + 1, begin.row + 1, 0, end.row + 1);
                    drawInterval(g1, bounds, end.col + 1, end.row + 1, 5, begin.row + 1);
                } else {
                    drawInterval(g1, bounds, begin.col + 1, begin.row + 1, end.col + 1, end.row + 1);
                }
            }
            g1.dispose();
        }
    }

    private void drawInterval(Graphics g, Bounds bounds, int x1, int y1, int x2, int y2) {
        g.drawRoundRect(
                getTableX(bounds, Math.min(x1, x2), 5) + 3,
                getTableY(bounds, Math.min(y1, y2), 5) + 3,
                getTableCellWidth(bounds, 5) * (Math.abs(x2 - x1) + 1) - 6,
                getTableCellHeight(bounds, 5) * (Math.abs(y2 - y1) + 1) - 6,
                10,
                10
        );
    }

    @Override
    public void propagate(InstanceState state) {
        LKarnaughMapData data = ComponentUtils.getOrCreateData(state, LKarnaughMapData::new);
        data.propogate(state);
    }

    private static final class LKarnaughMapData implements InstanceData, Cloneable {
        private final List<Integer> terms = new ArrayList<>();
        private final List<Integer> dontcares = new ArrayList<>();
        private final List<Integer> errored = new ArrayList<>();

        @Nullable
        private Value cachedVal = null;
        @Nullable
        private Value cachedMKNF = null;
        @Nullable
        private List<List<KMapCell>> cachedGroups = null;

        private boolean mknf = false;

        public LKarnaughMapData() {}

        public void propogate(InstanceState state) {
            boolean refreshMe = false;

            if(state.getPort(1) != cachedMKNF) {
                cachedMKNF = state.getPort(1);
                if(cachedMKNF == Value.TRUE && !mknf) {
                    mknf = true;
                    refreshMe = true;
                } else if(cachedMKNF == Value.FALSE && mknf) {
                    mknf = false;
                    refreshMe = true;
                }
            }
            if(state.getPort(0) != cachedVal) {
                Value valIn = state.getPort(0);
                cachedVal = state.getPort(0);
                terms.clear();
                dontcares.clear();
                errored.clear();

                for (int i = 0; i < valIn.getAll().length; i++) {
                    Value val = valIn.get(valIn.getWidth() - i - 1);

                    if(val == Value.TRUE) {
                        terms.add(i);
                    } else if(val == Value.UNKNOWN) {
                        dontcares.add(i);
                    } else if(val == Value.ERROR) {
                        errored.add(i);
                    }
                }
                refreshMe = true;
            }
            if(refreshMe) {
                recalculateIntervals();
            }
        }

        private void recalculateIntervals() {
            cachedGroups = KMapSolver.solve(4, terms, dontcares, mknf);
        }

        @Nullable
        public List<List<KMapCell>> getGroups() {
            return cachedGroups;
        }

        @Override
        public LKarnaughMapData clone() {
            LKarnaughMapData data = new LKarnaughMapData();
            data.recalculateIntervals();
            return data;
        }
    }
}
