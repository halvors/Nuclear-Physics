package org.halvors.nuclearphysics.common.init;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;
import org.halvors.nuclearphysics.api.item.armor.IArmorSet.EntityEquipmentSlot;
import org.halvors.nuclearphysics.common.item.ItemBase;
import org.halvors.nuclearphysics.common.item.ItemBucketBase;
import org.halvors.nuclearphysics.common.item.ItemCell;
import org.halvors.nuclearphysics.common.item.ItemRadioactive;
import org.halvors.nuclearphysics.common.item.armor.ItemArmorHazmat;
import org.halvors.nuclearphysics.common.item.particle.ItemAntimatterCell;
import org.halvors.nuclearphysics.common.item.reactor.fission.ItemBreederFuel;
import org.halvors.nuclearphysics.common.item.reactor.fission.ItemFissileFuel;
import org.halvors.nuclearphysics.common.item.reactor.fission.ItemUranium;
import org.halvors.nuclearphysics.common.item.reactor.fission.ItemUranium.EnumUranium;
import org.halvors.nuclearphysics.common.item.tool.ItemWrench;
import org.halvors.nuclearphysics.common.utility.FluidUtility;

import java.util.HashSet;
import java.util.Set;

public class ModItems {
    public static final Set<Item> items = new HashSet<>();

    // Basic Components
    public static final Item itemWrench = new ItemWrench();
    public static final Item itemCopperWire = new ItemBase("copper_wire");
    public static final Item itemMotor = new ItemBase("motor");

    public static final Item itemPlateBronze = new ItemBase("plate_bronze");
    public static final Item itemPlateSteel = new ItemBase("plate_steel");

    public static final Item itemCircuitBasic = new ItemBase("circuit_basic");
    public static final Item itemCircuitAdvanced = new ItemBase("circuit_advanced");
    public static final Item itemCircuitElite = new ItemBase("circuit_elite");

    // Buckets
    public static Item itemToxicWasteBucket = new ItemBucketBase("toxic_waste_bucket", ModFluids.toxicWaste);

    // Cells
    public static final Item itemAntimatterCell = new ItemAntimatterCell();
    public static final Item itemBreederFuel = new ItemBreederFuel();
    public static final Item itemCell = new ItemCell();
    public static final Item itemDarkMatterCell = new ItemBase("darkmatter_cell");
    public static final Item itemFissileFuel = new ItemFissileFuel();

    // Uranium
    public static final Item itemUranium = new ItemUranium();
    public static final Item itemYellowCake = new ItemRadioactive("yellowcake");

    // Hazmat
    public static final ItemArmor itemHazmatMask = new ItemArmorHazmat("hazmat_mask", EntityEquipmentSlot.HEAD);
    public static final ItemArmor itemHazmatBody = new ItemArmorHazmat("hazmat_body", EntityEquipmentSlot.CHEST);
    public static final ItemArmor itemHazmatLeggings = new ItemArmorHazmat("hazmat_leggings", EntityEquipmentSlot.LEGS);
    public static final ItemArmor itemHazmatBoots = new ItemArmorHazmat("hazmat_boots", EntityEquipmentSlot.FEET);

    public static void registerItems() {
        GameRegistry.registerItem(itemWrench, "itemWrench");
        GameRegistry.registerItem(itemCopperWire, "itemCopperWire");
        GameRegistry.registerItem(itemMotor, "itemMotor");

        GameRegistry.registerItem(itemPlateBronze, "itemPlateBronze");
        GameRegistry.registerItem(itemPlateSteel, "itemPlateSteel");

        GameRegistry.registerItem(itemCircuitBasic, "itemCircuitBasic");
        GameRegistry.registerItem(itemCircuitAdvanced, "itemCircuitAdvanced");
        GameRegistry.registerItem(itemCircuitElite, "itemCircuitElite");

        GameRegistry.registerItem(itemToxicWasteBucket, "itemToxicWasteBucket");

        GameRegistry.registerItem(itemAntimatterCell, "itemAntimatterCell");
        GameRegistry.registerItem(itemBreederFuel, "itemBreederFuel");
        GameRegistry.registerItem(itemCell, "itemCell");
        GameRegistry.registerItem(itemDarkMatterCell, "itemDarkMatterCell");
        GameRegistry.registerItem(itemFissileFuel, "itemFissileFuel");
        GameRegistry.registerItem(itemUranium, "itemUranium");
        GameRegistry.registerItem(itemYellowCake, "(itemYellowCake");
        GameRegistry.registerItem(itemHazmatMask, "itemHazmatMask");
        GameRegistry.registerItem(itemHazmatBody, "itemHazmatBody");
        GameRegistry.registerItem(itemHazmatLeggings, "itemHazmatLeggings");
        GameRegistry.registerItem(itemHazmatBoots, "itemHazmatBoots");

        // Basic Components
        OreDictionary.registerOre("plateBronze", itemPlateBronze);
        OreDictionary.registerOre("plateSteel", itemPlateSteel);

        OreDictionary.registerOre("circuitBasic", itemCircuitBasic);
        OreDictionary.registerOre("circuitAdvanced", itemCircuitAdvanced);
        OreDictionary.registerOre("circuitElite", itemCircuitElite);

        // Nuclear Physics
        OreDictionary.registerOre("fuelBreeder", itemBreederFuel);
        OreDictionary.registerOre("cellEmpty", itemCell);
        OreDictionary.registerOre("cellDarkmatter", itemDarkMatterCell);
        OreDictionary.registerOre("fuelFissile", itemFissileFuel);

        OreDictionary.registerOre("dustUranium", itemYellowCake);

        OreDictionary.registerOre("bucketToxicWaste", itemToxicWasteBucket);

        OreDictionary.registerOre("cellDeuterium", FluidUtility.getFilledCell(ModFluids.deuterium));
        OreDictionary.registerOre("cellTritium", FluidUtility.getFilledCell(ModFluids.tritium));
        OreDictionary.registerOre("cellWater", FluidUtility.getFilledCell(FluidRegistry.WATER));

        OreDictionary.registerOre("ingotUranium", new ItemStack(ModItems.itemUranium, 1, EnumUranium.URANIUM_235.ordinal()));
        OreDictionary.registerOre("ingotUranium235", new ItemStack(ModItems.itemUranium, 1, EnumUranium.URANIUM_235.ordinal()));
        OreDictionary.registerOre("ingotUranium238", new ItemStack(ModItems.itemUranium, 1, EnumUranium.URANIUM_238.ordinal()));

        OreDictionary.registerOre("antimatter", new ItemStack(ModItems.itemAntimatterCell, 1, ItemAntimatterCell.EnumAntimatterCell.MILLIGRAM.ordinal()));
        OreDictionary.registerOre("antimatterMilligram", new ItemStack(ModItems.itemAntimatterCell, 1, ItemAntimatterCell.EnumAntimatterCell.MILLIGRAM.ordinal()));
        OreDictionary.registerOre("antimatterGram", new ItemStack(ModItems.itemAntimatterCell, 1, ItemAntimatterCell.EnumAntimatterCell.GRAM.ordinal()));
    }
}
