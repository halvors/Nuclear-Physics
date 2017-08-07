package org.halvors.quantum.atomic.common;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.halvors.quantum.atomic.common.block.machine.BlockMachine.EnumMachine;
import org.halvors.quantum.atomic.common.block.machine.BlockMachineModel.EnumMachineModel;
import org.halvors.quantum.atomic.common.block.reactor.fusion.BlockElectromagnet;
import org.halvors.quantum.atomic.common.utility.FluidUtility;
import org.halvors.quantum.core.init.ComponentItems;

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
        GameRegistry.addRecipe(new ShapedOreRecipe(QuantumItems.itemHazmatMask, "SSS", "BAB", "SCS", 'A', Items.LEATHER_HELMET, 'C', ComponentItems.itemCircuitBasic, 'S', Blocks.WOOL));
        GameRegistry.addRecipe(new ShapedOreRecipe(QuantumItems.itemHazmatBody, "SSS", "BAB", "SCS", 'A', Items.LEATHER_CHESTPLATE, 'C', ComponentItems.itemCircuitBasic, 'S', Blocks.WOOL));
        GameRegistry.addRecipe(new ShapedOreRecipe(QuantumItems.itemHazmatLeggings, "SSS", "BAB", "SCS", 'A', Items.LEATHER_LEGGINGS, 'C', ComponentItems.itemCircuitBasic, 'S', Blocks.WOOL));
        GameRegistry.addRecipe(new ShapedOreRecipe(QuantumItems.itemHazmatBoots, "SSS", "BAB", "SCS", 'A', Items.LEATHER_BOOTS, 'C', ComponentItems.itemCircuitBasic, 'S', Blocks.WOOL));

        // Control Rod
        GameRegistry.addRecipe(new ShapedOreRecipe(QuantumBlocks.blockControlRod, "I", "I", "I", 'I', Items.IRON_INGOT));

        // Turbine
        GameRegistry.addRecipe(new ShapedOreRecipe(QuantumBlocks.blockElectricTurbine, " B ", "BMB", " B ", 'B', ComponentItems.itemPlateBronze, 'M', ComponentItems.itemMotor));

        // Electromagnet
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(QuantumBlocks.blockElectromagnet, 2), "BBB", "BMB", "BBB", 'B', "ingotBronze", 'M', ComponentItems.itemMotor));

        // Electromagnet Glass
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(QuantumBlocks.blockElectromagnet, 1, BlockElectromagnet.EnumElectromagnet.GLASS.ordinal()), QuantumBlocks.blockElectromagnet, Blocks.GLASS));

        // Fulmination Generator
        GameRegistry.addRecipe(new ShapedOreRecipe(QuantumBlocks.blockFulmination, "OSO", "SCS", "OSO", 'O', Blocks.OBSIDIAN, 'C', ComponentItems.itemCircuitAdvanced, 'S', ComponentItems.itemPlateSteel));

        // Particle Accelerator
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(QuantumBlocks.blockMachine, 1, EnumMachine.ACCELERATOR.ordinal()), "SCS", "CMC", "SCS", 'M', ComponentItems.itemMotor, 'C', ComponentItems.itemCircuitElite, 'S', ComponentItems.itemPlateSteel));

        // Gas Funnel
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(QuantumBlocks.blockMachine, 2, EnumMachine.GAS_FUNNEL.ordinal()), " B ", "B B", "B B", 'B', "ingotBronze"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(QuantumBlocks.blockMachine, 2, EnumMachine.GAS_FUNNEL.ordinal()), " B ", "B B", "B B", 'B', "ingotIron"));

        // Chemical Extractor
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(QuantumBlocks.blockMachineModel, 1, EnumMachineModel.CHEMICAL_EXTRACTOR.ordinal()), "BSB", "MCM", "BSB", 'C', ComponentItems.itemCircuitElite, 'S', ComponentItems.itemPlateSteel, 'B', ComponentItems.itemIngotBronze, 'M', ComponentItems.itemMotor));

        // Gas Centrifuge
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(QuantumBlocks.blockMachineModel, 1, EnumMachineModel.GAS_CENTRIFUGE.ordinal()), "BSB", "MCM", "BSB", 'C', ComponentItems.itemCircuitAdvanced, 'S', ComponentItems.itemPlateSteel, 'B', "ingotBronze", 'M', ComponentItems.itemMotor));

        // Nuclear Boiler
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(QuantumBlocks.blockMachineModel, 1, EnumMachineModel.NUCLEAR_BOILER.ordinal()), "S S", "FBF", "SMS", 'F', Blocks.FURNACE, 'S', ComponentItems.itemPlateSteel, 'B', Items.BUCKET, 'M', ComponentItems.itemMotor));

        // Quantum Assembler
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(QuantumBlocks.blockMachineModel, 1, EnumMachineModel.QUANTUM_ASSEMBLER.ordinal()), "CCC", "SXS", "SSS", 'X', new ItemStack(QuantumBlocks.blockMachineModel, 1, EnumMachineModel.GAS_CENTRIFUGE.ordinal()), 'C', ComponentItems.itemCircuitElite, 'S', ComponentItems.itemPlateSteel));

        // Siren
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(QuantumBlocks.blockSiren, 2), "NPN", 'N', Blocks.NOTEBLOCK, 'P', ComponentItems.itemPlateBronze));

        // Thermometer
        GameRegistry.addRecipe(new ShapedOreRecipe(QuantumBlocks.blockThermometer, "SSS", "GCG", "GSG", 'S', ComponentItems.itemPlateSteel, 'G', Blocks.GLASS, 'C', ComponentItems.itemCircuitBasic));

        // Plasma Heater
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(QuantumBlocks.blockMachineModel, 1, EnumMachineModel.PLASMA_HEATER.ordinal()), "CPC", "PFP", "CPC", 'P', ComponentItems.itemPlateSteel, 'F', QuantumBlocks.blockReactorCell, 'C', ComponentItems.itemCircuitElite));

        // Fission Reactor
        GameRegistry.addRecipe(new ShapedOreRecipe(QuantumBlocks.blockReactorCell, "SCS", "MEM", "SCS", 'E', "cellEmpty", 'C', ComponentItems.itemCircuitAdvanced, 'S', ComponentItems.itemPlateSteel, 'M', ComponentItems.itemMotor));

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
