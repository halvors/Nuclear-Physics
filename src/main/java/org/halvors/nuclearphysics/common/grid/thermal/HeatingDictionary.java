package org.halvors.nuclearphysics.common.grid.thermal;

import net.minecraft.block.Block;
import net.minecraft.block.material.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import java.util.HashMap;

/**
 * Dictionary of heat values related to blocks
 *
 * http://www.engineeringtoolbox.com/specific-heat-metals-d_152.html
 * http://www.engineeringtoolbox.com/specific-heat-solids-d_154.html
 */
public class HeatingDictionary {
    private static final HashMap<IBlockState, Float> blockStateToHeatMap = new HashMap<>();
    private static final HashMap<Material, Float> materialToHeatMap = new HashMap<>();

    static {
        register(Material.AIR, 1);
        register(Material.GRASS, 0.84F);
        register(Material.GROUND, 0.9F);
        register(Material.WOOD, 0.84F);
        register(Material.ROCK, 0.8F);
        register(Material.IRON, 0.45F);
        register(Material.ANVIL, 0.5F);
        register(Material.WATER, 1);
        register(Material.LAVA, 0.84F);
        register(Material.LEAVES, 0.84F);
        register(Material.PLANTS, 0.84F);
        register(Material.VINE, 0.84F);
        register(Material.SPONGE, 0.84F);
        register(Material.CLOTH, 2);
        register(Material.FIRE, 1);
        register(Material.SAND, 1);
        register(Material.CIRCUITS, 1);
        register(Material.CARPET, 2);
        register(Material.GLASS, 0.84F);
        register(Material.REDSTONE_LIGHT, 0.9F);
        register(Material.TNT, 2);
        register(Material.CORAL, 0.84F);
        register(Material.ICE, 1);
        register(Material.PACKED_ICE, 1);
        register(Material.SNOW, 1);
        register(Material.CRAFTED_SNOW, 1);
        register(Material.CACTUS, 0.84F);
        register(Material.CLAY, 0.92F);
        //register(Material.GOURD, 1); // TODO: Get this correct.
        register(Material.DRAGON_EGG, 0.84F);
        register(Material.PORTAL, 1);
        register(Material.CAKE, 2);
        register(Material.WEB, 0.84F);

        //register(Material.PISTON, 0.9F);
    }

    /** Registers a block with a specific heating value */
    public static void register(IBlockState state, float f) {
        if (state != null && f > 0) {
            blockStateToHeatMap.put(state, f);
        }
    }

    /**
     * Registers a material with a specific heating value
     */
    public static void register(Material material, float f) {
        if (material != null && f > 0) {
            materialToHeatMap.put(material, f);
        }
    }

    /**
     * Graps the specific heating point of a block at the location
     */
    public static float getSpecificHeat(IBlockAccess world, BlockPos pos) {
        final IBlockState state = world.getBlockState(pos);

        if (blockStateToHeatMap.containsKey(state)) {
            return blockStateToHeatMap.get(state);
        } else if (materialToHeatMap.containsKey(state.getMaterial())) {
            return materialToHeatMap.get(state.getMaterial());
        }

        return 5;
    }
}
