package org.halvors.nuclearphysics.client.gui.debug;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.gui.GuiComponentScreen;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.block.debug.BlockCreativeBuilder;
import org.halvors.nuclearphysics.common.network.packet.PacketCreativeBuilder;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;
import org.halvors.nuclearphysics.common.utility.type.Color;

import java.io.IOException;

@SideOnly(Side.CLIENT)
public class GuiCreativeBuilder extends GuiComponentScreen {
    private BlockPos pos;
    private int mode = 0;
    private GuiTextField textFieldSize;

    public GuiCreativeBuilder(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public void initGui() {
        super.initGui();

        textFieldSize = new GuiTextField(0, fontRendererObj, 39, 58, 50, 12);

        buttonList.add(new GuiButton(0, width / 2 - 80, height / 2 - 10, 58, 20, "Build"));
        buttonList.add(new GuiButton(1, width / 2 - 50, height / 2 - 35, 120, 20, "Mode"));
    }

    @Override
    protected void drawGuiScreenForegroundLayer(int mouseX, int mouseY) {
        String name = LanguageUtility.transelate("tile.creative_builder.name");

        fontRendererObj.drawString(name, (xSize / 2) - (fontRendererObj.getStringWidth(name) / 2), 6, 0x404040);

        fontRendererObj.drawString("This is a creative only cheat", (xSize / 2) - 80, 20, 0x404040);
        fontRendererObj.drawString("which allows you to auto build", (xSize / 2) - 80, 30, 0x404040);
        fontRendererObj.drawString("structures for testing.", (xSize / 2) - 80, 40, 0x404040);

        fontRendererObj.drawString("Size: ", (xSize / 2) - 80, 60, 0x404040);
        textFieldSize.drawTextBox();

        buttonList.get(1).displayString = LanguageUtility.transelate(BlockCreativeBuilder.getSchematic(mode).getName());
        fontRendererObj.drawString("Mode: ", (xSize / 2) - 80, 80, 0x404040);

        fontRendererObj.drawString("Warning!", (xSize / 2) - 80, 130, Color.DARK_RED.getHex());
        fontRendererObj.drawString("This will replace blocks without", (xSize / 2) - 80, 140, Color.DARK_RED.getHex());
        fontRendererObj.drawString("dropping it! You may lose items.", (xSize / 2) - 80, 150, Color.DARK_RED.getHex());

        super.drawGuiScreenForegroundLayer(mouseX, mouseY);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        textFieldSize.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
        super.mouseClicked(mouseX, mouseY, button);

        int guiWidth = (width - xSize) / 2;
        int guiHeight = (height - ySize) / 2;

        textFieldSize.mouseClicked(mouseX - guiWidth, mouseY - guiHeight, button);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        switch (button.id) {
            case 0:
                int radius;

                try {
                    radius = Integer.parseInt(textFieldSize.getText());
                } catch (NumberFormatException e) {
                    radius = 2;
                }

                if (radius > 0) {
                    NuclearPhysics.getPacketHandler().sendToServer(new PacketCreativeBuilder(pos, mode, radius));
                    mc.player.closeScreen();
                }

                break;

            case 1:
                mode = (mode + 1) % (BlockCreativeBuilder.getSchematicCount());
                break;
        }
    }
}