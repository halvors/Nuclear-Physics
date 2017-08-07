package org.halvors.quantum.atomic.client.gui.component;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IPowerInfoHandler {
	String getTooltip();

	double getLevel();
}