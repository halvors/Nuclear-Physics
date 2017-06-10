package org.halvors.quantum.common.tile.reactor;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import org.halvors.quantum.common.ConfigurationManager;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.base.tile.ITileNetworkable;
import org.halvors.quantum.lib.IBoilHandler;

public class TileElectricTurbine extends TileTurbine implements ITileNetworkable, IBoilHandler {
    public TileElectricTurbine() {
        maxPower = 5000000;
    }

    @Override
    public void updateEntity() {
        if (getMultiBlock().isConstructed()) {
            torque = (2500000 * getArea());
        } else {
            torque = 2500000;
        }

        super.updateEntity();
    }

    @Override
    public void onProduce() {
        energyStorage.receiveEnergy((int) (power * ConfigurationManager.General.turbineOutputMultiplier), false);
        produce();
    }

    @Override
    public void playSound() {
        if (worldObj.getWorldTime() % 26.0F == 0.0F) {
            double maxVelocity = getMaxPower() / torque * 4L;
            float percentage = angularVelocity * 4.0F / (float) maxVelocity;
            worldObj.playSoundEffect(xCoord, yCoord, zCoord, Reference.PREFIX + "turbine", percentage, 1);
        }
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return from == ForgeDirection.DOWN && super.canFill(from, fluid);
    }
}
