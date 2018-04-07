package org.halvors.nuclearphysics.common.effect.explosion;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class ExplosionBase extends Explosion {
    protected final Random random = new Random();
    protected final World world;
    protected final int x;
    protected final int y;
    protected final int z;
    protected final float size;

    public ExplosionBase(IBlockAccess world, Entity entity, int x, int y, int z, float size, boolean flaming, boolean damagesTerrain) {
        super((World) world, entity, x, y, z, size);

        this.world = (World) world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.size = size;

        this.isFlaming = flaming;
        this.isSmoking = damagesTerrain; // TODO: Is this correct?
    }

    public void explode() {
        doExplosionA();
        doExplosionB(true);

        // Send explosion packet to the client for client-side explosion.
        if (!world.isRemote) {
            for (Object object : world.playerEntities) {
                EntityPlayerMP player = (EntityPlayerMP) object;

                if (player.getDistanceSq(x, y, z) < 4096) {
                    player.playerNetServerHandler.sendPacket(new S27PacketExplosion(x, y, z, size, affectedBlockPositions, null));
                }
            }
        }
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
}
