package org.halvors.nuclearphysics.client.gui.component;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.awt.*;

@OnlyIn(Dist.CLIENT)
public interface IGuiComponent {
    Rectangle getBounds(int guiWidth, int guiHeight);

    void renderBackground(int xAxis, int yAxis, int guiWidth, int guiHeight);

    void renderForeground(int xAxis, int yAxis);

    void preMouseClicked(int xAxis, int yAxis, int button);

    void mouseClicked(int xAxis, int yAxis, int button);

    void mouseClickMove(int mouseX, int mouseY, int button, long ticks);

    void mouseReleased(int x, int y, int type);

    void mouseWheel(int x, int y, int delta);
}