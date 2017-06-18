package org.halvors.quantum.client.gui.component;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IPowerInfoHandler {
	String getTooltip();

	double getLevel();
}