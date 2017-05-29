package org.halvors.atomicscience.client.gui.configuration.category;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.GuiConfigEntries.CategoryEntry;
import cpw.mods.fml.client.config.IConfigElement;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import org.halvors.electrometrics.common.ConfigurationManager;
import org.halvors.electrometrics.common.Reference;
import org.halvors.electrometrics.common.util.LanguageUtils;

public class CategoryEntryMachine extends CategoryEntry {
    public CategoryEntryMachine(GuiConfig guiConfig, GuiConfigEntries guiConfigEntries, IConfigElement configElement) {
        super(guiConfig, guiConfigEntries, configElement);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected GuiScreen buildChildScreen() {
        String category = ConfigurationManager.CATEGORY_MACHINE;

        return new GuiConfig(owningScreen,
                new ConfigElement(org.halvors.electrometrics.AtomicScience.getConfiguration().getCategory(category)).getChildElements(),
                owningScreen.modID,
                category,
                false,
                false,
                Reference.NAME + " - " + LanguageUtils.localize("gui.configuration.category." + category));
    }
}