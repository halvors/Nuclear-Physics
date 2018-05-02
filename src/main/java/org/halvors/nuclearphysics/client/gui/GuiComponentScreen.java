package org.halvors.nuclearphysics.client.gui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import org.halvors.nuclearphysics.client.gui.component.IGuiComponent;
import org.halvors.nuclearphysics.client.utility.RenderUtility;
import org.halvors.nuclearphysics.common.type.EnumResource;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SideOnly(Side.CLIENT)
public class GuiComponentScreen extends GuiScreen implements IGuiWrapper {
    protected final ResourceLocation defaultResource = ResourceUtility.getResource(EnumResource.GUI, "empty.png");
    protected final Set<IGuiComponent> components = new HashSet<>();

    /** Starting X position for the Gui. Inconsistent use for Gui backgrounds. */
    private int guiLeft;

    /** Starting Y position for the Gui. Inconsistent use for Gui backgrounds. */
    private int guiTop;

    /** The X size of the inventory window in pixels. */
    protected int xSize = 176;

    /** The Y size of the inventory window in pixels. */
    protected int ySize = 217;

    public GuiComponentScreen() {

    }

    @Override
    public void initGui() {
        super.initGui();

        guiLeft = (width - xSize) / 2;
        guiTop = (height - ySize) / 2;
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTick) {
        drawDefaultBackground();

        GL11.glColor4d(1, 1, 1, 1);

        drawGuiScreenBackgroundLayer(partialTick, mouseX, mouseY);

        super.drawScreen(mouseX, mouseY, partialTick);

        GL11.glTranslated(guiLeft, guiTop, 0);

        drawGuiScreenForegroundLayer(mouseX, mouseY);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    protected void drawGuiScreenForegroundLayer(final int mouseX, final int mouseY) {
        final int xAxis = (mouseX - (width - xSize) / 2);
        final int yAxis = (mouseY - (height - ySize) / 2);

        for (final IGuiComponent component : components) {
            component.renderForeground(xAxis, yAxis);
        }
    }

    protected void drawGuiScreenBackgroundLayer(final float partialTick, final int mouseX, final int mouseY) {
        RenderUtility.bindTexture(defaultResource);

        GL11.glColor4d(1, 1, 1, 1);

        final int guiWidth = (width - xSize) / 2;
        final int guiHeight = (height - ySize) / 2;

        drawTexturedModalRect(guiWidth, guiHeight, 0, 0, xSize, ySize);

        final int xAxis = mouseX - guiWidth;
        final int yAxis = mouseY - guiHeight;

        for (final IGuiComponent component : components) {
            component.renderBackground(xAxis, yAxis, guiWidth, guiHeight);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int button) {
        final int xAxis = (mouseX - (width - xSize) / 2);
        final int yAxis = (mouseY - (height - ySize) / 2);

        for (final IGuiComponent component : components) {
            component.preMouseClicked(xAxis, yAxis, button);
        }

        super.mouseClicked(mouseX, mouseY, button);

        for (final IGuiComponent component : components) {
            component.mouseClicked(xAxis, yAxis, button);
        }
    }

    @Override
    protected void mouseClickMove(final int mouseX, final int mouseY, final int button, final long ticks) {
        super.mouseClickMove(mouseX, mouseY, button, ticks);

        final int xAxis = (mouseX - (width - xSize) / 2);
        final int yAxis = (mouseY - (height - ySize) / 2);

        for (final IGuiComponent component : components) {
            component.mouseClickMove(xAxis, yAxis, button, ticks);
        }
    }

    @Override
    protected void mouseReleased(final int mouseX, final int mouseY, final int type) {
        super.mouseReleased(mouseX, mouseY, type);

        final int xAxis = (mouseX - (width - xSize) / 2);
        final int yAxis = (mouseY - (height - ySize) / 2);

        for (final IGuiComponent component : components) {
            component.mouseReleased(xAxis, yAxis, type);
        }
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();

        final int xAxis = Mouse.getEventX() * width / mc.displayWidth - getXPos();
        final int yAxis = height - Mouse.getEventY() * height / mc.displayHeight - 1 - getYPos();
        final int delta = Mouse.getEventDWheel();

        if (delta != 0) {
            mouseWheel(xAxis, yAxis, delta);
        }
    }

    public void mouseWheel(final int xAxis, final int yAxis, final int delta) {
        for (final IGuiComponent component : components) {
            component.mouseWheel(xAxis, yAxis, delta);
        }
    }

    public int getXPos() {
        return (width - xSize) / 2;
    }

    public int getYPos() {
        return (height - ySize) / 2;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void drawTexturedRect(final int x, final int y, final int u, final int v, final int w, final int h) {
        drawTexturedModalRect(x, y, u, v, w, h);
    }

    @Override
    public void drawTexturedRectFromIcon(final int x, final int y, final IIcon icon, final int w, final int h) {
        drawTexturedModelRectFromIcon(x, y, icon, w, h);
    }

    @Override
    public void displayTooltip(final String text, final int xAxis, final int yAxis) {
        drawCreativeTabHoveringText(text, xAxis, yAxis);
    }

    @Override
    public void displayTooltips(final List<String> list, final int xAxis, final int yAxis) {
        drawHoveringText(list, xAxis, yAxis);
    }

    @Override
    public FontRenderer getFontRenderer() {
        return fontRendererObj;
    }
}