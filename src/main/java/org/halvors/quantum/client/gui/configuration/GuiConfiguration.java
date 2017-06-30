package org.halvors.quantum.client.gui.configuration;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.client.gui.configuration.category.CategoryEntryClient;
import org.halvors.quantum.client.gui.configuration.category.CategoryEntryGeneral;
import org.halvors.quantum.client.gui.configuration.category.CategoryEntryIntegration;
import org.halvors.quantum.common.ConfigurationManager;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.utility.LanguageUtility;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiConfiguration extends GuiConfig {
    private static final List<IConfigElement> configElements = new ArrayList<>();

    static {
        register(Configuration.CATEGORY_GENERAL, CategoryEntryGeneral.class);
        register(ConfigurationManager.CATEGORY_INTEGRATION, CategoryEntryIntegration.class);
        register(ConfigurationManager.CATEGORY_CLIENT, CategoryEntryClient.class);
    }

    public GuiConfiguration(GuiScreen parent) {
        super(parent, configElements, Reference.ID, false, false, Reference.NAME);

        titleLine2 = Quantum.getConfiguration().getConfigFile().getAbsolutePath();
    }

    private static void register(String category, Class<? extends GuiConfigEntries.IConfigEntry> configEntryClass) {
        configElements.add(new DummyConfigElement.DummyCategoryElement(LanguageUtility.localize("gui.configuration.category." + category), "gui.configuration.category." + category, configEntryClass));
    }
}