package org.halvors.atomicscience.common.base;

public enum ResourceType {
	GUI("gui"),
	GUI_COMPONENT("gui/components"),
	SOUND("sound"),
	TEXTURE_BLOCKS("textures/blocks"),
	TEXTURE_ITEMS("textures/items");

	private final String prefix;

	ResourceType(String prefix) {
		this.prefix = prefix;
	}

	public String getPrefix() {
		return prefix + "/";
	}
}