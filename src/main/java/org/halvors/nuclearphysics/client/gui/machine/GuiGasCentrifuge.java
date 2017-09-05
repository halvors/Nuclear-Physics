package org.halvors.nuclearphysics.client.gui.machine;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.gui.GuiComponentContainer;
import org.halvors.nuclearphysics.client.gui.component.GuiFluidGauge;
import org.halvors.nuclearphysics.client.gui.component.GuiProgress;
import org.halvors.nuclearphysics.client.gui.component.GuiSlot;
import org.halvors.nuclearphysics.client.gui.component.GuiSlot.SlotType;
import org.halvors.nuclearphysics.common.container.machine.ContainerGasCentrifuge;
import org.halvors.nuclearphysics.common.tile.machine.TileGasCentrifuge;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiGasCentrifuge extends GuiComponentContainer<TileGasCentrifuge> {
    public GuiGasCentrifuge(InventoryPlayer inventoryPlayer, TileGasCentrifuge tile) {
        super(tile, new ContainerGasCentrifuge(inventoryPlayer, tile));

        components.add(new GuiSlot(SlotType.NORMAL, this, 80, 25));
        components.add(new GuiSlot(SlotType.NORMAL, this, 100, 25));
        components.add(new GuiSlot(SlotType.BATTERY, this, 130, 25));
        components.add(new GuiProgress(() -> (double) tile.operatingTicks / tile.ticksRequired, this, 40, 26));
        components.add(new GuiFluidGauge(tile::getTank, this, 8, 18));
        components.add(new GuiSlot(SlotType.GAS, this, 24, 49));
    }

    @Override
    public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRendererObj.drawString(tile.getName(), (xSize / 2) - (fontRendererObj.getStringWidth(tile.getName()) / 2), 6, 0x404040);

        String displayText;

        if (tile.operatingTicks > 0) {
            displayText = "Processing";
        } else if (tile.canProcess()) {
            displayText = "Ready";
        } else {
            displayText = "Idle";
        }

        fontRendererObj.drawString("Status: " + displayText, 70, 50, 0x404040);

        //renderUniversalDisplay(8, 112, TileNuclearBoiler.energy * 20, mouseX, mouseY, UnitDisplay.Unit.WATT);
        //renderUniversalDisplay(100, 112, tile.getVoltageInput(null), mouseX, mouseY, UnitDisplay.Unit.VOLTAGE);

        List<String> list = LanguageUtility.splitStringPerWord(LanguageUtility.transelate(tile.getBlockType().getUnlocalizedName() + "." + tile.getType().ordinal() + ".text"), 4);

        for (int i = 0; i < list.size(); i++) {
            fontRendererObj.drawString(list.get(i), 9, 85 + i * 9, 0x404040);
        }

        fontRendererObj.drawString(LanguageUtility.transelate("container.inventory"), 8, (ySize - 96) + 2, 0x404040);

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }
}