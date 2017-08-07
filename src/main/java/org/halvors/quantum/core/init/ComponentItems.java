package org.halvors.quantum.core.init;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import org.halvors.quantum.atomic.common.item.ItemQuantum;
import org.halvors.quantum.atomic.common.item.armor.ItemArmorQuantum;
import org.halvors.quantum.core.item.ItemComponents;

public class ComponentItems {
    public static Item itemScrewdriver = new ItemComponents("screwdriver");
    public static Item itemMotor = new ItemComponents("motor");
    public static Item itemCircuitBasic = new ItemComponents("circuit_basic");
    public static Item itemCircuitAdvanced = new ItemComponents("circuit_advanced");
    public static Item itemCircuitElite = new ItemComponents("circuit_elite");
    public static Item itemPlateCopper = new ItemComponents("plate_copper");
    public static Item itemPlateTin = new ItemComponents("plate_tin");
    public static Item itemPlateBronze = new ItemComponents("plate_bronze");
    public static Item itemPlateSteel = new ItemComponents("plate_steel");
    public static Item itemPlateIron = new ItemComponents("plate_iron");
    public static Item itemPlateGold = new ItemComponents("plate_gold");
    public static Item itemIngotCopper = new ItemComponents("ingot_copper");
    public static Item itemIngotTin = new ItemComponents("ingot_tin");
    public static Item itemIngotSteel = new ItemComponents("ingot_steel");
    public static Item itemIngotBronze = new ItemComponents("ingot_bronze");
    public static Item itemDustSteel = new ItemComponents("dust_steel");
    public static Item itemDustBronze = new ItemComponents("dust_bronze");

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
