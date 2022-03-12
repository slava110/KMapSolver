package com.slava_110.kmapsolver.component;

import java.awt.Graphics;

import com.cburch.logisim.data.Bounds;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.InstanceFactory;
import com.cburch.logisim.instance.InstancePainter;
import com.cburch.logisim.instance.InstanceState;
import com.cburch.logisim.instance.Port;
import com.slava_110.kmapsolver.util.ComponentDataSingleton;
import com.slava_110.kmapsolver.util.ComponentDrawingUtils;
import com.slava_110.kmapsolver.util.ComponentUtils;

public class LTruthTable extends InstanceFactory {

    public LTruthTable() {
        super("truth-table", () -> "Truth Table");
        setOffsetBounds(Bounds.create(-120, -255, 120, 255));

        Port[] ports = new Port[1];
        ports[0] = new Port(-120, -90, Port.INPUT, 16);
        ports[0].setToolTip(() -> "Function Data");

        setPorts(ports);
    }

    @Override
    public void paintInstance(InstancePainter painter) {
        painter.drawBounds();
        painter.drawPorts();

        Graphics g = painter.getGraphics();
        Bounds bounds = painter.getBounds();

        ComponentDataSingleton<Value[]> data = ComponentUtils.getData(painter);
        Value[] cachedValues = data != null ? data.value : null;

        ComponentDrawingUtils.drawVarTable(
                g,
                bounds,
                15,
                new String[] {"a", "b", "c", "d"},
                new Value[][] {cachedValues}
        );
    }

    private String getDisplayForValue(Value val) {
        if(val == Value.TRUE) {
            return "1";
        } else if (val == Value.FALSE) {
            return "0";
        } else if (val == Value.UNKNOWN) {
            return "*";
        } else if(val == Value.ERROR) {
            return "!";
        } else {
            return "?";
        }
    }

    @Override
    public void propagate(InstanceState state) {
        ComponentDataSingleton<Value[]> data = ComponentUtils.getOrCreateData(state, () -> new ComponentDataSingleton<>(null));
        if(data.value != state.getPort(0).getAll()) {
            data.value = state.getPort(0).getAll();
        }
    }
}
