package org.halvors.nuclearphysics.client.gui.reactor;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.gui.GuiComponentContainer;
import org.halvors.nuclearphysics.client.gui.component.GuiBar;
import org.halvors.nuclearphysics.client.gui.component.GuiBar.BarType;
import org.halvors.nuclearphysics.client.gui.component.GuiFluidGauge;
import org.halvors.nuclearphysics.client.gui.component.GuiSlot;
import org.halvors.nuclearphysics.client.gui.component.GuiSlot.SlotType;
import org.halvors.nuclearphysics.common.container.reactor.ContainerReactorCell;
import org.halvors.nuclearphysics.common.tile.reactor.TileReactorCell;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiReactorCell extends GuiComponentContainer<TileReactorCell> {
    public GuiReactorCell(InventoryPlayer inventoryPlayer, TileReactorCell tile) {
        super(tile, new ContainerReactorCell(inventoryPlayer, tile));

        components.add(new GuiSlot(SlotType.NORMAL, this, (xSize / 2) - 10, 16));
        components.add(new GuiFluidGauge(tile::getTank, this, (xSize / 2) - 8, 36));
        components.add(new GuiBar(() -> tile.getTemperature() /  TileReactorCell.meltingPoint, BarType.TEMPERATURE, this, (xSize / 2) - 80, 70));
        components.add(new GuiBar(() -> {
            ItemStack itemStack = tile.getInventory().getStackInSlot(0);

            if (itemStack != null) {
                return (double) (itemStack.getMaxDamage() - itemStack.getMetadata()) / itemStack.getMaxDamage();
            }

            return 0;
        }, BarType.TIMER, this, (xSize / 2) + 14, 70));
    }

    @Override
    public void drawGuiContainerForegroundLayer(int x, int y) {
        fontRendererObj.drawString(tile.getLocalizedName(), (xSize / 2) - (fontRendererObj.getStringWidth(tile.getLocalizedName()) / 2), 6, 0x404040);

        ItemStack itemStack = tile.getInventory().getStackInSlot(0);

        if (itemStack != null) {
            // Test field for actual heat inside of reactor cell.
            fontRendererObj.drawString(LanguageUtility.transelate("tooltip.temperature"), (xSize / 2) - 80, 45, 0x404040);
            fontRendererObj.drawString((int) Math.floor(tile.getTemperature()) + "/" + (int) Math.floor(TileReactorCell.meltingPoint) + " K", (xSize / 2) - 80, 58, 0x404040);

            // Text field for total number of ticks remaining.
            int secondsLeft = itemStack.getMaxDamage() - itemStack.getMetadata();
            fontRendererObj.drawString(LanguageUtility.transelate("tooltip.remainingTime"), (xSize / 2) + 14, 45, 0x404040);
            fontRendererObj.drawString(secondsLeft + " seconds", (xSize / 2) + 14, 58, 0x404040);
        }

        List<String> list = LanguageUtility.splitStringPerWord(LanguageUtility.transelate("tile." + tile.getName() + ".tooltip"), 5);

        for (int i = 0; i < list.size(); i++) {
            fontRendererObj.drawString(list.get(i), (xSize / 2) - 80, 85 + i * 9, 0x404040);
        }

        fontRendererObj.drawString(LanguageUtility.transelate("container.inventory"), (xSize / 2) - 80, (ySize - 96) + 2, 0x404040);

        super.drawGuiContainerForegroundLayer(x, y);
    }
}