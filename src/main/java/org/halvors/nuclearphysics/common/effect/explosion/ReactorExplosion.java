package org.halvors.nuclearphysics.common.effect.explosion;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.IBlockAccess;
import org.halvors.nuclearphysics.api.BlockPos;
import org.halvors.nuclearphysics.common.init.ModBlocks;

public class ReactorExplosion extends RadioactiveExplosion {
    public ReactorExplosion(final IBlockAccess world, final Entity entity, final BlockPos pos, final float size) {
        super(world, entity, pos, size, false, true);
    }

    @Override
    public void doExplosionB(final boolean spawnParticles) {
        for (final Object affectedBlockPosition : affectedBlockPositions) {
            final ChunkPosition chunkPosition = (ChunkPosition) affectedBlockPosition;
            final BlockPos pos = new BlockPos(chunkPosition.chunkPosX, chunkPosition.chunkPosY, chunkPosition.chunkPosZ);
            final Block block = pos.getBlock(world);
            final Block blockUnder = pos.down().getBlock(world);

            if (block == Blocks.air && blockUnder.isOpaqueCube() && random.nextInt(3) == 0) {
                pos.setBlock(ModBlocks.blockRadioactiveGrass, world);
            }
        }

        super.doExplosionB(spawnParticles);
    }
}
