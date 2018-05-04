package org.halvors.nuclearphysics.common.grid.thermal;

import net.minecraft.block.*;
import net.minecraft.block.material.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
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
        register(Blocks.AIR, 1);

        /*
        public static final Block STONE;
        public static final BlockGrass GRASS;
        public static final Block DIRT;
        public static final Block COBBLESTONE;
        public static final Block PLANKS;
        public static final Block SAPLING;
        public static final Block BEDROCK;
        public static final BlockDynamicLiquid FLOWING_WATER;
        public static final BlockStaticLiquid WATER;
        public static final BlockDynamicLiquid FLOWING_LAVA;
        public static final BlockStaticLiquid LAVA;
        public static final BlockSand SAND;
        public static final Block GRAVEL;
        public static final Block GOLD_ORE;
        public static final Block IRON_ORE;
        public static final Block COAL_ORE;
        public static final Block LOG;
        public static final Block LOG2;
        public static final BlockLeaves LEAVES;
        public static final BlockLeaves LEAVES2;
        public static final Block SPONGE;
        public static final Block GLASS;
        public static final Block LAPIS_ORE;
        public static final Block LAPIS_BLOCK;
        public static final Block DISPENSER;
        public static final Block SANDSTONE;
        public static final Block NOTEBLOCK;
        public static final Block BED;
        public static final Block GOLDEN_RAIL;
        public static final Block DETECTOR_RAIL;
        public static final BlockPistonBase STICKY_PISTON;
        public static final Block WEB;
        public static final BlockTallGrass TALLGRASS;
        public static final BlockDeadBush DEADBUSH;
        public static final BlockPistonBase PISTON;
        public static final BlockPistonExtension PISTON_HEAD;
        public static final Block WOOL;
        public static final BlockPistonMoving PISTON_EXTENSION;
        public static final BlockFlower YELLOW_FLOWER;
        public static final BlockFlower RED_FLOWER;
        public static final BlockBush BROWN_MUSHROOM;
        public static final BlockBush RED_MUSHROOM;
        public static final Block GOLD_BLOCK;
        public static final Block IRON_BLOCK;
        public static final BlockSlab DOUBLE_STONE_SLAB;
        public static final BlockSlab STONE_SLAB;
        public static final Block BRICK_BLOCK;
        public static final Block TNT;
        public static final Block BOOKSHELF;
        public static final Block MOSSY_COBBLESTONE;
        public static final Block OBSIDIAN;
        public static final Block TORCH;
        public static final BlockFire FIRE;
        public static final Block MOB_SPAWNER;
        public static final Block OAK_STAIRS;
        public static final BlockChest CHEST;
        public static final BlockRedstoneWire REDSTONE_WIRE;
        public static final Block DIAMOND_ORE;
        public static final Block DIAMOND_BLOCK;
        public static final Block CRAFTING_TABLE;
        public static final Block WHEAT;
        public static final Block FARMLAND;
        public static final Block FURNACE;
        public static final Block LIT_FURNACE;
        public static final Block STANDING_SIGN;
        public static final BlockDoor OAK_DOOR;
        public static final BlockDoor SPRUCE_DOOR;
        public static final BlockDoor BIRCH_DOOR;
        public static final BlockDoor JUNGLE_DOOR;
        public static final BlockDoor ACACIA_DOOR;
        public static final BlockDoor DARK_OAK_DOOR;
        public static final Block LADDER;
        public static final Block RAIL;
        public static final Block STONE_STAIRS;
        public static final Block WALL_SIGN;
        public static final Block LEVER;
        public static final Block STONE_PRESSURE_PLATE;
        public static final BlockDoor IRON_DOOR;
        public static final Block WOODEN_PRESSURE_PLATE;
        public static final Block REDSTONE_ORE;
        public static final Block LIT_REDSTONE_ORE;
        public static final Block UNLIT_REDSTONE_TORCH;
        public static final Block REDSTONE_TORCH;
        public static final Block STONE_BUTTON;
        public static final Block SNOW_LAYER;
        public static final Block ICE;
        public static final Block SNOW;
        public static final BlockCactus CACTUS;
        public static final Block CLAY;
        public static final BlockReed REEDS;
        public static final Block JUKEBOX;
        public static final Block OAK_FENCE;
        public static final Block SPRUCE_FENCE;
        public static final Block BIRCH_FENCE;
        public static final Block JUNGLE_FENCE;
        public static final Block DARK_OAK_FENCE;
        public static final Block ACACIA_FENCE;
        public static final Block PUMPKIN;
        public static final Block NETHERRACK;
        public static final Block SOUL_SAND;
        public static final Block GLOWSTONE;
        public static final BlockPortal PORTAL;
        public static final Block LIT_PUMPKIN;
        public static final Block CAKE;
        public static final BlockRedstoneRepeater UNPOWERED_REPEATER;
        public static final BlockRedstoneRepeater POWERED_REPEATER;
        public static final Block TRAPDOOR;
        public static final Block MONSTER_EGG;
        public static final Block STONEBRICK;
        public static final Block BROWN_MUSHROOM_BLOCK;
        public static final Block RED_MUSHROOM_BLOCK;
        public static final Block IRON_BARS;
        public static final Block GLASS_PANE;
        public static final Block MELON_BLOCK;
        public static final Block PUMPKIN_STEM;
        public static final Block MELON_STEM;
        public static final Block VINE;
        public static final Block OAK_FENCE_GATE;
        public static final Block SPRUCE_FENCE_GATE;
        public static final Block BIRCH_FENCE_GATE;
        public static final Block JUNGLE_FENCE_GATE;
        public static final Block DARK_OAK_FENCE_GATE;
        public static final Block ACACIA_FENCE_GATE;
        public static final Block BRICK_STAIRS;
        public static final Block STONE_BRICK_STAIRS;
        public static final BlockMycelium MYCELIUM;
        public static final Block WATERLILY;
        public static final Block NETHER_BRICK;
        public static final Block NETHER_BRICK_FENCE;
        public static final Block NETHER_BRICK_STAIRS;
        public static final Block NETHER_WART;
        public static final Block ENCHANTING_TABLE;
        public static final Block BREWING_STAND;
        public static final BlockCauldron CAULDRON;
        public static final Block END_PORTAL;
        public static final Block END_PORTAL_FRAME;
        public static final Block END_STONE;
        public static final Block DRAGON_EGG;
        public static final Block REDSTONE_LAMP;
        public static final Block LIT_REDSTONE_LAMP;
        public static final BlockSlab DOUBLE_WOODEN_SLAB;
        public static final BlockSlab WOODEN_SLAB;
        public static final Block COCOA;
        public static final Block SANDSTONE_STAIRS;
        public static final Block EMERALD_ORE;
        public static final Block ENDER_CHEST;
        public static final BlockTripWireHook TRIPWIRE_HOOK;
        public static final Block TRIPWIRE;
        public static final Block EMERALD_BLOCK;
        public static final Block SPRUCE_STAIRS;
        public static final Block BIRCH_STAIRS;
        public static final Block JUNGLE_STAIRS;
        public static final Block COMMAND_BLOCK;
        public static final BlockBeacon BEACON;
        public static final Block COBBLESTONE_WALL;
        public static final Block FLOWER_POT;
        public static final Block CARROTS;
        public static final Block POTATOES;
        public static final Block WOODEN_BUTTON;
        public static final BlockSkull SKULL;
        public static final Block ANVIL;
        public static final Block TRAPPED_CHEST;
        public static final Block LIGHT_WEIGHTED_PRESSURE_PLATE;
        public static final Block HEAVY_WEIGHTED_PRESSURE_PLATE;
        public static final BlockRedstoneComparator UNPOWERED_COMPARATOR;
        public static final BlockRedstoneComparator POWERED_COMPARATOR;
        public static final BlockDaylightDetector DAYLIGHT_DETECTOR;
        public static final BlockDaylightDetector DAYLIGHT_DETECTOR_INVERTED;
        public static final Block REDSTONE_BLOCK;
        public static final Block QUARTZ_ORE;
        public static final BlockHopper HOPPER;
        public static final Block QUARTZ_BLOCK;
        public static final Block QUARTZ_STAIRS;
        public static final Block ACTIVATOR_RAIL;
        public static final Block DROPPER;
        public static final Block STAINED_HARDENED_CLAY;
        public static final Block BARRIER;
        public static final Block IRON_TRAPDOOR;
        public static final Block HAY_BLOCK;
        public static final Block CARPET;
        public static final Block HARDENED_CLAY;
        public static final Block COAL_BLOCK;
        public static final Block PACKED_ICE;
        public static final Block ACACIA_STAIRS;
        public static final Block DARK_OAK_STAIRS;
        public static final Block SLIME_BLOCK;
        public static final BlockDoublePlant DOUBLE_PLANT;
        public static final BlockStainedGlass STAINED_GLASS;
        public static final BlockStainedGlassPane STAINED_GLASS_PANE;
        public static final Block PRISMARINE;
        public static final Block SEA_LANTERN;
        public static final Block STANDING_BANNER;
        public static final Block WALL_BANNER;
        public static final Block RED_SANDSTONE;
        public static final Block RED_SANDSTONE_STAIRS;
        public static final BlockSlab DOUBLE_STONE_SLAB2;
        public static final BlockSlab STONE_SLAB2;
        public static final Block END_ROD;
        public static final Block CHORUS_PLANT;
        public static final Block CHORUS_FLOWER;
        public static final Block PURPUR_BLOCK;
        public static final Block PURPUR_PILLAR;
        public static final Block PURPUR_STAIRS;
        public static final BlockSlab PURPUR_DOUBLE_SLAB;
        public static final BlockSlab PURPUR_SLAB;
        public static final Block END_BRICKS;
        public static final Block BEETROOTS;
        public static final Block GRASS_PATH;
        public static final Block END_GATEWAY;
        public static final Block REPEATING_COMMAND_BLOCK;
        public static final Block CHAIN_COMMAND_BLOCK;
        public static final Block FROSTED_ICE;
        public static final Block MAGMA;
        public static final Block NETHER_WART_BLOCK;
        public static final Block RED_NETHER_BRICK;
        public static final Block BONE_BLOCK;
        public static final Block STRUCTURE_VOID;
        public static final Block STRUCTURE_BLOCK;
        */

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
        register(Material.GOURD, 0.84F);
        register(Material.DRAGON_EGG, 0.84F);
        register(Material.PORTAL, 1);
        register(Material.CAKE, 2);
        register(Material.WEB, 0.84F);
        register(Material.PISTON, 0.9F);
        register(Material.BARRIER, 1);
        register(Material.STRUCTURE_VOID, 1);
    }

    /** Registers a block with a specific heating value */
    public static void register(Block block, float f) {
        if (block != null && f > 0) {
            blockStateToHeatMap.put(block.getDefaultState(), f);
        }
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
