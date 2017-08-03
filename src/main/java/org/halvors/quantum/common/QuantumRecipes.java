package org.halvors.quantum.common;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.halvors.quantum.common.block.machine.BlockMachine.EnumMachine;
import org.halvors.quantum.common.block.machine.BlockMachineModel.EnumMachineModel;
import org.halvors.quantum.common.block.reactor.fusion.BlockElectromagnet;
import org.halvors.quantum.common.utility.FluidUtility;

public class QuantumRecipes {
    public static void register() {
        // Register recipes.

        // Antimatter
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(QuantumItems.itemAntimatterCell, 1, 1), QuantumItems.itemAntimatterCell, QuantumItems.itemAntimatterCell, QuantumItems.itemAntimatterCell, QuantumItems.itemAntimatterCell, QuantumItems.itemAntimatterCell, QuantumItems.itemAntimatterCell, QuantumItems.itemAntimatterCell, QuantumItems.itemAntimatterCell));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(QuantumItems.itemAntimatterCell, 8, 0), new ItemStack(QuantumItems.itemAntimatterCell, 1, 1)));

        // Empty Cell
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(QuantumItems.itemCell, 16), " T ", "TGT", " T ", 'T', "ingotTin", 'G', Blocks.GLASS));

        // Water Cell
        GameRegistry.addRecipe(new ShapelessOreRecipe(FluidUtility.getFilledCell(FluidRegistry.WATER), "cellEmpty", Items.WATER_BUCKET));

        // Breeder Fuel Rod
        GameRegistry.addRecipe(new ShapedOreRecipe(QuantumItems.itemBreederFuel, "CUC", "CUC", "CUC", 'U', "breederUranium", 'C', "cellEmpty"));

        // Fissile Fuel
        GameRegistry.addRecipe(new ShapedOreRecipe(QuantumItems.itemFissileFuel, "CUC", "CUC", "CUC", 'U', "ingotUranium", 'C', "cellEmpty"));

        // Hazmat
        GameRegistry.addRecipe(new ShapedOreRecipe(QuantumItems.itemHazmatMask, "SSS", "BAB", "SCS", 'A', Items.LEATHER_HELMET, 'C', QuantumItems.itemCircuitBasic, 'S', Blocks.WOOL));
        GameRegistry.addRecipe(new ShapedOreRecipe(QuantumItems.itemHazmatBody, "SSS", "BAB", "SCS", 'A', Items.LEATHER_CHESTPLATE, 'C', QuantumItems.itemCircuitBasic, 'S', Blocks.WOOL));
        GameRegistry.addRecipe(new ShapedOreRecipe(QuantumItems.itemHazmatLeggings, "SSS", "BAB", "SCS", 'A', Items.LEATHER_LEGGINGS, 'C', QuantumItems.itemCircuitBasic, 'S', Blocks.WOOL));
        GameRegistry.addRecipe(new ShapedOreRecipe(QuantumItems.itemHazmatBoots, "SSS", "BAB", "SCS", 'A', Items.LEATHER_BOOTS, 'C', QuantumItems.itemCircuitBasic, 'S', Blocks.WOOL));

        // Control Rod
        GameRegistry.addRecipe(new ShapedOreRecipe(QuantumBlocks.blockControlRod, "I", "I", "I", 'I', Items.IRON_INGOT));

        // Turbine
        GameRegistry.addRecipe(new ShapedOreRecipe(QuantumBlocks.blockElectricTurbine, " B ", "BMB", " B ", 'B', QuantumItems.itemPlateBronze, 'M', QuantumItems.itemMotor));

        // Electromagnet
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(QuantumBlocks.blockElectromagnet, 2), "BBB", "BMB", "BBB", 'B', "ingotBronze", 'M', QuantumItems.itemMotor));

        // Electromagnet Glass
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(QuantumBlocks.blockElectromagnet, 1, BlockElectromagnet.EnumElectromagnet.GLASS.ordinal()), QuantumBlocks.blockElectromagnet, Blocks.GLASS));

        // Fulmination Generator
        GameRegistry.addRecipe(new ShapedOreRecipe(QuantumBlocks.blockFulmination, "OSO", "SCS", "OSO", 'O', Blocks.OBSIDIAN, 'C', QuantumItems.itemCircuitAdvanced, 'S', QuantumItems.itemPlateSteel));

        // Particle Accelerator
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(QuantumBlocks.blockMachine, 1, EnumMachine.ACCELERATOR.ordinal()), "SCS", "CMC", "SCS", 'M', QuantumItems.itemMotor, 'C', QuantumItems.itemCircuitElite, 'S', QuantumItems.itemPlateSteel));

        // Gas Funnel
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(QuantumBlocks.blockMachine, 2, EnumMachine.GAS_FUNNEL.ordinal()), " B ", "B B", "B B", 'B', "ingotBronze"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(QuantumBlocks.blockMachine, 2, EnumMachine.GAS_FUNNEL.ordinal()), " B ", "B B", "B B", 'B', "ingotIron"));

        // Chemical Extractor
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(QuantumBlocks.blockMachineModel, 1, EnumMachineModel.CHEMICAL_EXTRACTOR.ordinal()), "BSB", "MCM", "BSB", 'C', QuantumItems.itemCircuitElite, 'S', QuantumItems.itemPlateSteel, 'B', QuantumItems.itemIngotBronze, 'M', QuantumItems.itemMotor));

        // Gas Centrifuge
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(QuantumBlocks.blockMachineModel, 1, EnumMachineModel.GAS_CENTRIFUGE.ordinal()), "BSB", "MCM", "BSB", 'C', QuantumItems.itemCircuitAdvanced, 'S', QuantumItems.itemPlateSteel, 'B', "ingotBronze", 'M', QuantumItems.itemMotor));

        // Nuclear Boiler
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(QuantumBlocks.blockMachineModel, 1, EnumMachineModel.NUCLEAR_BOILER.ordinal()), "S S", "FBF", "SMS", 'F', Blocks.FURNACE, 'S', QuantumItems.itemPlateSteel, 'B', Items.BUCKET, 'M', QuantumItems.itemMotor));

        // Quantum Assembler
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(QuantumBlocks.blockMachineModel, 1, EnumMachineModel.QUANTUM_ASSEMBLER.ordinal()), "CCC", "SXS", "SSS", 'X', new ItemStack(QuantumBlocks.blockMachineModel, 1, EnumMachineModel.GAS_CENTRIFUGE.ordinal()), 'C', QuantumItems.itemCircuitElite, 'S', QuantumItems.itemPlateSteel));

        // Siren
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(QuantumBlocks.blockSiren, 2), "NPN", 'N', Blocks.NOTEBLOCK, 'P', QuantumItems.itemPlateBronze));

        // Thermometer
        GameRegistry.addRecipe(new ShapedOreRecipe(QuantumBlocks.blockThermometer, "SSS", "GCG", "GSG", 'S', QuantumItems.itemPlateSteel, 'G', Blocks.GLASS, 'C', QuantumItems.itemCircuitBasic));

        // Plasma Heater
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(QuantumBlocks.blockMachineModel, 1, EnumMachineModel.PLASMA_HEATER.ordinal()), "CPC", "PFP", "CPC", 'P', QuantumItems.itemPlateSteel, 'F', QuantumBlocks.blockReactorCell, 'C', QuantumItems.itemCircuitElite));

        // Fission Reactor
        GameRegistry.addRecipe(new ShapedOreRecipe(QuantumBlocks.blockReactorCell, "SCS", "MEM", "SCS", 'E', "cellEmpty", 'C', QuantumItems.itemCircuitAdvanced, 'S', QuantumItems.itemPlateSteel, 'M', QuantumItems.itemMotor));

		/*
		// IC2 Recipes
		if (Loader.isModLoaded("IC2") && Settings.allowAlternateRecipes)
		{
			OreDictionary.registerOre("cellEmpty", Items.getItem("cell"));

			// Check to make sure we have actually registered the Ore, otherwise tell the user about
			// it.
			String cellEmptyName = OreDictionary.getOreName(OreDictionary.getOreID("cellEmpty"));
			if (cellEmptyName == "Unknown")
			{
				ResonantInduction.LOGGER.info("Unable to register cellEmpty in OreDictionary!");
			}

			// IC2 exchangeable recipes
			GameRegistry.addRecipe(new ShapelessOreRecipe(itemYellowCake, Items.getItem("reactorUraniumSimple")));
			GameRegistry.addRecipe(new ShapelessOreRecipe(Items.getItem("cell"), itemCell));
			GameRegistry.addRecipe(new ShapelessOreRecipe(itemCell, "cellEmpty"));
		}
		*/
    }
}
