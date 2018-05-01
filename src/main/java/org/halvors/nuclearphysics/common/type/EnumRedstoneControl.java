package org.halvors.nuclearphysics.common.type;

import org.halvors.nuclearphysics.common.utility.LanguageUtility;

public enum EnumRedstoneControl {
    DISABLED,
    HIGH,
    LOW,
    PULSE;

    public String getDisplay() {
        return LanguageUtility.transelate("gui.control." + name().toLowerCase());
    }
}