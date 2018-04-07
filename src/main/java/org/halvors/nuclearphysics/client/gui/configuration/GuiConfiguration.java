package org.halvors.nuclearphysics.client.gui.configuration;

import cpw.mods.fml.client.config.DummyConfigElement;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.Configuration;
import org.halvors.nuclearphysics.client.gui.configuration.category.CategoryEntryGeneral;
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
    }

    public GuiConfiguration(GuiScreen parent) {
        super(parent, configElements, Reference.ID, false, false, Reference.NAME);

        titleLine2 = NuclearPhysics.getConfiguration().getConfigFile().getAbsolutePath();
    }

    private static void register(String category, Class<? extends GuiConfigEntries.IConfigEntry> configEntryClass) {
        configElements.add(new DummyConfigElement.DummyCategoryElement(LanguageUtility.transelate("gui.configuration.category." + category), "gui.configuration.category." + category, configEntryClass));
    }
}