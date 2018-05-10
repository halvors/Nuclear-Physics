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
        register(Blocks.GRASS.getDefaultState().withProperty(BlockGrass.SNOWY, false), 0);
        register(Blocks.GRASS.getDefaultState().withProperty(BlockGrass.SNOWY, true), 0);
        register(Blocks.DIRT.getDefaultState().withProperty(BlockGrass.SNOWY, false), 0);
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
        register(Blocks.LOG2.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA), 0);
        register(Blocks.LOG2.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.DARK_OAK), 0);
        register(Blocks.LEAVES.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.OAK).withProperty(BlockLeaves.CHECK_DECAY, true).withProperty(BlockLeaves.DECAYABLE, true), 0);
        register(Blocks.LEAVES.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty(BlockLeaves.CHECK_DECAY, true).withProperty(BlockLeaves.DECAYABLE, true), 0);
        register(Blocks.LEAVES.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.BIRCH).withProperty(BlockLeaves.CHECK_DECAY, true).withProperty(BlockLeaves.DECAYABLE, true), 0);
        register(Blocks.LEAVES.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.JUNGLE).withProperty(BlockLeaves.CHECK_DECAY, true).withProperty(BlockLeaves.DECAYABLE, true), 0);
        register(Blocks.LEAVES2.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA).withProperty(BlockLeaves.CHECK_DECAY, true).withProperty(BlockLeaves.DECAYABLE, true), 0);
        register(Blocks.LEAVES2.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.DARK_OAK).withProperty(BlockLeaves.CHECK_DECAY, true).withProperty(BlockLeaves.DECAYABLE, true), 0);

        register(Blocks.SPONGE.getDefaultState().withProperty(BlockSponge.WET, false), 0);
        register(Blocks.SPONGE.getDefaultState().withProperty(BlockSponge.WET, true), 0);

        register(Blocks.GLASS.getDefaultState(), 0);
        register(Blocks.LAPIS_ORE.getDefaultState(), 0);
        register(Blocks.LAPIS_BLOCK.getDefaultState(), 0);

        for (final EnumFacing facing : EnumFacing.values()) {
            final IBlockState state = Blocks.DISPENSER.getDefaultState().withProperty(BlockDispenser.FACING, facing);
            final int specificHeatCapacity = 0;

            register(state.withProperty(BlockDispenser.TRIGGERED, false), specificHeatCapacity);
            register(state.withProperty(BlockDispenser.TRIGGERED, true), specificHeatCapacity);
        }

        register(Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.DEFAULT), 0);
        register(Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.CHISELED), 0);
        register(Blocks.SANDSTONE.getDefaultState().withProperty(BlockSandStone.TYPE, BlockSandStone.EnumType.SMOOTH), 0);
        register(Blocks.NOTEBLOCK.getDefaultState(), 0);

        for (final EnumFacing facing : EnumFacing.values()) {
            final IBlockState state = Blocks.BED.getDefaultState().withProperty(BlockDispenser.FACING, facing);
            final int specificHeatCapacity = 0;

            register(state.withProperty(BlockBed.PART, BlockBed.EnumPartType.FOOT).withProperty(BlockBed.OCCUPIED, false), specificHeatCapacity);
            register(state.withProperty(BlockBed.PART, BlockBed.EnumPartType.FOOT).withProperty(BlockBed.OCCUPIED, true), specificHeatCapacity);
            register(state.withProperty(BlockBed.PART, BlockBed.EnumPartType.HEAD).withProperty(BlockBed.OCCUPIED, false), specificHeatCapacity);
            register(state.withProperty(BlockBed.PART, BlockBed.EnumPartType.HEAD).withProperty(BlockBed.OCCUPIED, true), specificHeatCapacity);
        }

        for (final BlockRailBase.EnumRailDirection railDirection : BlockRailBase.EnumRailDirection.values()) {
            final IBlockState state = Blocks.GOLDEN_RAIL.getDefaultState().withProperty(BlockRailPowered.SHAPE, railDirection);
            final int specificHeatCapacity = 0;

            register(state.withProperty(BlockRailPowered.POWERED, false), specificHeatCapacity);
            register(state.withProperty(BlockRailPowered.POWERED, true), specificHeatCapacity);
        }

        for (final BlockRailBase.EnumRailDirection railDirection : BlockRailBase.EnumRailDirection.values()) {
            final IBlockState state = Blocks.DETECTOR_RAIL.getDefaultState().withProperty(BlockRailDetector.SHAPE, railDirection);
            final int specificHeatCapacity = 0;

            register(state.withProperty(BlockRailDetector.POWERED, false), specificHeatCapacity);
            register(state.withProperty(BlockRailDetector.POWERED, true), specificHeatCapacity);
        }

        for (final EnumFacing facing : EnumFacing.values()) {
            final IBlockState state = Blocks.STICKY_PISTON.getDefaultState().withProperty(BlockPistonBase.FACING, facing);
            final int specificHeatCapacity = 0;

            register(state.withProperty(BlockPistonBase.EXTENDED, false), specificHeatCapacity);
            register(state.withProperty(BlockPistonBase.EXTENDED, true), specificHeatCapacity);
        }

        register(Blocks.WEB.getDefaultState(), 0);
        register(Blocks.WEB.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.DEAD_BUSH), 0);
        register(Blocks.WEB.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.GRASS), 0);
        register(Blocks.WEB.getDefaultState().withProperty(BlockTallGrass.TYPE, BlockTallGrass.EnumType.FERN), 0);
        register(Blocks.DEADBUSH.getDefaultState(), 0);

        for (final EnumFacing facing : EnumFacing.values()) {
            final IBlockState state = Blocks.PISTON.getDefaultState().withProperty(BlockPistonBase.FACING, facing);
            final int specificHeatCapacity = 0;

            register(state.withProperty(BlockPistonBase.EXTENDED, false), specificHeatCapacity);
            register(state.withProperty(BlockPistonBase.EXTENDED, true), specificHeatCapacity);
        }

        for (final EnumFacing facing : EnumFacing.values()) {
            final IBlockState state = Blocks.PISTON_HEAD.getDefaultState().withProperty(BlockPistonExtension.FACING, facing);
            final int specificHeatCapacity = 0;

            register(state.withProperty(BlockPistonExtension.TYPE, BlockPistonExtension.EnumPistonType.DEFAULT).withProperty(BlockPistonExtension.SHORT, false), specificHeatCapacity);
            register(state.withProperty(BlockPistonExtension.TYPE, BlockPistonExtension.EnumPistonType.DEFAULT).withProperty(BlockPistonExtension.SHORT, true), specificHeatCapacity);
            register(state.withProperty(BlockPistonExtension.TYPE, BlockPistonExtension.EnumPistonType.STICKY).withProperty(BlockPistonExtension.SHORT, false), specificHeatCapacity);
            register(state.withProperty(BlockPistonExtension.TYPE, BlockPistonExtension.EnumPistonType.STICKY).withProperty(BlockPistonExtension.SHORT, true), specificHeatCapacity);
        }

        for (final EnumDyeColor color : EnumDyeColor.values()) {
            register(Blocks.WOOL.getDefaultState().withProperty(BlockColored.COLOR, color), 0);
        }

        for (final EnumFacing facing : EnumFacing.values()) {
            final IBlockState state = Blocks.PISTON_EXTENSION.getDefaultState().withProperty(BlockPistonExtension.FACING, facing);
            final int specificHeatCapacity = 0;

            register(state.withProperty(BlockPistonExtension.TYPE, BlockPistonExtension.EnumPistonType.DEFAULT), specificHeatCapacity);
            register(state.withProperty(BlockPistonExtension.TYPE, BlockPistonExtension.EnumPistonType.STICKY), specificHeatCapacity);
        }

        register(Blocks.YELLOW_FLOWER.getDefaultState(), 0);
        register(Blocks.RED_FLOWER.getDefaultState(), 0);
        register(Blocks.BROWN_MUSHROOM.getDefaultState(), 0);
        register(Blocks.RED_MUSHROOM.getDefaultState(), 0);
        register(Blocks.GOLD_BLOCK.getDefaultState(), 0);
        register(Blocks.IRON_BLOCK.getDefaultState(), 0);


        /*
        register(Blocks.DOUBLE_STONE_SLAB.getDefaultState().withProperty(Block), 0);

        BlockSlab DOUBLE_STONE_SLAB;

        public static final PropertyEnum<BlockSlab.EnumBlockHalf> HALF = PropertyEnum.<BlockSlab.EnumBlockHalf>create("half", BlockSlab.EnumBlockHalf.class);
        public static final PropertyBool SEAMLESS = PropertyBool.create("seamless");
        public static final PropertyEnum<BlockStoneSlab.EnumType> VARIANT = PropertyEnum.<BlockStoneSlab.EnumType>create("variant", BlockStoneSlab.EnumType.class);

        BlockSlab STONE_SLAB;
        */

        register(Blocks.BRICK_BLOCK.getDefaultState(), 0);
        register(Blocks.TNT.getDefaultState().withProperty(BlockTNT.EXPLODE, false), 0);
        register(Blocks.TNT.getDefaultState().withProperty(BlockTNT.EXPLODE, true), 0);
        register(Blocks.BOOKSHELF.getDefaultState(), 0);
        register(Blocks.MOSSY_COBBLESTONE.getDefaultState(), 0);
        register(Blocks.OBSIDIAN.getDefaultState(), 0);

        /*
        Blocks.TORCH;
        */

        /*
        for (int age = 0; age <= 15; age++) {
            final IBlockState state = Blocks.FIRE.getDefaultState().withProperty(BlockFire.AGE, age);
            final int specificHeatCapacity = 0;

            register(state.withProperty(BlockFire.NORTH, false), 0);
            register(state.withProperty(BlockFire.NORTH, true), 0);

            register(state.withProperty(BlockFire.NORTH, false).withProperty(BlockFire.EAST, false), 0);
            register(state.withProperty(BlockFire.NORTH, false).withProperty(BlockFire.EAST, true), 0);
            register(state.withProperty(BlockFire.NORTH, true).withProperty(BlockFire.EAST, false), 0);
            register(state.withProperty(BlockFire.NORTH, true).withProperty(BlockFire.EAST, true), 0);


            register(state.withProperty(BlockFire.NORTH, false).withProperty(BlockFire.EAST, false).withProperty(BlockFire.SOUTH, false), 0);
            register(state.withProperty(BlockFire.NORTH, false).withProperty(BlockFire.EAST, false).withProperty(BlockFire.SOUTH, true), 0);
            register(state.withProperty(BlockFire.NORTH, false).withProperty(BlockFire.EAST, true).withProperty(BlockFire.SOUTH, false), 0);
            register(state.withProperty(BlockFire.NORTH, true).withProperty(BlockFire.EAST, false).withProperty(BlockFire.SOUTH, false), 0);

            register(state.withProperty(BlockFire.NORTH, false).withProperty(BlockFire.EAST, false).withProperty(BlockFire.SOUTH, false), 0);


            // Continue here....



            register(state.withProperty(BlockFire.NORTH, false).withProperty(BlockFire.EAST, false).withProperty(BlockFire.SOUTH, true), 0);
            register(state.withProperty(BlockFire.NORTH, false).withProperty(BlockFire.EAST, true).withProperty(BlockFire.SOUTH, false), 0);
            register(state.withProperty(BlockFire.NORTH, true).withProperty(BlockFire.EAST, false).withProperty(BlockFire.SOUTH, false), 0);



            register(state.withProperty(BlockFire.NORTH, false).withProperty(BlockFire.EAST, true), 0);
            register(state.withProperty(BlockFire.NORTH, true).withProperty(BlockFire.EAST, false), 0);
            register(state.withProperty(BlockFire.NORTH, true).withProperty(BlockFire.EAST, true), 0);

            register(state.withProperty(BlockFire.NORTH, true).withProperty(BlockFire.EAST, false), 0);
            register(state, 0);
            register(state, 0);
        }

        public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 15);
        public static final PropertyBool NORTH = PropertyBool.create("north");
        public static final PropertyBool EAST = PropertyBool.create("east");
        public static final PropertyBool SOUTH = PropertyBool.create("south");
        public static final PropertyBool WEST = PropertyBool.create("west");
        public static final PropertyBool UPPER = PropertyBool.create("up");


        BlockFire FIRE;

        /*
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
        */

        /*
        protected PropertyEnum<BlockFlower.EnumFlowerType> type;

        DANDELION(BlockFlower.EnumFlowerColor.YELLOW, 0, "dandelion"),
                POPPY(BlockFlower.EnumFlowerColor.RED, 0, "poppy"),
                BLUE_ORCHID(BlockFlower.EnumFlowerColor.RED, 1, "blue_orchid", "blueOrchid"),
                ALLIUM(BlockFlower.EnumFlowerColor.RED, 2, "allium"),
                HOUSTONIA(BlockFlower.EnumFlowerColor.RED, 3, "houstonia"),
                RED_TULIP(BlockFlower.EnumFlowerColor.RED, 4, "red_tulip", "tulipRed"),
                ORANGE_TULIP(BlockFlower.EnumFlowerColor.RED, 5, "orange_tulip", "tulipOrange"),
                WHITE_TULIP(BlockFlower.EnumFlowerColor.RED, 6, "white_tulip", "tulipWhite"),
                PINK_TULIP(BlockFlower.EnumFlowerColor.RED, 7, "pink_tulip", "tulipPink"),
                OXEYE_DAISY(BlockFlower.EnumFlowerColor.RED, 8, "oxeye_daisy", "oxeyeDaisy");
        */

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        /*
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
        */
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
}
