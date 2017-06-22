package org.halvors.quantum.common.tile.particle;

import cofh.api.energy.EnergyStorage;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.ConfigurationManager;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.base.tile.ITileNetworkable;
import org.halvors.quantum.common.block.reactor.fusion.IElectromagnet;
import org.halvors.quantum.common.entity.particle.EntityParticle;
import org.halvors.quantum.common.item.particle.ItemAntimatter;
import org.halvors.quantum.common.item.particle.ItemDarkmatter;
import org.halvors.quantum.common.network.packet.PacketTileEntity;
import org.halvors.quantum.common.tile.TileElectricInventory;
import org.halvors.quantum.common.transform.vector.Vector3;
import org.halvors.quantum.lib.IRotatable;

import java.util.List;

public class TileAccelerator extends TileElectricInventory implements ITileNetworkable, IElectromagnet, IRotatable {
    /** Joules required per ticks. */
    public static final int energyPerTick = 4800; // TODO: Get the correct value here, 4800000 (UniversalElectricity) units.

    /** User client side to determine the velocity of the particle. */
    public static final float clientParticleVelocity = 0.9F;

    /** The total amount of energy consumed by this particle. In Joules. */
    public float totalEnergyConsumed = 0; // Synced

    /** The amount of anti-matter stored within the accelerator. Measured in milligrams. */
    public int antimatter; // Synced
    public EntityParticle entityParticle;

    public float velocity; // Synced

    private long clientEnergy = 0; // Synced

    private int lastSpawnTick = 0;

    /** Multiplier that is used to give extra anti-matter based on density (hardness) of a given ore. */
    private static final int DENSITY_MULTIPLYER_DEFAULT = 1;
    private int antiMatterDensityMultiplyer = DENSITY_MULTIPLYER_DEFAULT;

    public TileAccelerator() {
        super(4);

        energyStorage = new EnergyStorage(energyPerTick * 2, energyPerTick / 20);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        if (!worldObj.isRemote) {
            clientEnergy = energyStorage.getEnergyStored();
            velocity = 0;

            // Calculate accelerated particle velocity if it is spawned.
            if (entityParticle != null) {
                velocity = (float) entityParticle.getParticleVelocity();
            }

            if (getStackInSlot(1) != null) {
                // Check if item inside of empty cell slot is indeed an empty slot.
                if (getStackInSlot(1).getItem() == Quantum.itemCell) {
                    // Check if there are any empty cells we can store anti-matter in.
                    if (getStackInSlot(1).stackSize > 0) {
                        // Craft anti-matter item if there is enough anti-matter to actually do so.
                        if (antimatter >= 125) {
                            if (getStackInSlot(2) != null) {
                                // Increase the existing amount of anti-matter if stack already exists.
                                if (getStackInSlot(2).getItem() == Quantum.itemAntimatter) {
                                    ItemStack newStack = getStackInSlot(2).copy();

                                    if (newStack.stackSize < newStack.getMaxStackSize()) {
                                        // Remove an empty cell which we will put the anti-matter into.
                                        decrStackSize(1, 1);

                                        // Remove anti-matter from internal reserve and increase stack count.
                                        antimatter -= 125;
                                        newStack.stackSize++;
                                        setInventorySlotContents(2, newStack);
                                    }
                                }
                            } else {
                                // Remove some of the internal reserves of anti-matter and use it to craft an individual item.
                                antimatter -= 125;
                                decrStackSize(1, 1);
                                setInventorySlotContents(2, new ItemStack(Quantum.itemAntimatter));
                            }
                        }
                    }
                }
            }

            // Check if redstone signal is currently being applied.
            if (worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord)) {
                //if (energyStorage.extractEnergy(energyStorage.getMaxExtract(), true) >= energyStorage.getMaxExtract()) {
                    if (entityParticle == null) {
                        // Creates a accelerated particle if one needs to exist (on world load for example or player login).
                        if (getStackInSlot(0) != null && lastSpawnTick >= 40) {
                            Vector3 spawnAcceleratedParticle = new Vector3(this);
                            spawnAcceleratedParticle.translate(getDirection().getOpposite());
                            spawnAcceleratedParticle.translate(0.5F);

                            // Only render the particle if container within the proper environment for it.
                            if (EntityParticle.canSpawnParticle(worldObj, spawnAcceleratedParticle)) {
                                // Spawn the particle.
                                totalEnergyConsumed = 0;
                                entityParticle = new EntityParticle(worldObj, spawnAcceleratedParticle, new Vector3(this), getDirection().getOpposite());
                                worldObj.spawnEntityInWorld(entityParticle);

                                // Grabs input block hardness if available, otherwise defaults are used.
                                calculateParticleDensity();

                                // Decrease particle we want to collide.
                                decrStackSize(0, 1);
                                lastSpawnTick = 0;
                            }
                        }
                    } else {
                        if (entityParticle.isDead) {
                            // On particle collision we roll the dice to see if dark-matter is generated.
                            if (entityParticle.didParticleCollide) {
                                if (worldObj.rand.nextFloat() <= ConfigurationManager.General.darkMatterSpawnChance) {
                                    incrStackSize(3, new ItemStack(Quantum.itemDarkMatter));
                                }
                            }

                            entityParticle = null;
                        } else if (velocity > clientParticleVelocity) {
                            // Play sound of anti-matter being created.
                            worldObj.playSoundEffect(xCoord, yCoord, zCoord, Reference.PREFIX + "tile.antimatter", 2F, 1F - worldObj.rand.nextFloat() * 0.3F);

                            // Create anti-matter in the internal reserve.
                            int generatedAntimatter = 5 + worldObj.rand.nextInt(antiMatterDensityMultiplyer);
                            antimatter += generatedAntimatter;

                            // Reset energy consumption levels and destroy accelerated particle.
                            totalEnergyConsumed = 0;
                            entityParticle.setDead();
                            entityParticle = null;
                        }

                        // Plays sound of particle accelerating past the speed based on total velocity at the time of anti-matter creation.
                        if (entityParticle != null) {
                            worldObj.playSoundEffect(xCoord, yCoord, zCoord, Reference.PREFIX + "tile.accelerator", 1.5F, (float) (0.6F + (0.4 * (entityParticle.getParticleVelocity()) / TileAccelerator.clientParticleVelocity)));
                        }
                    }

                    energyStorage.extractEnergy(energyStorage.getMaxExtract(), false);
                /*
                } else {
                    if (entityParticle != null) {
                        entityParticle.setDead();
                    }

                    entityParticle = null;
                }
                */
            } else {
                if (entityParticle != null) {
                    entityParticle.setDead();
                }

                entityParticle = null;
            }

            if (worldObj.getWorldTime() % 5 == 0) {
                for (EntityPlayer player : playersUsing) {
                    Quantum.getPacketHandler().sendTo(new PacketTileEntity(this), (EntityPlayerMP) player);
                }
            }

            lastSpawnTick++;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);

        totalEnergyConsumed = tagCompound.getFloat("totalEnergyConsumed");
        antimatter = tagCompound.getInteger("antimatter");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        tagCompound.setFloat("totalEnergyConsumed", totalEnergyConsumed);
        tagCompound.setInteger("antimatter", antimatter);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handlePacketData(ByteBuf dataStream) throws Exception {
        if (worldObj.isRemote) {
            totalEnergyConsumed = dataStream.readFloat();
            antimatter = dataStream.readInt();
            velocity = dataStream.readFloat();
            clientEnergy = dataStream.readLong();
        }
    }

    @Override
    public List<Object> getPacketData(List<Object> objects) {
        objects.add(totalEnergyConsumed);
        objects.add(antimatter);
        objects.add(velocity);
        objects.add(clientEnergy);

        return objects;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean isRunning() {
        return true;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void calculateParticleDensity() {
        ItemStack itemToAccelerate = getStackInSlot(0);

        if (itemToAccelerate != null) {
            // Calculate block density multiplier if ore dictionary block.
            antiMatterDensityMultiplyer = DENSITY_MULTIPLYER_DEFAULT;

            try {
                Block potentialBlock = Block.getBlockFromItem(itemToAccelerate.getItem());

                if (potentialBlock != null) {
                    // Prevent negative numbers and disallow zero for density multiplier.

                    // TODO: Fix this.
                    //antiMatterDensityMultiplyer = BlockUtility.getBlockHardness(potentialBlock).asInstanceOf[Int];

                    if (antiMatterDensityMultiplyer <= 0) {
                        antiMatterDensityMultiplyer = 1;
                    }

                    // AtomicScience.LOGGER.info("[Particle Accelerator] " + String.valueOf(potentialBlock.getUnlocalizedName()) + " Hardness: " + String.valueOf(antiMatterDensityMultiplyer));
                }
            } catch (Exception e) {
                antiMatterDensityMultiplyer = DENSITY_MULTIPLYER_DEFAULT;
                // AtomicScience.LOGGER.info("[Particle Accelerator] Attempted to query Minecraft block-list with value out of index.");
            }
        }
    }

    @Override
    public int[] getSlotsForFace(int side) {
        return new int[] { 0, 1, 2, 3 };
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemStack, int j) {
        return isItemValidForSlot(slot, itemStack) && slot != 2 && slot != 3;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack itemstack, int j)
    {
        return slot == 2 || slot == 3;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
        switch (slot) {
            case 0:
                return true;
            case 1:
                return Quantum.itemCell == itemStack.getItem();
            case 2:
                return itemStack.getItem() instanceof ItemAntimatter;
            case 3:
                return itemStack.getItem() instanceof ItemDarkmatter;
        }

        return false;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
        if (!simulate) {
            totalEnergyConsumed += maxReceive;
        }

        if (getStackInSlot(0) != null) {//&&(worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord) || worldObj.getBlockPowerInput(xCoord, yCoord, zCoord) > 0)) {
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
    public ForgeDirection getDirection() {
        return ForgeDirection.getOrientation(worldObj.getBlockMetadata(xCoord, yCoord, zCoord));
    }

    @Override
    public void setDirection(ForgeDirection direction) {
        worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, direction.ordinal(), 3);
    }
}