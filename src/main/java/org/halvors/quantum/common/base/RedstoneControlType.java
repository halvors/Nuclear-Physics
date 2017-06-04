package org.halvors.quantum.common.base;

public enum RedstoneControlType {
	DISABLED("Disabled"),
	HIGH("High"),
	LOW("Low"),
	PULSE("Pulse");

	private final String display;

	RedstoneControlType(String display) {
		this.display = display;
	}

	public String getDisplay() {
		return display;
	}
}