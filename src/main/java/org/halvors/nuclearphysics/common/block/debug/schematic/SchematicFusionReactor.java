package org.halvors.nuclearphysics.common.block.debug.schematic;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.nuclearphysics.api.schematic.ISchematic;
import org.halvors.nuclearphysics.common.block.reactor.fusion.BlockElectromagnet.EnumElectromagnet;
import org.halvors.nuclearphysics.common.init.ModBlocks;
import org.halvors.nuclearphysics.common.type.Pair;
import org.halvors.nuclearphysics.common.type.Position;

import java.util.HashMap;

public class SchematicFusionReactor implements ISchematic {
    @Override
    public String getName() {
        return "schematic.fusion_reactor.name";
    }

    @Override
    public HashMap<Position, Pair<Block, Integer>> getStructure(final ForgeDirection facing, final int size) {
        final HashMap<Position, Pair<Block, Integer>> map = new HashMap<>();

        // Fusion Torus.
        final int radius = size + 2;

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                for (int y = 0; y <= size; y++) {
                    final Position pos = new Position(x, y, z);
                    final double magnitude = Math.sqrt(x * x + z * z);

                    if (!map.containsKey(pos)) {
                        map.put(pos, new Pair<>(Blocks.air, 0));
                    }

                    if (magnitude <= radius) {
                        if (y == 0 || y == size) {
                            if (magnitude >= 1) {
                                double yDeviation = (y == 0 ? size / 3 : -size / 3) + (y == 0 ? -1 : 1) * Math.sin(magnitude / radius * Math.PI) * size / 2;
                                map.put(pos.add(0, yDeviation, 0), new Pair<>(ModBlocks.blockElectromagnet, EnumElectromagnet.GLASS.ordinal()));
                            }
                        } else if (magnitude > radius - 1) {
                            map.put(pos, new Pair<>(ModBlocks.blockElectromagnet, EnumElectromagnet.NORMAL.ordinal()));
                        }
                    }
                }
            }
        }

        // Fusion Core
        for (int y = 0; y < size; y++) {
            map.put(new Position(0, y, 0), new Pair<>(ModBlocks.blockReactorCell, 0));
            map.put(new Position(1, y, 0), new Pair<>(ModBlocks.blockElectromagnet, EnumElectromagnet.NORMAL.ordinal()));
            map.put(new Position(0, y, 1), new Pair<>(ModBlocks.blockElectromagnet, EnumElectromagnet.NORMAL.ordinal()));
            map.put(new Position(0, y, -1), new Pair<>(ModBlocks.blockElectromagnet, EnumElectromagnet.NORMAL.ordinal()));
            map.put(new Position(-1, y, 0), new Pair<>(ModBlocks.blockElectromagnet, EnumElectromagnet.NORMAL.ordinal()));
        }

        map.put(new Position(0, 0, 0), new Pair<>(ModBlocks.blockReactorCell, 0));

        return map;
    }
}
