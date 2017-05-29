package org.halvors.atomicscience.old.particle.quantum;

import atomicscience.AtomicScience;
import java.util.HashSet;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ContainerQuantumAssembler
        extends Container
{
    private TileQuantumAssembler tileEntity;

    public ContainerQuantumAssembler(InventoryPlayer par1InventoryPlayer, TileQuantumAssembler tileEntity)
    {
        this.tileEntity = tileEntity;
        func_75146_a(new Slot(tileEntity, 0, 80, 40));
        func_75146_a(new Slot(tileEntity, 1, 53, 56));
        func_75146_a(new Slot(tileEntity, 2, 107, 56));
        func_75146_a(new Slot(tileEntity, 3, 53, 88));
        func_75146_a(new Slot(tileEntity, 4, 107, 88));
        func_75146_a(new Slot(tileEntity, 5, 80, 103));
        func_75146_a(new Slot(tileEntity, 6, 80, 72));
        for (int var3 = 0; var3 < 3; var3++) {
            for (int var4 = 0; var4 < 9; var4++) {
                func_75146_a(new Slot(par1InventoryPlayer, var4 + var3 * 9 + 9, 8 + var4 * 18, 148 + var3 * 18));
            }
        }
        for (var3 = 0; var3 < 9; var3++) {
            func_75146_a(new Slot(par1InventoryPlayer, var3, 8 + var3 * 18, 206));
        }
        this.tileEntity.getPlayersUsing().add(par1InventoryPlayer.field_70458_d);
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
            if (par1 > 6)
            {
                if (itemStack.field_77993_c == AtomicScience.itemDarkMatter.field_77779_bT)
                {
                    if (!func_75135_a(itemStack, 0, 6, false)) {
                        return null;
                    }
                }
                else if (!func_75139_a(6).func_75216_d()) {
                    if (!func_75135_a(itemStack, 6, 7, false)) {
                        return null;
                    }
                }
            }
            else if (!func_75135_a(itemStack, 7, 43, false)) {
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
