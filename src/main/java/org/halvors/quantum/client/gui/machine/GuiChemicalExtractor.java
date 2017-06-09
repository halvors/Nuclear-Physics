package org.halvors.quantum.client.gui.machine;


import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;
import org.halvors.quantum.common.container.machine.ContainerChemicalExtractor;
import org.halvors.quantum.common.tile.machine.TileChemicalExtractor;
import org.halvors.quantum.lib.gui.GuiContainerBase;
import universalelectricity.api.energy.UnitDisplay;

public class GuiChemicalExtractor extends GuiContainerBase {
    private TileChemicalExtractor tile;

    public GuiChemicalExtractor(InventoryPlayer inventoryPlayer, TileChemicalExtractor tile) {
        super(new ContainerChemicalExtractor(inventoryPlayer, tile));

        this.tile = tile;
    }

    /** Draw the foreground layer for the GuiContainer (everything in front of the items) */
    @Override
    public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRendererObj.drawString(tile.getInventoryName(), 45, 6, 4210752);

        renderUniversalDisplay(8, 112, TileChemicalExtractor.ENERGY * 20, mouseX, mouseY, UnitDisplay.Unit.WATT);
        //renderUniversalDisplay(100, 112, tile.getVoltageInput(null), mouseX, mouseY, UnitDisplay.Unit.VOLTAGE);

        fontRendererObj.drawString("The extractor can extract", 8, 75, 4210752);
        fontRendererObj.drawString("uranium, deuterium and tritium.", 8, 85, 4210752);
        fontRendererObj.drawString("Place them in the input slot.", 8, 95, 4210752);

        fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, 4210752);

        if (isPointInRegion(8, 18, meterWidth, meterHeight, mouseX, mouseY) && tile.inputTank.getFluid() != null) {
            if (tile.inputTank.getFluid() != null) {
                drawTooltip(mouseX - guiLeft, mouseY - guiTop + 10, tile.inputTank.getFluid().getFluid().getLocalizedName(), tile.inputTank.getFluid().amount + " L");
            }
        }

        if (isPointInRegion(154, 18, meterWidth, meterHeight, mouseX, mouseY) && tile.outputTank.getFluid() != null) {
            if (tile.outputTank.getFluid() != null) {
                drawTooltip(mouseX - guiLeft, mouseY - guiTop + 10, tile.outputTank.getFluid().getFluid().getLocalizedName(), tile.outputTank.getFluid().amount + " L");
            }
        }

        if (isPointInRegion(134, 49, 18, 18, mouseX, mouseY)) {
            if (tile.getStackInSlot(4) == null) {
                // drawTooltip(x - guiLeft, y - guiTop + 10, "Place empty cells.");
            }
        }

        if (isPointInRegion(52, 24, 18, 18, mouseX, mouseY)) {
            if (tile.outputTank.getFluidAmount() > 0 && tile.getStackInSlot(3) == null) {
                drawTooltip(mouseX - guiLeft, mouseY - guiTop + 10, "Input slot");
            }
        }
    }

    /** Draw the background layer for the GuiContainer (everything behind the items) */
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTick, mouseX, mouseY);

        drawSlot(79, 49, SlotType.BATTERY);
        drawSlot(52, 24);
        drawSlot(106, 24);
        drawBar(75, 24, (float) tile.time / (float) TileChemicalExtractor.TICK_TIME);
        drawMeter(8, 18, (float) tile.inputTank.getFluidAmount() / (float) tile.inputTank.getCapacity(), tile.inputTank.getFluid());
        drawSlot(24, 18, SlotType.LIQUID);
        drawSlot(24, 49, SlotType.LIQUID);
        drawMeter(154, 18, (float) tile.outputTank.getFluidAmount() / (float) tile.outputTank.getCapacity(), tile.outputTank.getFluid());
        drawSlot(134, 18, SlotType.LIQUID);
        drawSlot(134, 49, SlotType.LIQUID);
    }
}