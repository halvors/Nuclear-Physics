package org.halvors.atomicscience.old.fission.reactor;

import atomicscience.fission.ItemBreederFuel;
import atomicscience.fission.ItemFissileFuel;
import calclavia.lib.gui.ContainerBase;
import calclavia.lib.prefab.slot.SlotSpecific;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerReactorCell
        extends ContainerBase
{
    public ContainerReactorCell(EntityPlayer player, TileReactorCell tileEntity)
    {
        super(tileEntity);
        func_75146_a(new SlotSpecific(tileEntity, 0, 79, 17, new Class[] { ItemFissileFuel.class, ItemBreederFuel.class }));
        addPlayerInventory(player);
    }

    public ItemStack func_82846_b(EntityPlayer par1EntityPlayer, int par1)
    {
        ItemStack var2 = null;
        Slot var3 = (Slot)this.field_75151_b.get(par1);
        if ((var3 != null) && (var3.func_75216_d()))
        {
            ItemStack itemStack = var3.func_75211_c();
            var2 = itemStack.func_77946_l();
            if (par1 >= this.slotCount)
            {
                if (func_75139_a(0).func_75214_a(itemStack))
                {
                    if (!func_75135_a(itemStack, 0, 1, false)) {
                        return null;
                    }
                }
                else if (par1 < 27 + this.slotCount)
                {
                    if (!func_75135_a(itemStack, 27 + this.slotCount, 36 + this.slotCount, false)) {
                        return null;
                    }
                }
                else if ((par1 >= 27 + this.slotCount) && (par1 < 36 + this.slotCount) && (!func_75135_a(itemStack, 4, 30, false))) {
                    return null;
                }
            }
            else if (!func_75135_a(itemStack, this.slotCount, 36 + this.slotCount, false)) {
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
