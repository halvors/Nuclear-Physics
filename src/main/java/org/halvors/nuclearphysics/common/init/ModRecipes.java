package org.halvors.nuclearphysics.common.init;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.halvors.nuclearphysics.api.recipe.QuantumAssemblerRecipes;
import org.halvors.nuclearphysics.common.ConfigurationManager.General;
import org.halvors.nuclearphysics.common.block.machine.BlockMachine.EnumMachine;
import org.halvors.nuclearphysics.common.block.reactor.fusion.BlockElectromagnet.EnumElectromagnet;
import org.halvors.nuclearphysics.common.item.particle.ItemAntimatterCell.EnumAntimatterCell;
import org.halvors.nuclearphysics.common.utility.FluidUtility;

public class ModRecipes {
    public static void registerRecipes() {
        // Wrench.
        GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.itemWrench, " S ", " SS", "S  ", 'S', "ingotSteel"));

        // Copper wire.
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemCopperWire, 6), "WCW", "WCW", "WCW", 'W', Blocks.wool, 'C', "ingotCopper"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemCopperWire, 6), "LCL", "LCL", "LCL", 'L', Items.leather, 'C', "ingotCopper"));

        // Motor.
        GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.itemMotor, "WSW", "SIS", "WSW", 'W', ModItems.itemCopperWire, 'S', "ingotSteel", 'I', Items.iron_ingot));

        // Bronze plate.
        GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.itemPlateBronze, "BB ", "BB ", 'B', "ingotBronze"));

        // Steel plate.
        GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.itemPlateSteel, "SS ", "SS ", 'S', "ingotSteel"));

        // Basic circuit.
        GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.itemCircuitBasic, "WRW", "RPR", "WRW", 'W', ModItems.itemCopperWire, 'R', Items.redstone, 'P', "plateBronze"));
        GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.itemCircuitBasic, "WRW", "RPR", "WRW", 'W', ModItems.itemCopperWire, 'R', Items.redstone, 'P', "plateSteel"));

        // Advanced circuit.
        GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.itemCircuitAdvanced, "RRR", "CDC", "RRR", 'R', Items.redstone, 'C', "circuitBasic", 'D', Items.diamond));

        // Elite circuit.
        GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.itemCircuitElite, "GGG", "CLC", "GGG", 'G', Items.gold_ingot, 'C', "circuitAdvanced", 'L', Blocks.lapis_block));

        // Antimatter
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.itemAntimatterCell, 1, EnumAntimatterCell.GRAM.ordinal()), ModItems.itemAntimatterCell, ModItems.itemAntimatterCell, ModItems.itemAntimatterCell, ModItems.itemAntimatterCell, ModItems.itemAntimatterCell, ModItems.itemAntimatterCell, ModItems.itemAntimatterCell, ModItems.itemAntimatterCell));
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.itemAntimatterCell, 8, EnumAntimatterCell.MILLIGRAM.ordinal()), new ItemStack(ModItems.itemAntimatterCell, 1, EnumAntimatterCell.GRAM.ordinal())));

        // Empty Cell
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.itemCell, 16), " T ", "TGT", " T ", 'T', "ingotTin", 'G', Blocks.glass));

        // Water Cell
        GameRegistry.addRecipe(new ShapelessOreRecipe(FluidUtility.getFilledCell(FluidRegistry.WATER), "cellEmpty", Items.water_bucket));

        // Breeder Fuel
        GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.itemBreederFuel, "CUC", "CUC", "CUC", 'U', "ingotUranium238", 'C', ModItems.itemCell));

        // Fissile Fuel
        GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.itemFissileFuel, "CUC", "CUC", "CUC", 'U', "ingotUranium", 'C', ModItems.itemCell));

        // Hazmat
        GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.itemHazmatMask, "WWW", " H ", "WCW", 'H', Items.leather_helmet, 'C', "circuitBasic", 'W', Blocks.wool));
        GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.itemHazmatBody, "WWW", " P ", "WCW", 'P', Items.leather_chestplate, 'C', "circuitBasic", 'W', Blocks.wool));
        GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.itemHazmatLeggings, "WWW", " L ", "WCW", 'L', Items.leather_leggings, 'C', "circuitBasic", 'W', Blocks.wool));
        GameRegistry.addRecipe(new ShapedOreRecipe(ModItems.itemHazmatBoots, "WWW", " B ", "WCW", 'B', Items.leather_boots, 'C', "circuitBasic", 'W', Blocks.wool));

        // Control Rod
        GameRegistry.addRecipe(new ShapedOreRecipe(ModBlocks.blockControlRod, "I", "I", "I", 'I', Items.iron_ingot));

        // Turbine
        GameRegistry.addRecipe(new ShapedOreRecipe(ModBlocks.blockElectricTurbine, " B ", "BMB", " B ", 'B', "plateBronze", 'M', ModItems.itemMotor));

        // Electromagnet
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockElectromagnet, 2), "BBB", "BMB", "BBB", 'B', "ingotBronze", 'M', ModItems.itemMotor));

        // Electromagnet Glass
        GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.blockElectromagnet, 1, EnumElectromagnet.GLASS.ordinal()), ModBlocks.blockElectromagnet, Blocks.glass));

        // Fulmination Generator
        GameRegistry.addRecipe(new ShapedOreRecipe(ModBlocks.blockFulmination, "OPO", "PCP", "OPO", 'O', Blocks.obsidian, 'C', "circuitAdvanced", 'P', "plateSteel"));

        // Gas Funnel
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockGasFunnel, 2), " B ", "B B", "B B", 'B', "ingotBronze"));
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockGasFunnel, 2), " I ", "I I", "I I", 'I', Items.iron_ingot));

        // Chemical Extractor
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockMachine, 1, EnumMachine.CHEMICAL_EXTRACTOR.ordinal()), "BPB", "MCM", "BPB", 'C', "circuitElite", 'P', "plateSteel", 'B', "ingotBronze", 'M', ModItems.itemMotor));

        // Gas Centrifuge
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockMachine, 1, EnumMachine.GAS_CENTRIFUGE.ordinal()), "BPB", "MCM", "BPB", 'C', "circuitAdvanced", 'P', "plateSteel", 'B', "ingotBronze", 'M', ModItems.itemMotor));

        // Nuclear Boiler
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockMachine, 1, EnumMachine.NUCLEAR_BOILER.ordinal()), "P P", "FBF", "PMP", 'F', Blocks.furnace, 'P', "plateSteel", 'B', Items.bucket, 'M', ModItems.itemMotor));

        // Particle Accelerator
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockMachine, 1, EnumMachine.PARTICLE_ACCELERATOR.ordinal()), "PCP", "CMC", "PCP", 'M', ModItems.itemMotor, 'C', "circuitElite", 'P', "plateSteel"));

        // Plasma Heater
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockMachine, 1, EnumMachine.PLASMA_HEATER.ordinal()), "CPC", "PFP", "CPC", 'P', "plateSteel", 'F', ModBlocks.blockReactorCell, 'C', "circuitElite"));

        // Quantum Assembler
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockMachine, 1, EnumMachine.QUANTUM_ASSEMBLER.ordinal()), "CCC", "PGP", "PPP", 'G', new ItemStack(ModBlocks.blockMachine, 1, EnumMachine.GAS_CENTRIFUGE.ordinal()), 'C', "circuitElite", 'P', "plateSteel"));

        // Siren
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.blockSiren, 2), "NPN", 'N', Blocks.noteblock, 'P', "plateBronze"));

        // Thermometer
        GameRegistry.addRecipe(new ShapedOreRecipe(ModBlocks.blockThermometer, "PPP", "GCG", "GPG", 'P', "plateSteel", 'G', Blocks.glass, 'C', "circuitBasic"));

        // Reactor Cell
        GameRegistry.addRecipe(new ShapedOreRecipe(ModBlocks.blockReactorCell, "PCP", "MEM", "PCP", 'E', ModItems.itemCell, 'C', "circuitAdvanced", 'P', "plateSteel", 'M', ModItems.itemMotor));

        registerQuantumAssemblerRecipes();
    }

    private static void registerQuantumAssemblerRecipes() {
        if (General.allowGeneratedQuantumAssemblerRecipes) {
            final String[] prefixList = { "ore", "ingot", "nugget", "dust", "gem", "dye", "block", "stone", "crop", "slab", "stair", "pane", "gear", "rod", "stick", "plate", "dustTiny", "cover" };

            // Add common items and blocks from ore dictionary.
            for (String oreName : OreDictionary.getOreNames()) {
                for (String prefix : prefixList) {
                    if (oreName.startsWith(prefix)) {
                        for (ItemStack itemStack : OreDictionary.getOres(oreName)) {
                            QuantumAssemblerRecipes.addRecipe(itemStack);
                        }
                    }
                }
            }

            // Add recipes for all items in this mod.
            for (Item item : ModItems.items) {
                QuantumAssemblerRecipes.addRecipe(new ItemStack(item));
            }

            // Add recipes for all blocks in this mod.
            for (ItemBlock itemBlock : ModBlocks.itemBlocks) {
                QuantumAssemblerRecipes.addRecipe(new ItemStack(itemBlock));
            }
        }
    }
}
