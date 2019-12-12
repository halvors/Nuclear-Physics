package org.halvors.nuclearphysics.client.gui.component;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.IFluidTank;

@OnlyIn(Dist.CLIENT)
public interface IFluidInfoHandler {
    IFluidTank getTank();
}