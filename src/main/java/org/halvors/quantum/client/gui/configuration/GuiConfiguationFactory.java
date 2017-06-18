package org.halvors.quantum.client.gui.configuration;

import cpw.mods.fml.client.IModGuiFactory;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import java.util.Set;

@SideOnly(Side.CLIENT)
public class GuiConfiguationFactory implements IModGuiFactory {
    @Override
    public void initialize(Minecraft instance) {

    }

    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass() {
        return GuiConfiguration.class;
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }

    @Override
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
        return null;
    }
}