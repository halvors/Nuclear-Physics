package org.halvors.nuclearphysics.client.gui.process;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.gui.GuiMachine;
import org.halvors.nuclearphysics.client.gui.component.GuiFluidGauge;
import org.halvors.nuclearphysics.client.gui.component.GuiProgress;
import org.halvors.nuclearphysics.client.gui.component.GuiSlot;
import org.halvors.nuclearphysics.client.gui.component.GuiSlot.SlotType;
import org.halvors.nuclearphysics.common.container.process.ContainerGasCentrifuge;
import org.halvors.nuclearphysics.common.tile.process.TileGasCentrifuge;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiGasCentrifuge extends GuiMachine<TileGasCentrifuge> {
    public GuiGasCentrifuge(InventoryPlayer inventoryPlayer, TileGasCentrifuge tile) {
        super(tile, new ContainerGasCentrifuge(inventoryPlayer, tile));

        components.add(new GuiSlot(SlotType.NORMAL, this, 80, 25));
        components.add(new GuiSlot(SlotType.NORMAL, this, 100, 25));
        components.add(new GuiSlot(SlotType.BATTERY, this, 130, 25));
        components.add(new GuiProgress(() -> (double) tile.operatingTicks / tile.ticksRequired, this, 40, 26));
        components.add(new GuiFluidGauge(tile::getTank, this, (xSize / 2) - 80, 18));
        components.add(new GuiSlot(SlotType.GAS, this, 24, 49));
    }

    @Override
    public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String displayText;

        if (tile.operatingTicks > 0) {
            displayText = "gui.processing";
        } else if (tile.canProcess()) {
            displayText = "gui.ready";
        } else {
            displayText = "gui.idle";
        }

        fontRendererObj.drawString(LanguageUtility.transelate("gui.status") + ": " + LanguageUtility.transelate(displayText), 70, 50, 0x404040);

        List<String> list = LanguageUtility.splitStringPerWord(LanguageUtility.transelate(tile.getBlockType().getUnlocalizedName() + "." + tile.getType().ordinal() + ".text"), 4);

        for (int i = 0; i < list.size(); i++) {
            fontRendererObj.drawString(list.get(i), (xSize / 2) - 80, 85 + i * 9, 0x404040);
        }

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }
}