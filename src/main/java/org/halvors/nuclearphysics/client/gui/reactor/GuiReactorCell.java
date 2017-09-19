package org.halvors.nuclearphysics.client.gui.reactor;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.gui.GuiComponentContainer;
import org.halvors.nuclearphysics.client.gui.component.*;
import org.halvors.nuclearphysics.client.gui.component.GuiBar.BarType;
import org.halvors.nuclearphysics.client.gui.component.GuiSlot.SlotType;
import org.halvors.nuclearphysics.common.Reference;
import org.halvors.nuclearphysics.common.container.reactor.ContainerReactorCell;
import org.halvors.nuclearphysics.common.grid.thermal.ThermalPhysics;
import org.halvors.nuclearphysics.common.init.ModFluids;
import org.halvors.nuclearphysics.common.tile.reactor.TileReactorCell;
import org.halvors.nuclearphysics.common.unit.UnitDisplay;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiReactorCell extends GuiComponentContainer<TileReactorCell> {
    public GuiReactorCell(InventoryPlayer inventoryPlayer, TileReactorCell tile) {
        super(tile, new ContainerReactorCell(inventoryPlayer, tile));

        components.add(new GuiTemperatureInfo(ArrayList::new, this, -26, 183));
        components.add(new GuiSlot(SlotType.NORMAL, this, (xSize / 2) - 10, (ySize / 2) - 92));
        components.add(new GuiFluidGauge(tile::getTank, this, (xSize / 2) - 8, (ySize / 2) - 72));

        ItemStack itemStack = tile.getInventory().getStackInSlot(0);
        FluidStack fluidStack = tile.getTank().getFluid();

        if (!itemStack.isEmpty() || ModFluids.fluidStackPlasma.isFluidEqual(fluidStack)) {
            components.add(new GuiBar(() -> (tile.getTemperature() - ThermalPhysics.roomTemperature) / TileReactorCell.meltingPoint, BarType.TEMPERATURE, this, (xSize / 2) - 80, (ySize / 2) - 38));
        }

        if (!itemStack.isEmpty()) {
            components.add(new GuiBar(new IProgressInfoHandler() {
                ItemStack itemStack = tile.getInventory().getStackInSlot(0);

                @Override
                public double getProgress() {
                    if (itemStack != null) {
                        return (double) (itemStack.getMaxDamage() - itemStack.getMetadata()) / itemStack.getMaxDamage();
                    }

                    return 0;
                }
            }, BarType.TIMER, this, (xSize / 2) + 14, (ySize / 2) - 38));
        }
    }

    @Override
    public void drawGuiContainerForegroundLayer(int x, int y) {
        fontRenderer.drawString(tile.getLocalizedName(), (xSize / 2) - (fontRenderer.getStringWidth(tile.getLocalizedName()) / 2), (ySize / 2) - 102, 0x404040);

        ItemStack itemStack = tile.getInventory().getStackInSlot(0);
        FluidStack fluidStack = tile.getTank().getFluid();

        if (!itemStack.isEmpty() || ModFluids.fluidStackPlasma.isFluidEqual(fluidStack)) {
            // Text field for actual heat inside of reactor cell.
            fontRenderer.drawString(LanguageUtility.transelate("gui.temperature"), (xSize / 2) - 80, 45, 0x404040);
            fontRenderer.drawString(UnitDisplay.getTemperatureDisplay(Math.floor(tile.getTemperature())) + "/" + UnitDisplay.getTemperatureDisplay(TileReactorCell.meltingPoint), (xSize / 2) - 80, 58, 0x404040);
        }

        if (!itemStack.isEmpty()) {
            // Text field for total number of ticks remaining.
            int secondsLeft = itemStack.getMaxDamage() - itemStack.getMetadata();

            fontRenderer.drawString(LanguageUtility.transelate("gui.remaining"), (xSize / 2) + 14, 45, 0x404040);
            fontRenderer.drawString(secondsLeft + "s", (xSize / 2) + 14, 58, 0x404040);
        }

        List<String> list = LanguageUtility.splitStringPerWord(LanguageUtility.transelate("tile." + Reference.ID + "." + tile.getName() + ".tooltip"), 5);

        for (int i = 0; i < list.size(); i++) {
            fontRenderer.drawString(list.get(i), (xSize / 2) - 80, 85 + i * 9, 0x404040);
        }

        fontRenderer.drawString(LanguageUtility.transelate("container.inventory"), (xSize / 2) - 80, (ySize - 96) + 2, 0x404040);

        super.drawGuiContainerForegroundLayer(x, y);
    }
}