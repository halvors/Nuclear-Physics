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
import org.halvors.nuclearphysics.common.container.machine.ContainerNuclearBoiler;
import org.halvors.nuclearphysics.common.tile.machine.TileNuclearBoiler;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiNuclearBoiler extends GuiMachine<TileNuclearBoiler> {
    public GuiNuclearBoiler(final InventoryPlayer inventoryPlayer, final TileNuclearBoiler tile) {
        super(tile, new ContainerNuclearBoiler(inventoryPlayer, tile));

        final int centerX = xSize / 2;

        components.add(new GuiBar(tile.getEnergyStorage(), this, centerX - 80, 18));
        components.add(new GuiFluidGauge(tile::getInputTank, this, centerX - 70, 18));
        components.add(new GuiSlot(EnumSlotType.LIQUID, this, centerX - 54, 18));
        components.add(new GuiSlot(EnumSlotType.LIQUID, this, centerX - 54, 49));
        components.add(new GuiSlot(this, centerX - 24, 25));
        components.add(new GuiSlot(EnumSlotType.BATTERY, this, centerX - 4, 25));
        components.add(new GuiBar(() -> tile.getOperatingTicks() / TileNuclearBoiler.TICKS_REQUIRED, EnumBarType.PROGRESS, this, centerX + 30, 26));
        components.add(new GuiSlot(EnumSlotType.GAS, this, centerX + 46, 49));
        components.add(new GuiFluidGauge(tile::getOutputTank, this, centerX + 66, 18));
    }

    @Override
    public void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        final List<String> list = LanguageUtility.splitStringPerWord(LanguageUtility.transelate(tile.getBlockType().getUnlocalizedName() + "." + tile.getType().ordinal() + ".text"), 4);

        for (int i = 0; i < list.size(); i++) {
            fontRendererObj.drawString(list.get(i), centerX - 80, 85 + i * 9, 0x404040);
        }

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }
}