package org.halvors.atomicscience.old.util;

import net.minecraft.util.StatCollector;

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

    private static StringWrapper.WrappedString wrap(String str)
    {
        return new StringWrapper.WrappedString(str);
    }

    public static String getLocal(String key)
    {
        return wrap(key).getLocal();
    }

    public static List<String> splitStringPerWord(String string, int characters)
    {
        return wrap(string).listWrap(characters);
    }

}

