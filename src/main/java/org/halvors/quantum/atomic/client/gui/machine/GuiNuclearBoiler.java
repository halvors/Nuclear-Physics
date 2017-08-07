package org.halvors.quantum.atomic.client.gui.machine;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.atomic.client.gui.GuiContainerBase;
import org.halvors.quantum.atomic.common.block.machine.BlockMachineModel.EnumMachineModel;
import org.halvors.quantum.atomic.common.container.machine.ContainerNuclearBoiler;
import org.halvors.quantum.atomic.common.tile.machine.TileNuclearBoiler;
import org.halvors.quantum.atomic.common.utility.LanguageUtility;
import org.halvors.quantum.atomic.common.utility.energy.UnitDisplay;

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
        String name = LanguageUtility.localize("tile.machine_model." + EnumMachineModel.NUCLEAR_BOILER.ordinal() + ".name");

        fontRendererObj.drawString(name, (xSize / 2) - (fontRendererObj.getStringWidth(name) / 2), 6, 0x404040);

        renderUniversalDisplay(8, 112, TileNuclearBoiler.energy * 20, mouseX, mouseY, UnitDisplay.Unit.WATT);
        //renderUniversalDisplay(110, 112, tile.getVoltageInput(null), mouseX, mouseY, UnitDisplay.Unit.VOLTAGE);

        fontRendererObj.drawString("The nuclear boiler can boil", 8, 75, 0x404040);
        fontRendererObj.drawString("yellow cake into uranium", 8, 85, 0x404040);
        fontRendererObj.drawString("hexafluoride gas to be refined.", 8, 95, 0x404040);

        fontRendererObj.drawString(I18n.translateToLocal("container.inventory"), 8, this.ySize - 96 + 2, 0x404040);

        if (isPointInRegion(8, 18, meterWidth, meterHeight, mouseX, mouseY) && tile.getInputTank().getFluid() != null) {
            drawTooltip(mouseX - guiLeft, mouseY - guiTop + 10, tile.getInputTank().getFluid().getLocalizedName(), tile.getInputTank().getFluid().amount + " L");
        } else if (isPointInRegion(155, 18, meterWidth, meterHeight, mouseX, mouseY) && tile.getOutputTank().getFluid() != null) {
            drawTooltip(mouseX - guiLeft, mouseY - guiTop + 10, tile.getOutputTank().getFluid().getLocalizedName(), tile.getOutputTank().getFluid().amount + " L");
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
        drawMeter(8, 18, (float) tile.getInputTank().getFluidAmount() / (float) tile.getInputTank().getCapacity(), tile.getInputTank().getFluid());
        drawSlot(24, 18, SlotType.LIQUID);
        drawSlot(24, 49, SlotType.LIQUID);

        // Uranium Gas
        drawMeter(155, 18, (float) tile.getOutputTank().getFluidAmount() / (float) tile.getOutputTank().getCapacity(), tile.getOutputTank().getFluid());
        drawSlot(134, 49, SlotType.GAS);
    }
}