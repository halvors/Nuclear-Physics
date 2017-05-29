package org.halvors.atomicscience.old.process;

import calclavia.lib.gui.ContainerBase;
import calclavia.lib.prefab.slot.SlotEnergyItem;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;

public class ContainerChemicalExtractor
        extends ContainerBase
{
    private static final int slotCount = 5;
    private TileChemicalExtractor tileEntity;

    public ContainerChemicalExtractor(InventoryPlayer par1InventoryPlayer, TileChemicalExtractor tileEntity)
    {
        super(tileEntity);
        this.tileEntity = tileEntity;

        func_75146_a(new SlotEnergyItem(tileEntity, 0, 80, 50));

        func_75146_a(new Slot(tileEntity, 1, 53, 25));

        func_75146_a(new SlotFurnace(par1InventoryPlayer.field_70458_d, tileEntity, 2, 107, 25));

        func_75146_a(new Slot(tileEntity, 3, 25, 19));

        func_75146_a(new Slot(tileEntity, 4, 25, 50));

        func_75146_a(new Slot(tileEntity, 5, 135, 19));

        func_75146_a(new Slot(tileEntity, 6, 135, 50));

        addPlayerInventory(par1InventoryPlayer.field_70458_d);
    }
}
