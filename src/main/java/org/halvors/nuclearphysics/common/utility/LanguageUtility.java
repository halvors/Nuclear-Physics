package org.halvors.nuclearphysics.common.utility;

import net.minecraft.util.StatCollector;

import java.util.ArrayList;
import java.util.List;

public class LanguageUtility {
	@SuppressWarnings("deprecation")
	public static boolean canTranselate(String text) {
		return StatCollector.canTranslate(text);
	}

	@SuppressWarnings("deprecation")
	public static String transelate(String text, Object... parameters) {
		return StatCollector.translateToLocalFormatted(text, parameters);
	}

	public static List<String> splitStringPerWord(String text, int wordsPerLine) {
		String[] words = text.split("\\s+");
		List<String> lines = new ArrayList<>();

		for (int lineCount = 0; lineCount < Math.ceil((float) words.length / (float) wordsPerLine); lineCount++) {
			StringBuilder stringInLine = new StringBuilder();

			for (int i = lineCount * wordsPerLine; i < Math.min(wordsPerLine + lineCount * wordsPerLine, words.length); i++) {
				stringInLine.append(words[i]).append(" ");
			}

			lines.add(stringInLine.toString().trim());
		}

		return lines;
	}
}
