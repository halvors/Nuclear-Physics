package org.halvors.nuclearphysics.common.effect.explosion;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

public class ExplosionBase extends Explosion {
    protected final Random random = new Random();
    protected final World world;
    protected final BlockPos pos;
    protected final float size;

    public ExplosionBase(final IBlockAccess world, final Entity entity, final BlockPos pos, final float size, final boolean flaming, final boolean damagesTerrain) {
        super((World) world, entity, pos.getX(), pos.getY(), pos.getZ(), size, flaming, damagesTerrain);

        this.world = (World) world;
        this.pos = pos;
        this.size = size;
    }

    public void explode() {
        doExplosionA();
        doExplosionB(true);

        // Send explosion packet to the client for client-side explosion.
        if (!world.isRemote) {
            for (final EntityPlayer player : world.playerEntities) {
                if (player.getDistanceSq(pos) < 4096) {
                    ((EntityPlayerMP) player).connection.sendPacket(new SPacketExplosion(pos.getX(), pos.getY(), pos.getZ(), size, getAffectedBlockPositions(), getPlayerKnockbackMap().get(player)));
                }
            }
        }
    }
}
