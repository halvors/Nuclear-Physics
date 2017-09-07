package org.halvors.nuclearphysics.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.init.ModItems;
import org.halvors.nuclearphysics.common.utility.FluidUtility;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;

import javax.annotation.Nonnull;

public class ItemCell extends ItemTooltip {
    public static final int capacity = 200;

    public ItemCell() {
        super("cell");

        setContainerItem(ModItems.itemCell);
    }

    @Override
    public void registerItemModel() {
        NuclearPhysics.getProxy().registerItemRenderer(this, 0, name);
    }

    @Override
    @Nonnull
    @SideOnly(Side.CLIENT)
    public String getItemStackDisplayName(@Nonnull ItemStack itemStack) {
        FluidStack fluidStack = FluidUtil.getFluidContained(itemStack);
        String fluidName = fluidStack != null ? fluidStack.getLocalizedName() : LanguageUtility.transelate("fluid.empty");

        return fluidName + " " + LanguageUtility.transelate(getUnlocalizedName() + ".name");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(@Nonnull Item item, CreativeTabs tab, NonNullList<ItemStack> subItems) {
        for (EnumCell type : EnumCell.values()) {
            subItems.add(type == EnumCell.EMPTY ? new ItemStack(item) : FluidUtility.getFilledCell(FluidRegistry.getFluid(type.getName())));
        }
    }

    @Override
    @Nonnull
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new FluidHandlerItemStackSimple(stack, capacity);
    }

    public enum EnumCell {
        EMPTY("empty"),
        DEUTERIUM("deuterium"),
        TRITIUM("tritium"),
        WATER("water");

        private String name;

        EnumCell(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}