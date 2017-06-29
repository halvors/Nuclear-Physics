package org.halvors.quantum.common.utility.type;

public enum ResourceType {
	GUI("gui"),
	GUI_COMPONENT("gui/components"),
	MODEL("models"),
	SOUND("sounds"),
	TEXTURE_BLOCKS("textures/blocks"),
	TEXTURE_ITEMS("textures/items"),
	TEXTURE_MODELS("textures/models");

	private final String prefix;

	ResourceType(String prefix) {
		this.prefix = prefix;
	}

	public String getPrefix() {
		return prefix + "/";
	}
}