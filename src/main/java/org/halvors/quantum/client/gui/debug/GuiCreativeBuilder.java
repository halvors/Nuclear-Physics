package org.halvors.quantum.client.gui.debug;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.block.debug.BlockCreativeBuilder;
import org.halvors.quantum.common.container.ContainerDummy;
import org.halvors.quantum.common.network.packet.PacketCreativeBuilder;
import org.halvors.quantum.common.transform.vector.Vector3;
import org.halvors.quantum.common.utility.LanguageUtility;
import org.halvors.quantum.common.utility.location.Location;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class GuiCreativeBuilder extends GuiContainer {
    private GuiTextField textFieldSize;
    private int mode = 0;
    private Vector3 position;

    public ResourceLocation baseTexture;
    protected int containerWidth;
    protected int containerHeight;

    public GuiCreativeBuilder(Vector3 position) {
        super(new ContainerDummy());

        this.position = position;
        this.baseTexture = new ResourceLocation(Reference.PREFIX + "/textures/gui/gui_empty.png");
    }

    @Override
    public void initGui() {
        super.initGui();
        this.textFieldSize = new GuiTextField(fontRendererObj, 45, 58, 50, 12);
        this.buttonList.add(new GuiButton(0, this.width / 2 - 80, this.height / 2 - 10, 58, 20, "Build"));
        this.buttonList.add(new GuiButton(1, this.width / 2 - 50, this.height / 2 - 35, 120, 20, "Mode"));
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRendererObj.drawString("Creative Builder", 60, 6, 4210752);
        fontRendererObj.drawString("This is a creative only cheat", 9, 20, 4210752);
        this.fontRendererObj.drawString("which allows you to auto build", 9, 30, 4210752);
        this.fontRendererObj.drawString("structures for testing.", 9, 40, 4210752);

        this.fontRendererObj.drawString("Size: ", 9, 60, 4210752);
        this.textFieldSize.drawTextBox();

        ((GuiButton) this.buttonList.get(1)).displayString = LanguageUtility.localize(BlockCreativeBuilder.getSchematic(mode).getName());
        this.fontRendererObj.drawString("Mode: ", 9, 80, 4210752);
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        this.fontRendererObj.drawString("Warning!", 9, 130, 4210752);
        this.fontRendererObj.drawString("This will replace blocks without", 9, 140, 4210752);
        this.fontRendererObj.drawString("dropping it! You may lose items.", 9, 150, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
        this.containerWidth = (this.width - this.xSize) / 2;
        this.containerHeight = (this.height - this.ySize) / 2;

        this.mc.renderEngine.bindTexture(this.baseTexture);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        this.drawTexturedModalRect(this.containerWidth, this.containerHeight, 0, 0, this.xSize, this.ySize);
    }

    @Override
    protected void keyTyped(char par1, int par2) {
        super.keyTyped(par1, par2);
        this.textFieldSize.textboxKeyTyped(par1, par2);
    }

    @Override
    protected void mouseClicked(int x, int y, int par3) {
        super.mouseClicked(x, y, par3);
        this.textFieldSize.mouseClicked(x - containerWidth, y - containerHeight, par3);
    }

    @Override
    protected void actionPerformed(GuiButton par1GuiButton) {
        super.actionPerformed(par1GuiButton);

        if (par1GuiButton.id == 0) {
            int radius = 0;

            try {
                radius = Integer.parseInt(textFieldSize.getText());
            } catch (Exception e) {

            }

            if (radius > 0) {
                //PacketDispatcher.sendPacketToServer(References.PACKET_TILE.getPacket(position.intX(), position.intY(), position.intZ(), mode, radius));
                Quantum.getPacketHandler().sendToServer(new PacketCreativeBuilder(new Location(0, (int) position.x, (int) position.y, (int) position.z), mode, radius));
                mc.thePlayer.closeScreen();
            }
        } else if (par1GuiButton.id == 1) {
            mode = (mode + 1) % (BlockCreativeBuilder.getSchematicCount());
        }
    }

}