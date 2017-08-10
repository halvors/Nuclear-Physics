package org.halvors.quantum.client.gui.debug;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.client.gui.GuiContainerBase;
import org.halvors.quantum.client.utility.RenderUtility;
import org.halvors.quantum.common.Quantum;
import org.halvors.quantum.common.block.debug.BlockCreativeBuilder;
import org.halvors.quantum.common.block.machine.BlockMachineModel;
import org.halvors.quantum.common.container.ContainerDummy;
import org.halvors.quantum.common.network.packet.PacketCreativeBuilder;
import org.halvors.quantum.common.utility.LanguageUtility;
import org.halvors.quantum.common.utility.ResourceUtility;
import org.halvors.quantum.common.utility.type.Color;
import org.halvors.quantum.common.utility.type.ResourceType;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiCreativeBuilder extends GuiContainerBase {
    private GuiTextField textFieldSize;
    private int mode = 0;
    private BlockPos pos;

    public ResourceLocation baseTexture;
    protected int containerWidth;
    protected int containerHeight;

    public GuiCreativeBuilder(BlockPos pos) {
        super(new ContainerDummy());

        this.pos = pos;
        this.baseTexture = ResourceUtility.getResource(ResourceType.GUI, "gui_empty.png");
    }

    @Override
    public void initGui() {
        super.initGui();

        textFieldSize = new GuiTextField(0, fontRendererObj, 45, 58, 50, 12);

        buttonList.add(new GuiButton(0, width / 2 - 80, height / 2 - 10, 58, 20, "Build"));
        buttonList.add(new GuiButton(1, width / 2 - 50, height / 2 - 35, 120, 20, "Mode"));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String name = "Creative Builder";

        fontRendererObj.drawString(name, (xSize / 2) - (fontRendererObj.getStringWidth(name) / 2), 6, 0x404040);
        fontRendererObj.drawString("This is a creative only cheat", 9, 20, 0x404040);
        fontRendererObj.drawString("which allows you to auto build", 9, 30, 0x404040);
        fontRendererObj.drawString("structures for testing.", 9, 40, 0x404040);

        fontRendererObj.drawString("Size: ", 9, 60, 0x404040);
        textFieldSize.drawTextBox();

        (buttonList.get(1)).displayString = LanguageUtility.transelate(BlockCreativeBuilder.getSchematic(mode).getName());
        fontRendererObj.drawString("Mode: ", 9, 80, 0x404040);
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        fontRendererObj.drawString("Warning!", 9, 130, Color.DARK_RED.getHex());
        fontRendererObj.drawString("This will replace blocks without", 9, 140, Color.DARK_RED.getHex());
        fontRendererObj.drawString("dropping it! You may lose items.", 9, 150, Color.DARK_RED.getHex());
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
        containerWidth = (width - xSize) / 2;
        containerHeight = (height - ySize) / 2;

        RenderUtility.bindTexture(baseTexture);
        GlStateManager.color(1, 1, 1, 1);

        drawTexturedModalRect(containerWidth, containerHeight, 0, 0, xSize, ySize);
    }

    @Override
    protected void keyTyped(char par1, int par2) throws IOException {
        super.keyTyped(par1, par2);

        textFieldSize.textboxKeyTyped(par1, par2);
    }

    @Override
    protected void mouseClicked(int x, int y, int par3) throws IOException {
        super.mouseClicked(x, y, par3);

        textFieldSize.mouseClicked(x - containerWidth, y - containerHeight, par3);
    }

    @Override
    protected void actionPerformed(GuiButton par1GuiButton) throws IOException {
        super.actionPerformed(par1GuiButton);

        if (par1GuiButton.id == 0) {
            int radius = 0;

            try {
                radius = Integer.parseInt(textFieldSize.getText());
            } catch (Exception e) {

            }

            if (radius > 0) {
                Quantum.getPacketHandler().sendToServer(new PacketCreativeBuilder(pos, mode, radius));
                mc.player.closeScreen();
            }
        } else if (par1GuiButton.id == 1) {
            mode = (mode + 1) % (BlockCreativeBuilder.getSchematicCount());
        }
    }

}