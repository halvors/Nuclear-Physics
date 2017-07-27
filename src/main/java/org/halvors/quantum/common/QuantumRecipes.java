package org.halvors.quantum.common;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.halvors.quantum.common.block.machine.BlockMachine.EnumMachine;

public class QuantumRecipes {
    public static void register() {
        // Register recipes.

        // Cells
        // Antimatter
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(QuantumItems.itemAntimatterCell, 1, 1), QuantumItems.itemAntimatterCell, QuantumItems.itemAntimatterCell, QuantumItems.itemAntimatterCell, QuantumItems.itemAntimatterCell, QuantumItems.itemAntimatterCell, QuantumItems.itemAntimatterCell, QuantumItems.itemAntimatterCell, QuantumItems.itemAntimatterCell));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(QuantumItems.itemAntimatterCell, 8, 0), new ItemStack(QuantumItems.itemAntimatterCell, 1, 1)));

        // Breeder Fuel Rod
        GameRegistry.addRecipe(new ShapedOreRecipe(QuantumItems.itemBreederFuel, "CUC", "CUC", "CUC", 'U', "breederUranium", 'C', "cellEmpty"));

        // Empty Cell
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(QuantumItems.itemCell, 16), " T ", "TGT", " T ", 'T', "ingotTin", 'G', Blocks.GLASS));

        // Fissile Fuel
        GameRegistry.addRecipe(new ShapedOreRecipe(QuantumItems.itemFissileFuel, "CUC", "CUC", "CUC", 'U', "ingotUranium", 'C', "cellEmpty"));

        // Water Cell
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(QuantumItems.itemWaterCell), "cellEmpty", Items.WATER_BUCKET));

        // Hazmat
        //GameRegistry.addRecipe(new ShapedOreRecipe(itemHazmatMask, "SSS", "BAB", "SCS", 'A', Items.leather_helmet, 'C', UniversalRecipe.CIRCUIT_T1.get(Settings.allowAlternateRecipes), 'S', Blocks.cloth }));
        //GameRegistry.addRecipe(new ShapedOreRecipe(itemHazmatBody, "SSS", "BAB", "SCS", 'A', Items.leather_plate, 'C', UniversalRecipe.CIRCUIT_T1.get(Settings.allowAlternateRecipes), 'S', Block.cloth }));
        //GameRegistry.addRecipe(new ShapedOreRecipe(itemHazmatLeggings, "SSS", "BAB", "SCS", 'A', Items.leather_legs, 'C', UniversalRecipe.CIRCUIT_T1.get(Settings.allowAlternateRecipes), 'S', Block.cloth }));
        //GameRegistry.addRecipe(new ShapedOreRecipe(itemHazmatBoots, "SSS", "BAB", "SCS", 'A', Items.leather_boots, 'C', UniversalRecipe.CIRCUIT_T1.get(Settings.allowAlternateRecipes), 'S', Block.cloth }));

        // Particle Accelerator
        //GameRegistry.addRecipe(new ShapedOreRecipe(blockAccelerator, "SCS", "CMC", "SCS", 'M', UniversalRecipe.MOTOR.get(Settings.allowAlternateRecipes), 'C', UniversalRecipe.CIRCUIT_T3.get(Settings.allowAlternateRecipes), 'S', UniversalRecipe.PRIMARY_PLATE.get(Settings.allowAlternateRecipes)));

        // Chemical Extractor
        //GameRegistry.addRecipe(new ShapedOreRecipe(blockChemicalExtractor, "BSB", "MCM", "BSB", 'C', UniversalRecipe.CIRCUIT_T3.get(Settings.allowAlternateRecipes), 'S', UniversalRecipe.PRIMARY_PLATE.get(Settings.allowAlternateRecipes), 'B', UniversalRecipe.SECONDARY_METAL.get(Settings.allowAlternateRecipes), 'M', UniversalRecipe.MOTOR.get(Settings.allowAlternateRecipes)));

        // Control Rod
        GameRegistry.addRecipe(new ShapedOreRecipe(QuantumBlocks.blockControlRod, "I", "I", "I", 'I', Items.IRON_INGOT));

        // Turbine
        //GameRegistry.addRecipe(new ShapedOreRecipe(blockElectricTurbine, " B ", "BMB", " B ", 'B', UniversalRecipe.SECONDARY_PLATE.get(Settings.allowAlternateRecipes), 'M', UniversalRecipe.MOTOR.get(Settings.allowAlternateRecipes)));

        // Electromagnet
        //GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockElectromagnet, 2, 0), "BBB", "BMB", "BBB", 'B', UniversalRecipe.SECONDARY_METAL.get(Settings.allowAlternateRecipes), 'M', UniversalRecipe.MOTOR.get(Settings.allowAlternateRecipes)));

        // Electromagnet Glass
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(QuantumBlocks.blockElectromagnet, 1, 1), QuantumBlocks.blockElectromagnet, Blocks.GLASS));

        // Fulmination Generator
        //GameRegistry.addRecipe(new ShapedOreRecipe(blockFulmination, "OSO", "SCS", "OSO", 'O', Blocks.obsidian, 'C', UniversalRecipe.CIRCUIT_T2.get(Settings.allowAlternateRecipes), 'S', UniversalRecipe.PRIMARY_PLATE.get(Settings.allowAlternateRecipes)));

        // Gas Centrifuge
        //GameRegistry.addRecipe(new ShapedOreRecipe(blockGasCentrifuge, "BSB", "MCM", "BSB", 'C', UniversalRecipe.CIRCUIT_T2.get(Settings.allowAlternateRecipes), 'S', UniversalRecipe.PRIMARY_PLATE.get(Settings.allowAlternateRecipes), 'B', UniversalRecipe.SECONDARY_METAL.get(Settings.allowAlternateRecipes), 'M', UniversalRecipe.MOTOR.get(Settings.allowAlternateRecipes)));

        // Gas Funnel
        //GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockGasFunnel, 2), " B ", "B B", "B B", 'B', UniversalRecipe.SECONDARY_METAL.get(Settings.allowAlternateRecipes)));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(QuantumBlocks.blockMachine, 2, EnumMachine.GAS_FUNNEL.ordinal()), " B ", "B B", "B B", 'B', "ingotIron"));

        // Nuclear Boiler
        //GameRegistry.addRecipe(new ShapedOreRecipe(blockNuclearBoiler, "S S", "FBF", "SMS", 'F', Block.furnaceIdle, 'S', UniversalRecipe.PRIMARY_PLATE.get(Settings.allowAlternateRecipes), 'B', Item.bucketEmpty, 'M', UniversalRecipe.MOTOR.get(Settings.allowAlternateRecipes)));

        // Siren
        //GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockSiren, 2), "NPN", 'N', Blocks.noteblock, 'P', UniversalRecipe.SECONDARY_PLATE.get(Settings.allowAlternateRecipes)));

        // Thermometer
        //GameRegistry.addRecipe(new ShapedOreRecipe(blockThermometer, "SSS", "GCG", "GSG", 'S', UniversalRecipe.PRIMARY_METAL.get(Settings.allowAlternateRecipes), 'G', Blocks.glass, 'C', UniversalRecipe.CIRCUIT_T1.get(Settings.allowAlternateRecipes)));

        // Plasma Heater
        //GameRegistry.addRecipe(new ShapedOreRecipe(blockPlasmaHeater, "CPC", "PFP", "CPC", 'P', UniversalRecipe.PRIMARY_PLATE.get(Settings.allowAlternateRecipes), 'F', blockReactorCell, 'C', UniversalRecipe.CIRCUIT_T3.get(Settings.allowAlternateRecipes)));

        // Quantum Assembler
        //GameRegistry.addRecipe(new ShapedOreRecipe(blockQuantumAssembler, "CCC", "SXS", "SSS", 'X', blockGasCentrifuge, 'C', UniversalRecipe.CIRCUIT_T3.get(Settings.allowAlternateRecipes), 'S', UniversalRecipe.PRIMARY_PLATE.get(Settings.allowAlternateRecipes)));

        // Fission Reactor
        //GameRegistry.addRecipe(new ShapedOreRecipe(blockReactorCell, "SCS", "MEM", "SCS", 'E', "cellEmpty", 'C', UniversalRecipe.CIRCUIT_T2.get(Settings.allowAlternateRecipes), 'S', UniversalRecipe.PRIMARY_PLATE.get(Settings.allowAlternateRecipes), 'M', UniversalRecipe.MOTOR.get(Settings.allowAlternateRecipes)));

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
