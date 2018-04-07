package org.halvors.nuclearphysics.common.event;

import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class WorldEventBase extends WorldEvent {
    private final int x;
    private final int y;
    private final int z;

    public WorldEventBase(IBlockAccess world, int x, int y, int z) {
        super((World) world);

        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public World getWorld() {
        return world;
    }
}
