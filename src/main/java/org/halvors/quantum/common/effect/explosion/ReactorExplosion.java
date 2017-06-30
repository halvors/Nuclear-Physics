package org.halvors.quantum.common.effect.explosion;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import org.halvors.quantum.Quantum;

import java.util.Random;

public class ReactorExplosion extends Explosion {
    private Random explosionRAND = new Random();
    private World world;

    public ReactorExplosion(World world, Entity entity, double x, double y, double z, float f) {
        super(world, entity, x, y, z, f);

        this.world = world;
        this.isFlaming = true;
    }

    public void explode() {
        doExplosionA();
        doExplosionB(true);
    }

    @Override
    public void doExplosionB(boolean flag) {
        super.doExplosionB(flag);

        for (Object affectedBlockPosition : affectedBlockPositions) {
            ChunkPosition chunkPosition = (ChunkPosition) affectedBlockPosition;
            int x = chunkPosition.chunkPosX;
            int y = chunkPosition.chunkPosY;
            int z = chunkPosition.chunkPosZ;
            Block block = world.getBlock(x, y, z);
            Block blockUnder = world.getBlock(x, y - 1, z);

            if (block == Blocks.air && blockUnder.isOpaqueCube() && explosionRAND.nextInt(3) == 0) {
                world.setBlock(x, y, z, Quantum.blockRadioactiveGrass);
            }
        }
    }
}
