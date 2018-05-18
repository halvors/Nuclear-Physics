package org.halvors.nuclearphysics.common.event;

import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import org.halvors.nuclearphysics.api.BlockPos;

public class WorldEventBase extends WorldEvent {
    private final BlockPos pos;

    public WorldEventBase(final IBlockAccess world, final BlockPos pos) {
        super((World) world);

        this.pos = pos;
    }

    public BlockPos getPos() {
        return pos;
    }

    public World getWorld() {
        return world;
    }
}
