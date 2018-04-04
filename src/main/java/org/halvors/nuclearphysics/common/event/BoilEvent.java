package org.halvors.nuclearphysics.common.event;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.FluidStack;

public class BoilEvent extends WorldEventBase {
    private final FluidStack fluid;
    private final FluidStack gas;
    private final int maxSpread;
    private final boolean reactor;

    /**
     * @param world - The world object
     * @param pos - The position in which the boiling happens.
     * @param fluid - The fluid being boiled.
     * @param gas - The steam made from this event.
     * @param maxSpread - The maximum distance the evaporated fluid can spread.
     * @param reactor - Determined if heat source if from power generation or a weapon.
     */
    public BoilEvent(IBlockAccess world, BlockPos pos, FluidStack fluid, FluidStack gas, int maxSpread, boolean reactor) {
        super(world, pos);

        this.fluid = fluid;
        this.gas = gas;
        this.maxSpread = maxSpread;
        this.reactor = reactor;
    }

    public BoilEvent(IBlockAccess world, BlockPos pos, FluidStack fluid, FluidStack gas, int maxSpread) {
        this(world, pos, fluid, gas, maxSpread, false);
    }

    public FluidStack getFluid() {
        return fluid;
    }

    public FluidStack getGas() {
        return gas;
    }

    public int getMaxSpread() {
        return maxSpread;
    }

    public boolean isReactor() {
        return reactor;
    }

    // Fluid spread causes loss. Gets the remaining amount of fluid left after spreading.
    public FluidStack getRemainForSpread(int spread) {
        float spreadPercentage = (float) spread / (float) maxSpread;
        FluidStack returnFluid = fluid.copy();
        returnFluid.amount *= spreadPercentage;

        return returnFluid;
    }
}
