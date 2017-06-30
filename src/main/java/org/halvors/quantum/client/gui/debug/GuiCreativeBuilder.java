package org.halvors.quantum.client.gui.debug;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.client.gui.GuiContainerBase;
import org.halvors.quantum.client.utility.render.RenderUtility;
import org.halvors.quantum.common.block.debug.BlockCreativeBuilder;
import org.halvors.quantum.common.container.ContainerDummy;
import org.halvors.quantum.common.network.packet.PacketCreativeBuilder;
import org.halvors.quantum.common.utility.LanguageUtility;
import org.halvors.quantum.common.utility.ResourceUtility;
import org.halvors.quantum.common.utility.location.Location;
import org.halvors.quantum.common.utility.transform.vector.Vector3;
import org.halvors.quantum.common.utility.type.ResourceType;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiCreativeBuilder extends GuiContainerBase {
    private GuiTextField textFieldSize;
    private int mode = 0;
    private Vector3 position;

    public ResourceLocation baseTexture;
    protected int containerWidth;
    protected int containerHeight;

    public GuiCreativeBuilder(Vector3 position) {
        super(new ContainerDummy());

        this.position = position;
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
        fontRendererObj.drawString("Creative Builder", 60, 6, 4210752);
        fontRendererObj.drawString("This is a creative only cheat", 9, 20, 4210752);
        fontRendererObj.drawString("which allows you to auto build", 9, 30, 4210752);
        fontRendererObj.drawString("structures for testing.", 9, 40, 4210752);

        fontRendererObj.drawString("Size: ", 9, 60, 4210752);
        textFieldSize.drawTextBox();

        ((GuiButton) buttonList.get(1)).displayString = LanguageUtility.localize(BlockCreativeBuilder.getSchematic(mode).getName());
        fontRendererObj.drawString("Mode: ", 9, 80, 4210752);
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        fontRendererObj.drawString("Warning!", 9, 130, 4210752);
        fontRendererObj.drawString("This will replace blocks without", 9, 140, 4210752);
        fontRendererObj.drawString("dropping it! You may lose items.", 9, 150, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
        containerWidth = (width - xSize) / 2;
        containerHeight = (height - ySize) / 2;

        RenderUtility.bind(baseTexture);
        GL11.glColor4f(1, 1, 1, 1);

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
                Quantum.getPacketHandler().sendToServer(new PacketCreativeBuilder(new Location(0, (int) position.x, (int) position.y, (int) position.z), mode, radius));
                mc.player.closeScreen();
            }
        } else if (par1GuiButton.id == 1) {
            mode = (mode + 1) % (BlockCreativeBuilder.getSchematicCount());
        }
    }

}