package org.halvors.nuclearphysics.client.gui.machine;

import net.minecraft.inventory.Container;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.gui.GuiComponentContainer;
import org.halvors.nuclearphysics.client.gui.component.GuiEnergyInfo;
import org.halvors.nuclearphysics.client.gui.component.GuiRedstoneControl;
import org.halvors.nuclearphysics.common.tile.machine.TileMachine;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;
import org.halvors.nuclearphysics.common.utility.energy.UnitDisplay;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiMachine<T extends TileMachine> extends GuiComponentContainer<T> {
    public GuiMachine(T tile, Container container) {
        super(tile, container);

        components.add(new GuiEnergyInfo(() -> {
            IEnergyStorage energyStorage = tile.getEnergyStorage();
            List<String> list = new ArrayList<>();
            list.add(LanguageUtility.transelate("gui.using") + ": " + UnitDisplay.getEnergyDisplay(tile.energyUsed) + "/t");

            if (energyStorage.getEnergyStored() < energyStorage.getMaxEnergyStored()) {
                list.add(LanguageUtility.transelate("gui.needed") + ": " + UnitDisplay.getEnergyDisplay(energyStorage.getMaxEnergyStored() - energyStorage.getEnergyStored()));
            } else {
                list.add(LanguageUtility.transelate("gui.buffer") + ": " + UnitDisplay.getEnergyDisplay(energyStorage.getEnergyStored()));
            }

            return list;
        }, this));
        components.add(new GuiRedstoneControl(tile, this));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(tile.getName(), (xSize / 2) - (fontRenderer.getStringWidth(tile.getName()) / 2), (ySize / 2) - 102, 0x404040);
        fontRenderer.drawString(LanguageUtility.transelate("container.inventory"), (xSize / 2) - 80, (ySize - 96) + 2, 0x404040);

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }
}
