package org.halvors.nuclearphysics.common.science.physics;

import net.minecraft.block.*;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.util.HashMap;
import java.util.Map;

/**
 * A thermal block manager
 *
 * @author Calclavia
 */
public class ThermoPhysics {
    public static final double ROOM_TEMPERATURE = 295;
    public static final double ICE_MELT_TEMPERATURE = 273.15;
    public static final double WATER_BOIL_TEMPERATURE = 373.15;

    private static final Map<IBlockState, Integer> stateToSpecificHeatCapacityMap = new HashMap<>();
    private static final Map<Material, Integer> materialToSpecificHeatCapacityMap = new HashMap<>();

    static {
        /*
         * TODO: Data should be fetched from: https://www.engineeringtoolbox.com/specific-heat-capacity-d_391.html
         *                                    https://www.engineeringtoolbox.com/specific-heat-solids-d_154.html
         */
        
        register(Blocks.AIR.getDefaultState(), 0);
        register(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE), 0);
        register(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE), 0);
        register(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.GRANITE_SMOOTH), 0);
        register(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE), 0);
        register(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.DIORITE_SMOOTH), 0);
        register(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE), 0);
        register(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.ANDESITE_SMOOTH), 0);
        register(Blocks.GRASS.getDefaultState(), 0);
        register(Blocks.GRASS.getDefaultState().withProperty(BlockGrass.SNOWY, true), 0);
        register(Blocks.DIRT.getDefaultState(), 0);
        register(Blocks.DIRT.getDefaultState().withProperty(BlockGrass.SNOWY, true), 0);
        register(Blocks.COBBLESTONE.getDefaultState(), 0);
        register(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.OAK), 0);
        register(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE), 0);
        register(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.BIRCH), 0);
        register(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.JUNGLE), 0);
        register(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA), 0);
        register(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.DARK_OAK), 0);
        register(Blocks.SAPLING.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.OAK), 0);
        register(Blocks.SAPLING.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE), 0);
        register(Blocks.SAPLING.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.BIRCH), 0);
        register(Blocks.SAPLING.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.JUNGLE), 0);
        register(Blocks.SAPLING.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA), 0);
        register(Blocks.SAPLING.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.DARK_OAK), 0);
        register(Blocks.BEDROCK.getDefaultState(), 0);

        for (int level = 0; level <= 15; level++) { // TODO: Calculate heat capacity based on level.
            register(Blocks.FLOWING_WATER.getDefaultState().withProperty(BlockLiquid.LEVEL, level), 0);
            register(Blocks.WATER.getDefaultState().withProperty(BlockLiquid.LEVEL, level), 0);
            register(Blocks.FLOWING_LAVA.getDefaultState().withProperty(BlockLiquid.LEVEL, level), 0);
            register(Blocks.LAVA.getDefaultState().withProperty(BlockLiquid.LEVEL, level), 0);
        }

        register(Blocks.SAND.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.SAND), 0);
        register(Blocks.SAND.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.RED_SAND), 0);
        register(Blocks.GRAVEL.getDefaultState(), 0);
        register(Blocks.GOLD_ORE.getDefaultState(), 0);
        register(Blocks.IRON_ORE.getDefaultState(), 0);
        register(Blocks.COAL_ORE.getDefaultState(), 0);
        register(Blocks.LOG.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.OAK), 0);
        register(Blocks.LOG.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE), 0);
        register(Blocks.LOG.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.BIRCH), 0);
        register(Blocks.LOG.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.JUNGLE), 0);
        register(Blocks.LOG.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA), 0);
        register(Blocks.LOG.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.DARK_OAK), 0);
        register(Blocks.LOG2.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.OAK), 0);
        register(Blocks.LOG2.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE), 0);
        register(Blocks.LOG2.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.BIRCH), 0);
        register(Blocks.LOG2.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.JUNGLE), 0);
        register(Blocks.LOG2.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA), 0);
        register(Blocks.LOG2.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.DARK_OAK), 0);
        register(Blocks.LEAVES.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.OAK).withProperty(BlockLeaves.CHECK_DECAY, true).withProperty(BlockLeaves.DECAYABLE, true), 0);
        register(Blocks.LEAVES.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty(BlockLeaves.CHECK_DECAY, true).withProperty(BlockLeaves.DECAYABLE, true), 0);
        register(Blocks.LEAVES.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.BIRCH).withProperty(BlockLeaves.CHECK_DECAY, true).withProperty(BlockLeaves.DECAYABLE, true), 0);
        register(Blocks.LEAVES.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.JUNGLE).withProperty(BlockLeaves.CHECK_DECAY, true).withProperty(BlockLeaves.DECAYABLE, true), 0);
        register(Blocks.LEAVES2.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockLeaves.CHECK_DECAY, true).withProperty(BlockLeaves.DECAYABLE, true), 0);
        register(Blocks.LEAVES2.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.DARK_OAK).withProperty(BlockLeaves.CHECK_DECAY, true).withProperty(BlockLeaves.DECAYABLE, true), 0);
        register(Blocks.SPONGE.getDefaultState(), 0);
        register(Blocks.SPONGE.getDefaultState().withProperty(BlockSponge.WET, true), 0);

        // Continue.
        Blocks.GLASS;
        Blocks.LAPIS_ORE;
        Blocks.LAPIS_BLOCK;
        Blocks.DISPENSER;
        Blocks.SANDSTONE;
        Blocks.NOTEBLOCK;
        Blocks.BED;
        Blocks.GOLDEN_RAIL;
        Blocks.DETECTOR_RAIL;
        BlockPistonBase STICKY_PISTON;
        Blocks.WEB;
        BlockTallGrass TALLGRASS;
        BlockDeadBush DEADBUSH;
        BlockPistonBase PISTON;
        BlockPistonExtension PISTON_HEAD;
        Blocks.WOOL;
        BlockPistonMoving PISTON_EXTENSION;
        BlockFlower YELLOW_FLOWER;
        BlockFlower RED_FLOWER;
        BlockBush BROWN_MUSHROOM;
        BlockBush RED_MUSHROOM;
        Blocks.GOLD_BLOCK;
        Blocks.IRON_BLOCK;
        BlockSlab DOUBLE_STONE_SLAB;
        BlockSlab STONE_SLAB;
        Blocks.BRICK_BLOCK;
        Blocks.TNT;
        Blocks.BOOKSHELF;
        Blocks.MOSSY_COBBLESTONE;
        Blocks.OBSIDIAN;
        Blocks.TORCH;
        BlockFire FIRE;
        Blocks.MOB_SPAWNER;
        Blocks.OAK_STAIRS;
        BlockChest CHEST;
        BlockRedstoneWire REDSTONE_WIRE;
        Blocks.DIAMOND_ORE;
        Blocks.DIAMOND_BLOCK;
        Blocks.CRAFTING_TABLE;
        Blocks.WHEAT;
        Blocks.FARMLAND;
        Blocks.FURNACE;
        Blocks.LIT_FURNACE;
        Blocks.STANDING_SIGN;
        BlockDoor OAK_DOOR;
        BlockDoor SPRUCE_DOOR;
        BlockDoor BIRCH_DOOR;
        BlockDoor JUNGLE_DOOR;
        BlockDoor ACACIA_DOOR;
        BlockDoor DARK_OAK_DOOR;
        Blocks.LADDER;
        Blocks.RAIL;
        Blocks.STONE_STAIRS;
        Blocks.WALL_SIGN;
        Blocks.LEVER;
        Blocks.STONE_PRESSURE_PLATE;
        BlockDoor IRON_DOOR;
        Blocks.WOODEN_PRESSURE_PLATE;
        Blocks.REDSTONE_ORE;
        Blocks.LIT_REDSTONE_ORE;
        Blocks.UNLIT_REDSTONE_TORCH;
        Blocks.REDSTONE_TORCH;
        Blocks.STONE_BUTTON;
        Blocks.SNOW_LAYER;
        Blocks.ICE;
        Blocks.SNOW;
        BlockCactus CACTUS;
        Blocks.CLAY;
        BlockReed REEDS;
        Blocks.JUKEBOX;
        Blocks.OAK_FENCE;
        Blocks.SPRUCE_FENCE;
        Blocks.BIRCH_FENCE;
        Blocks.JUNGLE_FENCE;
        Blocks.DARK_OAK_FENCE;
        Blocks.ACACIA_FENCE;
        Blocks.PUMPKIN;
        Blocks.NETHERRACK;
        Blocks.SOUL_SAND;
        Blocks.GLOWSTONE;
        BlockPortal PORTAL;
        Blocks.LIT_PUMPKIN;
        Blocks.CAKE;
        BlockRedstoneRepeater UNPOWERED_REPEATER;
        BlockRedstoneRepeater POWERED_REPEATER;
        Blocks.TRAPDOOR;
        Blocks.MONSTER_EGG;
        Blocks.STONEBRICK;
        Blocks.BROWN_MUSHROOM_BLOCK;
        Blocks.RED_MUSHROOM_BLOCK;
        Blocks.IRON_BARS;
        Blocks.GLASS_PANE;
        Blocks.MELON_BLOCK;
        Blocks.PUMPKIN_STEM;
        Blocks.MELON_STEM;
        Blocks.VINE;
        Blocks.OAK_FENCE_GATE;
        Blocks.SPRUCE_FENCE_GATE;
        Blocks.BIRCH_FENCE_GATE;
        Blocks.JUNGLE_FENCE_GATE;
        Blocks.DARK_OAK_FENCE_GATE;
        Blocks.ACACIA_FENCE_GATE;
        Blocks.BRICK_STAIRS;
        Blocks.STONE_BRICK_STAIRS;
        BlockMycelium MYCELIUM;
        Blocks.WATERLILY;
        Blocks.NETHER_BRICK;
        Blocks.NETHER_BRICK_FENCE;
        Blocks.NETHER_BRICK_STAIRS;
        Blocks.NETHER_WART;
        Blocks.ENCHANTING_TABLE;
        Blocks.BREWING_STAND;
        BlockCauldron CAULDRON;
        Blocks.END_PORTAL;
        Blocks.END_PORTAL_FRAME;
        Blocks.END_STONE;
        Blocks.DRAGON_EGG;
        Blocks.REDSTONE_LAMP;
        Blocks.LIT_REDSTONE_LAMP;
        BlockSlab DOUBLE_WOODEN_SLAB;
        BlockSlab WOODEN_SLAB;
        Blocks.COCOA;
        Blocks.SANDSTONE_STAIRS;
        Blocks.EMERALD_ORE;
        Blocks.ENDER_CHEST;
        BlockTripWireHook TRIPWIRE_HOOK;
        Blocks.TRIPWIRE;
        Blocks.EMERALD_BLOCK;
        Blocks.SPRUCE_STAIRS;
        Blocks.BIRCH_STAIRS;
        Blocks.JUNGLE_STAIRS;
        Blocks.COMMAND_BLOCK;
        BlockBeacon BEACON;
        Blocks.COBBLESTONE_WALL;
        Blocks.FLOWER_POT;
        Blocks.CARROTS;
        Blocks.POTATOES;
        Blocks.WOODEN_BUTTON;
        BlockSkull SKULL;
        Blocks.ANVIL;
        Blocks.TRAPPED_CHEST;
        Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE;
        Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE;
        BlockRedstoneComparator UNPOWERED_COMPARATOR;
        BlockRedstoneComparator POWERED_COMPARATOR;
        BlockDaylightDetector DAYLIGHT_DETECTOR;
        BlockDaylightDetector DAYLIGHT_DETECTOR_INVERTED;
        Blocks.REDSTONE_BLOCK;
        Blocks.QUARTZ_ORE;
        BlockHopper HOPPER;
        Blocks.QUARTZ_BLOCK;
        Blocks.QUARTZ_STAIRS;
        Blocks.ACTIVATOR_RAIL;
        Blocks.DROPPER;
        Blocks.STAINED_HARDENED_CLAY;
        Blocks.BARRIER;
        Blocks.IRON_TRAPDOOR;
        Blocks.HAY_BLOCK;
        Blocks.CARPET;
        Blocks.HARDENED_CLAY;
        Blocks.COAL_BLOCK;
        Blocks.PACKED_ICE;
        Blocks.ACACIA_STAIRS;
        Blocks.DARK_OAK_STAIRS;
        Blocks.SLIME_BLOCK;
        BlockDoublePlant DOUBLE_PLANT;
        BlockStainedGlass STAINED_GLASS;
        BlockStainedGlassPane STAINED_GLASS_PANE;
        Blocks.PRISMARINE;
        Blocks.SEA_LANTERN;
        Blocks.STANDING_BANNER;
        Blocks.WALL_BANNER;
        Blocks.RED_SANDSTONE;
        Blocks.RED_SANDSTONE_STAIRS;
        BlockSlab DOUBLE_STONE_SLAB2;
        BlockSlab STONE_SLAB2;
        Blocks.END_ROD;
        Blocks.CHORUS_PLANT;
        Blocks.CHORUS_FLOWER;
        Blocks.PURPUR_BLOCK;
        Blocks.PURPUR_PILLAR;
        Blocks.PURPUR_STAIRS;
        BlockSlab PURPUR_DOUBLE_SLAB;
        BlockSlab PURPUR_SLAB;
        Blocks.END_BRICKS;
        Blocks.BEETROOTS;
        Blocks.GRASS_PATH;
        Blocks.END_GATEWAY;
        Blocks.REPEATING_COMMAND_BLOCK;
        Blocks.CHAIN_COMMAND_BLOCK;
        Blocks.FROSTED_ICE;
        Blocks.MAGMA;
        Blocks.NETHER_WART_BLOCK;
        Blocks.RED_NETHER_BRICK;
        Blocks.BONE_BLOCK;
        Blocks.STRUCTURE_VOID;
        Blocks.STRUCTURE_BLOCK;
        
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        
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
     * Registers a block state with a specific heating value
     *
     * @param specificHeatCapacity - The specific heat capacity in J/Kg K
     */
    public static void register(final IBlockState state, int specificHeatCapacity) {
        if (state != null && specificHeatCapacity > 0) {
            stateToSpecificHeatCapacityMap.put(state, specificHeatCapacity);
        }
    }

    /**
     * Registers a material with a specific heating value
     *
     * @param specificHeatCapacity - The specific heat capacity in J/Kg K
     */
    public static void register(final Material material, int specificHeatCapacity) {
        if (material != null && specificHeatCapacity > 0) {
            materialToSpecificHeatCapacityMap.put(material, specificHeatCapacity);
        }
    }

    /**
     * Gets the specific heat capacity of a certain material
     */
    public static int getSpecificHeatCapacity(final IBlockState state) {
        if (stateToSpecificHeatCapacityMap.containsKey(state)) {
            stateToSpecificHeatCapacityMap.getOrDefault(state, 0);
        }

        return materialToSpecificHeatCapacityMap.getOrDefault(state.getMaterial(), 0);
    }

    public static int getSpecificHeatCapacity(final Block block) {
        return getSpecificHeatCapacity(block.getDefaultState());
    }

    public static int getSpecificHeatCapacity(final Fluid fluid) {
        return getSpecificHeatCapacity(fluid.getBlock());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static double getTemperatureForEnergy(double mass, double specificHeatCapacity, double energy) {
        return energy / (mass * specificHeatCapacity);
    }

    /**
     * Q = cmT
     *
     * Q = (4.2*10^3 J/kgC^-1) (1 kg) (80C)
     * Q = 336000 J
     *
     * @param world - world this happens in
     * @param x - coordinate of block
     * @param z - coordinate of block
     * @param volume - in liters
     *
     * @return The required energy to boil volume of water into steam in joules
     */
    public static double getRequiredBoilWaterEnergy(World world, int x, int z, int volume) {
        double temperatureChange = WATER_BOIL_TEMPERATURE - getDefaultTemperature(world, new BlockPos(x, 0, z));
        double mass = getMass(new FluidStack(FluidRegistry.WATER, volume));
        int specificHeatCapacity = getSpecificHeatCapacity(Blocks.WATER);

        return getEnergyForTemperatureChange(mass, specificHeatCapacity, temperatureChange) + ThermoPhysics.getEnergyForStateChange(mass, 2257000);
    }

    /**
     * Temperature: 0.5f = 22C
     *
     * @return The temperature of the coordinate in the world in kelvin.
     */
    public static double getDefaultTemperature(final World world, final BlockPos pos) {
        final double averageTemperature = ICE_MELT_TEMPERATURE + (world.getBiome(pos).getFloatTemperature(pos) - 0.4) * 50;
        final double dayNightVariance = averageTemperature * 0.05; // TODO: Check if this could be handled differently.

        return averageTemperature + (world.isDaytime() ? dayNightVariance : -dayNightVariance);
    }

    /**
     * Q = cmT
     *
     * @param mass                 - in kg
     * @param specificHeatCapacity - in J/kg*K
     * @param temperature          - in K
     *
     * @return Q, energy in joules (J)
     */
    public static double getEnergyForTemperatureChange(final double mass, final double specificHeatCapacity, final double temperature) {
        return mass * specificHeatCapacity * temperature;
    }

    /**
     * Q = mL
     *
     * @param mass               - in kg
     * @param latentHeatCapacity - in J/kg
     *
     * @return Q, energy in joules (J)
     */
    public static double getEnergyForStateChange(final double mass, final double latentHeatCapacity) {
        return mass * latentHeatCapacity;
    }

    /**
     * Gets the mass of an object from volume and density.
     *
     * Mass (kg) = Volume (Cubic Meters) * Densitry (kg/m-cubed)
     *
     * Formula: m = ρ * V
     *
     * @param density - in kg/m^3
     * @param volume  - in liters
     *
     * @return The mass in kg.
     */
    public static double getMass(final double volume, final double density) {
        return density * (volume / Math.pow(10, 3));
    }

    /**
     * Get the mass of a fluid stack.
     *
     * @param fluidStack - the fluid stack to get the mass of.
     *
     * @return The mass in kg.
     */
    public static double getMass(final FluidStack fluidStack) {
        return getMass(fluidStack.getFluid().getDensity(), fluidStack.amount);
    }
}