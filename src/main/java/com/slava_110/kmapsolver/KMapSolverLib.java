package com.slava_110.kmapsolver;

import java.util.Arrays;
import java.util.List;

import com.cburch.logisim.tools.AddTool;
import com.cburch.logisim.tools.Library;
import com.cburch.logisim.tools.Tool;
import com.slava_110.kmapsolver.component.LKarnaughMap;
import com.slava_110.kmapsolver.component.LTruthTable;

public final class KMapSolverLib extends Library {
    private final List<Tool> tools = Arrays.asList(
            new AddTool(new LTruthTable()),
            new AddTool(new LKarnaughMap())
    );

    @Override
    public String getName() {
        return "kmapsolver";
    }

    @Override
    public String getDisplayName() {
        return "Karnaugh Map Solver";
    }

    @Override
    public List<? extends Tool> getTools() {
        return tools;
    }
}
