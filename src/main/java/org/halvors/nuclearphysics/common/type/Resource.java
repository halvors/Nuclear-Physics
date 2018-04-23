package org.halvors.nuclearphysics.common.type;

public enum Resource {
	GUI("textures/gui"),
	GUI_COMPONENT("textures/gui/components"),
	MODEL("block"),
	SOUND("sounds"),
	TEXTURE_MODELS("models");

	private final String prefix;

	Resource(final String prefix) {
		this.prefix = prefix;
	}

	public String getPrefix() {
		return prefix + "/";
	}
}