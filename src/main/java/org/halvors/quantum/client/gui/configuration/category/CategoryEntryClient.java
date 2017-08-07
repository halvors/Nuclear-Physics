package org.halvors.quantum.client.gui.configuration.category;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.common.ConfigurationManager;
import org.halvors.quantum.common.Quantum;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.utility.LanguageUtility;

@SideOnly(Side.CLIENT)
public class CategoryEntryClient extends GuiConfigEntries.CategoryEntry {
    public CategoryEntryClient(GuiConfig guiConfig, GuiConfigEntries guiConfigEntries, IConfigElement configElement) {
        super(guiConfig, guiConfigEntries, configElement);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected GuiScreen buildChildScreen() {
        String category = ConfigurationManager.CATEGORY_CLIENT;

        return new GuiConfig(owningScreen,
                new ConfigElement(Quantum.getConfiguration().getCategory(category)).getChildElements(),
                owningScreen.modID,
                category,
                false,
                false,
                Reference.NAME + " - " + LanguageUtility.transelate("gui.configuration.category." + category));
    }
}