package org.halvors.quantum.atomic.common;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import org.halvors.quantum.atomic.common.item.ItemCell;
import org.halvors.quantum.atomic.common.item.ItemQuantum;
import org.halvors.quantum.core.item.ItemComponents;
import org.halvors.quantum.atomic.common.item.ItemRadioactive;
import org.halvors.quantum.atomic.common.item.armor.ItemArmorHazmat;
import org.halvors.quantum.atomic.common.item.armor.ItemArmorQuantum;
import org.halvors.quantum.atomic.common.item.particle.ItemAntimatterCell;
import org.halvors.quantum.atomic.common.item.reactor.fission.ItemBreederFuel;
import org.halvors.quantum.atomic.common.item.reactor.fission.ItemFissileFuel;
import org.halvors.quantum.atomic.common.item.reactor.fission.ItemUranium;
import org.halvors.quantum.atomic.common.utility.FluidUtility;

public class QuantumItems {
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
