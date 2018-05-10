package org.halvors.nuclearphysics.client.gui.component;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.event.TextureEventHandler;
import org.halvors.nuclearphysics.client.gui.IGuiWrapper;
import org.halvors.nuclearphysics.common.science.unit.UnitDisplay;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;

import java.awt.*;

@SideOnly(Side.CLIENT)
public class GuiEnergyGauge extends GuiGauge {
    private final IEnergyInfoHandler energyInfoHandler;

    public GuiEnergyGauge(final IEnergyInfoHandler energyInfoHandler, final IGuiWrapper gui, final int x, final int y) {
        super(gui, x, y);

        this.energyInfoHandler = energyInfoHandler;
    }

    @Override
    public Rectangle getBounds(int guiWidth, int guiHeight) {
        return new Rectangle(guiWidth - 26, guiHeight + 6, 26, 26);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected int getScaledLevel() {
        final IEnergyStorage energyStorage = energyInfoHandler.getEnergyStorage();

        if (energyStorage.getEnergyStored() == Integer.MAX_VALUE) {
            return HEIGHT - 2;
        }

        return energyStorage.getEnergyStored() * (HEIGHT - 2) / energyStorage.getMaxEnergyStored();
    }

    @Override
    protected TextureAtlasSprite getTexture() {
        return TextureEventHandler.getTexture("energy");
    }

    @Override
    protected String getTooltip() {
        final IEnergyStorage energyStorage = energyInfoHandler.getEnergyStorage();

        return energyStorage.getEnergyStored() > 0 ? UnitDisplay.getEnergyDisplay(energyStorage.getEnergyStored()) : LanguageUtility.transelate("gui.empty");
    }
}