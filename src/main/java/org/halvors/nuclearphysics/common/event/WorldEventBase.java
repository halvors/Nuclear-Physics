package org.halvors.nuclearphysics.common.event;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class WorldEventBase extends WorldEvent {
    private final BlockPos pos;

    public WorldEventBase(final IWorld world, final BlockPos pos) {
        super((World) world);

        this.pos = pos;
    }

    public BlockPos getPos() {
        return pos;
    }
}
