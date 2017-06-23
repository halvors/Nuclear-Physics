package org.halvors.quantum.common.tile.machine;

import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyReceiver;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.api.recipe.QuantumAssemblerRecipes;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.base.tile.ITileNetworkable;
import org.halvors.quantum.common.network.packet.PacketTileEntity;
import org.halvors.quantum.common.tile.TileElectricInventory;

import java.util.List;

public class TileQuantumAssembler extends TileElectricInventory implements ITileNetworkable, IEnergyReceiver {
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
    public void updateEntity() {
        super.updateEntity();

        if (!worldObj.isRemote) {
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

            if (worldObj.getWorldTime() % 10 == 0) {
                if (!worldObj.isRemote) {
                    Quantum.getPacketHandler().sendToReceivers(new PacketTileEntity(this), getPlayersUsing());
                }
            }
        } else if (time > 0) {
            if (worldObj.getWorldTime() % 600 == 0) {
                worldObj.playSoundEffect(xCoord, yCoord, zCoord, Reference.PREFIX + "tile.assembler", 0.7F, 1F);
            }

            rotationYaw1 += 3;
            rotationYaw2 += 2;
            rotationYaw3 += 1;

            ItemStack itemStack = getStackInSlot(6);

            if (itemStack != null) {
                itemStack = itemStack.copy();
                itemStack.stackSize = 1;

                if (entityItem == null) {
                    entityItem = new EntityItem(worldObj, 0, 0, 0, itemStack);
                } else if (!itemStack.isItemEqual(entityItem.getEntityItem())) {
                    entityItem = new EntityItem(worldObj, 0, 0, 0, itemStack);
                }

                entityItem.age++;
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
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        tagCompound.setInteger("smeltingTicks", time);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handlePacketData(ByteBuf dataStream) throws Exception {
        // TODO: Check if there should be any kind of item sync here.

        time = dataStream.readInt();
    }

    @Override
    public List<Object> getPacketData(List<Object> objects) {
        // TODO: Check if there should be any kind of item sync here.

        objects.add(time);

        return objects;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
        if (canProcess()) {
            return super.receiveEnergy(from, maxReceive, simulate);
        }

        return 0;
    }

    @Override
    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
        return 0;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        if (slot == 6) {
            return true;
        }

        return itemStack.getItem() == Quantum.itemDarkMatter;
    }

    @Override
    public void openChest() {
        if (!worldObj.isRemote) {
            Quantum.getPacketHandler().sendToReceivers(new PacketTileEntity(this), this);
        }
    }

    public boolean canProcess() {
        if (getStackInSlot(6) != null) {
            if (QuantumAssemblerRecipes.hasItemStack(getStackInSlot(6))) {
                for (int i = 0; i < 6; i++) {
                    if (getStackInSlot(i) == null) {
                        return false;
                    }

                    if (getStackInSlot(i).getItem() != Quantum.itemDarkMatter) {
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