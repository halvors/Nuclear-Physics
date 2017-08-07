package org.halvors.quantum.atomic.client.gui.configuration.category;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.atomic.common.ConfigurationManager;
import org.halvors.quantum.atomic.common.Quantum;
import org.halvors.quantum.atomic.common.Reference;
import org.halvors.quantum.atomic.common.utility.LanguageUtility;

@SideOnly(Side.CLIENT)
public class CategoryEntryIntegration extends GuiConfigEntries.CategoryEntry {
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