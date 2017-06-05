package org.halvors.quantum.common.tile;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import org.halvors.quantum.common.ConfigurationManager;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.lib.IBoilHandler;
import org.halvors.quantum.lib.prefab.turbine.TileTurbine;

public class TileElectricTurbine extends TileTurbine implements IBoilHandler {
    public TileElectricTurbine() {
        this.maxPower = 5000000L;
    }

    @Override
    public void updateEntity() {
        if (getMultiBlock().isConstructed()) {
            torque = (2500000L * getArea());
        } else {
            torque = 2500000L;
        }

        super.updateEntity();
    }

    public void onProduce() {
        energy.receiveEnergy((long) (power * ConfigurationManager.General.turbineOutputMultiplier), true);
        produce();
    }

    public void playSound() {
        if (worldObj.getWorldTime() % 26.0F == 0.0F) {
            double maxVelocity = getMaxPower() / this.torque * 4L;
            float percentage = this.angularVelocity * 4.0F / (float)maxVelocity;
            worldObj.playSoundEffect(xCoord, yCoord, zCoord, Reference.PREFIX + "turbine", percentage, 1.0F);
        }
    }

    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return from == ForgeDirection.DOWN && super.canFill(from, fluid);
    }
}
