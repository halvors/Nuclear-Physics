package org.halvors.nuclearphysics.common.init;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.halvors.nuclearphysics.common.block.machine.BlockMachine.EnumMachine;
import org.halvors.nuclearphysics.common.block.machine.BlockMachineModel.EnumMachineModel;
import org.halvors.nuclearphysics.common.block.reactor.fusion.BlockElectromagnet.EnumElectromagnet;
import org.halvors.nuclearphysics.common.item.particle.ItemAntimatterCell.EnumAntimatterCell;
import org.halvors.nuclearphysics.common.utility.FluidUtility;

public class ModRecipes {
    public static void registerRecipes() {
        // Register recipes.

        // TODO: Oredictionaficate.
        // TODO: IC2 recipes? Mekanism recipes?

        // Wrench.
        GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.itemWrench, " S ", " SS", "S  ", 'S', "ingotSteel"));

        // Copper wire.
        GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.itemCopperWire, "WCW", "WCW", "WCW", 'W', Blocks.WOOL, 'C', "ingotCopper"));
        GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.itemCopperWire, "LCL", "LCL", "LCL", 'L', Items.LEATHER, 'C', "ingotCopper"));

        // Motor.
        GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.itemMotor, "WSW", "SIS", "WSW", 'W', ModItems.itemCopperWire, 'S', "ingotSteel", 'I', Items.IRON_INGOT));

        // Bronze plate.
        GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.itemPlateBronze, "BB ", "BB ", 'B', "ingotBronze"));

        // Steel plate.
        GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.itemPlateSteel, "SS ", "SS ", 'S', "ingotSteel"));

        // Basic circuit.
        GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.itemCircuitBasic, "WRW", "RPR", "WRW", 'W', ModItems.itemCopperWire, 'R', Items.REDSTONE, 'P', "plateBronze"));
        GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.itemCircuitBasic, "WRW", "RPR", "WRW", 'W', ModItems.itemCopperWire, 'R', Items.REDSTONE, 'P', "plateSteel"));

        // Advanced circuit.
        GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.itemCircuitAdvanced, "RRR", "CDC", "RRR", 'R', Items.REDSTONE, 'C', "circuitBasic", 'D', Items.DIAMOND));

        // Elite circuit.
        GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.itemCircuitElite, "GGG", "CLC", "GGG", 'G', Items.GOLD_INGOT, 'C', "circuitAdvanced", 'L', Blocks.LAPIS_BLOCK));

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Antimatter
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.itemAntimatterCell, 1, EnumAntimatterCell.MILLIGRAM.ordinal()), ModItems.itemAntimatterCell, ModItems.itemAntimatterCell, ModItems.itemAntimatterCell, ModItems.itemAntimatterCell, ModItems.itemAntimatterCell, ModItems.itemAntimatterCell, ModItems.itemAntimatterCell, ModItems.itemAntimatterCell));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.itemAntimatterCell, 8, 0), new ItemStack(ModItems.itemAntimatterCell, 1, EnumAntimatterCell.GRAM.ordinal())));

        // Empty Cell
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemCell, 16), " T ", "TGT", " T ", 'T', "ingotTin", 'G', Blocks.GLASS));

        // Water Cell
        GameRegistry.addRecipe(new ShapelessOreRecipe(FluidUtility.getFilledCell(FluidRegistry.WATER), "cellEmpty", Items.WATER_BUCKET));

        // Breeder Fuel Rod
        GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.itemBreederFuel, "CUC", "CUC", "CUC", 'U', "breederUranium", 'C', ModItems.itemCell));

        // Fissile Fuel
        GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.itemFissileFuel, "CUC", "CUC", "CUC", 'U', "ingotUranium", 'C', ModItems.itemCell));

        // Hazmat
        GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.itemHazmatMask, "SSS", "BAB", "SCS", 'A', Items.LEATHER_HELMET, 'C', "circuitBasic", 'S', Blocks.WOOL));
        GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.itemHazmatBody, "SSS", "BAB", "SCS", 'A', Items.LEATHER_CHESTPLATE, 'C', "circuitBasic", 'S', Blocks.WOOL));
        GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.itemHazmatLeggings, "SSS", "BAB", "SCS", 'A', Items.LEATHER_LEGGINGS, 'C', "circuitBasic", 'S', Blocks.WOOL));
        GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.itemHazmatBoots, "SSS", "BAB", "SCS", 'A', Items.LEATHER_BOOTS, 'C', "circuitBasic", 'S', Blocks.WOOL));

        // Control Rod
        GameRegistry.addRecipe(new ShapedOreRecipe(ModBlocks.blockControlRod, "I", "I", "I", 'I', Items.IRON_INGOT));

        // Turbine
        GameRegistry.addRecipe(new ShapedOreRecipe(ModBlocks.blockElectricTurbine, " B ", "BMB", " B ", 'B', "plateBronze", 'M', ModItems.itemMotor));

        // Electromagnet
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockElectromagnet, 2), "BBB", "BMB", "BBB", 'B', "ingotBronze", 'M', ModItems.itemMotor));

        // Electromagnet Glass
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.blockElectromagnet, 1, EnumElectromagnet.GLASS.ordinal()), ModBlocks.blockElectromagnet, Blocks.GLASS));

        // Fulmination Generator
        GameRegistry.addRecipe(new ShapedOreRecipe(ModBlocks.blockFulmination, "OSO", "SCS", "OSO", 'O', Blocks.OBSIDIAN, 'C', "circuitAdvanced", 'S', "plateSteel"));

        // Particle Accelerator
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockMachine, 1, EnumMachine.ACCELERATOR.ordinal()), "SCS", "CMC", "SCS", 'M', ModItems.itemMotor, 'C', "circuitElite", 'S', "plateSteel"));

        // Gas Funnel
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockGasFunnel, 2), " B ", "B B", "B B", 'B', "ingotBronze"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockGasFunnel, 2), " I ", "I I", "I I", 'I', Items.IRON_INGOT));

        // Chemical Extractor
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockMachineModel, 1, EnumMachineModel.CHEMICAL_EXTRACTOR.ordinal()), "BSB", "MCM", "BSB", 'C', "circuitElite", 'S', "plateSteel", 'B', "ingotBronze", 'M', ModItems.itemMotor));

        // Gas Centrifuge
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockMachineModel, 1, EnumMachineModel.GAS_CENTRIFUGE.ordinal()), "BSB", "MCM", "BSB", 'C', "circuitAdvanced", 'S', "plateSteel", 'B', "ingotBronze", 'M', ModItems.itemMotor));

        // Nuclear Boiler
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockMachineModel, 1, EnumMachineModel.NUCLEAR_BOILER.ordinal()), "S S", "FBF", "SMS", 'F', Blocks.FURNACE, 'S', "plateSteel", 'B', Items.BUCKET, 'M', ModItems.itemMotor));

        // NuclearPhysics Assembler
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockMachineModel, 1, EnumMachineModel.QUANTUM_ASSEMBLER.ordinal()), "CCC", "SXS", "SSS", 'X', new ItemStack(ModBlocks.blockMachineModel, 1, EnumMachineModel.GAS_CENTRIFUGE.ordinal()), 'C', "circuitElite", 'S', "plateSteel"));

        // Siren
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockSiren, 2), "NPN", 'N', Blocks.NOTEBLOCK, 'P', "plateBronze"));

        // Thermometer
        GameRegistry.addRecipe(new ShapedOreRecipe(ModBlocks.blockThermometer, "SSS", "GCG", "GSG", 'S', "plateSteel", 'G', Blocks.GLASS, 'C', "circuitBasic"));

        // Plasma Heater
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockMachineModel, 1, EnumMachineModel.PLASMA_HEATER.ordinal()), "CPC", "PFP", "CPC", 'P', "plateSteel", 'F', ModBlocks.blockReactorCell, 'C', "circuitElite"));

        // Fission Reactor
        GameRegistry.addRecipe(new ShapedOreRecipe(ModBlocks.blockReactorCell, "SCS", "MEM", "SCS", 'E', ModItems.itemCell, 'C', "circuitAdvanced", 'S', "plateSteel", 'M', ModItems.itemMotor));
    }
}
