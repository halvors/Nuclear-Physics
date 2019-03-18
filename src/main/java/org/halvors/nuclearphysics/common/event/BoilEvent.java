package org.halvors.nuclearphysics.common.event;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.FluidStack;
import org.halvors.nuclearphysics.common.init.ModFluids;

public class BoilEvent extends WorldEventBase {
    private final double volume;
    private final double maxSpread;
    private final boolean reactor;

    /**
     * @param world - The world object
     * @param pos - The position in which the boiling happens.
     * @param volume - The amount of fluid being boiled.
     * @param maxSpread - The maximum distance the evaporated fluid can spread.
     * @param reactor - Determined if heat source if from power generation or a weapon.
     */
    public BoilEvent(IBlockAccess world, BlockPos pos, double volume, int maxSpread, boolean reactor) {
        super(world, pos);

        this.volume = volume;
        this.maxSpread = maxSpread;
        this.reactor = reactor;
    }

    public BoilEvent(IBlockAccess world, BlockPos pos, double volume, int maxSpread) {
        this(world, pos, volume, maxSpread, false);
    }

    public double getVolume() {
        return volume;
    }

    public double getMaxSpread() {
        return maxSpread;
    }

    public boolean isReactor() {
        return reactor;
    }

    // Fluid spread causes loss. Gets the remaining amount of steam left after spreading.
    public FluidStack getGas(int spread) {
        double spreadPercentage = (double) spread / maxSpread;

        return new FluidStack(ModFluids.steam, (int) Math.floor(volume * spreadPercentage));
    }
}