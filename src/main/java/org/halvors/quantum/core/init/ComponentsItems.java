package org.halvors.quantum.core.init;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.oredict.OreDictionary;
import org.halvors.quantum.atomic.common.item.ItemQuantum;
import org.halvors.quantum.atomic.common.item.armor.ItemArmorQuantum;
import org.halvors.quantum.core.item.ItemComponents;

import java.util.HashSet;
import java.util.Set;

public class ComponentsItems {
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

    @EventBusSubscriber
    public static class RegistrationHandler {
        public static final Set<Item> ITEMS = new HashSet<>();

        /**
         * Register this mod's {@link Item}s.
         *
         * @param event The event
         */
        @SubscribeEvent
        public static void registerItems(final RegistryEvent.Register<Item> event) {
            final Item[] items = {
                    itemScrewdriver,
                    itemMotor,
                    itemCircuitBasic,
                    itemCircuitAdvanced,
                    itemCircuitElite,
                    itemPlateCopper,
                    itemPlateTin,
                    itemPlateBronze,
                    itemPlateSteel,
                    itemPlateIron,
                    itemPlateGold,
                    itemIngotCopper,
                    itemIngotTin,
                    itemIngotSteel,
                    itemIngotBronze,
                    itemDustSteel,
                    itemDustBronze
            };

            final IForgeRegistry<Item> registry = event.getRegistry();

            for (final Item item : items) {
                registry.register(item);

                if (item instanceof ItemQuantum) {
                    ((ItemQuantum) item).registerItemModel();
                } else if (item instanceof ItemArmorQuantum) {
                    ((ItemArmorQuantum) item).registerItemModel();
                }

                ITEMS.add(item);
            }

            OreDictionary.registerOre("circuitBasic", itemCircuitBasic);
            OreDictionary.registerOre("circuitAdvanced", itemCircuitAdvanced);
            OreDictionary.registerOre("circuitElite", itemCircuitElite);
            OreDictionary.registerOre("plateCopper", itemPlateCopper);
            OreDictionary.registerOre("plateTin", itemPlateTin);
            OreDictionary.registerOre("plateBronze", itemPlateBronze);
            OreDictionary.registerOre("plateSteel", itemPlateSteel);
            OreDictionary.registerOre("plateIron", itemPlateIron);
            OreDictionary.registerOre("plateGold", itemPlateGold);
            OreDictionary.registerOre("ingotCopper", itemIngotCopper);
            OreDictionary.registerOre("ingotTin", itemIngotTin);
            OreDictionary.registerOre("ingotSteel", itemIngotSteel);
            OreDictionary.registerOre("ingotBronze", itemIngotBronze);
            OreDictionary.registerOre("dustSteel", itemDustSteel);
            OreDictionary.registerOre("dustBronze", itemDustBronze);
        }
    }
}
