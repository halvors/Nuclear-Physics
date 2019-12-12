package org.halvors.nuclearphysics.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.halvors.nuclearphysics.client.gui.component.IGuiComponent;
import org.halvors.nuclearphysics.client.utility.RenderUtility;
import org.halvors.nuclearphysics.common.type.EnumResource;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@OnlyIn(Dist.CLIENT)
public class GuiComponentContainer<T extends TileEntity> extends GuiContainer implements IGuiWrapper {
    protected ResourceLocation defaultResource = ResourceUtility.getResource(EnumResource.GUI, "base.png");
    protected final Set<IGuiComponent> components = new HashSet<>();
    protected final T tile;

    public GuiComponentContainer(final T tile, final Container container) {
        super(container);

        this.tile = tile;

        ySize = 217;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(final int mouseX, final int mouseY) {
        super.drawGuiContainerForegroundLayer(mouseX, mouseY);

        final int xAxis = (mouseX - (width - xSize) / 2);
        final int yAxis = (mouseY - (height - ySize) / 2);

        for (final IGuiComponent component : components) {
            component.renderForeground(xAxis, yAxis);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(final float partialTick, final int mouseX, final int mouseY) {
        RenderUtility.bindTexture(defaultResource);

        GlStateManager.color4f(1, 1, 1, 1);

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
    protected void mouseClicked(final int mouseX, final int mouseY, final int button) throws IOException {
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
    public void handleMouseInput() throws IOException {
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
    public void drawTexturedRectFromIcon(final int x, final int y, final TextureAtlasSprite icon, final int w, final int h) {
        drawTexturedModalRect(x, y, icon, w, h);
    }

    @Override
    public void displayTooltip(final String text, final int xAxis, final int yAxis) {
        drawHoveringText(text, xAxis, yAxis);
    }

    @Override
    public void displayTooltips(final List<String> list, final int xAxis, final int yAxis) {
        drawHoveringText(list, xAxis, yAxis);
    }
}
