package com.slava_110.kmapsolver.util;

import com.cburch.logisim.instance.InstanceData;
import com.cburch.logisim.instance.InstanceDataSingleton;

/**
 * Because {@link InstanceDataSingleton isn't generic :c}
 * @param <T>
 */
public class ComponentDataSingleton<T> implements InstanceData, Cloneable {
    public T value;

    public ComponentDataSingleton(T value) {
        this.value = value;
    }

    @Override
    public ComponentDataSingleton<T> clone() {
        try {
            return (ComponentDataSingleton<T>) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
