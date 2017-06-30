package org.halvors.quantum.common.effect.explosion;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import org.halvors.quantum.Quantum;

import java.util.Random;

public class ReactorExplosion extends Explosion {
    private Random explosionRAND = new Random();
    private World world;

    public ReactorExplosion(World world, Entity entity, BlockPos pos, float size) {
        super(world, entity, pos.getX(), pos.getY(), pos.getZ(), size, true, false);

        this.world = world;
    }

    public void explode() {
        doExplosionA();
        doExplosionB(true);
    }

    @Override
    public void doExplosionB(boolean flag) {
        super.doExplosionB(flag);

        for (BlockPos affectedBlockPosition : getAffectedBlockPositions()) {
            //ChunkPos chunkPos = new ChunkPos(affectedBlockPosition);

            /*
            ChunkPosition chunkPosition = (ChunkPosition) affectedBlockPosition;
            int x = chunkPosition.chunkPosX;
            int y = chunkPosition.chunkPosY;
            int z = chunkPosition.chunkPosZ;
            Block block = world.getBlock(x, y, z);
            Block blockUnder = world.getBlock(x, y - 1, z);

            if (block == Blocks.AIR && blockUnder.getDefaultState().isOpaqueCube() && explosionRAND.nextInt(3) == 0) {
                world.setBlockState(pos, Quantum.blockRadioactiveGrass.getDefaultState());
            }
            */
        }
    }
}
