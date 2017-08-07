package org.halvors.quantum.atomic.common.effect.explosion;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import org.halvors.quantum.atomic.common.init.QuantumBlocks;

import java.util.Random;

public class ReactorExplosion extends Explosion {
    private World world;
    private Random explosionRAND = new Random();

    public ReactorExplosion(World world, Entity entity, BlockPos pos, float size) {
        super(world, entity, pos.getX(), pos.getY(), pos.getZ(), size, true, true);

        this.world = world;
    }

    public void explode() {
        doExplosionA();
        doExplosionB(true);
    }

    @Override
    public void doExplosionB(boolean flag) {
        super.doExplosionB(flag);

        for (BlockPos pos : getAffectedBlockPositions()) {
            IBlockState stateUnder = world.getBlockState(pos.down());

            if (world.isAirBlock(pos) && stateUnder.isOpaqueCube() && explosionRAND.nextInt(3) == 0) {
                world.setBlockState(pos, QuantumBlocks.blockRadioactiveGrass.getDefaultState());
            }
        }
    }
}
