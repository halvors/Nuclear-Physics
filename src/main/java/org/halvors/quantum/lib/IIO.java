package org.halvors.quantum.lib;

import net.minecraftforge.common.util.ForgeDirection;

import java.util.EnumSet;

public interface IIO {
    EnumSet<ForgeDirection> getInputDirections();

    EnumSet<ForgeDirection> getOutputDirections();

    void setIO(ForgeDirection paramForgeDirection, int paramInt);

    int getIO(ForgeDirection paramForgeDirection);
}
