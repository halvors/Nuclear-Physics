package org.halvors.nuclearphysics.common.utility;

import net.minecraft.util.text.translation.I18n;

import java.util.ArrayList;
import java.util.List;

public class LanguageUtility {
	public static boolean canTranselate(String text) {
		return I18n.canTranslate(text);
	}

	public static String transelate(String text, Object... parameters) {
		return I18n.translateToLocalFormatted(text, parameters);
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

	public static String capitalizeFirst(String text) {
		return text.substring(0, 1).toUpperCase() + text.substring(1);
	}

	public static String decapitalizeFirst(String text) {
		return text.substring(0, 1).toLowerCase() + text.substring(1);
	}
}
