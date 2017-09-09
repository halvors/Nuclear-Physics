package org.halvors.nuclearphysics.common.tile.machine;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.ItemStackHandler;
import org.halvors.nuclearphysics.api.recipe.QuantumAssemblerRecipes;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.block.machine.BlockMachine.EnumMachine;
import org.halvors.nuclearphysics.common.init.ModItems;
import org.halvors.nuclearphysics.common.init.ModSoundEvents;
import org.halvors.nuclearphysics.common.network.packet.PacketTileEntity;
import org.halvors.nuclearphysics.common.utility.EnergyUtility;
import org.halvors.nuclearphysics.common.utility.InventoryUtility;
import org.halvors.nuclearphysics.common.utility.OreDictionaryHelper;

public class TileQuantumAssembler extends TileMachine implements ITickable {
    private static final int energyPerTick = 10000000; // Fix this.

    // Used for rendering.
    public float rotationYaw1 = 0;
    public float rotationYaw2 = 0;
    public float rotationYaw3 = 0;

    //Used for rendering.
    public EntityItem entityItem = null;

    public TileQuantumAssembler() {
        this(EnumMachine.QUANTUM_ASSEMBLER);
    }

    public TileQuantumAssembler(EnumMachine type) {
        super(type);

        ticksRequired = 120 * 20;

        energyStorage = new EnergyStorage(energyPerTick);
        inventory = new ItemStackHandler(7) {
            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                markDirty();
            }

            private boolean isItemValidForSlot(int slot, ItemStack itemStack) {
                return slot == 6 || OreDictionaryHelper.isDarkmatterCell(itemStack);
            }

            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                if (!isItemValidForSlot(slot, stack)) {
                    return stack;
                }

                return super.insertItem(slot, stack, simulate);
            }
        };
    }

    @Override
    public void update() {
        if (!world.isRemote) {
            EnergyUtility.discharge(0, this);

            if (canProcess() && energyStorage.extractEnergy(energyPerTick, true) >= energyPerTick) {
                if (operatingTicks < ticksRequired) {
                    operatingTicks++;
                } else {
                    process();

                    operatingTicks = 0;
                }

                energyStorage.extractEnergy(energyPerTick, false);
            }

            if (world.getWorldTime() % 10 == 0) {
                NuclearPhysics.getPacketHandler().sendToReceivers(new PacketTileEntity(this), this);
            }
        } else if (operatingTicks > 0) {
            if (world.getWorldTime() % 600 == 0) {
                world.playSound(null, pos, ModSoundEvents.ASSEMBLER, SoundCategory.BLOCKS, 0.7F, 1);
            }

            rotationYaw1 += 3;
            rotationYaw2 += 2;
            rotationYaw3 += 1;

            ItemStack itemStack = inventory.getStackInSlot(6);

            if (itemStack != null) {
                itemStack = itemStack.copy();
                itemStack.stackSize = 1;

                if (entityItem == null) {
                    entityItem = new EntityItem(world, 0, 0, 0, itemStack);
                } else if (!itemStack.isItemEqual(entityItem.getEntityItem())) {
                    entityItem = new EntityItem(world, 0, 0, 0, itemStack);
                }

                // TODO: Howto port this to 1.10.2?
                //entityItem.age++;
            } else {
                entityItem = null;
            }
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*

    @Override
    public void openInventory(EntityPlayer player) {
        if (!world.isRemote) {
            NuclearPhysics.getPacketHandler().sendTo(new PacketTileEntity(this), (EntityPlayerMP) player);
        }
    }
    */

    public boolean canProcess() {
        ItemStack itemStack = inventory.getStackInSlot(6);

        if (itemStack != null) {
            if (QuantumAssemblerRecipes.hasItemStack(itemStack)) {
                for (int i = 0; i < 6; i++) {
                    ItemStack slotItemStack = inventory.getStackInSlot(i);

                    if (slotItemStack == null) {
                        return false;
                    }

                    if (slotItemStack.getItem() != ModItems.itemDarkMatterCell) {
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
            for (int slot = 0; slot < 6; slot++) {
                if (inventory.getStackInSlot(slot) != null) {
                    InventoryUtility.decrStackSize(inventory, slot);
                }
            }

            ItemStack itemStack = inventory.getStackInSlot(6);

            if (itemStack != null) {
                itemStack.stackSize++;
            }
        }
    }
}