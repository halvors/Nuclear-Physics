package org.halvors.atomicscience.old.process.turbine;

import atomicscience.Settings;
import calclavia.api.resonantinduction.IBoilHandler;
import calclavia.lib.prefab.turbine.TileTurbine;
import calclavia.lib.prefab.turbine.TurbineMultiBlockHandler;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import universalelectricity.api.energy.EnergyStorageHandler;

public class TileElectricTurbine
        extends TileTurbine
        implements IBoilHandler
{
    public TileElectricTurbine()
    {
        this.maxPower = 5000000L;
    }

    public void func_70316_g()
    {
        if (getMultiBlock().isConstructed()) {
            this.torque = (2500000L * getArea());
        } else {
            this.torque = 2500000L;
        }
        super.func_70316_g();
    }

    public void onProduce()
    {
        this.energy.receiveEnergy((this.power * Settings.turbineOutputMultiplier), true);
        produce();
    }

    public void playSound()
    {
        if (this.ticks % 18L == 0L)
        {
            double maxVelocity = getMaxPower() / this.torque;
            float percentage = this.angularVelocity / (float)maxVelocity;
            this.field_70331_k.func_72908_a(this.field_70329_l, this.field_70330_m, this.field_70327_n, "atomicscience:turbine", percentage, 0.7F + 0.2F * percentage);
        }
    }

    public boolean canFill(ForgeDirection from, Fluid fluid)
    {
        return (from == ForgeDirection.DOWN) && (super.canFill(from, fluid));
    }
}
