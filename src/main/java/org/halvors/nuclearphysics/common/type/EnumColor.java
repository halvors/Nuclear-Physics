package org.halvors.nuclearphysics.common.type;

import org.halvors.nuclearphysics.common.utility.LanguageUtility;

/**
 * Simple color enum for adding colors to in-game GUI strings of text.
 * This class is borrowed from Mekanism and is customized to fit our use.
 *
 * @author aidanBrady
 */
public enum EnumColor {
	BLACK(	     "\u00a70", "black",       new int[] { 0,   0,   0 },   0x000000, 0),
	DARK_BLUE(   "\u00a71", "darkBlue",    new int[] { 0,   0,   170 }, 0x0000AA, 4),
	DARK_GREEN(  "\u00a72", "darkGreen",   new int[] { 0,   170, 0 },   0x00AA00, 2),
	DARK_AQUA(   "\u00a73", "darkAqua",    new int[] { 0,   255, 255 }, 0x00FFFF, 6),
	DARK_RED(    "\u00a74", "darkRed",     new int[] { 170, 0,   0 },   0xAA0000, -1),
	PURPLE(      "\u00a75", "purple",      new int[] { 170, 0,   170 }, 0xAA00AA, 5),
	ORANGE(      "\u00a76", "orange",      new int[] { 255, 170, 0 },   0xFFAA00, 14),
	GREY(        "\u00a77", "grey",        new int[] { 170, 170, 170 }, 0xAAAAAA, 7),
	DARK_GREY(   "\u00a78", "darkGrey",    new int[] { 85,  85,  85 },  0x555555, 8),
	INDIGO(		 "\u00a79", "indigo",      new int[] { 85,  85,  255 }, 0x5555FF, 12),
	BRIGHT_GREEN("\u00a7a", "brightGreen", new int[] { 85,  255, 85 },  0x55FF55, 10),
	AQUA(		 "\u00a7b", "aqua", 	   new int[] { 85,  255, 255 }, 0x55FFFF, -1),
	RED(		 "\u00a7c", "red", 		   new int[] { 255, 0,   0 },   0xFF0000, 1),
	PINK(		 "\u00a7d", "pink", 	   new int[] { 255, 85,  255 }, 0xFF55FF, 13),
	YELLOW(		 "\u00a7e", "yellow", 	   new int[] { 255, 255, 85 },  0xFFFF55, 11),
	WHITE(		 "\u00a7f", "white", 	   new int[] { 255, 255, 255 }, 0xFFFFFF, 15),
	//Extras for dye-completeness
	BROWN(		 "\u00a76", "brown", 	   new int[] { 150, 75,  0 },   0x964B00, 3),
	BRIGHT_PINK( "\u00a7d", "brightPink",  new int[] { 255, 192, 203 }, 0xFFC0CB, 9);

	private final String code;
	private final String name;
	private final int[] rgbCode;
	private final int hex;
	private final int meta;

	EnumColor(final String code, final String name, final int[] rgbCode, final int hex, final int meta) {
		this.code = code;
		this.name = name;
		this.rgbCode = rgbCode;
		this.hex = hex;
		this.meta = meta;
	}

	/**
	 * Gets the localized name of this color by translating the unlocalized name.
	 * @return localized name
	 */
	public String getLocalizedName() {
		return LanguageUtility.transelate("color." + name);
	}

	/**
	 * Gets the name of this color with it's color prefix code.
	 * @return the color's name and color prefix
	 */
	public String getName() {
		return code + getLocalizedName();
	}

	/**
	 * Gets the 0-1 of this color's RGB value by dividing by 255 (used for OpenGL coloring).
	 * @param index - R:0, G:1, B:2
	 * @return the color value
	 */
	public float getColor(final int index) {
		return (float) rgbCode[index] / 255;
	}

	public int getHex() {
		return hex;
	}

	/**
	 * Gets the value of this color mapped to MC in-game item colors present in dyes and wool.
	 * @return mc meta value
	 */
	public int getMeta() {
		return meta;
	}

	@Override
	public String toString() {
		return code;
	}
}
