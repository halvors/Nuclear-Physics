package org.halvors.nuclearphysics.common.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import org.halvors.nuclearphysics.common.Reference;
import org.halvors.nuclearphysics.common.init.ModFluids;
import org.halvors.nuclearphysics.common.utility.FluidUtility;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemCell extends ItemTooltip implements IFluidContainerItem {
    public static final int CAPACITY = 200; // In gram / mL.
    public static final Map<EnumCell, IIcon> iconMap = new HashMap<>();

    private final ItemStack itemStackEmpty = new ItemStack(this);

    public ItemCell() {
        super("cell");

        // Add empty NBT tag to empty cell because Forge does not remove tag when emptying.
        itemStackEmpty.setTagCompound(new NBTTagCompound());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister iconRegister) {
        for (final EnumCell type : EnumCell.values()) {
            if (type != EnumCell.EMPTY) {
                iconMap.put(type, iconRegister.registerIcon(Reference.PREFIX + name + "_" + type.getName()));
            }
        }

        super.registerIcons(iconRegister);
    }

    @SuppressWarnings("unchecked")
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack itemStack, final EntityPlayer player, final List list, final boolean flag) {
        final FluidStack fluidStack = ((IFluidContainerItem) itemStack.getItem()).getFluid(itemStack);

        if (fluidStack != null) {
            list.add(LanguageUtility.transelate(getUnlocalizedName(itemStack) + ".tooltip", fluidStack.getLocalizedName()) + fluidStack.amount + "mB" );
        } else {
            list.add(LanguageUtility.transelate("tooltip.empty"));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(final Item item, final CreativeTabs tab, final List list) {
        for (final EnumCell type : EnumCell.values()) {
            list.add(type.getFluid() == null ? itemStackEmpty : FluidUtility.getFilledCell(type.getFluid()));
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean hasContainerItem(final ItemStack itemStack) {
        return itemStackEmpty != null;
    }

    @Override
    public ItemStack getContainerItem(final ItemStack itemStack) {
        if (hasContainerItem(itemStack)) {
            return itemStackEmpty.copy();
        }

        return super.getContainerItem(itemStack);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public FluidStack getFluid(ItemStack itemStack) {
        if (itemStack.stackTagCompound == null || !itemStack.stackTagCompound.hasKey("fluid")) {
            return null;
        }

        return FluidStack.loadFluidStackFromNBT(itemStack.stackTagCompound.getCompoundTag("fluid"));
    }

    @Override
    public int getCapacity(final ItemStack itemStack) {
        return CAPACITY;
    }

    @Override
    public int fill(final ItemStack itemStack, final FluidStack resource, final boolean doFill) {
        if (resource == null) {
            return 0;
        }

        if (!doFill) {
            if (itemStack.stackTagCompound == null || !itemStack.stackTagCompound.hasKey("fluid")) {
                return Math.min(CAPACITY, resource.amount);
            }

            final FluidStack stack = FluidStack.loadFluidStackFromNBT(itemStack.stackTagCompound.getCompoundTag("fluid"));

            if (stack == null) {
                return Math.min(CAPACITY, resource.amount);
            }

            if (!stack.isFluidEqual(resource)) {
                return 0;
            }

            return Math.min(CAPACITY - stack.amount, resource.amount);
        }

        if (itemStack.stackTagCompound == null) {
            itemStack.stackTagCompound = new NBTTagCompound();
        }

        if (!itemStack.stackTagCompound.hasKey("fluid")) {									// empty cell
        	FluidStack nfs = resource.copy();
        	if(nfs.amount > CAPACITY) nfs.amount = CAPACITY;
            final NBTTagCompound fluidTag = nfs.writeToNBT(new NBTTagCompound());

            /*if (CAPACITY < resource.amount) {
                fluidTag.setInteger("amount", CAPACITY);
                itemStack.stackTagCompound.setTag("fluid", fluidTag);

                return CAPACITY;
            }*/

            itemStack.stackTagCompound.setTag("fluid", fluidTag);

            return nfs.amount;
        }

        final NBTTagCompound fluidTag = itemStack.stackTagCompound.getCompoundTag("fluid");
        final FluidStack stack = FluidStack.loadFluidStackFromNBT(fluidTag);

        if (!stack.isFluidEqual(resource)) {
            return 0;
        }

        int filled = CAPACITY - stack.amount;

        if (resource.amount < filled) {
            stack.amount += resource.amount;
            filled = resource.amount;
        } else {
            stack.amount = CAPACITY;
        }

        itemStack.stackTagCompound.setTag("fluid", stack.writeToNBT(fluidTag));

        return filled;
    }

    @Override
    public FluidStack drain(final ItemStack itemStack, final int maxDrain, final boolean doDrain) {
        if (itemStack.stackTagCompound == null || !itemStack.stackTagCompound.hasKey("fluid")) {
            return null;
        }

        final FluidStack stack = FluidStack.loadFluidStackFromNBT(itemStack.stackTagCompound.getCompoundTag("fluid"));

        if (stack == null) {
            return null;
        }

        final int currentAmount = stack.amount;
        stack.amount = Math.min(stack.amount, maxDrain);

        if (doDrain) {
            if (currentAmount == stack.amount) {
                itemStack.stackTagCompound.removeTag("fluid");

                if (itemStack.stackTagCompound.hasNoTags()) {
                    itemStack.stackTagCompound = null;
                }

                return stack;
            }

            final NBTTagCompound fluidTag = itemStack.stackTagCompound.getCompoundTag("fluid");
            fluidTag.setInteger("amount", currentAmount - stack.amount);
            itemStack.stackTagCompound.setTag("fluid", fluidTag);
        }

        return stack;
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