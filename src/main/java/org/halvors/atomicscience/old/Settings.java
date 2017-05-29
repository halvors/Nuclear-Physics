package org.halvors.atomicscience.old;

import calclavia.api.atomicscience.QuantumAssemblerRecipes;
import calclavia.lib.config.Config;
import calclavia.lib.content.IDManager;
import com.calclavia.core.lib.prefab.potion.PotionRadiation;
import cpw.mods.fml.common.Loader;
import java.io.File;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

public class Settings
{
    public static final IDManager idManager = new IDManager(3768, 13768);
    public static final Configuration CONFIGURATION = new Configuration(new File(Loader.instance().getConfigDir(), "AtomicScience.cfg"));
    @Config
    public static double fulminationOutputMultiplier = 1.0D;
    @Config
    public static double turbineOutputMultiplier = 1.0D;
    @Config
    public static double fissionBoilVolumeMultiplier = 1.0D;
    @Config
    public static boolean allowTurbineStacking = true;
    @Config
    public static boolean allowToxicWaste = true;
    @Config
    public static boolean allowRadioactiveOres = true;
    @Config
    public static boolean allowOreDictionaryCompatibility = true;
    @Config
    public static boolean allowAlternateRecipes = true;
    @Config
    public static boolean allowIC2UraniumCompression = true;
    @Config(comment="0 = Do not generate, 1 = Generate items only, 2 = Generate all")
    public static int quantumAssemblerGenerateMode = 1;
    @Config
    public static int uraniumHexaflourideRatio = 200;
    @Config
    public static int waterPerDeutermium = 4;
    @Config
    public static int deutermiumPerTritium = 4;
    @Config(comment="Put a list of block/item IDs to be used by the Quantum Assembler. Separate by commas, no space.")
    public static int[] quantumAssemblerRecipes = new int[0];
    @Config
    public static double darkMatterSpawnChance = 0.2D;

    public static int getNextBlockID()
    {
        return idManager.getNextBlockID();
    }

    public static int getNextItemID()
    {
        return idManager.getNextItemID();
    }

    public static void load()
    {
        for (int recipeID : quantumAssemblerRecipes) {
            try
            {
                QuantumAssemblerRecipes.addRecipe(new ItemStack(recipeID, 1, 0));
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        PotionRadiation.INSTANCE.func_76396_c();
    }
}
