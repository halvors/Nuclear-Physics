package org.halvors.quantum.client.gui.machine;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.container.machine.ContainerQuantumAssembler;
import org.halvors.quantum.common.tile.machine.TileQuantumAssembler;
import org.halvors.quantum.lib.gui.GuiContainerBase;
import org.lwjgl.opengl.GL11;
import universalelectricity.api.energy.UnitDisplay;

public class GuiQuantumAssembler extends GuiContainerBase {
    public static final ResourceLocation texture = new ResourceLocation(Reference.PREFIX + "textures/gui/gui_atomic_assembler.png");

    private TileQuantumAssembler tileEntity;

    private int containerWidth;
    private int containerHeight;

    public GuiQuantumAssembler(InventoryPlayer par1InventoryPlayer, TileQuantumAssembler tileEntity) {
        super(new ContainerQuantumAssembler(par1InventoryPlayer, tileEntity));

        this.tileEntity = tileEntity;
        ySize = 230;
    }

    /** Draw the foreground layer for the GuiContainer (everything in front of the items) */
    @Override
    public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRendererObj.drawString(tileEntity.getInventoryName(), 65 - tileEntity.getInventoryName().length(), 6, 4210752);

        String displayText;

        if (tileEntity.time > 0) {
            displayText = "Process: " + (int) (100 - ((float) tileEntity.time / (float) tileEntity.maxTime) * 100) + "%";
        } else if (tileEntity.canProcess()) {
            displayText = "Ready";
        } else {
            displayText = "Idle";
        }

        fontRendererObj.drawString(displayText, 9, ySize - 106, 4210752);
        renderUniversalDisplay(100, ySize - 94, tileEntity.getVoltageInput(null), mouseX, mouseY, UnitDisplay.Unit.VOLTAGE);
        renderUniversalDisplay(8, ySize - 95, tileEntity.maxTime, mouseX, mouseY, UnitDisplay.Unit.WATT);
    }

    /** Draw the background layer for the GuiContainer (everything behind the items) */
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY) {
        mc.renderEngine.bindTexture(texture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        containerWidth = (width - xSize) / 2;
        containerHeight = (height - ySize) / 2;

        drawTexturedModalRect(containerWidth, containerHeight, 0, 0, xSize, ySize);
    }
}