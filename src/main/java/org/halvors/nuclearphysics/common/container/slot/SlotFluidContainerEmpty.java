package org.halvors.nuclearphysics.common.container.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidContainerRegistry.FluidContainerData;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;

/**
 * Slot for empty and partial-empty containers accepting given fluids
 * @author nictrace
 */
public class SlotFluidContainerEmpty extends Slot {

	private FluidStack[] flist;
	private FluidContainerData[] fcd;
	
	/**
	 * Slot for empty or partial empty simple & advanced fluid containers 
	 * @param valid - array of FluidStacks that can be accepted by inserted containers
	 */
	public SlotFluidContainerEmpty(IInventory inventory, int index, int x, int y, FluidStack... valid) {
		super(inventory, index, x, y);
		this.flist = valid;
		this.fcd = FluidContainerRegistry.getRegisteredFluidContainerData();		
	}

	@Override
	public boolean isItemValid(ItemStack itemstack) {
		if(FluidContainerRegistry.isContainer(itemstack)) {
			if(FluidContainerRegistry.isEmptyContainer(itemstack)) {
				for(int i=0; i< fcd.length; i++) {
					if((itemstack.isItemEqual(fcd[i].emptyContainer)) && (inFluidArray(flist, fcd[i].fluid))) {
						return true;	// this container can contains at least one of given liquids
					}
				}
			}
		} else if(itemstack.getItem() instanceof IFluidContainerItem) {
			// I think it can contains *any* of registered liquids
			IFluidContainerItem tank = (IFluidContainerItem)itemstack.getItem();
			FluidStack probe = tank.getFluid(itemstack);
			if(probe == null) return true;	// tank is empty
			// in case of not empty
			if((probe.amount < tank.getCapacity(itemstack)) && (inFluidArray(flist, probe))) return true;
		}
		return false;
	}
	
	protected boolean inFluidArray(FluidStack[] store, FluidStack fs) {
		for(FluidStack ofs: store)
			if(ofs.isFluidEqual(fs)) return true;
		return false;
	}
}
