package org.halvors.nuclearphysics.common.utility.type;

public enum ResourceType {
	GUI("textures/gui"),
	GUI_COMPONENT("textures/gui/components"),
	MODEL("block"),
	SOUND("sounds"),
	TEXTURE_MODELS("models");

	private final String prefix;

	ResourceType(String prefix) {
		this.prefix = prefix;
	}

	public String getPrefix() {
		return prefix + "/";
	}
}