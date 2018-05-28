package org.halvors.nuclearphysics.common.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

public class SlotFliudContainer extends Slot {

	private FluidStack flist[];

	/**
	 * Slot for simple & advanced filled fiuid containers
	 * @param valid - array of FluidStacks that can be accepted by input tank
	 */
	public SlotFliudContainer(IInventory inventory, int index, int x, int y, FluidStack... valid) {
		super(inventory, index, x, y);
		this.flist = valid;

	}

	@Override
	public boolean isItemValid(ItemStack itemstack) {
		if(FluidContainerRegistry.isContainer(itemstack)) {
			if(FluidContainerRegistry.isFilledContainer(itemstack)) {
				for(int i = 0; i< flist.length; i++) {
					if(FluidContainerRegistry.containsFluid(itemstack, flist[i])) {
						return true;
					}
				}
			} else {	// is empty container
				return false;
			}
		}
		else if(itemstack.getItem() instanceof IFluidContainerItem) {
			IFluidContainerItem citem = (IFluidContainerItem) itemstack.getItem();
			if(citem.getFluid(itemstack) == null) return false;
			for(int i = 0; i< flist.length; i++) {
				if(citem.getFluid(itemstack).isFluidEqual(flist[i])) return true;
			}
		}
		return false;
	}
}
