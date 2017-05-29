package org.halvors.atomicscience.old.process.fission;

import atomicscience.AtomicScience;
import calclavia.lib.gui.ContainerBase;
import calclavia.lib.prefab.slot.SlotEnergyItem;
import java.util.HashSet;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;

public class ContainerCentrifuge
        extends ContainerBase
{
    private static final int slotCount = 4;
    private TileCentrifuge tileEntity;

    public ContainerCentrifuge(InventoryPlayer par1InventoryPlayer, TileCentrifuge tileEntity)
    {
        super(tileEntity);
        this.tileEntity = tileEntity;

        func_75146_a(new SlotEnergyItem(tileEntity, 0, 131, 26));

        func_75146_a(new Slot(tileEntity, 1, 25, 50));

        func_75146_a(new SlotFurnace(par1InventoryPlayer.field_70458_d, tileEntity, 2, 81, 26));

        func_75146_a(new SlotFurnace(par1InventoryPlayer.field_70458_d, tileEntity, 3, 101, 26));
        addPlayerInventory(par1InventoryPlayer.field_70458_d);
        tileEntity.func_70295_k_();
    }

    public void func_75134_a(EntityPlayer entityplayer)
    {
        super.func_75134_a(entityplayer);
        this.tileEntity.getPlayersUsing().remove(entityplayer);
    }

    public boolean func_75145_c(EntityPlayer par1EntityPlayer)
    {
        return this.tileEntity.func_70300_a(par1EntityPlayer);
    }

    public ItemStack func_82846_b(EntityPlayer par1EntityPlayer, int par1)
    {
        ItemStack var2 = null;
        Slot var3 = (Slot)this.field_75151_b.get(par1);
        if ((var3 != null) && (var3.func_75216_d()))
        {
            ItemStack itemStack = var3.func_75211_c();
            var2 = itemStack.func_77946_l();
            if (par1 >= 4)
            {
                if (func_75139_a(0).func_75214_a(itemStack))
                {
                    if (!func_75135_a(itemStack, 0, 1, false)) {
                        return null;
                    }
                }
                else if (AtomicScience.isItemStackUraniumOre(itemStack))
                {
                    if (!func_75135_a(itemStack, 1, 2, false)) {
                        return null;
                    }
                }
                else if (AtomicScience.isItemStackEmptyCell(itemStack))
                {
                    if (!func_75135_a(itemStack, 3, 4, false)) {
                        return null;
                    }
                }
                else if (par1 < 31)
                {
                    if (!func_75135_a(itemStack, 31, 40, false)) {
                        return null;
                    }
                }
                else if ((par1 >= 31) && (par1 < 40) && (!func_75135_a(itemStack, 4, 30, false))) {
                    return null;
                }
            }
            else if (!func_75135_a(itemStack, 4, 40, false)) {
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
