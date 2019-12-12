package org.halvors.nuclearphysics.common.effect.explosion;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import org.halvors.nuclearphysics.common.init.ModBlocks;

public class ReactorExplosion extends RadioactiveExplosion {
    public ReactorExplosion(final IWorld world, final Entity entity, final BlockPos pos, final float size) {
        super(world, entity, pos, size, false, true);
    }

    @Override
    public void doExplosionB(final boolean spawnParticles) {
        for (final BlockPos pos : getAffectedBlockPositions()) {
            final BlockState stateUnder = world.getBlockState(pos.down());

            if (world.isAirBlock(pos) && stateUnder.isOpaqueCube(world, pos) && random.nextInt(3) == 0) {
                world.setBlockState(pos, ModBlocks.blockRadioactiveGrass.getDefaultState());
            }
        }

        super.doExplosionB(spawnParticles);
    }
}
