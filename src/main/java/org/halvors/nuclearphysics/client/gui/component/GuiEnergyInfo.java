package org.halvors.nuclearphysics.client.gui.component;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.gui.IGuiWrapper;
import org.halvors.nuclearphysics.client.utility.RenderUtility;
import org.halvors.nuclearphysics.common.ConfigurationManager.General;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;
import org.halvors.nuclearphysics.common.utility.energy.ElectricUnit;
import org.halvors.nuclearphysics.common.utility.type.Resource;

import java.awt.*;
import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiEnergyInfo extends GuiComponent {
    private IInfoHandler infoHandler;

    public GuiEnergyInfo(IInfoHandler infoHandler, IGuiWrapper gui) {
        super(ResourceUtility.getResource(Resource.GUI_COMPONENT, "energy_info.png"), gui, -26, 105);

        this.infoHandler = infoHandler;
    }

    @Override
    public Rectangle getBounds(int guiWidth, int guiHeight) {
        return new Rectangle(guiWidth + xLocation, guiHeight + yLocation, 26, 26);
    }

    @Override
    public void renderBackground(int xAxis, int yAxis, int guiWidth, int guiHeight) {
        RenderUtility.bindTexture(resource);

        gui.drawTexturedRect(guiWidth + xLocation, guiHeight + yLocation, 0, 0, 26, 26);
    }

    @Override
    public void renderForeground(int xAxis, int yAxis) {
        if (xAxis >= xLocation + 3 && xAxis <= xLocation + 23 && yAxis >= yLocation + 3 && yAxis <= yLocation + 22) {
            List<String> info = infoHandler.getInfo();
            info.add(LanguageUtility.transelate("gui.unit") + ": " + General.electricUnit.getSymbol());

            displayTooltips(info, xAxis, yAxis);
        }
    }

    @Override
    public void preMouseClicked(int xAxis, int yAxis, int button) {

    }

    @Override
    public void mouseClicked(int xAxis, int yAxis, int button) {
        switch (button) {
            case 0:
                if (xAxis >= xLocation + 3 && xAxis <= xLocation + 23 && yAxis >= yLocation + 3 && yAxis <= yLocation + 22) {
                    General.electricUnit = ElectricUnit.values()[(General.electricUnit.ordinal() + 1) % ElectricUnit.values().length];
                }
                break;
        }
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int button, long ticks) {

    }

    @Override
    public void mouseReleased(int x, int y, int type) {

    }

    @Override
    public void mouseWheel(int x, int y, int delta) {

    }
}
