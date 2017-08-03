package org.halvors.quantum.common;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import org.halvors.quantum.common.item.ItemCell;
import org.halvors.quantum.common.item.ItemQuantum;
import org.halvors.quantum.common.item.ItemQuantumComponents;
import org.halvors.quantum.common.item.ItemRadioactive;
import org.halvors.quantum.common.item.armor.ItemArmorHazmat;
import org.halvors.quantum.common.item.armor.ItemArmorQuantum;
import org.halvors.quantum.common.item.particle.ItemAntimatterCell;
import org.halvors.quantum.common.item.reactor.fission.ItemBreederFuel;
import org.halvors.quantum.common.item.reactor.fission.ItemFissileFuel;
import org.halvors.quantum.common.item.reactor.fission.ItemUranium;
import org.halvors.quantum.common.utility.FluidUtility;

public class QuantumItems {
    // Basic Components
    public static Item itemScrewdriver = new ItemQuantumComponents("screwdriver");
    public static Item itemMotor = new ItemQuantumComponents("motor");
    public static Item itemCircuitBasic = new ItemQuantumComponents("circuit_basic");
    public static Item itemCircuitAdvanced = new ItemQuantumComponents("circuit_advanced");
    public static Item itemCircuitElite = new ItemQuantumComponents("circuit_elite");
    public static Item itemPlateCopper = new ItemQuantumComponents("plate_copper");
    public static Item itemPlateTin = new ItemQuantumComponents("plate_tin");
    public static Item itemPlateBronze = new ItemQuantumComponents("plate_bronze");
    public static Item itemPlateSteel = new ItemQuantumComponents("plate_steel");
    public static Item itemPlateIron = new ItemQuantumComponents("plate_iron");
    public static Item itemPlateGold = new ItemQuantumComponents("plate_gold");
    public static Item itemIngotCopper = new ItemQuantumComponents("ingot_copper");
    public static Item itemIngotTin = new ItemQuantumComponents("ingot_tin");
    public static Item itemIngotSteel = new ItemQuantumComponents("ingot_steel");
    public static Item itemIngotBronze = new ItemQuantumComponents("ingot_bronze");
    public static Item itemDustSteel = new ItemQuantumComponents("dust_steel");
    public static Item itemDustBronze = new ItemQuantumComponents("dust_bronze");

    public static Item itemAntimatterCell = new ItemAntimatterCell();
    public static Item itemBreederFuel = new ItemBreederFuel();
    public static Item itemCell = new ItemCell();
    public static Item itemDarkMatterCell = new ItemQuantum("darkmatter_cell");
    public static Item itemFissileFuel = new ItemFissileFuel();
    public static Item itemUranium = new ItemUranium();
    public static Item itemYellowCake = new ItemRadioactive("yellowcake");

    public static ItemArmor itemHazmatMask = new ItemArmorHazmat("hazmat_mask", EntityEquipmentSlot.HEAD);
    public static ItemArmor itemHazmatBody = new ItemArmorHazmat("hazmat_body", EntityEquipmentSlot.CHEST);
    public static ItemArmor itemHazmatLeggings = new ItemArmorHazmat("hazmat_leggings", EntityEquipmentSlot.LEGS);
    public static ItemArmor itemHazmatBoots = new ItemArmorHazmat("hazmat_boots", EntityEquipmentSlot.FEET);

    // Register items.
    public static void register() {
        // Basic Components
        register(itemScrewdriver);
        register(itemMotor);
        register(itemCircuitBasic, "circuitBasic");
        register(itemCircuitAdvanced, "circuitAdvanced");
        register(itemCircuitElite, "circuitElite");
        register(itemPlateCopper, "plateCopper");
        register(itemPlateTin, "plateTin");
        register(itemPlateBronze, "plateBronze");
        register(itemPlateSteel, "plateSteel");
        register(itemPlateIron, "plateIron");
        register(itemPlateGold, "plateGold");
        register(itemIngotCopper, "ingotCopper");
        register(itemIngotTin, "ingotTin");
        register(itemIngotSteel, "ingotSteel");
        register(itemIngotBronze, "ingotBronze");
        register(itemDustSteel, "dustSteel");
        register(itemDustBronze, "dustBronze");

        register(itemAntimatterCell);
        register(itemBreederFuel, "fuelBreeder");
        register(itemCell, "cellEmpty");
        register(itemDarkMatterCell, "cellDarkmatter");
        register(itemFissileFuel, "fuelFissile");
        register(itemUranium);
        register(itemYellowCake, "dustUranium");

        register(itemHazmatMask);
        register(itemHazmatBody);
        register(itemHazmatLeggings);
        register(itemHazmatBoots);

        OreDictionary.registerOre("cellDeuterium", FluidUtility.getFilledCell(FluidRegistry.getFluid("deuterium")));
        OreDictionary.registerOre("cellTritium", FluidUtility.getFilledCell(FluidRegistry.getFluid("tritium")));
        OreDictionary.registerOre("cellWater", FluidUtility.getFilledCell(FluidRegistry.WATER));

        OreDictionary.registerOre("ingotUranium", QuantumItems.itemUranium);
        OreDictionary.registerOre("itemUranium", new ItemStack(QuantumItems.itemUranium, 1, ItemUranium.EnumUranium.URANIUM_238.ordinal()));

        OreDictionary.registerOre("antimatterMilligram", new ItemStack(QuantumItems.itemAntimatterCell, 1, ItemAntimatterCell.EnumAntimatterCell.MILLIGRAM.ordinal()));
        OreDictionary.registerOre("antimatterGram", new ItemStack(QuantumItems.itemAntimatterCell, 1, ItemAntimatterCell.EnumAntimatterCell.GRAM.ordinal()));
    }

    private static <T extends Item> T register(T item, String name) {
        item = register(item);

        OreDictionary.registerOre(name, item);

        return item;
    }

    private static <T extends Item> T register(T item) {
        GameRegistry.register(item);

        if (item instanceof ItemQuantum) {
            ((ItemQuantum) item).registerItemModel();
        } else if (item instanceof ItemArmorQuantum) {
            ((ItemArmorQuantum) item).registerItemModel();
        }

        return item;
    }
}
