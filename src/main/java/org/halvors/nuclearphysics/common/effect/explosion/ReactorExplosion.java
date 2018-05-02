package org.halvors.nuclearphysics.common.effect.explosion;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.IBlockAccess;
import org.halvors.nuclearphysics.common.init.ModBlocks;

public class ReactorExplosion extends RadioactiveExplosion {
    public ReactorExplosion(final IBlockAccess world, final Entity entity, final int x, final int y, final int z, final float size) {
        super(world, entity, x, y, z, size, false, true);
    }

    @Override
    public void doExplosionB(boolean spawnParticles) {
        for (final Object affectedBlockPosition : affectedBlockPositions) {
            final ChunkPosition chunkPosition = (ChunkPosition) affectedBlockPosition;
            final int x = chunkPosition.chunkPosX;
            final int y = chunkPosition.chunkPosY;
            final int z = chunkPosition.chunkPosZ;
            final Block block = world.getBlock(x, y, z);
            final Block blockUnder = world.getBlock(x, y - 1, z);

            if (block == Blocks.air && blockUnder.isOpaqueCube() && random.nextInt(3) == 0) {
                world.setBlock(x, y, z, ModBlocks.blockRadioactiveGrass);
            }
        }

        super.doExplosionB(spawnParticles);
    }
}
