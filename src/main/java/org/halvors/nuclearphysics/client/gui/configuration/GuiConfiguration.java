package org.halvors.nuclearphysics.client.gui.configuration;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.gui.configuration.category.CategoryEntryGeneral;
import org.halvors.nuclearphysics.client.gui.configuration.category.CategoryEntryIntegration;
import org.halvors.nuclearphysics.common.ConfigurationManager;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.Reference;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiConfiguration extends GuiConfig {
    private static final List<IConfigElement> configElements = new ArrayList<>();

    static {
        register(Configuration.CATEGORY_GENERAL, CategoryEntryGeneral.class);
        register(ConfigurationManager.CATEGORY_INTEGRATION, CategoryEntryIntegration.class);
    }

    public GuiConfiguration(GuiScreen parent) {
        super(parent, configElements, Reference.ID, false, false, Reference.NAME);

        titleLine2 = NuclearPhysics.getConfiguration().getConfigFile().getAbsolutePath();
    }

    private static void register(String category, Class<? extends GuiConfigEntries.IConfigEntry> configEntryClass) {
        configElements.add(new DummyConfigElement.DummyCategoryElement(LanguageUtility.transelate("gui.configuration.category." + category), "gui.configuration.category." + category, configEntryClass));
    }
}