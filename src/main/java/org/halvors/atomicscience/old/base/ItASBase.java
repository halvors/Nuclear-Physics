package org.halvors.atomicscience.old.base;

import com.calclavia.core.lib.prefab.item.ItemBase;
import org.halvors.atomicscience.old.TabAS;
import org.halvors.atomicscience.old.Settings;

public class ItASBase extends ItemBase {
    public ItASBase(String name) {
        super(name, Settings.CONFIGURATION, "atomicscience:", TabAS.INSTANCE);
    }
}
