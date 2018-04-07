package org.halvors.nuclearphysics.client.gui.component;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.fluids.IFluidTank;

@SideOnly(Side.CLIENT)
public interface IFluidInfoHandler {
    IFluidTank getTank();
}