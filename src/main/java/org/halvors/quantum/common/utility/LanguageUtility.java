package org.halvors.quantum.common.utility;

import net.minecraft.util.StatCollector;

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
		return StatCollector.translateToLocal(text);
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
}
