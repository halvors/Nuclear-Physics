package org.halvors.atomicscience.old.fusion;

import atomicscience.AtomicScience;
import atomicscience.process.fission.TileNuclearBoiler;
import calclavia.lib.gui.ContainerBase;
import calclavia.lib.prefab.slot.SlotEnergyItem;
import calclavia.lib.prefab.slot.SlotSpecific;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public class ContainerNuclearBoiler
        extends ContainerBase
{
    private static final int slotCount = 4;
    private TileNuclearBoiler tileEntity;

    public ContainerNuclearBoiler(InventoryPlayer par1InventoryPlayer, TileNuclearBoiler tileEntity)
    {
        super(tileEntity);
        this.tileEntity = tileEntity;

        func_75146_a(new SlotEnergyItem(tileEntity, 0, 56, 26));

        func_75146_a(new Slot(tileEntity, 1, 25, 50));

        func_75146_a(new Slot(tileEntity, 2, 136, 50));

        func_75146_a(new SlotSpecific(tileEntity, 3, 81, 26, new ItemStack[] { new ItemStack(AtomicScience.itemYellowCake), new ItemStack(AtomicScience.blockUraniumOre) }));
        addPlayerInventory(par1InventoryPlayer.field_70458_d);
        tileEntity.func_70295_k_();
    }

    public boolean func_75145_c(EntityPlayer par1EntityPlayer)
    {
        return this.tileEntity.func_70300_a(par1EntityPlayer);
    }

    public ItemStack func_82846_b(EntityPlayer par1EntityPlayer, int slotID)
    {
        ItemStack var2 = null;
        Slot slot = (Slot)this.field_75151_b.get(slotID);
        if ((slot != null) && (slot.func_75216_d()))
        {
            ItemStack itemStack = slot.func_75211_c();
            var2 = itemStack.func_77946_l();
            if (slotID >= 4)
            {
                if (func_75139_a(0).func_75214_a(itemStack))
                {
                    if (!func_75135_a(itemStack, 0, 1, false)) {
                        return null;
                    }
                }
                else if (AtomicScience.FLUIDSTACK_WATER.isFluidEqual(FluidContainerRegistry.getFluidForFilledItem(itemStack)))
                {
                    if (!func_75135_a(itemStack, 1, 2, false)) {
                        return null;
                    }
                }
                else if (func_75139_a(3).func_75214_a(itemStack))
                {
                    if (!func_75135_a(itemStack, 3, 4, false)) {
                        return null;
                    }
                }
                else if (slotID < 31)
                {
                    if (!func_75135_a(itemStack, 31, 40, false)) {
                        return null;
                    }
                }
                else if ((slotID >= 31) && (slotID < 40) && (!func_75135_a(itemStack, 4, 30, false))) {
                    return null;
                }
            }
            else if (!func_75135_a(itemStack, 4, 40, false)) {
                return null;
            }
            if (itemStack.field_77994_a == 0) {
                slot.func_75215_d((ItemStack)null);
            } else {
                slot.func_75218_e();
            }
            if (itemStack.field_77994_a == var2.field_77994_a) {
                return null;
            }
            slot.func_82870_a(par1EntityPlayer, itemStack);
        }
        return var2;
    }
}
