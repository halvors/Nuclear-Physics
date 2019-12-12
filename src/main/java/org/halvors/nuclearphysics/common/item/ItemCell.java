package org.halvors.nuclearphysics.common.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStackSimple;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.NuclearPhysics;
import org.halvors.nuclearphysics.common.init.ModFluids;
import org.halvors.nuclearphysics.common.utility.FluidUtility;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemCell extends ItemTooltip {
    public static final int CAPACITY = 200; // In gram / mL.

    private final ItemStack itemStackEmpty = new ItemStack(this);

    public ItemCell() {
        super("cell");

        // Add empty NBT tag to empty cell because Forge does not remove tag when emptying.
        itemStackEmpty.setTag(new CompoundNBT());
    }

    @Override
    public void registerItemModel() {
        NuclearPhysics.getProxy().registerItemRenderer(this, 0, name);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack itemStack, @Nullable final World world, final List<String> list, final ITooltipFlag flag) {
        final FluidStack fluidStack = FluidUtil.getFluidContained(itemStack);

        if (fluidStack != null) {
            list.add(LanguageUtility.transelate(getTranslationKey(itemStack) + ".tooltip", fluidStack.getLocalizedName()));
        } else {
            list.add(LanguageUtility.transelate("tooltip.empty"));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(@Nonnull final CreativeTabs tab, @Nonnull final NonNullList<ItemStack> list) {
        if (isInCreativeTab(tab)) {
            for (final EnumCell type : EnumCell.values()) {
                list.add(type.getFluid() == null ? itemStackEmpty : FluidUtility.getFilledCell(type.getFluid()));
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean hasContainerItem(final ItemStack itemStack) {
        return itemStackEmpty != null;
    }

    @Override
    @Nonnull
    public ItemStack getContainerItem(@Nonnull final ItemStack itemStack) {
        if (hasContainerItem(itemStack)) {
            return itemStackEmpty.copy();
        }

        return super.getContainerItem(itemStack);
    }

    @Override
    @Nonnull
    public ICapabilityProvider initCapabilities(final ItemStack itemStack, final NBTTagCompound tag) {
        return new FluidHandlerItemStackSimple(itemStack, CAPACITY);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public enum EnumCell {
        EMPTY,
        DEUTERIUM(ModFluids.deuterium),
        TRITIUM(ModFluids.tritium),
        WATER(FluidRegistry.WATER);

        private Fluid fluid;

        EnumCell() {

        }

        EnumCell(final Fluid fluid) {
            this.fluid = fluid;
        }

        public String getName() {
            return name().toLowerCase();
        }

        public Fluid getFluid() {
            return fluid;
        }
    }
}