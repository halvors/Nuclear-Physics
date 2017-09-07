package org.halvors.nuclearphysics.client.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.gui.component.IGuiComponent;
import org.halvors.nuclearphysics.client.utility.RenderUtility;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;
import org.halvors.nuclearphysics.common.utility.type.ResourceType;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SideOnly(Side.CLIENT)
public class GuiComponentContainer<T extends TileEntity> extends GuiContainer implements IGuiWrapper {
    protected ResourceLocation defaultResource;
    protected Set<IGuiComponent> components = new HashSet<>();
    protected T tile;

    public GuiComponentContainer(T tile, Container container, ResourceLocation defaultResource) {
        super(container);

        this.defaultResource = defaultResource;
        this.tile = tile;

        ySize = 217;
    }

    public GuiComponentContainer(T tile, Container container) {
        this(tile, container, ResourceUtility.getResource(ResourceType.GUI, "gui_base.png"));
    }

    public float getNeededScale(String text, int maxX) {
        int length = fontRenderer.getStringWidth(text);

        if (length <= maxX) {
            return 1;
        } else {
            return (float) maxX / length;
        }
    }

    public void renderScaledText(String text, int x, int y, int color, int maxX) {
        int length = fontRenderer.getStringWidth(text);

        if (length <= maxX) {
            fontRenderer.drawString(text, x, y, color);
        } else {
            float scale = (float) maxX / length;
            float reverse = 1 / scale;
            float yAdd = 4 - (scale * 8) / 2F;

            GlStateManager.pushMatrix();

            GlStateManager.scale(scale, scale, scale);
            fontRenderer.drawString(text, (int) (x * reverse), (int) ((y * reverse) + yAdd), color);

            GlStateManager.popMatrix();
        }
    }

    public static boolean isTextboxKey(char c, int i) {
        return i == Keyboard.KEY_BACK || i == Keyboard.KEY_DELETE || i == Keyboard.KEY_LEFT || i == Keyboard.KEY_RIGHT || i == Keyboard.KEY_END || i == Keyboard.KEY_HOME || i == Keyboard.KEY_BACK || isKeyComboCtrlA(i) || isKeyComboCtrlC(i) || isKeyComboCtrlV(i) || isKeyComboCtrlX(i);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        int xAxis = (mouseX - (width - xSize) / 2);
        int yAxis = (mouseY - (height - ySize) / 2);

        for (IGuiComponent component : components) {
            component.renderForeground(xAxis, yAxis);
        }
    }

    protected boolean isMouseOverSlot(Slot slot, int mouseX, int mouseY) {
        return isPointInRegion(slot.xPos, slot.yPos, 16, 16, mouseX, mouseY);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY) {
        RenderUtility.bindTexture(defaultResource);

        GlStateManager.color(1, 1, 1, 1);

        int guiWidth = (width - xSize) / 2;
        int guiHeight = (height - ySize) / 2;

        drawTexturedModalRect(guiWidth, guiHeight, 0, 0, xSize, ySize);

        int xAxis = mouseX - guiWidth;
        int yAxis = mouseY - guiHeight;

        for (IGuiComponent component : components) {
            component.renderBackground(xAxis, yAxis, guiWidth, guiHeight);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button) throws IOException {
        int xAxis = (mouseX - (width - xSize) / 2);
        int yAxis = (mouseY - (height - ySize) / 2);

        for (IGuiComponent component : components) {
            component.preMouseClicked(xAxis, yAxis, button);
        }

        super.mouseClicked(mouseX, mouseY, button);

        for (IGuiComponent component : components) {
            component.mouseClicked(xAxis, yAxis, button);
        }
    }

    @Override
    public void drawTexturedRect(int x, int y, int u, int v, int w, int h) {
        drawTexturedModalRect(x, y, u, v, w, h);
    }

    @Override
    public void drawTexturedRectFromIcon(int x, int y, TextureAtlasSprite icon, int w, int h) {
        drawTexturedModalRect(x, y, icon, w, h);
    }

    @Override
    public void displayTooltip(String text, int x, int y) {
        drawHoveringText(text, x, y);
    }

    @Override
    public void displayTooltips(List<String> list, int xAxis, int yAxis) {
        drawHoveringText(list, xAxis, yAxis);
    }

    @Override
    public FontRenderer getFontRenderer() {
        return fontRenderer;
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int button, long ticks) {
        super.mouseClickMove(mouseX, mouseY, button, ticks);

        int xAxis = (mouseX - (width - xSize) / 2);
        int yAxis = (mouseY - (height - ySize) / 2);

        for (IGuiComponent component : components) {
            component.mouseClickMove(xAxis, yAxis, button, ticks);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int type) {
        super.mouseReleased(mouseX, mouseY, type);

        int xAxis = (mouseX - (width - xSize) / 2);
        int yAxis = (mouseY - (height - ySize) / 2);

        for (IGuiComponent component : components) {
            component.mouseReleased(xAxis, yAxis, type);
        }
    }

    public void handleMouse(Slot slot, int slotIndex, int button, ClickType modifier) {
        handleMouseClick(slot, slotIndex, button, modifier);
    }

    @Override
    public void handleMouseInput() throws java.io.IOException {
        super.handleMouseInput();

        int xAxis = Mouse.getEventX() * width / mc.displayWidth - getXPos();
        int yAxis = height - Mouse.getEventY() * height / mc.displayHeight - 1 - getYPos();
        int delta = Mouse.getEventDWheel();

        if (delta != 0) {
            mouseWheel(xAxis, yAxis, delta);
        }
    }

    public void mouseWheel(int xAxis, int yAxis, int delta) {
        for (IGuiComponent component : components) {
            component.mouseWheel(xAxis, yAxis, delta);
        }
    }

    public int getXPos() {
        return (width - xSize) / 2;
    }

    public int getYPos() {
        return (height - ySize) / 2;
    }
}
