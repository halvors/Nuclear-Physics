package org.halvors.atomicscience.client.gui.configuration.category;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.GuiConfigEntries.CategoryEntry;
import cpw.mods.fml.client.config.IConfigElement;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import org.halvors.electrometrics.common.Reference;
import org.halvors.electrometrics.common.util.LanguageUtils;

public class CategoryEntryGeneral extends CategoryEntry {
    public CategoryEntryGeneral(GuiConfig guiConfig, GuiConfigEntries guiConfigEntries, IConfigElement configElement) {
        super(guiConfig, guiConfigEntries, configElement);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected GuiScreen buildChildScreen() {
        String category = Configuration.CATEGORY_GENERAL;

        return new GuiConfig(owningScreen,
                new ConfigElement(org.halvors.electrometrics.AtomicScience.getConfiguration().getCategory(category)).getChildElements(),
                owningScreen.modID,
                category,
                false,
                false,
                Reference.NAME + " - " + LanguageUtils.localize("gui.configuration.category." + category));
    }
}