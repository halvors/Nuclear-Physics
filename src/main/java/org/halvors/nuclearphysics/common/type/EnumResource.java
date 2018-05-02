package org.halvors.nuclearphysics.common.type;

public enum EnumResource {
	GUI("textures/gui"),
	GUI_COMPONENT("textures/gui/components"),
	MODEL("models/block"),
	SOUND("sounds"),
	TEXTURE_BLOCKS("textures/blocks"),
	TEXTURE_MODELS("textures/models");

	private final String prefix;

	EnumResource(final String prefix) {
		this.prefix = prefix;
	}

	public String getPrefix() {
		return prefix + "/";
	}
}