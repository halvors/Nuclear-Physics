package org.halvors.quantum.client.gui.configuration.category;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.GuiConfigEntries.CategoryEntry;
import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.ConfigurationManager;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.utility.LanguageUtility;

@SideOnly(Side.CLIENT)
public class CategoryEntryIntegration extends CategoryEntry {
    public CategoryEntryIntegration(GuiConfig guiConfig, GuiConfigEntries guiConfigEntries, IConfigElement configElement) {
        super(guiConfig, guiConfigEntries, configElement);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected GuiScreen buildChildScreen() {
        String category = ConfigurationManager.CATEGORY_INTEGRATION;

        return new GuiConfig(owningScreen,
                new ConfigElement(Quantum.getConfiguration().getCategory(category)).getChildElements(),
                owningScreen.modID,
                category,
                false,
                false,
                Reference.NAME + " - " + LanguageUtility.localize("gui.configuration.category." + category));
    }
}