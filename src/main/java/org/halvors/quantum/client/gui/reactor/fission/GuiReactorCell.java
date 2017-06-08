package org.halvors.quantum.client.gui.reactor.fission;

import net.minecraft.entity.player.EntityPlayer;
import org.halvors.quantum.common.container.reactor.fission.ContainerReactorCell;
import org.halvors.quantum.common.tile.reactor.fission.TileReactorCell;
import org.halvors.quantum.common.utility.LanguageUtility;
import org.halvors.quantum.lib.gui.GuiContainerBase;
import org.lwjgl.opengl.GL11;
import universalelectricity.api.energy.UnitDisplay;
import universalelectricity.api.energy.UnitDisplay.Unit;

import java.util.List;

public class GuiReactorCell extends GuiContainerBase {
    private TileReactorCell tileEntity;

    public GuiReactorCell(EntityPlayer player, TileReactorCell tileEntity) {
        super(new ContainerReactorCell(player, tileEntity));

        this.tileEntity = tileEntity;
    }

    /** Draw the foreground layer for the GuiContainer (everything in front of the items) */
    @Override
    public void drawGuiContainerForegroundLayer(int x, int y) {
        fontRendererObj.drawString(tileEntity.getInventoryName(), xSize / 2 - fontRendererObj.getStringWidth(tileEntity.getInventoryName()) / 2, 6, 4210752);

        if (tileEntity.getStackInSlot(0) != null) {
            // Test field for actual heat inside of reactor cell.
            fontRendererObj.drawString(LanguageUtility.localize("tooltip.temperature"), 9, 45, 4210752);
            fontRendererObj.drawString(String.valueOf((int) tileEntity.getTemperature()) + "/" + String.valueOf(TileReactorCell.meltingPoint) + " K", 9, 58, 4210752);

            // Text field for total number of ticks remaining.
            int secondsLeft = (tileEntity.getStackInSlot(0).getMaxDurability() - tileEntity.getStackInSlot(0).getMetadata());
            fontRendererObj.drawString(LanguageUtility.localize("tooltip.remainingTime"), 100, 45, 4210752);
            fontRendererObj.drawString(secondsLeft + " seconds", 100, 58, 4210752);
        }

        fontRendererObj.drawString(LanguageUtility.localize("tooltip.remainingTime"), 100, 45, 4210752);

        if (isPointInRegion(80, 40, meterWidth, meterHeight, x, y)) {
            if (tileEntity.tank.getFluid() != null) {
                drawTooltip(x - guiLeft, y - guiTop + 10, tileEntity.tank.getFluid().getFluid().getLocalizedName(), UnitDisplay.getDisplay(tileEntity.tank.getFluidAmount(), Unit.LITER));
            } else {
                drawTooltip(x - guiLeft, y - guiTop + 10, "No Fluid");
            }
        }

        List<String> desc = LanguageUtility.splitStringPerWord(LanguageUtility.localize("tile.reactorCell.tooltip"), 5);

        for (int i = 0; i < desc.size(); i++) {
            fontRendererObj.drawString(desc.get(i), 9, 85 + i * 9, 4210752);
        }
    }

    /** Draw the background layer for the GuiContainer (everything behind the items) */
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int x, int y) {
        super.drawGuiContainerBackgroundLayer(par1, x, y);

        drawSlot(78, 16);
        drawMeter(80, 36, tileEntity.tank.getFluidAmount() / tileEntity.tank.getCapacity(), tileEntity.tank.getFluid());

        if (tileEntity.getStackInSlot(0) != null) {
            // Progress bar of temperature inside of reactor.
            GL11.glPushMatrix();
            GL11.glTranslatef(32 * 2, 0, 0);
            GL11.glScalef(0.5f, 1, 1);
            drawForce(20, 70, (tileEntity.getTemperature()) / (TileReactorCell.meltingPoint));
            GL11.glPopMatrix();

            // Progress bar of remaining burn time on reactor cell.
            GL11.glPushMatrix();
            GL11.glTranslatef(68 * 2, 0, 0);
            GL11.glScalef(0.5f, 1, 1);
            float ticksLeft = (tileEntity.getStackInSlot(0).getMaxDurability() - tileEntity.getStackInSlot(0).getMetadata());
            drawElectricity(70, 70, ticksLeft / tileEntity.getStackInSlot(0).getMaxDurability());
            GL11.glPopMatrix();
        }
    }
}