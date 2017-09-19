package org.halvors.nuclearphysics.client.gui.debug;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.gui.GuiComponentScreen;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.block.debug.BlockCreativeBuilder;
import org.halvors.nuclearphysics.common.network.packet.PacketCreativeBuilder;
import org.halvors.nuclearphysics.common.type.Color;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;

import java.io.IOException;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiCreativeBuilder extends GuiComponentScreen {
    private Block block;
    private BlockPos pos;
    private int mode = 0;
    private GuiTextField textFieldSize;

    public GuiCreativeBuilder(Block block, BlockPos pos) {
        this.block = block;
        this.pos = pos;
    }

    @Override
    public void initGui() {
        super.initGui();

        textFieldSize = new GuiTextField(0, fontRendererObj, (xSize / 2) - 40, (ySize / 2) - 30, 50, 12);
        buttonList.add(new GuiButton(0, (width / 2) - 41, (height / 2) - 15, 120, 20, ""));
        buttonList.add(new GuiButton(1, (width / 2) - 80, (height / 2) + 10, 60, 20, "Build"));
    }

    @Override
    protected void drawGuiScreenForegroundLayer(int mouseX, int mouseY) {
        String name = LanguageUtility.transelate(block.getUnlocalizedName() + ".name");
        fontRendererObj.drawString(name, (xSize / 2) - (fontRendererObj.getStringWidth(name) / 2), (ySize / 2) - 102, 0x404040);

        List<String> list = LanguageUtility.splitStringPerWord(LanguageUtility.transelate(block.getUnlocalizedName() + ".tooltip"), 4);

        for (int i = 0; i < list.size(); i++) {
            fontRendererObj.drawString(list.get(i), (xSize / 2) - 80, 35 + i * 9, 0x404040);
        }

        fontRendererObj.drawString(LanguageUtility.transelate("gui.radius") + ": ", (xSize / 2) - 80, (ySize / 2) - 28, 0x404040);
        textFieldSize.drawTextBox();

        buttonList.get(0).displayString = LanguageUtility.transelate(BlockCreativeBuilder.getSchematic(mode).getName());
        fontRendererObj.drawString(LanguageUtility.transelate("gui.mode") + ": ", (xSize / 2) - 80, (ySize / 2) - 9, 0x404040);

        list = LanguageUtility.splitStringPerWord(LanguageUtility.transelate(block.getUnlocalizedName() + ".text"), 4);

        for (int i = 0; i < list.size(); i++) {
            fontRendererObj.drawString(list.get(i), (xSize / 2) - 80, 150 + i * 9, Color.DARK_RED.getHex());
        }

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
                mode = (mode + 1) % (BlockCreativeBuilder.getSchematicCount());
                break;

            case 1:
                int radius;

                try {
                    radius = Integer.parseInt(textFieldSize.getText());
                } catch (NumberFormatException e) {
                    radius = 2;
                }

                if (radius > 0) {
                    NuclearPhysics.getPacketHandler().sendToServer(new PacketCreativeBuilder(pos, mode, radius));
                    Minecraft.getMinecraft().player.closeScreen();
                }

                break;
        }
    }
}