package org.halvors.quantum.common.event;

import cpw.mods.fml.common.eventhandler.Event.HasResult;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fluids.FluidStack;
import org.halvors.quantum.common.utility.transform.vector.Vector3;

@HasResult
public class BoilEvent extends WorldEvent {
    private final Vector3 position;
    private final FluidStack fluid;
    private final int maxSpread;
    private final boolean reactor;

    /** @param world - The World Objecto
     * @param position - The position in which the boiling happens.
     * @param result - The fluid being boiled.
     * @param maxSpread - The maximum distance the evaporated fluid can spread.
     * @param reactor - Determined if heat source if from power generation or a weapon. */
    public BoilEvent(World world, Vector3 position, FluidStack source, FluidStack result, int maxSpread, boolean reactor) {
        super(world);

        this.position = position;
        this.fluid = result;
        this.maxSpread = maxSpread;
        this.reactor = reactor;
    }

    public BoilEvent(World world, Vector3 position, FluidStack source, FluidStack result, int maxSpread) {
        this(world, position, source, result, maxSpread, false);
    }

    public FluidStack getFluid() {
        return fluid;
    }

    public Vector3 getPosition() {
        return position;
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
