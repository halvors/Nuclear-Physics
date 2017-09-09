package org.halvors.nuclearphysics.common.init;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.halvors.nuclearphysics.api.recipe.QuantumAssemblerRecipes;
import org.halvors.nuclearphysics.common.ConfigurationManager;

public class ModRecipes {
    public static void registerRecipes() {
        // Quantum assembler recipes.
        if (ConfigurationManager.General.quantumAssemblerGenerateMode > 0) {
            /*
            for (Item item : Item.itemsList) {
                if (item != null) {
                    if (item.itemID > 256 || Settings.quantumAssemblerGenerateMode == 2) {
                        ItemStack itemStack = new ItemStack(item);

                        if (itemStack != null) {
                            QuantumAssemblerRecipes.addRecipe(itemStack);
                        }
                    }
                }
            }

            if (ConfigurationManager.General.quantumAssemblerGenerateMode == 2) {
                for (Block block : Block.REGISTRY.getblocksList) {
                    if (block != null) {
                        ItemStack itemStack = new ItemStack(block);
                        if (itemStack != null) {
                            QuantumAssemblerRecipes.addRecipe(itemStack);
                        }
                    }
                }
            }
            */

            for (String oreName : OreDictionary.getOreNames()) {
                if (oreName.startsWith("ingot")) {
                    for (ItemStack itemStack : OreDictionary.getOres(oreName)) {
                        QuantumAssemblerRecipes.addRecipe(itemStack);
                    }
                }
            }
        }
    }
}
