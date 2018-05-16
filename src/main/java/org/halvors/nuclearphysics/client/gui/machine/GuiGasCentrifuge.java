package org.halvors.nuclearphysics.client.gui.machine;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.gui.GuiMachine;
import org.halvors.nuclearphysics.client.gui.component.GuiBar;
import org.halvors.nuclearphysics.client.gui.component.GuiBar.EnumBarType;
import org.halvors.nuclearphysics.client.gui.component.GuiFluidGauge;
import org.halvors.nuclearphysics.client.gui.component.GuiSlot;
import org.halvors.nuclearphysics.client.gui.component.GuiSlot.EnumSlotType;
import org.halvors.nuclearphysics.common.container.machine.ContainerGasCentrifuge;
import org.halvors.nuclearphysics.common.tile.machine.TileGasCentrifuge;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiGasCentrifuge extends GuiMachine<TileGasCentrifuge> {
    public GuiGasCentrifuge(final InventoryPlayer inventoryPlayer, final TileGasCentrifuge tile) {
        super(tile, new ContainerGasCentrifuge(inventoryPlayer, tile));

        components.add(new GuiBar(tile.getEnergyStorage(), this, centerX - 80, 18));
        components.add(new GuiFluidGauge(tile::getTank, this, centerX - 70, 18));
        components.add(new GuiSlot(EnumSlotType.GAS, this, centerX - 54, 49));
        components.add(new GuiBar(() -> tile.getOperatingTicks() / TileGasCentrifuge.TICKS_REQUIRED, EnumBarType.PROGRESS, this, centerX - 40, 25));
        components.add(new GuiSlot(this, centerX - 8, 25));
        components.add(new GuiSlot(this, centerX + 12, 25));
        components.add(new GuiSlot(EnumSlotType.BATTERY, this, centerX + 42, 25));
    }

    @Override
    public void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        final String displayText;

        if (tile.getOperatingTicks() > 0) {
            displayText = "gui.processing";
        } else if (tile.canProcess()) {
            displayText = "gui.ready";
        } else {
            displayText = "gui.idle";
        }

        fontRendererObj.drawString(LanguageUtility.transelate("gui.status") + ": " + LanguageUtility.transelate(displayText), 70, 55, 0x404040);

        final List<String> list = LanguageUtility.splitStringPerWord(LanguageUtility.transelate(tile.getBlockType().getUnlocalizedName() + "." + tile.getType().ordinal() + ".text"), 4);

        for (int i = 0; i < list.size(); i++) {
            fontRendererObj.drawString(list.get(i), centerX - 80, 85 + i * 9, 0x404040);
        }

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }
}