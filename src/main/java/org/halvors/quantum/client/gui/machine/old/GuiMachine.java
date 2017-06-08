package org.halvors.quantum.client.gui.machine.old;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.halvors.quantum.client.gui.GuiComponentContainerScreen;
import org.halvors.quantum.common.tile.machine.old.TileEntityMachine;

@SideOnly(Side.CLIENT)
public class GuiMachine extends GuiComponentContainerScreen {
	protected GuiMachine(TileEntityMachine tileEntity) {
		super(tileEntity);
	}
}