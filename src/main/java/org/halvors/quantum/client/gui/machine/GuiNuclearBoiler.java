package org.halvors.quantum.client.gui.machine;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;
import org.halvors.quantum.common.container.machine.ContainerNuclearBoiler;
import org.halvors.quantum.common.tile.machine.TileNuclearBoiler;
import org.halvors.quantum.lib.gui.GuiContainerBase;
import universalelectricity.api.energy.UnitDisplay;

public class GuiNuclearBoiler extends GuiContainerBase {
    private TileNuclearBoiler tileEntity;

    public GuiNuclearBoiler(InventoryPlayer inventoryPlayer, TileNuclearBoiler tileEntity) {
        super(new ContainerNuclearBoiler(inventoryPlayer, tileEntity));

        this.tileEntity = tileEntity;
    }

    /** Draw the foreground layer for the GuiContainer (everything in front of the items) */
    @Override
    public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRendererObj.drawString(tileEntity.getInventoryName(), 52, 6, 4210752);

        renderUniversalDisplay(8, 112, TileNuclearBoiler.DIAN * 20, mouseX, mouseY, UnitDisplay.Unit.WATT);
        renderUniversalDisplay(110, 112, this.tileEntity.getVoltageInput(null), mouseX, mouseY, UnitDisplay.Unit.VOLTAGE);

        fontRendererObj.drawString("The nuclear boiler can boil", 8, 75, 4210752);
        fontRendererObj.drawString("yellow cake into uranium", 8, 85, 4210752);
        fontRendererObj.drawString("hexafluoride gas to be refined.", 8, 95, 4210752);

        fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);

        if (isPointInRegion(8, 18, meterWidth, meterHeight, mouseX, mouseY) && tileEntity.waterTank.getFluid() != null) {
            drawTooltip(mouseX - guiLeft, mouseY - guiTop + 10, tileEntity.waterTank.getFluid().getFluid().getLocalizedName(), tileEntity.waterTank.getFluid().amount + " L");
        } else if (isPointInRegion(155, 18, meterWidth, meterHeight, mouseX, mouseY) && tileEntity.gasTank.getFluid() != null) {
            drawTooltip(mouseX - guiLeft, mouseY - guiTop + 10, tileEntity.gasTank.getFluid().getFluid().getLocalizedName(), tileEntity.gasTank.getFluid().amount + " L");
        }
    }

    /** Draw the background layer for the GuiContainer (everything behind the items) */
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTick, mouseX, mouseY);

        drawSlot(55, 25, SlotType.BATTERY);
        drawSlot(80, 25);

        drawBar(110, 26, (float) tileEntity.timer / (float) tileEntity.SHI_JIAN);

        // Water
        drawMeter(8, 18, (float) tileEntity.waterTank.getFluidAmount() / (float) tileEntity.waterTank.getCapacity(), tileEntity.waterTank.getFluid());
        drawSlot(24, 49, SlotType.LIQUID);

        // Uranium Gas
        drawMeter(155, 18, (float) tileEntity.gasTank.getFluidAmount() / (float) tileEntity.gasTank.getCapacity(), tileEntity.gasTank.getFluid());
        drawSlot(135, 49, SlotType.GAS);
    }
}