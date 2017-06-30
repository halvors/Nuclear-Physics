package org.halvors.quantum.common.utility;

import net.minecraft.util.text.translation.I18n;

import java.util.ArrayList;
import java.util.List;

public class LanguageUtility {
	/**
	 * Get the translation for the current text.
	 *
	 * @param text the text that we want to localize.
	 * @return text
	 */
	public static String localize(String text) {
		return I18n.translateToLocal(text);
	}

	public static List<String> splitStringPerWord(String string, int wordsPerLine) {
		String[] words = string.split(" ");
		List<String> lines = new ArrayList<>();

		for (int lineCount = 0; lineCount < Math.ceil(words.length / wordsPerLine); lineCount++) {
			StringBuilder stringInLine = new StringBuilder();

			for (int i = lineCount * wordsPerLine; i < Math.min(wordsPerLine + lineCount * wordsPerLine, words.length); i++) {
				stringInLine.append(words[i]).append(" ");
			}

			lines.add(stringInLine.toString().trim());
		}

		return lines;
	}

	public static String capitalizeFirst(String text) {
		return text.substring(0, 1).toUpperCase() + text.substring(1);
	}

	public static String decapitalizeFirst(String text) {
		return text.substring(0, 1).toLowerCase() + text.substring(1);
	}
}
