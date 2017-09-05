package org.halvors.nuclearphysics.client.gui.modular;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.gui.modular.component.GuiFluidGauge;
import org.halvors.nuclearphysics.client.gui.modular.component.GuiProgress;
import org.halvors.nuclearphysics.client.gui.modular.component.GuiSlot;
import org.halvors.nuclearphysics.client.gui.modular.component.GuiSlot.SlotType;
import org.halvors.nuclearphysics.client.sound.SoundHandler;
import org.halvors.nuclearphysics.client.utility.RenderUtility;
import org.halvors.nuclearphysics.common.block.machine.BlockMachineModel;
import org.halvors.nuclearphysics.common.container.machine.ContainerChemicalExtractor;
import org.halvors.nuclearphysics.common.tile.machine.TileChemicalExtractor;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;
import org.halvors.nuclearphysics.common.utility.type.ResourceType;

import java.io.IOException;
import java.util.ArrayList;

@SideOnly(Side.CLIENT)
public class GuiChemicalExtractor extends GuiComponentContainer {
    private TileChemicalExtractor tile;

    public GuiChemicalExtractor(InventoryPlayer inventory, TileChemicalExtractor tile) {
        super(tile, new ContainerChemicalExtractor(inventory, tile));

        this.tile = tile;

        /*
        drawSlot(79, 49, GuiContainerBase.SlotType.BATTERY);
        drawSlot(52, 24);
        drawSlot(106, 24);
        drawBar(75, 24, (float) tile.timer / (float) TileChemicalExtractor.tickTime);
        drawMeter(8, 18, tile.getInputTank().getFluidAmount() / tile.getInputTank().getCapacity(), tile.getInputTank().getFluid());
        drawSlot(24, 18, GuiContainerBase.SlotType.LIQUID);
        drawSlot(24, 49, GuiContainerBase.SlotType.LIQUID);
        drawMeter(154, 18, tile.getOutputTank().getFluidAmount() / tile.getOutputTank().getCapacity(), tile.getOutputTank().getFluid());
        drawSlot(134, 18, GuiContainerBase.SlotType.LIQUID);
        drawSlot(134, 49, GuiContainerBase.SlotType.LIQUID);
        */

        components.add(new GuiSlot(SlotType.BATTERY, this, 79, 49));
        components.add(new GuiSlot(this, 52, 24));
        components.add(new GuiSlot(this, 106, 24));
        components.add(new GuiProgress(this, 75, 24, tile.timer / TileChemicalExtractor.tickTime));
        // TODO: Set fixed fluid here.
        components.add(new GuiFluidGauge(tile::getInputTank, this, 8, 18));
        components.add(new GuiSlot(SlotType.LIQUID, this, 24, 18));
        components.add(new GuiSlot(SlotType.LIQUID, this, 24, 49));
        components.add(new GuiFluidGauge(tile::getOutputTank, this, 154, 18));
        components.add(new GuiSlot(SlotType.LIQUID, this, 134, 18));
        components.add(new GuiSlot(SlotType.LIQUID, this, 134, 49));
    }

    @Override
    protected void mouseClicked(int x, int y, int button) throws IOException {
        super.mouseClicked(x, y, button);

        int xAxis = (x - (width - xSize) / 2);
        int yAxis = (y - (height - ySize) / 2);

        if (xAxis > 8 && xAxis < 17 && yAxis > 73 && yAxis < 82) {
            ArrayList data = new ArrayList();
            data.add((byte) 0);

            //Mekanism.packetHandler.sendToServer(new TileEntityMessage(Coord4D.get(tileEntity), data));
            SoundHandler.playSound(SoundEvents.UI_BUTTON_CLICK);
        } else if (xAxis > 160 && xAxis < 169 && yAxis > 73 && yAxis < 82) {
            ArrayList data = new ArrayList();
            data.add((byte) 1);

            //Mekanism.packetHandler.sendToServer(new TileEntityMessage(Coord4D.get(tileEntity), data));
            SoundHandler.playSound(SoundEvents.UI_BUTTON_CLICK);
        }
    }

    /** Draw the foreground layer for the GuiContainer (everything in front of the items) */
    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String name = LanguageUtility.transelate("tile.machine_model." + BlockMachineModel.EnumMachineModel.CHEMICAL_EXTRACTOR.ordinal() + ".name");

        fontRendererObj.drawString(name, (xSize / 2) - (fontRendererObj.getStringWidth(name) / 2), 6, 0x404040);

        //renderUniversalDisplay(8, 112, TileChemicalExtractor.energy * 20, mouseX, mouseY, UnitDisplay.Unit.WATT);
        //renderUniversalDisplay(100, 112, tile.getVoltageInput(null), mouseX, mouseY, UnitDisplay.Unit.VOLTAGE);

        // TODO: Transelate this.
        fontRendererObj.drawString("The extractor can extract", 8, 75, 0x404040);
        fontRendererObj.drawString("uranium, deuterium and tritium.", 8, 85, 0x404040);
        fontRendererObj.drawString("Place them in the input slot.", 8, 95, 0x404040);

        fontRendererObj.drawString(LanguageUtility.transelate("container.inventory"), 8, ySize - 96 + 2, 0x404040);

        /*
        if (isPointInRegion(8, 18, meterWidth, meterHeight, mouseX, mouseY)) {
            if (tile.getInputTank().getFluid() != null) {
                drawTooltip(mouseX - guiLeft, mouseY - guiTop + 10, tile.getInputTank().getFluid().getLocalizedName(), tile.getInputTank().getFluid().amount + " L");
            } else {
                drawTooltip(mouseX - guiLeft, mouseY - guiTop + 10, "No Fluid"); // TODO: Localize this.
            }
        }


        if (isPointInRegion(154, 18, meterWidth, meterHeight, mouseX, mouseY)) {
            if (tile.getOutputTank().getFluid() != null) {
                drawTooltip(mouseX - guiLeft, mouseY - guiTop + 10, tile.getOutputTank().getFluid().getLocalizedName(), tile.getOutputTank().getFluid().amount + " L");
            } else {
                drawTooltip(mouseX - guiLeft, mouseY - guiTop + 10, "No Fluid"); // TODO: Localize this.
            }
        }

        if (isPointInRegion(134, 49, 18, 18, mouseX, mouseY)) {
            if (tile.getInventory().getStackInSlot(4) == null) {
                // drawTooltip(x - guiLeft, y - guiTop + 10, "Place empty cells.");
            }
        }

        if (isPointInRegion(52, 24, 18, 18, mouseX, mouseY)) {
            if (tile.getOutputTank().getFluidAmount() > 0 && tile.getInventory().getStackInSlot(3) == null) {
                drawTooltip(mouseX - guiLeft, mouseY - guiTop + 10, "Input slot");
            }
        }
        */

        super.drawGuiContainerForegroundLayer(mouseX, mouseY);
    }

    /** Draw the background layer for the GuiContainer (everything behind the items) */
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY) {
        RenderUtility.bindTexture(ResourceUtility.getResource(ResourceType.GUI, "gui_base.png"));

        GlStateManager.color(1, 1, 1, 1);

        int guiWidth = (width - xSize) / 2;
        int guiHeight = (height - ySize) / 2;

        drawTexturedModalRect(guiWidth, guiHeight, 0, 0, xSize, ySize);

        super.drawGuiContainerBackgroundLayer(partialTick, mouseX, mouseY);
    }
}
