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
import org.halvors.nuclearphysics.common.container.machine.ContainerChemicalExtractor;
import org.halvors.nuclearphysics.common.tile.machine.TileChemicalExtractor;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;
import org.halvors.nuclearphysics.common.utility.energy.UnitDisplay;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiChemicalExtractor extends GuiComponentContainer<TileChemicalExtractor> {
    public GuiChemicalExtractor(InventoryPlayer inventory, TileChemicalExtractor tile) {
        super(tile, new ContainerChemicalExtractor(inventory, tile));

        components.add(new GuiSlot(SlotType.BATTERY, this, 79, 49));
        components.add(new GuiSlot(SlotType.NORMAL, this, 52, 24, "Input slot"));
        components.add(new GuiSlot(SlotType.NORMAL, this, 106, 24));
        components.add(new GuiProgress(() -> (double) tile.operatingTicks / tile.ticksRequired, this, 75, 24));
        components.add(new GuiFluidGauge(tile::getInputTank, this, (xSize / 2) - 80, 18));
        components.add(new GuiSlot(SlotType.LIQUID, this, 24, 18));
        components.add(new GuiSlot(SlotType.LIQUID, this, 24, 49));
        components.add(new GuiSlot(SlotType.LIQUID, this, 134, 18));
        components.add(new GuiSlot(SlotType.LIQUID, this, 134, 49));
        components.add(new GuiFluidGauge(tile::getOutputTank, this, 154, 18));
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
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRenderer.drawString(tile.getName(), (xSize / 2) - (fontRenderer.getStringWidth(tile.getName()) / 2), (ySize / 2) - 102, 0x404040);

        List<String> list = LanguageUtility.splitStringPerWord(LanguageUtility.transelate(tile.getBlockType().getUnlocalizedName() + "." + tile.getType().ordinal() + ".text"), 4);

        for (int i = 0; i < list.size(); i++) {
            fontRenderer.drawString(list.get(i), (xSize / 2) - 80, 85 + i * 9, 0x404040);
        }

        fontRenderer.drawString(LanguageUtility.transelate("container.inventory"), (xSize / 2) - 80, (ySize - 96) + 2, 0x404040);

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }
}
