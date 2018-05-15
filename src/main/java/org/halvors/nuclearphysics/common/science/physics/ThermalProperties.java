package org.halvors.nuclearphysics.common.science.physics;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;

import java.util.HashMap;
import java.util.Map;

public class ThermalProperties {
    private static final Map<Material, Integer> materialToSpecificHeatCapacityMap = new HashMap<>();

    static {
        register(Material.IRON, 449); // Updated.
        register(Material.AIR, 1005); // Updated.
        register(Material.GROUND, 8000);
        register(Material.WOOD, 2000); // Updated.
        register(Material.ROCK, 8400);
        register(Material.ANVIL, 5000);
        register(Material.WATER, 4182); // Updated.
        register(Material.LAVA, 9000);
        register(Material.LEAVES, 8400);
        register(Material.PLANTS, 8400);
        register(Material.VINE, 8400);
        register(Material.SPONGE, 8400);
        register(Material.CLOTH, 1400);
        register(Material.FIRE, 1000);
        register(Material.SAND, 800); // Updated.
        register(Material.CIRCUITS, 1000);
        register(Material.CARPET, 1130);
        register(Material.GLASS, 840); // Updated.
        register(Material.REDSTONE_LIGHT, 1000);
        register(Material.TNT, 1000);
        register(Material.CORAL, 8400);
        register(Material.ICE, 2090); // Updated.
        register(Material.SNOW, 2090);
        register(Material.CRAFTED_SNOW, 4200);
        register(Material.CACTUS, 8400);
        register(Material.CLAY, 920); // Updated.
        register(Material.GOURD, 8400);
        register(Material.DRAGON_EGG, 8400);
        register(Material.PORTAL, 1000);
        register(Material.CAKE, 2000);
        register(Material.WEB, 8400);
        register(Material.PISTON, 4500);
    }

    /**
     * Registers a material with a specific heating value
     *
     * @param specificHeatCapacity - The specific heat capacity in J/Kg*K
     */
    public static void register(final Material material, int specificHeatCapacity) {
        if (material != null && specificHeatCapacity > 0) {
            materialToSpecificHeatCapacityMap.put(material, specificHeatCapacity);
        }
    }

    /**
     * Gets the specific heat capacity of a certain material
     */
    public static int getSpecificHeatCapacity(final Material material) {
        return materialToSpecificHeatCapacityMap.getOrDefault(material, 0);
    }

    public static int getSpecificHeatCapacity(final IBlockState state) {
        return getSpecificHeatCapacity(state.getMaterial());
    }

    public static int getSpecificHeatCapacity(final Block block) {
        return getSpecificHeatCapacity(block.getDefaultState());
    }

    public static int getSpecificHeatCapacity(final Fluid fluid) {
        return getSpecificHeatCapacity(fluid.getBlock());
    }
}
