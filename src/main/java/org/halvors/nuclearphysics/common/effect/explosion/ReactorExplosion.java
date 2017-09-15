package org.halvors.nuclearphysics.common.effect.explosion;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.halvors.nuclearphysics.common.init.ModBlocks;

import java.util.Random;

public class ReactorExplosion extends RadioactiveExplosion {
    private Random random = new Random();

    public ReactorExplosion(World world, Entity entity, BlockPos pos, float size) {
        super(world, entity, pos, size, false, true);
    }

    @Override
    public void doExplosionB(boolean spawnParticles) {
        for (BlockPos pos : getAffectedBlockPositions()) {
            IBlockState stateUnder = world.getBlockState(pos.down());

            if (world.isAirBlock(pos) && stateUnder.isOpaqueCube() && random.nextInt(3) == 0) {
                world.setBlockState(pos, ModBlocks.blockRadioactiveGrass.getDefaultState());
            }
        }

        super.doExplosionB(spawnParticles);
    }
}
