package org.halvors.nuclearphysics.common.init;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import org.halvors.nuclearphysics.common.item.ItemBase;
import org.halvors.nuclearphysics.common.item.ItemCell;
import org.halvors.nuclearphysics.common.item.ItemRadioactive;
import org.halvors.nuclearphysics.common.item.armor.ItemArmorBase;
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
    // Basic Components
    public static Item itemWrench = new ItemWrench();
    public static Item itemCopperWire = new ItemBase("copper_wire");
    public static Item itemMotor = new ItemBase("motor");

    public static Item itemPlateBronze = new ItemBase("plate_bronze");
    public static Item itemPlateSteel = new ItemBase("plate_steel");

    public static Item itemCircuitBasic = new ItemBase("circuit_basic");
    public static Item itemCircuitAdvanced = new ItemBase("circuit_advanced");
    public static Item itemCircuitElite = new ItemBase("circuit_elite");

    // Cells
    public static Item itemAntimatterCell = new ItemAntimatterCell();
    public static Item itemBreederFuel = new ItemBreederFuel();
    public static Item itemCell = new ItemCell();
    public static Item itemDarkMatterCell = new ItemBase("darkmatter_cell");
    public static Item itemFissileFuel = new ItemFissileFuel();

    // Uranium
    public static Item itemUranium = new ItemUranium();
    public static Item itemYellowCake = new ItemRadioactive("yellowcake");

    // Hazmat
    public static ItemArmor itemHazmatMask = new ItemArmorHazmat("hazmat_mask", EntityEquipmentSlot.HEAD);
    public static ItemArmor itemHazmatBody = new ItemArmorHazmat("hazmat_body", EntityEquipmentSlot.CHEST);
    public static ItemArmor itemHazmatLeggings = new ItemArmorHazmat("hazmat_leggings", EntityEquipmentSlot.LEGS);
    public static ItemArmor itemHazmatBoots = new ItemArmorHazmat("hazmat_boots", EntityEquipmentSlot.FEET);

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
                    itemWrench,
                    itemCopperWire,
                    itemMotor,

                    itemPlateBronze,
                    itemPlateSteel,

                    itemCircuitBasic,
                    itemCircuitAdvanced,
                    itemCircuitElite,

                    itemAntimatterCell,
                    itemBreederFuel,
                    itemCell,
                    itemDarkMatterCell,
                    itemFissileFuel,
                    itemUranium,
                    itemYellowCake,
                    itemHazmatMask,
                    itemHazmatBody,
                    itemHazmatLeggings,
                    itemHazmatBoots
            };

            final IForgeRegistry<Item> registry = event.getRegistry();

            for (final Item item : items) {
                registry.register(item);

                if (item instanceof ItemBase) {
                    ((ItemBase) item).registerItemModel();
                } else if (item instanceof ItemArmorBase) {
                    ((ItemArmorBase) item).registerItemModel();
                }

                ITEMS.add(item);
            }

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

            OreDictionary.registerOre("cellDeuterium", FluidUtility.getFilledCell(FluidRegistry.getFluid("deuterium")));
            OreDictionary.registerOre("cellTritium", FluidUtility.getFilledCell(FluidRegistry.getFluid("tritium")));
            OreDictionary.registerOre("cellWater", FluidUtility.getFilledCell(FluidRegistry.WATER));

            OreDictionary.registerOre("ingotUranium", new ItemStack(ModItems.itemUranium, 1, EnumUranium.URANIUM_235.ordinal()));
            //OreDictionary.registerOre("ingotUranium235", new ItemStack(ModItems.itemUranium, 1, EnumUranium.URANIUM_235.ordinal()));
            OreDictionary.registerOre("ingotUranium238", new ItemStack(ModItems.itemUranium, 1, EnumUranium.URANIUM_238.ordinal()));

            OreDictionary.registerOre("antimatterMilligram", new ItemStack(ModItems.itemAntimatterCell, 1, ItemAntimatterCell.EnumAntimatterCell.MILLIGRAM.ordinal()));
            OreDictionary.registerOre("antimatterGram", new ItemStack(ModItems.itemAntimatterCell, 1, ItemAntimatterCell.EnumAntimatterCell.GRAM.ordinal()));
        }
    }
}
