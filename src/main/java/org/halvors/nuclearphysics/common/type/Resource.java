package org.halvors.nuclearphysics.common.type;

public enum Resource {
	GUI("textures/gui"),
	GUI_COMPONENT("textures/gui/components"),
	MODEL("models/block"),
	SOUND("sounds"),
	TEXTURE_BLOCKS("textures/blocks"),
	TEXTURE_MODELS("textures/models");

	private final String prefix;

	Resource(String prefix) {
		this.prefix = prefix;
	}

	public String getPrefix() {
		return prefix + "/";
	}
}