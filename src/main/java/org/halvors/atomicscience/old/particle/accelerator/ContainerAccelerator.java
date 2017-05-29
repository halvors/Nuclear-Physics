package org.halvors.atomicscience.old.particle.accelerator;

import atomicscience.AtomicScience;
import calclavia.lib.gui.ContainerBase;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ContainerAccelerator
        extends ContainerBase
{
    private TileAccelerator tileEntity;

    public ContainerAccelerator(InventoryPlayer par1InventoryPlayer, TileAccelerator tileEntity)
    {
        super(tileEntity);
        this.tileEntity = tileEntity;

        func_75146_a(new Slot(tileEntity, 0, 132, 26));
        func_75146_a(new Slot(tileEntity, 1, 132, 51));

        func_75146_a(new SlotFurnace(par1InventoryPlayer.field_70458_d, tileEntity, 2, 132, 75));
        func_75146_a(new SlotFurnace(par1InventoryPlayer.field_70458_d, tileEntity, 3, 106, 75));
        addPlayerInventory(par1InventoryPlayer.field_70458_d);
    }

    public ItemStack func_82846_b(EntityPlayer par1EntityPlayer, int par1)
    {
        ItemStack var2 = null;
        Slot var3 = (Slot)this.field_75151_b.get(par1);
        if ((var3 != null) && (var3.func_75216_d()))
        {
            ItemStack itemStack = var3.func_75211_c();
            var2 = itemStack.func_77946_l();
            if (par1 > 2)
            {
                if (itemStack.field_77993_c == AtomicScience.itemCell.field_77779_bT)
                {
                    if (!func_75135_a(itemStack, 1, 2, false)) {
                        return null;
                    }
                }
                else if (!func_75135_a(itemStack, 0, 1, false)) {
                    return null;
                }
            }
            else if (!func_75135_a(itemStack, 3, 39, false)) {
                return null;
            }
            if (itemStack.field_77994_a == 0) {
                var3.func_75215_d((ItemStack)null);
            } else {
                var3.func_75218_e();
            }
            if (itemStack.field_77994_a == var2.field_77994_a) {
                return null;
            }
            var3.func_82870_a(par1EntityPlayer, itemStack);
        }
        return var2;
    }
}
