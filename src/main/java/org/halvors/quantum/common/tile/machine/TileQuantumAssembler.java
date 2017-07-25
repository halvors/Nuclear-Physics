package org.halvors.quantum.common.tile.machine;

import cofh.api.energy.EnergyStorage;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import org.halvors.quantum.api.recipe.QuantumAssemblerRecipes;
import org.halvors.quantum.common.Quantum;
import org.halvors.quantum.common.QuantumItems;
import org.halvors.quantum.common.network.packet.PacketTileEntity;
import org.halvors.quantum.common.tile.ITileNetwork;

import java.util.List;

public class TileQuantumAssembler extends TileMachine implements ITickable {
    public static final int tickTime = 20 * 120;
    private static final int energy = 10000000; // Fix this.

    public int time = 0; // Synced

    // Used for rendering.
    public float rotationYaw1 = 0;
    public float rotationYaw2 = 0;
    public float rotationYaw3 = 0;

    //Used for rendering.
    public EntityItem entityItem = null;

    public TileQuantumAssembler() {
        super(6 + 1);

        energyStorage = new EnergyStorage(energy);
    }

    @Override
    public void update() {
        if (!world.isRemote) {
            if (canProcess() && energyStorage.extractEnergy(energyStorage.getMaxExtract(), true) >= energyStorage.getMaxExtract()) {
                if (time == 0) {
                    time = tickTime;
                }

                if (time > 0) {
                    time--;

                    if (time < 1) {
                        doProcess();
                        time = 0;
                    }
                } else {
                    time = 0;
                }

                energyStorage.extractEnergy(energy, false);
            } else {
                time = 0;
            }

            if (world.getWorldTime() % 10 == 0) {
                if (!world.isRemote) {
                    Quantum.getPacketHandler().sendToReceivers(new PacketTileEntity(this), getPlayersUsing());
                }
            }
        } else if (time > 0) {
            if (world.getWorldTime() % 600 == 0) {
                //world.playSoundEffect(xCoord, yCoord, zCoord, Reference.PREFIX + "tile.assembler", 0.7F, 1F);
            }

            rotationYaw1 += 3;
            rotationYaw2 += 2;
            rotationYaw3 += 1;

            ItemStack itemStack = getStackInSlot(6);

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


    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);

        time = tagCompound.getInteger("smeltingTicks");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        tagCompound.setInteger("smeltingTicks", time);

        return tagCompound;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handlePacketData(ByteBuf dataStream) {
        super.handlePacketData(dataStream);

        // TODO: Check if there should be any kind of item sync here.

        time = dataStream.readInt();
    }

    @Override
    public List<Object> getPacketData(List<Object> objects) {
        super.getPacketData(objects);

        // TODO: Check if there should be any kind of item sync here.

        objects.add(time);

        return objects;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
        if (canProcess()) {
            return super.receiveEnergy(from, maxReceive, simulate);
        }

        return 0;
    }

    @Override
    public int extractEnergy(EnumFacing from, int maxExtract, boolean simulate) {
        return 0;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        if (slot == 6) {
            return true;
        }

        return itemStack.getItem() == QuantumItems.itemDarkMatterCell;
    }

    @Override
    public void openInventory(EntityPlayer player) {
        if (!world.isRemote) {
            Quantum.getPacketHandler().sendTo(new PacketTileEntity(this), (EntityPlayerMP) player);
        }
    }

    public boolean canProcess() {
        if (getStackInSlot(6) != null) {
            if (QuantumAssemblerRecipes.hasItemStack(getStackInSlot(6))) {
                for (int i = 0; i < 6; i++) {
                    if (getStackInSlot(i) == null) {
                        return false;
                    }

                    if (getStackInSlot(i).getItem() != QuantumItems.itemDarkMatterCell) {
                        return false;
                    }
                }
            }

            return getStackInSlot(6).stackSize < 64;
        }

        return false;
    }

    // Turn one item from the furnace source stack into the appropriate smelted item in the furnace result stack.
    public void doProcess() {
        if (canProcess()) {
            for (int i = 0; i < 6; i++) {
                if (getStackInSlot(i) != null) {
                    decrStackSize(i, 1);
                }
            }

            if (getStackInSlot(6) != null) {
                getStackInSlot(6).stackSize++;
            }
        }
    }
}