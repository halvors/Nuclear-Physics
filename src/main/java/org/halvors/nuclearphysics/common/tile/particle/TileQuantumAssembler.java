package org.halvors.nuclearphysics.common.tile.particle;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import org.halvors.nuclearphysics.api.recipe.QuantumAssemblerRecipes;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.Reference;
import org.halvors.nuclearphysics.common.block.machine.BlockMachine.EnumMachine;
import org.halvors.nuclearphysics.common.capabilities.energy.EnergyStorage;
import org.halvors.nuclearphysics.common.init.ModSounds;
import org.halvors.nuclearphysics.common.network.packet.PacketTileEntity;
import org.halvors.nuclearphysics.common.tile.TileInventoryMachine;
import org.halvors.nuclearphysics.common.utility.InventoryUtility;
import org.halvors.nuclearphysics.common.utility.OreDictionaryHelper;

public class TileQuantumAssembler extends TileInventoryMachine {
    public static final int ticksRequired = 120 * 20;
    private static final int energyPerTick = 2048000;

    // Used for rendering.
    private EntityItem entityItem = null;
    private float rotationYaw1, rotationYaw2, rotationYaw3;

    public TileQuantumAssembler() {
        this(EnumMachine.QUANTUM_ASSEMBLER);
    }

    public TileQuantumAssembler(EnumMachine type) {
        super(type, 7);

        energyStorage = new EnergyStorage(energyPerTick * 2);

        /*
        inventory = new ItemStackHandler(7) {
            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                markDirty();
            }

            private boolean isItemValidForSlot(int slot, ItemStack itemStack) {
                switch (slot) {
                    case 6:
                        return QuantumAssemblerRecipes.hasRecipe(itemStack);
                }

                return OreDictionaryHelper.isDarkmatterCell(itemStack);
            }

            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                if (!isItemValidForSlot(slot, stack)) {
                    return stack;
                }

                return super.insertItem(slot, stack, simulate);
            }
        };
        */
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void updateEntity() {
        super.updateEntity();

        if (!worldObj.isRemote) {
            if (canFunction() && canProcess() && energyStorage.extractEnergy(energyPerTick, true) >= energyPerTick) {
                if (operatingTicks < ticksRequired) {
                    operatingTicks++;
                } else {
                    process();
                    reset();
                }

                energyUsed = energyStorage.extractEnergy(energyPerTick, false);
            } else if (getStackInSlot(6) == null) {
                reset();
            }

            if (worldObj.getWorldTime() % 10 == 0) {
                NuclearPhysics.getPacketHandler().sendToReceivers(new PacketTileEntity(this), this);
            }
        } else  {
            if (operatingTicks > 0) {
                if (worldObj.getWorldTime() % 600 == 0) {
                    worldObj.playSoundEffect(xCoord, yCoord, zCoord, ModSounds.ASSEMBLER, 0.7F, 1);
                }

                rotationYaw1 += 3;
                rotationYaw2 += 2;
                rotationYaw3 += 1;
            }

            ItemStack itemStack = getStackInSlot(6);

            if (itemStack != null) {
                if (entityItem == null || !itemStack.isItemEqual(entityItem.getEntityItem())) {
                    entityItem = getEntityForItem(itemStack);
                }
            } else {
                entityItem = null;
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public boolean canProcess() {
        ItemStack itemStack = getStackInSlot(6);

        if (itemStack != null) {
            if (QuantumAssemblerRecipes.hasRecipe(itemStack)) {
                for (int i = 0; i <= 5; i++) {
                    ItemStack itemStackInSlot = getStackInSlot(i);

                    if (!OreDictionaryHelper.isDarkmatterCell(itemStackInSlot)) {
                        return false;
                    }
                }
            }

            return itemStack.stackSize < 64;
        }

        return false;
    }

    // Turn one item from the furnace source stack into the appropriate smelted item in the furnace result stack.
    private void process() {
        if (canProcess()) {
            for (int slot = 0; slot <= 5; slot++) {
                ItemStack itemStack = getStackInSlot(slot);

                if (itemStack != null) {
                    InventoryUtility.decrStackSize(itemStack, slot);
                }
            }

            ItemStack itemStack = getStackInSlot(6);

            if (itemStack != null) {
                itemStack.stackSize++;
            }
        }
    }

    private EntityItem getEntityForItem(ItemStack itemStack) {
        EntityItem entityItem = new EntityItem(worldObj, 0, 0, 0, itemStack.copy());
        entityItem.setAgeToCreativeDespawnTime();

        return entityItem;
    }

    public EntityItem getEntityItem() {
        return entityItem;
    }

    public float getRotationYaw1() {
        return rotationYaw1;
    }

    public float getRotationYaw2() {
        return rotationYaw2;
    }

    public float getRotationYaw3() {
        return rotationYaw3;
    }
}