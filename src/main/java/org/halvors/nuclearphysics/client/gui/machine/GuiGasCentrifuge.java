package org.halvors.nuclearphysics.client.gui.machine;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.gui.GuiComponentContainer;
import org.halvors.nuclearphysics.client.gui.component.GuiEnergyInfo;
import org.halvors.nuclearphysics.client.gui.component.GuiFluidGauge;
import org.halvors.nuclearphysics.client.gui.component.GuiProgress;
import org.halvors.nuclearphysics.client.gui.component.GuiSlot;
import org.halvors.nuclearphysics.client.gui.component.GuiSlot.SlotType;
import org.halvors.nuclearphysics.common.container.machine.ContainerGasCentrifuge;
import org.halvors.nuclearphysics.common.tile.machine.TileGasCentrifuge;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;
import org.halvors.nuclearphysics.common.utility.energy.UnitDisplay;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiGasCentrifuge extends GuiComponentContainer<TileGasCentrifuge> {
    public GuiGasCentrifuge(InventoryPlayer inventoryPlayer, TileGasCentrifuge tile) {
        super(tile, new ContainerGasCentrifuge(inventoryPlayer, tile));

        components.add(new GuiSlot(SlotType.NORMAL, this, 80, 25));
        components.add(new GuiSlot(SlotType.NORMAL, this, 100, 25));
        components.add(new GuiSlot(SlotType.BATTERY, this, 130, 25));
        components.add(new GuiProgress(() -> (double) tile.operatingTicks / tile.ticksRequired, this, 40, 26));
        components.add(new GuiFluidGauge(tile::getTank, this, (xSize / 2) - 80, 18));
        components.add(new GuiSlot(SlotType.GAS, this, 24, 49));
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
    }

    @Override
    public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(tile.getName(), (xSize / 2) - (fontRenderer.getStringWidth(tile.getName()) / 2), (ySize / 2) - 102, 0x404040);

        String displayText;

        if (tile.operatingTicks > 0) {
            displayText = "Processing";
        } else if (tile.canProcess()) {
            displayText = "Ready";
        } else {
            displayText = "Idle";
        }

        fontRenderer.drawString("Status: " + displayText, 70, 50, 0x404040);

        List<String> list = LanguageUtility.splitStringPerWord(LanguageUtility.transelate(tile.getBlockType().getUnlocalizedName() + "." + tile.getType().ordinal() + ".text"), 4);

        for (int i = 0; i < list.size(); i++) {
            fontRenderer.drawString(list.get(i), (xSize / 2) - 80, 85 + i * 9, 0x404040);
        }

        fontRenderer.drawString(LanguageUtility.transelate("container.inventory"), (xSize / 2) - 80, (ySize - 96) + 2, 0x404040);

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }
}