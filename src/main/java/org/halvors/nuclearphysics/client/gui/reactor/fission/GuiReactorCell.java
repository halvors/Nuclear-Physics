package org.halvors.nuclearphysics.client.gui.reactor.fission;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import org.halvors.nuclearphysics.client.gui.GuiContainerBase;
import org.halvors.nuclearphysics.common.container.reactor.ContainerReactorCell;
import org.halvors.nuclearphysics.common.tile.reactor.TileReactorCell;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;
import org.halvors.nuclearphysics.common.utility.energy.UnitDisplay;
import org.halvors.nuclearphysics.common.utility.energy.UnitDisplay.Unit;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiReactorCell extends GuiContainerBase {
    private TileReactorCell tile;

    public GuiReactorCell(InventoryPlayer inventoryPlayer, TileReactorCell tile) {
        super(new ContainerReactorCell(inventoryPlayer, tile));

        this.tile = tile;
    }

    /** Draw the foreground layer for the GuiContainer (everything in front of the items) */
    @Override
    public void drawGuiContainerForegroundLayer(int x, int y) {
        IItemHandler inventory = tile.getInventory();
        String name = LanguageUtility.transelate("tile." + tile.getName() + ".name");

        fontRendererObj.drawString(name, (xSize / 2) - (fontRendererObj.getStringWidth(name) / 2), 6, 0x404040);

        if (inventory.getStackInSlot(0) != null) {
            // Test field for actual heat inside of reactor cell.
            fontRendererObj.drawString(LanguageUtility.transelate("tooltip.temperature"), 9, 45, 0x404040);
            fontRendererObj.drawString((int) Math.floor(tile.getTemperature()) + "/" + (int) Math.floor(TileReactorCell.meltingPoint) + " K", 9, 58, 0x404040);

            // Text field for total number of ticks remaining.
            int secondsLeft = (inventory.getStackInSlot(0).getMaxDamage() - inventory.getStackInSlot(0).getMetadata());
            fontRendererObj.drawString(LanguageUtility.transelate("tooltip.remainingTime"), 100, 45, 0x404040);
            fontRendererObj.drawString(secondsLeft + " seconds", 100, 58, 0x404040);
        }

        if (isPointInRegion(80, 40, meterWidth, meterHeight, x, y)) {
            if (tile.getTank().getFluid() != null) {
                drawTooltip(x - guiLeft, y - guiTop + 10, tile.getTank().getFluid().getLocalizedName(), UnitDisplay.getDisplay(tile.getTank().getFluidAmount(), Unit.LITER));
            } else {
                drawTooltip(x - guiLeft, y - guiTop + 10, "No Fluid");
            }
        }

        List<String> desc = LanguageUtility.splitStringPerWord(LanguageUtility.transelate("tile." + tile.getName() + ".tooltip"), 5);

        for (int i = 0; i < desc.size(); i++) {
            fontRendererObj.drawString(desc.get(i), 9, 85 + i * 9, 0x404040);
        }
    }

    /** Draw the background layer for the GuiContainer (everything behind the items) */
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int x, int y) {
        super.drawGuiContainerBackgroundLayer(par1, x, y);

        drawSlot(78, 16);
        drawMeter(80, 36, tile.getTank().getFluidAmount() / tile.getTank().getCapacity(), tile.getTank().getFluid());

        IItemHandler inventory = tile.getInventory();

        if (inventory.getStackInSlot(0) != null) {
            // Progress bar of temperature inside of reactor.
            GlStateManager.pushMatrix();
            GlStateManager.translate((32 * 2) + 11, 0, 0);
            GlStateManager.scale(0.5, 1, 1);
            drawForce(20, 70, (tile.getTemperature()) / (TileReactorCell.meltingPoint));
            GlStateManager.popMatrix();

            // Progress bar of remaining burn time on reactor cell.
            GlStateManager.pushMatrix();
            GlStateManager.translate((68 * 2) + 5, 0, 0);
            GlStateManager.scale(0.5F, 1, 1);
            float ticksLeft = (inventory.getStackInSlot(0).getMaxDamage() - inventory.getStackInSlot(0).getMetadata());
            drawElectricity(70, 70, ticksLeft / inventory.getStackInSlot(0).getMaxDamage());
            GlStateManager.popMatrix();
        }
    }
}