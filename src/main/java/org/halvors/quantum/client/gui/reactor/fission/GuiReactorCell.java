package org.halvors.quantum.client.gui.reactor.fission;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.client.gui.GuiContainerBase;
import org.halvors.quantum.common.block.machine.BlockMachine;
import org.halvors.quantum.common.container.reactor.fission.ContainerReactorCell;
import org.halvors.quantum.common.tile.reactor.fission.TileReactorCell;
import org.halvors.quantum.common.utility.LanguageUtility;
import org.halvors.quantum.common.utility.energy.UnitDisplay;
import org.halvors.quantum.common.utility.energy.UnitDisplay.Unit;
import org.lwjgl.opengl.GL11;

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
        String name = LanguageUtility.localize("tile.reactor_cell.name");

        fontRendererObj.drawString(name, (xSize / 2) - (fontRendererObj.getStringWidth(name) / 2), 6, 0x404040);

        if (tile.getInventory().getStackInSlot(0) != null) {
            // Test field for actual heat inside of reactor cell.
            fontRendererObj.drawString(LanguageUtility.localize("tooltip.temperature"), 9, 45, 0x404040);
            fontRendererObj.drawString(String.valueOf((int) tile.getTemperature()) + "/" + String.valueOf(TileReactorCell.meltingPoint) + " K", 9, 58, 0x404040);

            // Text field for total number of ticks remaining.
            int secondsLeft = (tile.getInventory().getStackInSlot(0).getMaxDamage() - tile.getInventory().getStackInSlot(0).getMetadata());
            fontRendererObj.drawString(LanguageUtility.localize("tooltip.remainingTime"), 100, 45, 0x404040);
            fontRendererObj.drawString(secondsLeft + " seconds", 100, 58, 0x404040);
        }

        if (isPointInRegion(80, 40, meterWidth, meterHeight, x, y)) {
            if (tile.tank.getFluid() != null) {
                drawTooltip(x - guiLeft, y - guiTop + 10, tile.tank.getFluid().getLocalizedName(), UnitDisplay.getDisplay(tile.tank.getFluidAmount(), Unit.LITER));
            } else {
                drawTooltip(x - guiLeft, y - guiTop + 10, "No Fluid");
            }
        }

        List<String> desc = LanguageUtility.splitStringPerWord(LanguageUtility.localize("tile.reactorCell.tooltip"), 5);

        for (int i = 0; i < desc.size(); i++) {
            fontRendererObj.drawString(desc.get(i), 9, 85 + i * 9, 0x404040);
        }
    }

    /** Draw the background layer for the GuiContainer (everything behind the items) */
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int x, int y) {
        super.drawGuiContainerBackgroundLayer(par1, x, y);

        drawSlot(78, 16);
        drawMeter(80, 36, tile.tank.getFluidAmount() / tile.tank.getCapacity(), tile.tank.getFluid());

        if (tile.getInventory().getStackInSlot(0) != null) {
            // Progress bar of temperature inside of reactor.
            GL11.glPushMatrix();
            GL11.glTranslatef(32 * 2, 0, 0);
            GL11.glScalef(0.5f, 1, 1);
            drawForce(20, 70, (tile.getTemperature()) / (TileReactorCell.meltingPoint));
            GL11.glPopMatrix();

            // Progress bar of remaining burn time on reactor cell.
            GL11.glPushMatrix();
            GL11.glTranslatef(68 * 2, 0, 0);
            GL11.glScalef(0.5F, 1, 1);
            float ticksLeft = (tile.getInventory().getStackInSlot(0).getMaxDamage() - tile.getInventory().getStackInSlot(0).getMetadata());
            drawElectricity(70, 70, ticksLeft / tile.getInventory().getStackInSlot(0).getMaxDamage());
            GL11.glPopMatrix();
        }
    }
}