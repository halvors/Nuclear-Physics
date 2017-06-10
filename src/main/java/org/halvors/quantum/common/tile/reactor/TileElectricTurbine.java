package org.halvors.quantum.common.tile.reactor;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import org.halvors.quantum.common.ConfigurationManager;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.lib.thermal.IBoilHandler;

public class TileElectricTurbine extends TileTurbine implements IBoilHandler {
    public TileElectricTurbine() {
        super();

        maxPower = 5000000;
    }

    @Override
    public void updateEntity() {
        if (getMultiBlock().isConstructed()) {
            torque = defaultTorque * 500 * getArea();
        } else {
            torque = defaultTorque * 500;
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
        if (worldObj.getWorldTime() % 26 == 0) {
            double maxVelocity = (getMaxPower() / torque) * 4;
            float percentage = angularVelocity * 4 / (float) maxVelocity;

            worldObj.playSoundEffect(xCoord, yCoord, zCoord, Reference.PREFIX + "turbine", percentage, 1);
        }
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return from == ForgeDirection.DOWN && super.canFill(from, fluid);
    }
}
