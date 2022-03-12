package com.slava_110.kmapsolver.util;

import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.cburch.logisim.instance.InstanceData;
import com.cburch.logisim.instance.InstanceState;

public final class ComponentUtils {

    @Nullable
    public static <T extends InstanceData> T getData(InstanceState state) {
        return (T) state.getData();
    }

    @NotNull
    public static <T extends InstanceData> T getOrCreateData(InstanceState state, Supplier<T> provider) {
        T data = getData(state);
        if(data == null) {
            data = provider.get();
            state.setData(data);
        }
        return data;
    }
}
