package org.halvors.quantum.common.tile.machine;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.ItemStackHandler;
import org.halvors.quantum.api.recipe.QuantumAssemblerRecipes;
import org.halvors.quantum.common.Quantum;
import org.halvors.quantum.common.init.QuantumItems;
import org.halvors.quantum.common.init.QuantumSoundEvents;
import org.halvors.quantum.common.network.packet.PacketTileEntity;
import org.halvors.quantum.common.utility.InventoryUtility;
import org.halvors.quantum.common.utility.OreDictionaryHelper;

public class TileQuantumAssembler extends TileMachine implements ITickable {
    public static final int tickTime = 20 * 120;
    private static final int energy = 10000000; // Fix this.

    public int timer = 0; // Synced

    // Used for rendering.
    public float rotationYaw1 = 0;
    public float rotationYaw2 = 0;
    public float rotationYaw3 = 0;

    //Used for rendering.
    public EntityItem entityItem = null;

    public TileQuantumAssembler() {
        energyStorage = new EnergyStorage(energy);
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
            if (canProcess() && energyStorage.extractEnergy(energy, true) >= energy) {
                if (timer == 0) {
                    timer = tickTime;
                }

                if (timer > 0) {
                    timer--;

                    if (timer < 1) {
                        doProcess();
                        timer = 0;
                    }
                } else {
                    timer = 0;
                }

                energyStorage.extractEnergy(energy, false);
            } else {
                timer = 0;
            }

            if (world.getWorldTime() % 10 == 0) {
                if (!world.isRemote) {
                    //Quantum.getPacketHandler().sendToReceivers(new PacketTileEntity(this), getPlayersUsing());
                    Quantum.getPacketHandler().sendToReceivers(new PacketTileEntity(this), this);
                }
            }
        } else if (timer > 0) {
            if (world.getWorldTime() % 600 == 0) {
                world.playSound(null, pos, QuantumSoundEvents.ASSEMBLER, SoundCategory.BLOCKS, 0.7F, 1);
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
            Quantum.getPacketHandler().sendTo(new PacketTileEntity(this), (EntityPlayerMP) player);
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

                    if (slotItemStack.getItem() != QuantumItems.itemDarkMatterCell) {
                        return false;
                    }
                }
            }

            return itemStack.stackSize < 64;
        }

        return false;
    }

    // Turn one item from the furnace source stack into the appropriate smelted item in the furnace result stack.
    private void doProcess() {
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