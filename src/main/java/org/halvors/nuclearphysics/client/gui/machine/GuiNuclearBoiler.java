package org.halvors.nuclearphysics.client.gui.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.InventoryPlayer;
import org.halvors.nuclearphysics.client.gui.GuiMachine;
import org.halvors.nuclearphysics.client.gui.component.GuiFluidGauge;
import org.halvors.nuclearphysics.client.gui.component.GuiProgress;
import org.halvors.nuclearphysics.client.gui.component.GuiSlot;
import org.halvors.nuclearphysics.client.gui.component.GuiSlot.EnumSlotType;
import org.halvors.nuclearphysics.common.container.machine.ContainerNuclearBoiler;
import org.halvors.nuclearphysics.common.tile.machine.TileNuclearBoiler;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiNuclearBoiler extends GuiMachine<TileNuclearBoiler> {
    public GuiNuclearBoiler(final InventoryPlayer inventoryPlayer, final TileNuclearBoiler tile) {
        super(tile, new ContainerNuclearBoiler(inventoryPlayer, tile));

        components.add(new GuiSlot(EnumSlotType.BATTERY, this, 55, 25));
        components.add(new GuiSlot(this, 80, 25));
        components.add(new GuiProgress(() -> (double) tile.getOperatingTicks() / TileNuclearBoiler.TICKS_REQUIRED, this, 110, 26));
        components.add(new GuiFluidGauge(tile::getInputTank, this, (xSize / 2) - 80, 18));
        components.add(new GuiSlot(EnumSlotType.LIQUID, this, 24, 18));
        components.add(new GuiSlot(EnumSlotType.LIQUID, this, 24, 49));
        components.add(new GuiFluidGauge(tile::getOutputTank, this, 154, 18));
        components.add(new GuiSlot(EnumSlotType.GAS, this, 134, 49));
    }

    @Override
    public void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        final List<String> list = LanguageUtility.splitStringPerWord(LanguageUtility.transelate(tile.getBlockType().getUnlocalizedName() + "." + tile.getType().ordinal() + ".text"), 4);

        for (int i = 0; i < list.size(); i++) {
            fontRendererObj.drawString(list.get(i), (xSize / 2) - 80, 85 + i * 9, 0x404040);
        }

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }
}