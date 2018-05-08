package org.halvors.nuclearphysics.common.science.event;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.FluidStack;
import org.halvors.nuclearphysics.common.event.WorldEventBase;
import org.halvors.nuclearphysics.common.init.ModFluids;

public class FluidEvent extends WorldEventBase {
    protected final FluidStack fluidStack;

    public FluidEvent(final IBlockAccess world, final BlockPos pos, final FluidStack fluidStack) {
        super(world, pos);

        this.fluidStack = fluidStack;
    }

    public FluidStack getFluid() {
        return fluidStack;
    }

    public static class CondensateEvent extends FluidEvent {
        public CondensateEvent(IBlockAccess world, BlockPos pos, FluidStack fluidStack) {
            super(world, pos, fluidStack);
        }
    }

    public static class EvaporateEvent extends FluidEvent {
        private final int maxSpreadDistance;
        private final boolean reactor;

        /**
         * @param world - The world object
         * @param pos - The position in which the boiling happens.
         * @param fluidStack - The fluid being boiled.
         * @param maxSpreadDistance - The maximum distance the evaporated fluid can spread.
         * @param reactor - Determined if heat source if from power generation or a weapon.
         */
        public EvaporateEvent(final IBlockAccess world, final BlockPos pos, final FluidStack fluidStack, final int maxSpreadDistance, final boolean reactor) {
            super(world, pos, fluidStack);

            this.maxSpreadDistance = maxSpreadDistance;
            this.reactor = reactor;
        }

        public EvaporateEvent(final IBlockAccess world, final BlockPos pos, final FluidStack fluidStack, final int maxSpread) {
            this(world, pos, fluidStack, maxSpread, false);
        }

        public int getMaxSpreadDistance() {
            return maxSpreadDistance;
        }

        public boolean isReactor() {
            return reactor;
        }

        // Fluid spread causes loss. Gets the remaining amount of steam left after spreading.
        public FluidStack getGas(final int spreadDistance) {
            final double spreadPercentage = (double) spreadDistance / maxSpreadDistance;
            final FluidStack gasStack = new FluidStack(ModFluids.steam, fluidStack.amount);
            gasStack.amount *= spreadPercentage;

            return gasStack;
        }
    }
}
