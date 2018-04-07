package org.halvors.nuclearphysics.common.effect.explosion;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.IBlockAccess;
import org.halvors.nuclearphysics.common.init.ModBlocks;

public class ReactorExplosion extends RadioactiveExplosion {
    public ReactorExplosion(IBlockAccess world, Entity entity, int x, int y, int z, float size) {
        super(world, entity, x, y, z, size, false, true);
    }

    @Override
    public void doExplosionB(boolean spawnParticles) {
        for (Object affectedBlockPosition : affectedBlockPositions) {
            ChunkPosition chunkPosition = (ChunkPosition) affectedBlockPosition;
            int x = chunkPosition.chunkPosX;
            int y = chunkPosition.chunkPosY;
            int z = chunkPosition.chunkPosZ;
            Block block = world.getBlock(x, y, z);
            Block blockUnder = world.getBlock(x, y - 1, z);

            if (block == Blocks.air && blockUnder.isOpaqueCube() && random.nextInt(3) == 0) {
                world.setBlock(x, y, z, ModBlocks.blockRadioactiveGrass);
            }
        }

        super.doExplosionB(spawnParticles);
    }
}
