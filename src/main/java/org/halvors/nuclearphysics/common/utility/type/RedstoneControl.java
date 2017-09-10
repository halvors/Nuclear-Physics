package org.halvors.nuclearphysics.common.utility.type;

import org.halvors.nuclearphysics.common.utility.LanguageUtility;

public enum RedstoneControl {
    DISABLED("gui.control.disabled"),
    HIGH("gui.control.high"),
    LOW("gui.control.low"),
    PULSE("gui.control.pulse");

    private String display;

    RedstoneControl(String display) {
        this.display = display;
    }

    public String getDisplay() {
        return LanguageUtility.transelate(display);
    }
}