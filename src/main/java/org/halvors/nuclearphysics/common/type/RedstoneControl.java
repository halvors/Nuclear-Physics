package org.halvors.nuclearphysics.common.type;

import org.halvors.nuclearphysics.common.utility.LanguageUtility;

public enum RedstoneControl {
    DISABLED("gui.control.disabled"),
    HIGH("gui.control.high"),
    LOW("gui.control.low"),
    PULSE("gui.control.pulse");

    private final String display;

    RedstoneControl(final String display) {
        this.display = display;
    }

    public String getDisplay() {
        return LanguageUtility.transelate(display);
    }
}