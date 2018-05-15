package org.halvors.nuclearphysics.common.tile.machine;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.items.ItemStackHandler;
import org.halvors.nuclearphysics.api.recipe.QuantumAssemblerRecipes;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.block.states.BlockStateMachine.EnumMachine;
import org.halvors.nuclearphysics.common.capabilities.energy.EnergyStorage;
import org.halvors.nuclearphysics.common.init.ModSoundEvents;
import org.halvors.nuclearphysics.common.network.packet.PacketTileEntity;
import org.halvors.nuclearphysics.common.tile.TileInventoryMachine;
import org.halvors.nuclearphysics.common.utility.InventoryUtility;
import org.halvors.nuclearphysics.common.utility.OreDictionaryHelper;

public class TileQuantumAssembler extends TileInventoryMachine {
    private static final int ENERGY_PER_TICK = 2048000;
    public static final int TICKS_REQUIRED = 120 * 20;

    // Used for rendering.
    private EntityItem entityItem = null;
    private float rotationYaw1, rotationYaw2, rotationYaw3;

    public TileQuantumAssembler() {
        this(EnumMachine.QUANTUM_ASSEMBLER);
    }

    public TileQuantumAssembler(final EnumMachine type) {
        super(type);

        energyStorage = new EnergyStorage(ENERGY_PER_TICK * 2);
        inventory = new ItemStackHandler(7) {
            @Override
            protected void onContentsChanged(final int slot) {
                super.onContentsChanged(slot);
                markDirty();
            }

            private boolean isItemValidForSlot(final int slot, final ItemStack itemStack) {
                switch (slot) {
                    case 6:
                        return QuantumAssemblerRecipes.hasRecipe(itemStack);
                }

                return OreDictionaryHelper.isDarkmatterCell(itemStack);
            }

            @Override
            public ItemStack insertItem(final int slot, final ItemStack stack, final boolean simulate) {
                if (!isItemValidForSlot(slot, stack)) {
                    return stack;
                }

                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void update() {
        super.update();

        if (!world.isRemote) {
            if (canFunction() && canProcess() && energyStorage.extractEnergy(ENERGY_PER_TICK, true) >= ENERGY_PER_TICK) {
                if (operatingTicks < TICKS_REQUIRED) {
                    operatingTicks++;
                } else {
                    process();
                    reset();
                }

                energyUsed = energyStorage.extractEnergy(ENERGY_PER_TICK, false);
            } else if (inventory.getStackInSlot(6) == null) {
                reset();
            }

            if (world.getWorldTime() % 10 == 0) {
                NuclearPhysics.getPacketHandler().sendToReceivers(new PacketTileEntity(this), this);
            }
        } else  {
            if (operatingTicks > 0) {
                if (world.getWorldTime() % 600 == 0) {
                    world.playSound(null, pos, ModSoundEvents.ASSEMBLER, SoundCategory.BLOCKS, 0.7F, 1);
                }

                rotationYaw1 += 3;
                rotationYaw2 += 2;
                rotationYaw3 += 1;
            }

            final ItemStack itemStack = inventory.getStackInSlot(6);

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
        final ItemStack itemStack = inventory.getStackInSlot(6);

        if (itemStack != null) {
            if (QuantumAssemblerRecipes.hasRecipe(itemStack)) {
                for (int i = 0; i <= 5; i++) {
                    final ItemStack itemStackInSlot = inventory.getStackInSlot(i);

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
                if (inventory.getStackInSlot(slot) != null) {
                    InventoryUtility.decrStackSize(inventory, 1);
                }
            }

            final ItemStack itemStack = inventory.getStackInSlot(6);

            if (itemStack != null) {
                itemStack.stackSize++;
            }
        }
    }

    private EntityItem getEntityForItem(final ItemStack itemStack) {
        final EntityItem entityItem = new EntityItem(world, 0, 0, 0, itemStack.copy());
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
