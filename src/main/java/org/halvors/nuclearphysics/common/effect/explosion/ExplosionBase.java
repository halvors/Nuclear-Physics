package org.halvors.nuclearphysics.common.effect.explosion;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ExplosionBase extends net.minecraft.world.Explosion {
    protected final World world;
    protected final BlockPos pos;
    protected final float size;

    public ExplosionBase(World world, Entity entity, BlockPos pos, float size, boolean flaming, boolean damagesTerrain) {
        super(world, entity, pos.getX(), pos.getY(), pos.getZ(), size, flaming, damagesTerrain);

        this.world = world;
        this.pos = pos;
        this.size = size;
    }

    public void explode() {
        doExplosionA();
        doExplosionB(true);

        // Send explosion packet to the client for client-side explosion.
        if (!world.isRemote) {
            for (EntityPlayer player : world.playerEntities) {
                if (player.getDistanceSq(pos) < 4096) {
                    ((EntityPlayerMP) player).connection.sendPacket(new SPacketExplosion(pos.getX(), pos.getY(), pos.getZ(), size, getAffectedBlockPositions(), getPlayerKnockbackMap().get(player)));
                }
            }
        }
    }
}
