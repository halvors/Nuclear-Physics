package org.halvors.nuclearphysics.common.effect.explosion;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.halvors.nuclearphysics.common.block.states.BlockStateRadioactive;
import org.halvors.nuclearphysics.common.block.states.BlockStateRadioactive.EnumRadioactive;
import org.halvors.nuclearphysics.common.init.ModBlocks;

public class ReactorExplosion extends RadioactiveExplosion {
    public ReactorExplosion(IBlockAccess world, Entity entity, BlockPos pos, float size) {
        super(world, entity, pos, size, false, true);
    }

    @Override
    public void doExplosionB(boolean spawnParticles) {
        for (BlockPos pos : getAffectedBlockPositions()) {
            IBlockState stateUnder = world.getBlockState(pos.down());

            if (world.isAirBlock(pos) && stateUnder.isOpaqueCube() && random.nextInt(3) == 0) {
                world.setBlockState(pos, ModBlocks.blockRadioactive.getDefaultState().withProperty(BlockStateRadioactive.TYPE, EnumRadioactive.GRASS));
            }
        }

        super.doExplosionB(spawnParticles);
    }
}
