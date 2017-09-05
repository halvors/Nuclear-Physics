package org.halvors.nuclearphysics.client.gui.reactor;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.gui.GuiComponentContainer;
import org.halvors.nuclearphysics.client.gui.component.GuiFluidGauge;
import org.halvors.nuclearphysics.client.gui.component.GuiSlot;
import org.halvors.nuclearphysics.common.container.reactor.ContainerReactorCell;
import org.halvors.nuclearphysics.common.tile.reactor.TileReactorCell;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiReactorCell extends GuiComponentContainer<TileReactorCell> {
    public GuiReactorCell(InventoryPlayer inventoryPlayer, TileReactorCell tile) {
        super(tile, new ContainerReactorCell(inventoryPlayer, tile));

        components.add(new GuiSlot(GuiSlot.SlotType.NORMAL, this, 78, 16));
        components.add(new GuiFluidGauge(tile::getTank, this, 80, 36));
    }

    @Override
    public void drawGuiContainerForegroundLayer(int x, int y) {
        fontRendererObj.drawString(tile.getLocalizedName(), (xSize / 2) - (fontRendererObj.getStringWidth(tile.getLocalizedName()) / 2), 6, 0x404040);

        ItemStack itemStack = tile.getInventory().getStackInSlot(0);

        if (itemStack != null) {
            // Test field for actual heat inside of reactor cell.
            fontRendererObj.drawString(LanguageUtility.transelate("tooltip.temperature"), 9, 45, 0x404040);
            fontRendererObj.drawString((int) Math.floor(tile.getTemperature()) + "/" + (int) Math.floor(TileReactorCell.meltingPoint) + " K", 9, 58, 0x404040);

            // Text field for total number of ticks remaining.
            int secondsLeft = (itemStack.getMaxDamage() - itemStack.getMetadata());
            fontRendererObj.drawString(LanguageUtility.transelate("tooltip.remainingTime"), 100, 45, 0x404040);
            fontRendererObj.drawString(secondsLeft + " seconds", 100, 58, 0x404040);
        }

        List<String> list = LanguageUtility.splitStringPerWord(LanguageUtility.transelate("tile." + tile.getName() + ".tooltip"), 5);

        for (int i = 0; i < list.size(); i++) {
            fontRendererObj.drawString(list.get(i), 9, 85 + i * 9, 0x404040);
        }

        super.drawGuiContainerForegroundLayer(x, y);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int x, int y) {
        ItemStack itemStack = tile.getInventory().getStackInSlot(0);

        if (itemStack != null) {
            // Progress bar of temperature inside of reactor.
            GlStateManager.pushMatrix();
            GlStateManager.translate((32 * 2) + 11, 0, 0);
            GlStateManager.scale(0.5, 1, 1);
            //drawForce(20, 70, (tile.getTemperature()) / (TileReactorCell.meltingPoint));
            GlStateManager.popMatrix();

            // Progress bar of remaining burn time on reactor cell.
            GlStateManager.pushMatrix();
            GlStateManager.translate((68 * 2) + 5, 0, 0);
            GlStateManager.scale(0.5F, 1, 1);
            float ticksLeft = (itemStack.getMaxDamage() - itemStack.getMetadata());
            //drawElectricity(70, 70, ticksLeft / inventory.getStackInSlot(0).getMaxDamage());
            GlStateManager.popMatrix();
        }

        super.drawGuiContainerBackgroundLayer(partialTick, x, y);
    }
}