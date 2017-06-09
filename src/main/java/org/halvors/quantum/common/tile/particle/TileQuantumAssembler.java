package org.halvors.quantum.common.tile.particle;

import net.minecraft.block.material.Material;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.api.recipe.QuantumAssemblerRecipes;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.transform.vector.Vector3;
import org.halvors.quantum.lib.prefab.tile.TileElectricalInventory;
import universalelectricity.api.electricity.IVoltageInput;
import universalelectricity.api.energy.EnergyStorageHandler;

public class TileQuantumAssembler extends TileElectricalInventory implements IVoltageInput { // IPacketReceiver
    private long energyCapacity = 10000000000000L;
    public int maxTime = 20 * 120;
    public int time = 0;

    // Used for rendering.
    public float rotationYaw1 = 0;
    public float rotationYaw2 = 0;
    public float rotationYaw3 = 0;

    //Used for rendering.
    public EntityItem entityItem = null;

    public TileQuantumAssembler() {
        super(Material.iron);

        energy = new EnergyStorageHandler(energyCapacity);
        maxSlots = 6 + 1;
        isOpaqueCube = false;
        normalRender = false;
        customItemRender = true;
        textureName = "machine";
    }

    /**
     * Called when the block is right clicked by the player
     */
    @Override
    public boolean use(EntityPlayer player, int side, Vector3 hit) {
        if (!world().isRemote) {
            player.openGui(Quantum.getInstance(), 0, getWorld(), xCoord, yCoord, zCoord);

            return true;
        }

        return true;
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        if (!this.worldObj.isRemote) {
            if (this.canProcess()) {
                if (energy.checkExtract()) {
                    if (time == 0) {
                        time = maxTime;
                    }

                    if (time > 0) {
                        time--;

                        if (time < 1) {
                            process();
                            time = 0;
                        }
                    } else {
                        this.time = 0;
                    }

                    energy.extractEnergy(energyCapacity, true);
                }
            } else {
                time = 0;
            }

            if (ticks % 10 == 0) {
                for (EntityPlayer player : getPlayersUsing()) {
                    // TODO: Fix this.
                    //PacketDispatcher.sendPacketToPlayer(getDescriptionPacket(), player);
                }
            }
        } else if (time > 0) {
            if (ticks % 600 == 0) {
                worldObj.playSoundEffect(xCoord, yCoord, zCoord, Reference.PREFIX + "assembler", 0.7F, 1F);
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
    public long onReceiveEnergy(ForgeDirection from, long receive, boolean doReceive) {
        if (canProcess()) {
            return super.onReceiveEnergy(from, receive, doReceive);
        }

        return 0;
    }


    /*
    public void onReceivePacket(ByteArrayDataInput data, EntityPlayer player, extra: AnyRef*) {
        try {
            time = data.readInt();
            int itemId = data.readInt();
            int itemAmount = data.readInt();
            int itemMeta = data.readInt();

            if (itemId != -1 && itemAmount != -1 && itemMeta != -1) {
                setInventorySlotContents(6, new ItemStack(Item.itemsList(itemId), itemAmount, itemMeta))
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */

    @Override
    public Packet getDescriptionPacket() {
        // TODO: Fix this.
        /*
        if (this.getStackInSlot(6) != null) {
            return ResonantInduction.PACKET_TILE.getPacket(this, Int.box(time), Int.box(getStackInSlot(6).itemID), Int.box(getStackInSlot(6).stackSize), Int.box(getStackInSlot(6).getItemDamage));
        }

        return ResonantInduction.PACKET_TILE.getPacket(this, Int.box(time), Int.box(-1), Int.box(-1), Int.box(-1));
        */

        return null;
    }

    @Override
    public void openChest() {
        if (!worldObj.isRemote) {
            for (EntityPlayer player : getPlayersUsing()) {
                // TODO: Fix this.
                //PacketDispatcher.sendPacketToPlayer(getDescriptionPacket(), player)
            }
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

    /**
     * Turn one item from the furnace source stack into the appropriate smelted item in the furnace
     * result stack
     */
    public void process() {
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

    /**
     * Reads a tile entity from NBT.
     */
    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);

        time = tagCompound.getInteger("smeltingTicks");
    }

    /**
     * Writes a tile entity to NBT.
     */
    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        tagCompound.setInteger("smeltingTicks", time);
    }

    @Override
    public boolean isItemValidForSlot(int slotId, ItemStack itemStack) {
        if (slotId == 6) {
            return true;
        }

        return itemStack.getItem() == Quantum.itemDarkMatter;
    }

    public long getVoltageInput(ForgeDirection from) {
        return 1000;
    }

    public void onWrongVoltage(ForgeDirection direction, long voltage) {

    }
}