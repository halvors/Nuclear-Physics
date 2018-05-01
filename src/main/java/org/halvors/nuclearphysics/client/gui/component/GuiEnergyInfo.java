package org.halvors.nuclearphysics.client.gui.component;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.gui.IGuiWrapper;
import org.halvors.nuclearphysics.common.ConfigurationManager.General;
import org.halvors.nuclearphysics.common.type.Resource;
import org.halvors.nuclearphysics.common.unit.ElectricUnit;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;

import java.util.List;

@SideOnly(Side.CLIENT)
public class GuiEnergyInfo extends GuiInfo {
    public GuiEnergyInfo(final IInfoHandler infoHandler, final IGuiWrapper gui, final int x, final int y) {
        super(infoHandler, ResourceUtility.getResource(Resource.GUI_COMPONENT, "energy_info.png"), gui, x, y);
    }

    @Override
    protected List<String> getInfo(final List<String> list) {
        list.add(LanguageUtility.transelate("gui.unit") + ": " + General.electricUnit.getSymbol());

        return list;
    }

    @Override
    protected void buttonClicked() {
        General.electricUnit = ElectricUnit.values()[(General.electricUnit.ordinal() + 1) % ElectricUnit.values().length];
    }
}
