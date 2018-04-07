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
import org.halvors.nuclearphysics.common.init.ModItems;
import org.halvors.nuclearphysics.common.utility.FluidUtility;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemCell extends ItemTooltip implements IFluidContainerItem {
    public static final Map<EnumCell, IIcon> iconMap = new HashMap<>();
    public static final int capacity = 200;

    public ItemCell() {
        super("cell");

        setContainerItem(ModItems.itemCell);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        for (EnumCell type : EnumCell.values()) {
            if (type != EnumCell.EMPTY) {
                iconMap.put(type, iconRegister.registerIcon(Reference.PREFIX + name + "_" + type.getName()));
            }
        }

        super.registerIcons(iconRegister);
    }

    @SuppressWarnings("unchecked")
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean flag) {
        FluidStack fluidStack = ((IFluidContainerItem) itemStack.getItem()).getFluid(itemStack);

        if (fluidStack != null) {
            list.add(LanguageUtility.transelate(getUnlocalizedName(itemStack) + ".tooltip", fluidStack.getLocalizedName()));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        for (EnumCell type : EnumCell.values()) {
            list.add(type == EnumCell.EMPTY ? new ItemStack(item) : FluidUtility.getFilledCell(type.getFluid()));
        }
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
    public int getCapacity(ItemStack itemStack) {
        return capacity;
    }

    @Override
    public int fill(ItemStack itemStack, FluidStack resource, boolean doFill) {
        if (resource == null) {
            return 0;
        }

        if (!doFill) {
            if (itemStack.stackTagCompound == null || !itemStack.stackTagCompound.hasKey("fluid")) {
                return Math.min(capacity, resource.amount);
            }

            FluidStack stack = FluidStack.loadFluidStackFromNBT(itemStack.stackTagCompound.getCompoundTag("fluid"));

            if (stack == null) {
                return Math.min(capacity, resource.amount);
            }

            if (!stack.isFluidEqual(resource)) {
                return 0;
            }

            return Math.min(capacity - stack.amount, resource.amount);
        }

        if (itemStack.stackTagCompound == null) {
            itemStack.stackTagCompound = new NBTTagCompound();
        }

        if (!itemStack.stackTagCompound.hasKey("fluid")) {
            NBTTagCompound fluidTag = resource.writeToNBT(new NBTTagCompound());

            if (capacity < resource.amount) {
                fluidTag.setInteger("amount", capacity);
                itemStack.stackTagCompound.setTag("fluid", fluidTag);

                return capacity;
            }

            itemStack.stackTagCompound.setTag("fluid", fluidTag);

            return resource.amount;
        }

        NBTTagCompound fluidTag = itemStack.stackTagCompound.getCompoundTag("fluid");
        FluidStack stack = FluidStack.loadFluidStackFromNBT(fluidTag);

        if (!stack.isFluidEqual(resource)) {
            return 0;
        }

        int filled = capacity - stack.amount;

        if (resource.amount < filled) {
            stack.amount += resource.amount;
            filled = resource.amount;
        } else {
            stack.amount = capacity;
        }

        itemStack.stackTagCompound.setTag("fluid", stack.writeToNBT(fluidTag));

        return filled;
    }

    @Override
    public FluidStack drain(ItemStack itemStack, int maxDrain, boolean doDrain) {
        if (itemStack.stackTagCompound == null || !itemStack.stackTagCompound.hasKey("fluid")) {
            return null;
        }

        FluidStack stack = FluidStack.loadFluidStackFromNBT(itemStack.stackTagCompound.getCompoundTag("fluid"));

        if (stack == null) {
            return null;
        }

        int currentAmount = stack.amount;
        stack.amount = Math.min(stack.amount, maxDrain);

        if (doDrain) {
            if (currentAmount == stack.amount) {
                itemStack.stackTagCompound.removeTag("fluid");

                if (itemStack.stackTagCompound.hasNoTags()) {
                    itemStack.stackTagCompound = null;
                }

                return stack;
            }

            NBTTagCompound fluidTag = itemStack.stackTagCompound.getCompoundTag("fluid");
            fluidTag.setInteger("amount", currentAmount - stack.amount);
            itemStack.stackTagCompound.setTag("fluid", fluidTag);
        }

        return stack;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public enum EnumCell {
        EMPTY("empty"),
        DEUTERIUM("deuterium", ModFluids.deuterium),
        TRITIUM("tritium", ModFluids.tritium),
        WATER("water", FluidRegistry.WATER);

        private String name;
        private Fluid fluid;

        EnumCell(String name) {
            this.name = name;
        }

        EnumCell(String name, Fluid fluid) {
            this(name);

            this.fluid = fluid;
        }

        public String getName() {
            return name;
        }

        public Fluid getFluid() {
            return fluid;
        }
    }
}