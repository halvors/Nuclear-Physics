package org.halvors.quantum.client.gui.machine;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.client.gui.GuiContainerBase;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.block.machine.BlockMachineModel.EnumMachineModel;
import org.halvors.quantum.common.container.machine.ContainerQuantumAssembler;
import org.halvors.quantum.common.tile.machine.TileQuantumAssembler;
import org.halvors.quantum.common.utility.LanguageUtility;
import org.halvors.quantum.common.utility.energy.UnitDisplay;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiQuantumAssembler extends GuiContainerBase {
    public static final ResourceLocation texture = new ResourceLocation(Reference.PREFIX + "textures/gui/gui_atomic_assembler.png");

    private TileQuantumAssembler tile;

    public GuiQuantumAssembler(InventoryPlayer inventoryPlayer, TileQuantumAssembler tile) {
        super(new ContainerQuantumAssembler(inventoryPlayer, tile));

        this.tile = tile;

        ySize = 230;
    }

    /** Draw the foreground layer for the GuiContainer (everything in front of the items) */
    @Override
    public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String name = LanguageUtility.transelate("tile.machine_model." + EnumMachineModel.QUANTUM_ASSEMBLER.ordinal() + ".name");

        fontRenderer.drawString(name, (xSize / 2) - (fontRenderer.getStringWidth(name) / 2), 6, 0x404040);

        String displayText;

        if (tile.timer > 0) {
            displayText = "Process: " + (int) (100 - ((float) tile.timer / (float) TileQuantumAssembler.tickTime) * 100) + "%";
        } else if (tile.canProcess()) {
            displayText = "Ready";
        } else {
            displayText = "Idle";
        }

        fontRenderer.drawString(displayText, 9, ySize - 106, 4210752);
        //renderUniversalDisplay(100, ySize - 94, tile.getVoltageInput(null), mouseX, mouseY, UnitDisplay.Unit.VOLTAGE);
        renderUniversalDisplay(8, ySize - 95, TileQuantumAssembler.tickTime, mouseX, mouseY, UnitDisplay.Unit.WATT);
    }

    /** Draw the background layer for the GuiContainer (everything behind the items) */
    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY) {
        mc.renderEngine.bindTexture(texture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        int containerWidth = (width - xSize) / 2;
        int containerHeight = (height - ySize) / 2;

        drawTexturedModalRect(containerWidth, containerHeight, 0, 0, xSize, ySize);
    }
}