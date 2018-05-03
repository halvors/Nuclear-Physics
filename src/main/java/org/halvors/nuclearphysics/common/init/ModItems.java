package org.halvors.nuclearphysics.common.init;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.oredict.OreDictionary;
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

    @EventBusSubscriber
    public static class RegistrationHandler {
        /**
         * Register this mod's {@link Item}s.
         *
         * @param event The event
         */
        @SubscribeEvent
        public static void registerItems(final RegistryEvent.Register<Item> event) {
            final Item[] registerItems = {
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

            for (final Item item : registerItems) {
                registry.register(item);

                if (item instanceof ItemBase) {
                    ((ItemBase) item).registerItemModel();
                } else if (item instanceof ItemArmorBase) {
                    ((ItemArmorBase) item).registerItemModel();
                }

                items.add(item);
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
            OreDictionary.registerOre("ingotUranium235", new ItemStack(ModItems.itemUranium, 1, EnumUranium.URANIUM_235.ordinal()));
            OreDictionary.registerOre("ingotUranium238", new ItemStack(ModItems.itemUranium, 1, EnumUranium.URANIUM_238.ordinal()));

            OreDictionary.registerOre("antimatter", new ItemStack(ModItems.itemAntimatterCell, 1, ItemAntimatterCell.EnumAntimatterCell.MILLIGRAM.ordinal()));
            OreDictionary.registerOre("antimatterMilligram", new ItemStack(ModItems.itemAntimatterCell, 1, ItemAntimatterCell.EnumAntimatterCell.MILLIGRAM.ordinal()));
            OreDictionary.registerOre("antimatterGram", new ItemStack(ModItems.itemAntimatterCell, 1, ItemAntimatterCell.EnumAntimatterCell.GRAM.ordinal()));
        }
    }
}
