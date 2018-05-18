package org.halvors.nuclearphysics.common.effect.explosion;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.S27PacketExplosion;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.halvors.nuclearphysics.api.BlockPos;

import java.util.Random;

public class ExplosionBase extends Explosion {
    protected final Random random = new Random();
    protected final World world;
    protected final BlockPos pos;
    protected final float size;

public ExplosionBase(final IBlockAccess world, final Entity entity, final BlockPos pos, final float size, final boolean flaming, final boolean damagesTerrain) {
        super((World) world, entity, pos.getX(), pos.getY(), pos.getZ(), size);

        this.world = (World) world;
        this.pos = pos;
        this.size = size;

        this.isFlaming = flaming;
        this.isSmoking = damagesTerrain; // TODO: Is this correct?
    }

    public void explode() {
        doExplosionA();
        doExplosionB(true);

        // Send explosion packet to the client for client-side explosion.
        if (!world.isRemote) {
            for (final Object object : world.playerEntities) {
                final EntityPlayerMP player = (EntityPlayerMP) object;

                if (player.getDistanceSq(pos.getX(), pos.getY(), pos.getZ()) < 4096) {
                    player.playerNetServerHandler.sendPacket(new S27PacketExplosion(pos.getX(), pos.getY(), pos.getZ(), size, affectedBlockPositions, null));
                }
            }
        }
    }

    public BlockPos getPos() {
        return pos;
    }
}
