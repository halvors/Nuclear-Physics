package org.halvors.quantum.common;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import org.halvors.quantum.common.item.ItemCell;
import org.halvors.quantum.common.item.ItemQuantum;
import org.halvors.quantum.common.item.ItemRadioactive;
import org.halvors.quantum.common.item.armor.ItemArmorHazmat;
import org.halvors.quantum.common.item.armor.ItemArmorQuantum;
import org.halvors.quantum.common.item.particle.ItemAntimatterCell;
import org.halvors.quantum.common.item.particle.ItemDarkmatterCell;
import org.halvors.quantum.common.item.reactor.fission.ItemBreederFuel;
import org.halvors.quantum.common.item.reactor.fission.ItemFissileFuel;
import org.halvors.quantum.common.item.reactor.fission.ItemUranium;

public class QuantumItems {
    // Cells
    public static Item itemAntimatterCell = new ItemAntimatterCell();
    public static Item itemBreederFuel = new ItemBreederFuel();
    public static Item itemCell = new ItemCell("empty_cell");
    public static Item itemDarkMatterCell = new ItemDarkmatterCell();
    public static Item itemDeuteriumCell = new ItemCell("deuterium_cell");
    public static Item itemFissileFuel = new ItemFissileFuel();
    public static Item itemTritiumCell = new ItemCell("tritium_cell");
    public static Item itemWaterCell = new ItemCell("water_cell");

    // Uranium
    public static Item itemUranium = new ItemUranium();
    public static Item itemYellowCake = new ItemRadioactive("yellowcake");

    // Hazmat
    public static ItemArmor itemHazmatMask = new ItemArmorHazmat("hazmat_mask", EntityEquipmentSlot.HEAD);
    public static ItemArmor itemHazmatBody = new ItemArmorHazmat("hazmat_body", EntityEquipmentSlot.CHEST);
    public static ItemArmor itemHazmatLeggings = new ItemArmorHazmat("hazmat_leggings", EntityEquipmentSlot.LEGS);
    public static ItemArmor itemHazmatBoots = new ItemArmorHazmat("hazmat_boots", EntityEquipmentSlot.FEET);

    // Register items.
    public static void register() {
        register(itemAntimatterCell);
        register(itemBreederFuel, "fuelBreeder");
        register(itemCell, "cellEmpty");
        register(itemDarkMatterCell, "cellDarkmatter");
        register(itemDeuteriumCell, "cellDeuterium");
        register(itemFissileFuel, "fuelFissile");
        register(itemTritiumCell, "cellTritium");
        register(itemWaterCell, "cellWater");

        register(itemUranium);
        register(itemYellowCake, "dustUranium");

        register(itemHazmatMask);
        register(itemHazmatBody);
        register(itemHazmatLeggings);
        register(itemHazmatBoots);

        /*
		OreDictionary.registerOre("ingotUranium", QuantumItems.itemUranium);
		OreDictionary.registerOre("itemUranium", new ItemStack(QuantumItems.itemUranium, 1, EnumUranium.URANIUM_238.ordinal()));
		OreDictionary.registerOre("antimatterMilligram", new ItemStack(QuantumItems.itemAntimatterCell, 1, EnumAntimatterCell.MILLIGRAM.ordinal()));
		OreDictionary.registerOre("antimatterGram", new ItemStack(QuantumItems.itemAntimatterCell, 1, EnumAntimatterCell.GRAM.ordinal()));
        */
    }

    private static <T extends Item> T register(T item, String name) {
        OreDictionary.registerOre(name, item);

        return register(item);
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
