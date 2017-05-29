package org.halvors.atomicscience.old;

import java.util.Iterator;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class ReactorExplosion extends Explosion
{
    private Random rand = new Random();
    private World world;

    public ReactorExplosion(World world, Entity entity, double x, double y, double z, float par1) {
        super(world, entity, x, y, z, par1);

        this.world = world;
        this.isFlaming = true;
    }

    public void doExplosionB(boolean par1) {
        super.doExplosionB(par1);

        Iterator iterator = affectedBlockPositions.iterator();

        while (iterator.hasNext()) {
            ChunkPosition chunkPosition = (ChunkPosition)iterator.next();
            int x = chunkPosition.chunkPosX;
            int y = chunkPosition.chunkPosY;
            int z = chunkPosition.chunkPosZ;

            /* TODO: Fix this.
            int id = this.worldObj.func_72798_a(x, y, z);
            int i1 = this.worldObj.func_72798_a(x, y - 1, z);
            if ((id == 0) && (Block.field_71970_n[i1] != 0) && (rand.nextInt(3) == 0)) {
                this.world.func_94575_c(x, y, z, AtomicScience.blockRadioactive.field_71990_ca);
            }
            */
        }
    }
}
