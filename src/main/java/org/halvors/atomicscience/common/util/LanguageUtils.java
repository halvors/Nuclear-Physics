package org.halvors.atomicscience.common.util;

import net.minecraft.util.StatCollector;

public class LanguageUtils {
	/**
	 * Get the translation for the current text.
	 *
	 * @param text the text that we want to localize.
	 * @return text
	 */
	public static String localize(String text) {
		return StatCollector.translateToLocal(text);
	}
}
