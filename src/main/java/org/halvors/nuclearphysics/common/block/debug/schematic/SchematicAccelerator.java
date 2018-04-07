package org.halvors.nuclearphysics.common.block.debug.schematic;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.nuclearphysics.common.block.reactor.fusion.BlockElectromagnet.EnumElectromagnet;
import org.halvors.nuclearphysics.common.init.ModBlocks;
import org.halvors.nuclearphysics.common.type.Pair;
import org.halvors.nuclearphysics.common.type.Position;

import java.util.HashMap;

public class SchematicAccelerator implements ISchematic {
    @Override
    public String getName() {
        return "schematic.accelerator.name";
    }

    @Override
    public HashMap<Position, Pair<Block, Integer>> getStructure(ForgeDirection facing, int size) {
        final HashMap<Position, Pair<Block, Integer>> map = new HashMap<>();

        if (size < 4) {
            size = 4;
        }

        int radius = Math.max(size, 3);

        for (int x = -radius; x < radius; x++) {
            for (int z = -radius; z < radius; z++) {
                for (int y = -1; y <= 1; y++) {
                    if (x == -radius || x == radius - 1 || z == -radius || z == radius - 1) {
                        map.put(new Position(x, y, z), new Pair<>(ModBlocks.blockElectromagnet, EnumElectromagnet.NORMAL.ordinal()));
                    }
                }
            }
        }

        radius = size - 2;

        for (int x = -radius; x < radius; x++) {
            for (int z = -radius; z < radius; z++) {
                for (int y = -1; y <= 1; y++) {
                    if (x == -radius || x == radius - 1 || z == -radius || z == radius - 1) {
                        map.put(new Position(x, y, z), new Pair<>(ModBlocks.blockElectromagnet, EnumElectromagnet.NORMAL.ordinal()));
                    }
                }
            }
        }

        radius = size - 1;

        for (int x = -radius; x < radius; x++) {
            for (int z = -radius; z < radius; z++) {
                for (int y = -1; y <= 1; y++) {
                    if (x == -radius || x == radius - 1 || z == -radius || z == radius - 1) {
                        if (y == -1 || y == 1) {
                            map.put(new Position(x, y, z), new Pair<>(ModBlocks.blockElectromagnet, EnumElectromagnet.GLASS.ordinal()));
                        } else {
                            map.put(new Position(x, y, z), new Pair<>(Blocks.air, 0));
                        }
                    }
                }
            }
        }

        return map;
    }
}