package org.halvors.nuclearphysics.client.gui.modular.component;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.gui.modular.IGuiWrapper;
import org.halvors.nuclearphysics.client.utility.RenderUtility;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;
import org.halvors.nuclearphysics.common.utility.type.ResourceType;

@SideOnly(Side.CLIENT)
public abstract class GuiGauge<T> extends GuiComponent {
    protected int xLocation;
    protected int yLocation;

    protected int width = 14;
    protected int height = 49;
    protected int textureX = 0;
    protected int textureY = 0;

    protected int number = 1;

    public GuiGauge(IGuiWrapper gui, ResourceLocation def, int x, int y) {
        super(ResourceUtility.getResource(ResourceType.GUI_COMPONENT, "gauge.png"), gui, def);

        this.xLocation = x;
        this.yLocation = y;
    }

    public abstract int getScaledLevel();

    public abstract TextureAtlasSprite getIcon();

    public abstract String getTooltipText();

    public int getRenderColor() {
        return -1;
    }

    @Override
    public void renderBackground(int xAxis, int yAxis, int guiWidth, int guiHeight) {
        RenderUtility.bindTexture(RESOURCE);

        guiObj.drawTexturedRect(guiWidth + xLocation, guiHeight + yLocation, textureX, textureY, width, height);

        renderScale(xAxis, yAxis, guiWidth, guiHeight);

        RenderUtility.bindTexture(defaultLocation);
    }

    public void renderScale(int xAxis, int yAxis, int guiWidth, int guiHeight) {
        if (getScaledLevel() == 0 || getIcon() == null) {
            guiObj.drawTexturedRect(guiWidth + xLocation, guiHeight + yLocation, width, 0, width, height);
            return;
        }

        int scale = getScaledLevel();
        int start = 0;

        while (scale > 0) {
            int renderRemaining = 0;

            if (scale > 16) {
                renderRemaining = 16;
                scale -= 16;
            } else {
                renderRemaining = scale;
                scale = 0;
            }

            RenderUtility.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

            if (getRenderColor() != -1) {
                RenderUtility.color(getRenderColor());
            }

            for (int i = 0; i < number; i++) {
                guiObj.drawTexturedRectFromIcon(guiWidth + xLocation + 16 * i + 1, guiHeight + yLocation + height - renderRemaining - start - 1, getIcon(), width - 2, renderRemaining);
            }

            GlStateManager.resetColor();

            start += 16;

            if (renderRemaining == 0 || scale == 0) {
                break;
            }
        }

        RenderUtility.bindTexture(RESOURCE);

        guiObj.drawTexturedRect(guiWidth + xLocation, guiHeight + yLocation, width, 0, width, height);
    }

    @Override
    public void renderForeground(int xAxis, int yAxis) {
        if (xAxis >= xLocation + 1 && xAxis <= xLocation + width - 1 && yAxis >= yLocation + 1 && yAxis <= yLocation + height - 1) {
            guiObj.displayTooltip(getTooltipText(), xAxis, yAxis);
        }
    }

    @Override
    public void preMouseClicked(int xAxis, int yAxis, int button) {

    }

    @Override
    public void mouseClicked(int xAxis, int yAxis, int button) {

    }

    @Override
    public Rectangle4i getBounds(int guiWidth, int guiHeight) {
        return new Rectangle4i(guiWidth + xLocation, guiHeight + yLocation, width, height);
    }
}
