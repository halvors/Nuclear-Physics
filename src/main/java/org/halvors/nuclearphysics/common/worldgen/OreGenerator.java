package org.halvors.nuclearphysics.common.worldgen;

import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockMatcher;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.AbstractChunkProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;
import org.halvors.nuclearphysics.common.ConfigurationManager.General;
import org.halvors.nuclearphysics.common.init.ModBlocks;

import java.util.Random;

public class OreGenerator implements IWorldGenerator {
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, ChunkGenerator chunkGenerator, AbstractChunkProvider chunkProvider) {
        if (!(chunkGenerator instanceof ChunkGeneratorHell) && !(chunkGenerator instanceof ChunkGeneratorEnd)) {
            if (General.enableOreRegeneration) {
                for (int i = 0; i < General.uraniumPerChunk; i++) {
                    final BlockPos pos = new BlockPos(chunkX * 16 + random.nextInt(16), random.nextInt(25), (chunkZ * 16) + random.nextInt(16));
                    new WorldGenMinable(ModBlocks.blockUraniumOre.getDefaultState(), 3, BlockMatcher.forBlock(Blocks.STONE)).generate(world, random, pos);
                }
            }
        }
    }
}
