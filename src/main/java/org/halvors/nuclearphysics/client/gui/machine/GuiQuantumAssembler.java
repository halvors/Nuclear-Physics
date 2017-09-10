package org.halvors.nuclearphysics.client.gui.machine;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.gui.GuiComponentContainer;
import org.halvors.nuclearphysics.client.gui.component.GuiEnergyInfo;
import org.halvors.nuclearphysics.client.gui.component.GuiSlot;
import org.halvors.nuclearphysics.client.gui.component.GuiSlot.SlotType;
import org.halvors.nuclearphysics.common.container.machine.ContainerQuantumAssembler;
import org.halvors.nuclearphysics.common.tile.machine.TileQuantumAssembler;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;
import org.halvors.nuclearphysics.common.utility.energy.UnitDisplay;
import org.halvors.nuclearphysics.common.utility.type.ResourceType;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiQuantumAssembler extends GuiComponentContainer<TileQuantumAssembler> {
    public GuiQuantumAssembler(InventoryPlayer inventoryPlayer, TileQuantumAssembler tile) {
        super(tile, new ContainerQuantumAssembler(inventoryPlayer, tile), ResourceUtility.getResource(ResourceType.GUI, "quantum_assembler.png"));

        ySize = 230;

        components.add(new GuiSlot(SlotType.NORMAL, this, 79, 39));
        components.add(new GuiSlot(SlotType.NORMAL, this, 52, 55));
        components.add(new GuiSlot(SlotType.NORMAL, this, 106, 55));
        components.add(new GuiSlot(SlotType.NORMAL, this, 52, 87));
        components.add(new GuiSlot(SlotType.NORMAL, this, 106, 87));
        components.add(new GuiSlot(SlotType.NORMAL, this, 79, 102));
        components.add(new GuiSlot(SlotType.NORMAL, this, 79, 71));
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
            displayText = "Process: " + (int) (100 - ((float) tile.operatingTicks / (float) tile.ticksRequired) * 100) + "%";
        } else if (tile.canProcess()) {
            displayText = "Ready";
        } else {
            displayText = "Idle";
        }

        fontRenderer.drawString(displayText, (xSize / 2) - 80, ySize - 106, 0x404040);

        fontRenderer.drawString(LanguageUtility.transelate("container.inventory"), (xSize / 2) - 80, (ySize - 96) + 2, 0x404040);

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }
}