package org.halvors.quantum.common;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.halvors.quantum.common.item.ItemCell;
import org.halvors.quantum.common.item.ItemQuantum;
import org.halvors.quantum.common.item.ItemRadioactive;
import org.halvors.quantum.common.item.armor.ItemArmorHazmat;
import org.halvors.quantum.common.item.particle.ItemAntimatterCell;
import org.halvors.quantum.common.item.particle.ItemDarkmatterCell;
import org.halvors.quantum.common.item.reactor.fission.ItemBreederFuel;
import org.halvors.quantum.common.item.reactor.fission.ItemBucketToxicWaste;
import org.halvors.quantum.common.item.reactor.fission.ItemFissileFuel;
import org.halvors.quantum.common.item.reactor.fission.ItemUranium;

public class QuantumItems {
    // Cells
    public static Item itemAntimatterCell = new ItemAntimatterCell();
    public static Item itemBreederFuel = new ItemBreederFuel();
    public static Item itemCell = new ItemCell("cellEmpty");
    public static Item itemDarkMatterCell = new ItemDarkmatterCell();
    public static Item itemDeuteriumCell = new ItemCell("cellDeuterium");
    public static Item itemFissileFuel = new ItemFissileFuel();
    public static Item itemTritiumCell = new ItemCell("cellTritium");
    public static Item itemWaterCell = new ItemCell("cellWater");

    // Buckets
    public static Item itemBucketToxicWaste = new ItemBucketToxicWaste();

    // Uranium
    public static Item itemUranium = new ItemUranium();
    public static Item itemYellowCake = new ItemRadioactive("yellowcake");

    // Hazmat
    public static ItemArmor itemHazmatMask = new ItemArmorHazmat("hazmatMask", EntityEquipmentSlot.HEAD);
    public static ItemArmor itemHazmatBody = new ItemArmorHazmat("hazmatBody", EntityEquipmentSlot.CHEST);
    public static ItemArmor itemHazmatLeggings = new ItemArmorHazmat("hazmatLeggings", EntityEquipmentSlot.LEGS);
    public static ItemArmor itemHazmatBoots = new ItemArmorHazmat("hazmatBoots", EntityEquipmentSlot.FEET);

    // Register items.
    public static void register() {
        register(itemAntimatterCell);
        register(itemBreederFuel);
        register(itemCell);
        register(itemDarkMatterCell);
        register(itemDeuteriumCell);
        register(itemFissileFuel);
        register(itemTritiumCell);
        register(itemWaterCell);
        register(itemBucketToxicWaste);

        register(itemUranium);
        register(itemYellowCake);

        register(itemHazmatMask);
        register(itemHazmatBody);
        register(itemHazmatLeggings);
        register(itemHazmatBoots);
    }

    private static <T extends Item> T register(T item) {
        GameRegistry.register(item);

        if (item instanceof ItemQuantum) {
            ((ItemQuantum) item).registerItemModel();
        }

        return item;
    }
}
