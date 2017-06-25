package org.halvors.quantum.client.gui.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;
import org.halvors.quantum.client.gui.GuiContainerBase;
import org.halvors.quantum.common.container.machine.ContainerNuclearBoiler;
import org.halvors.quantum.common.tile.machine.TileNuclearBoiler;
import universalelectricity.api.energy.UnitDisplay;

@SideOnly(Side.CLIENT)
public class GuiNuclearBoiler extends GuiContainerBase {
    private TileNuclearBoiler tile;

    public GuiNuclearBoiler(InventoryPlayer inventoryPlayer, TileNuclearBoiler tile) {
        super(new ContainerNuclearBoiler(inventoryPlayer, tile));

        this.tile = tile;
    }

    /** Draw the foreground layer for the GuiContainer (everything in front of the items) */
    @Override
    public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRendererObj.drawString(tile.getInventoryName(), 52, 6, 4210752);

        renderUniversalDisplay(8, 112, TileNuclearBoiler.energy * 20, mouseX, mouseY, UnitDisplay.Unit.WATT);
        //renderUniversalDisplay(110, 112, tile.getVoltageInput(null), mouseX, mouseY, UnitDisplay.Unit.VOLTAGE);

        fontRendererObj.drawString("The nuclear boiler can boil", 8, 75, 4210752);
        fontRendererObj.drawString("yellow cake into uranium", 8, 85, 4210752);
        fontRendererObj.drawString("hexafluoride gas to be refined.", 8, 95, 4210752);

        fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 4210752);

        if (isPointInRegion(8, 18, meterWidth, meterHeight, mouseX, mouseY) && tile.waterTank.getFluid() != null) {
            drawTooltip(mouseX - guiLeft, mouseY - guiTop + 10, tile.waterTank.getFluid().getLocalizedName(), tile.waterTank.getFluid().amount + " L");
        } else if (isPointInRegion(155, 18, meterWidth, meterHeight, mouseX, mouseY) && tile.gasTank.getFluid() != null) {
            drawTooltip(mouseX - guiLeft, mouseY - guiTop + 10, tile.gasTank.getFluid().getLocalizedName(), tile.gasTank.getFluid().amount + " L");
        }
    }

    /** Draw the background layer for the GuiContainer (everything behind the items) */
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTick, mouseX, mouseY);

        drawSlot(55, 25, SlotType.BATTERY);
        drawSlot(80, 25);

        drawBar(110, 26, (float) tile.timer / (float) TileNuclearBoiler.tickTime);

        // Water
        drawMeter(8, 18, (float) tile.waterTank.getFluidAmount() / (float) tile.waterTank.getCapacity(), tile.waterTank.getFluid());
        drawSlot(24, 18, SlotType.LIQUID);
        drawSlot(24, 49, SlotType.LIQUID);

        // Uranium Gas
        drawMeter(155, 18, (float) tile.gasTank.getFluidAmount() / (float) tile.gasTank.getCapacity(), tile.gasTank.getFluid());
        drawSlot(134, 49, SlotType.GAS);
    }
}