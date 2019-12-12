package org.halvors.nuclearphysics.common.utility;

import net.minecraft.client.resources.I18n;

import java.util.ArrayList;
import java.util.List;

public class LanguageUtility {
	public static boolean canTranselate(final String text) {
		return I18n.hasKey(text);
	}

	public static String transelate(final String text, final Object... parameters) {
		return I18n.format(text, parameters);
	}

	public static List<String> splitStringPerWord(final String text, final int wordsPerLine) {
		final String[] words = text.split("\\s+");
		final List<String> lines = new ArrayList<>();

		for (int lineCount = 0; lineCount < Math.ceil((float) words.length / (float) wordsPerLine); lineCount++) {
			final StringBuilder stringInLine = new StringBuilder();

			for (int i = lineCount * wordsPerLine; i < Math.min(wordsPerLine + lineCount * wordsPerLine, words.length); i++) {
				stringInLine.append(words[i]).append(" ");
			}

			lines.add(stringInLine.toString().trim());
		}

		return lines;
	}
}
