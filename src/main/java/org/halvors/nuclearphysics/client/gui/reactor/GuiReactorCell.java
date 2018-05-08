package org.halvors.nuclearphysics.client.gui.reactor;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.gui.GuiComponentContainer;
import org.halvors.nuclearphysics.client.gui.component.*;
import org.halvors.nuclearphysics.client.gui.component.GuiBar.EnumBarType;
import org.halvors.nuclearphysics.common.Reference;
import org.halvors.nuclearphysics.common.container.reactor.ContainerReactorCell;
import org.halvors.nuclearphysics.common.init.ModFluids;
import org.halvors.nuclearphysics.common.science.physics.ThermalPhysics;
import org.halvors.nuclearphysics.common.science.unit.UnitDisplay;
import org.halvors.nuclearphysics.common.tile.reactor.TileReactorCell;
import org.halvors.nuclearphysics.common.type.EnumColor;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;

import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiReactorCell extends GuiComponentContainer<TileReactorCell> {
    public GuiReactorCell(final InventoryPlayer inventoryPlayer, final TileReactorCell tile) {
        super(tile, new ContainerReactorCell(inventoryPlayer, tile));

        components.add(new GuiTemperatureInfo(ArrayList::new, this, -26, 183));
        components.add(new GuiSlot(this, (xSize / 2) - 10, (ySize / 2) - 92));
        components.add(new GuiFluidGauge(tile::getTank, this, (xSize / 2) - 8, (ySize / 2) - 72));

        final ItemStack itemStack = tile.getInventory().getStackInSlot(0);
        final FluidStack fluidStack = tile.getTank().getFluid();

        if (itemStack != null || ModFluids.fluidStackPlasma.isFluidEqual(fluidStack)) {
            components.add(new GuiBar(() -> (tile.getTemperature() - ThermalPhysics.ROOM_TEMPERATURE) / TileReactorCell.MELTING_POINT, EnumBarType.TEMPERATURE, this, (xSize / 2) - 80, (ySize / 2) - 38));
        }

        if (itemStack != null) {
            components.add(new GuiBar(new IProgressInfoHandler() {
                final ItemStack itemStack = tile.getInventory().getStackInSlot(0);

                @Override
                public double getProgress() {
                    if (itemStack != null) {
                        return (double) (itemStack.getMaxDamage() - itemStack.getMetadata()) / itemStack.getMaxDamage();
                    }

                    return 0;
                }
            }, EnumBarType.TIMER, this, (xSize / 2) + 14, (ySize / 2) - 38));
        }
    }

    @Override
    public void drawGuiContainerForegroundLayer(final int x, final int y) {
        fontRendererObj.drawString(tile.getLocalizedName(), (xSize / 2) - (fontRendererObj.getStringWidth(tile.getLocalizedName()) / 2), (ySize / 2) - 102, 0x404040);

        final ItemStack itemStack = tile.getInventory().getStackInSlot(0);
        final FluidStack fluidStack = tile.getTank().getFluid();

        if (itemStack != null || ModFluids.fluidStackPlasma.isFluidEqual(fluidStack)) {
            // Text field for actual heat inside of reactor cell.
            final String meltingPoint = UnitDisplay.getTemperatureDisplay(TileReactorCell.MELTING_POINT);
            final String meltingPointColor = tile.getTemperature() >= TileReactorCell.MELTING_POINT ? EnumColor.DARK_RED.toString() : "";
            final String temperature = UnitDisplay.getTemperatureDisplay(Math.floor(tile.getTemperature()));

            fontRendererObj.drawString(LanguageUtility.transelate("gui.temperature"), (xSize / 2) - 80, 45, 0x404040);
            fontRendererObj.drawString(temperature + "/" + meltingPointColor + meltingPoint, (xSize / 2) - 80, 58, 0x404040);
        }

        if (itemStack != null) {
            // Text field for total number of ticks remaining.
            int secondsLeft = itemStack.getMaxDamage() - itemStack.getMetadata();

            fontRendererObj.drawString(LanguageUtility.transelate("gui.remaining"), (xSize / 2) + 14, 45, 0x404040);
            fontRendererObj.drawString(secondsLeft + "s", (xSize / 2) + 14, 58, 0x404040);
        }

        final List<String> list = LanguageUtility.splitStringPerWord(LanguageUtility.transelate("tile." + Reference.ID + "." + tile.getName() + ".tooltip"), 5);

        for (int i = 0; i < list.size(); i++) {
            fontRendererObj.drawString(list.get(i), (xSize / 2) - 80, 85 + i * 9, 0x404040);
        }

        fontRendererObj.drawString(LanguageUtility.transelate("container.inventory"), (xSize / 2) - 80, (ySize - 96) + 2, 0x404040);

        super.drawGuiContainerForegroundLayer(x, y);
    }
}