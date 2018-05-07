package org.halvors.nuclearphysics.client.gui.component;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.gui.IGuiWrapper;
import org.halvors.nuclearphysics.common.ConfigurationManager.General;
import org.halvors.nuclearphysics.common.science.unit.EnumElectricUnit;
import org.halvors.nuclearphysics.common.type.EnumResource;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiEnergyInfo extends GuiInfo {
    public GuiEnergyInfo(final IInfoHandler infoHandler, final IGuiWrapper gui, final int x, final int y) {
        super(infoHandler, ResourceUtility.getResource(EnumResource.GUI_COMPONENT, "energy_info.png"), gui, x, y);
    }

    @Override
    protected List<String> getInfo(final List<String> list) {
        list.add(LanguageUtility.transelate("gui.unit") + ": " + General.electricUnit.getSymbol());

        return list;
    }

    @Override
    protected void buttonClicked() {
        General.electricUnit = EnumElectricUnit.values()[(General.electricUnit.ordinal() + 1) % EnumElectricUnit.values().length];
    }
}
