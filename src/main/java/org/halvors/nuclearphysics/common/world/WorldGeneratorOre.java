package org.halvors.nuclearphysics.common.world;

import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderEnd;
import net.minecraft.world.gen.ChunkProviderHell;
import net.minecraft.world.gen.feature.WorldGenMinable;
import org.halvors.nuclearphysics.common.ConfigurationManager.General;
import org.halvors.nuclearphysics.common.init.ModBlocks;

import java.util.Random;

public class WorldGeneratorOre implements IWorldGenerator {
    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {

    //public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (!(chunkGenerator instanceof ChunkProviderHell) && !(chunkGenerator instanceof ChunkProviderEnd)) {
            if (General.enableOreRegeneration) {
                for (int i = 0; i < General.uraniumPerChunk; i++) {
                    int x = chunkX * 16 + random.nextInt(16);
                    int y = random.nextInt(25);
                    int z = (chunkZ * 16) + random.nextInt(16);

                    new WorldGenMinable(ModBlocks.blockUraniumOre, 3, Blocks.stone).generate(world, random, x, y, z);
                }
            }
        }
    }
}
