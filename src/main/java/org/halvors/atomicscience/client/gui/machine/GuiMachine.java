package org.halvors.atomicscience.client.gui.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.halvors.electrometrics.client.gui.GuiComponentContainerScreen;
import org.halvors.electrometrics.common.tile.machine.TileEntityMachine;

@SideOnly(Side.CLIENT)
public class GuiMachine extends GuiComponentContainerScreen {
	protected GuiMachine(TileEntityMachine tileEntity) {
		super(tileEntity);
	}
}