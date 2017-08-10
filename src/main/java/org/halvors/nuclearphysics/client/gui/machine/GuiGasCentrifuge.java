package org.halvors.nuclearphysics.client.gui.machine;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.gui.GuiContainerBase;
import org.halvors.nuclearphysics.common.block.machine.BlockMachineModel.EnumMachineModel;
import org.halvors.nuclearphysics.common.container.machine.ContainerGasCentrifuge;
import org.halvors.nuclearphysics.common.tile.machine.TileGasCentrifuge;
import org.halvors.nuclearphysics.common.tile.machine.TileNuclearBoiler;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;
import org.halvors.nuclearphysics.common.utility.energy.UnitDisplay;

@SideOnly(Side.CLIENT)
public class GuiGasCentrifuge extends GuiContainerBase {
    private TileGasCentrifuge tile;

    public GuiGasCentrifuge(InventoryPlayer inventoryPlayer, TileGasCentrifuge tile) {
        super(new ContainerGasCentrifuge(inventoryPlayer, tile));
        
        this.tile = tile;
    }

    /** Draw the foreground layer for the GuiContainer (everything in front of the items) */
    @Override
    public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String name = LanguageUtility.transelate("tile.machine_model." + EnumMachineModel.GAS_CENTRIFUGE.ordinal() + ".name");

        fontRendererObj.drawString(name, (xSize / 2) - (fontRendererObj.getStringWidth(name) / 2), 6, 0x404040);

        String displayText;

        if (tile.timer > 0) {
            displayText = "Processing";
        } else if (tile.canProcess()) {
            displayText = "Ready";
        } else {
            displayText = "Idle";
        }

        fontRendererObj.drawString("Status: " + displayText, 70, 50, 0x404040);

        renderUniversalDisplay(8, 112, TileNuclearBoiler.energy * 20, mouseX, mouseY, UnitDisplay.Unit.WATT);
        //renderUniversalDisplay(100, 112, tile.getVoltageInput(null), mouseX, mouseY, UnitDisplay.Unit.VOLTAGE);

        fontRendererObj.drawString("The centrifuge spins", 8, 75, 0x404040);
        fontRendererObj.drawString("uranium hexafluoride gas into", 8, 85, 0x404040);
        fontRendererObj.drawString("enriched uranium for fission.", 8, 95, 0x404040);

        fontRendererObj.drawString(LanguageUtility.transelate("container.inventory"), 8, ySize - 96 + 2, 0x404040);

        if (isPointInRegion(8, 18, meterWidth, meterHeight, mouseX, mouseY) && tile.getTank().getFluid() != null) {
            drawTooltip(mouseX - guiLeft, mouseY - guiTop + 10, tile.getTank().getFluid().getLocalizedName(), tile.getTank().getFluid().amount + " L");
        }
    }

    /** Draw the background layer for the GuiContainer (everything behind the items) */
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTick, mouseX, mouseY);

        drawSlot(80, 25);
        drawSlot(100, 25);
        drawSlot(130, 25, SlotType.BATTERY);

        drawBar(40, 26, (float) tile.timer / (float) TileGasCentrifuge.tickTime);

        // Uranium Gas
        drawMeter(8, 18, (float) tile.getTank().getFluidAmount() / (float) tile.getTank().getCapacity(), tile.getTank().getFluid());
        drawSlot(24, 49, SlotType.GAS);
    }
}