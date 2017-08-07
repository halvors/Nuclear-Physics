package org.halvors.quantum.atomic.common.init;

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
import org.halvors.quantum.atomic.common.item.ItemCell;
import org.halvors.quantum.atomic.common.item.ItemQuantum;
import org.halvors.quantum.atomic.common.item.ItemRadioactive;
import org.halvors.quantum.atomic.common.item.armor.ItemArmorHazmat;
import org.halvors.quantum.atomic.common.item.armor.ItemArmorQuantum;
import org.halvors.quantum.atomic.common.item.particle.ItemAntimatterCell;
import org.halvors.quantum.atomic.common.item.reactor.fission.ItemBreederFuel;
import org.halvors.quantum.atomic.common.item.reactor.fission.ItemFissileFuel;
import org.halvors.quantum.atomic.common.item.reactor.fission.ItemUranium;
import org.halvors.quantum.atomic.common.utility.FluidUtility;

import java.util.HashSet;
import java.util.Set;

public class QuantumItems {
    // Cells
    public static Item itemAntimatterCell = new ItemAntimatterCell();
    public static Item itemBreederFuel = new ItemBreederFuel();
    public static Item itemCell = new ItemCell();
    public static Item itemDarkMatterCell = new ItemQuantum("darkmatter_cell");
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

                if (item instanceof ItemQuantum) {
                    ((ItemQuantum) item).registerItemModel();
                } else if (item instanceof ItemArmorQuantum) {
                    ((ItemArmorQuantum) item).registerItemModel();
                }

                ITEMS.add(item);
            }

            OreDictionary.registerOre("fuelBreeder", itemBreederFuel);
            OreDictionary.registerOre("cellEmpty", itemCell);
            OreDictionary.registerOre("cellDarkmatter", itemDarkMatterCell);
            OreDictionary.registerOre("fuelFissile", itemFissileFuel);

            OreDictionary.registerOre("dustUranium", itemYellowCake);

            OreDictionary.registerOre("cellDeuterium", FluidUtility.getFilledCell(FluidRegistry.getFluid("deuterium")));
            OreDictionary.registerOre("cellTritium", FluidUtility.getFilledCell(FluidRegistry.getFluid("tritium")));
            OreDictionary.registerOre("cellWater", FluidUtility.getFilledCell(FluidRegistry.WATER));

            OreDictionary.registerOre("ingotUranium", QuantumItems.itemUranium);
            OreDictionary.registerOre("itemUranium", new ItemStack(QuantumItems.itemUranium, 1, ItemUranium.EnumUranium.URANIUM_238.ordinal()));

            OreDictionary.registerOre("antimatterMilligram", new ItemStack(QuantumItems.itemAntimatterCell, 1, ItemAntimatterCell.EnumAntimatterCell.MILLIGRAM.ordinal()));
            OreDictionary.registerOre("antimatterGram", new ItemStack(QuantumItems.itemAntimatterCell, 1, ItemAntimatterCell.EnumAntimatterCell.GRAM.ordinal()));
        }
    }
}
