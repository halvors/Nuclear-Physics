package org.halvors.quantum.common.tile.particle;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.items.ItemStackHandler;
import org.halvors.quantum.api.tile.IElectromagnet;
import org.halvors.quantum.common.ConfigurationManager;
import org.halvors.quantum.common.Quantum;
import org.halvors.quantum.common.QuantumItems;
import org.halvors.quantum.common.entity.EntityParticle;
import org.halvors.quantum.common.item.particle.ItemAntimatterCell;
import org.halvors.quantum.common.item.particle.ItemDarkmatterCell;
import org.halvors.quantum.common.network.packet.PacketTileEntity;
import org.halvors.quantum.common.tile.machine.TileMachine;
import org.halvors.quantum.common.utility.InventoryUtility;
import org.halvors.quantum.common.utility.OreDictionaryUtility;
import org.halvors.quantum.common.utility.transform.vector.Vector3;

import java.util.List;

public class TileAccelerator extends TileMachine implements ITickable, IElectromagnet {
    // Energy required per ticks.
    public int acceleratorEnergyCostPerTick = ConfigurationManager.General.acceleratorEnergyCostPerTick;

    // User client side to determine the velocity of the particle.
    public static final float clientParticleVelocity = 0.9F;

    /** The total amount of energy consumed by this particle. In Joules. */
    public float totalEnergyConsumed = 0; // Synced

    /** The amount of anti-matter stored within the accelerator. Measured in milligrams. */
    public int antimatter = 0; // Synced
    public EntityParticle entityParticle;

    public float velocity = 0; // Synced
    private long clientEnergy = 0; // Synced
    private int lastSpawnTick = 0;

    private int maxTransfer = acceleratorEnergyCostPerTick / 20;

    /**
     * Multiplier that is used to give extra anti-matter based on density (hardness) of a given ore.
     */
    private int acceleratorAntimatterDensityMultiplyer = ConfigurationManager.General.acceleratorAntimatterDensityMultiplier;

    public TileAccelerator() {
        energyStorage = new EnergyStorage(acceleratorEnergyCostPerTick * 2, maxTransfer);
        inventory = new ItemStackHandler(4) {
            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                markDirty();
            }

            public boolean isItemValidForSlot(int slot, ItemStack itemStack) {
                switch (slot) {
                    case 0:
                        return true;

                    case 1:
                        return OreDictionaryUtility.isEmptyCell(itemStack);

                    case 2:
                        return itemStack.getItem() instanceof ItemAntimatterCell;

                    case 3:
                        return itemStack.getItem() instanceof ItemDarkmatterCell;
                }

                return false;
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
            clientEnergy = energyStorage.getEnergyStored();
            velocity = getParticleVelocity();

            // TODO: Fix this.
            //outputAntimatter();

            // Check if redstone signal is currently being applied.
            if (inventory.getStackInSlot(0) != null && world.isBlockIndirectlyGettingPowered(pos) > 0) {
                //if (energyStorage.extractEnergy(energyStorage.getMaxExtract(), true) >= energyStorage.getMaxExtract()) {
                    if (entityParticle == null) {
                        // Creates a accelerated particle if one needs to exist (on world load for example or player login).
                        if (inventory.getStackInSlot(0) != null && lastSpawnTick >= 40) {
                            Vector3 spawnAcceleratedParticle = new Vector3(this);
                            spawnAcceleratedParticle.translate(EnumFacing.NORTH); //getDirection().getOpposite());
                            spawnAcceleratedParticle.translate(0.5F);

                            // Only render the particle if container within the proper environment for it.
                            if (EntityParticle.canSpawnParticle(world, spawnAcceleratedParticle)) {
                                // Spawn the particle.
                                totalEnergyConsumed = 0;
                                entityParticle = new EntityParticle(world, spawnAcceleratedParticle, new Vector3(this), EnumFacing.NORTH); //getDirection().getOpposite());
                                world.spawnEntity(entityParticle);

                                // Grabs input block hardness if available, otherwise defaults are used.
                                calculateParticleDensity();

                                // Decrease particle we want to collide.
                                InventoryUtility.decrStackSize(inventory, 0);
                                lastSpawnTick = 0;
                            }
                        }
                    } else {
                        if (entityParticle.isDead) {
                            // On particle collision we roll the dice to see if dark-matter is generated.
                            if (entityParticle.didParticleCollide) {
                                if (world.rand.nextFloat() <= ConfigurationManager.General.darkMatterSpawnChance) {
                                    inventory.insertItem(3, new ItemStack(QuantumItems.itemDarkMatterCell), false);
                                }
                            }

                            entityParticle = null;
                        } else if (velocity > clientParticleVelocity) {
                            // Play sound of anti-matter being created.
                            //world.playSoundEffect(xCoord, yCoord, zCoord, Reference.PREFIX + "tile.antimatter", 2F, 1F - world.rand.nextFloat() * 0.3F);

                            // Create anti-matter in the internal reserve.
                            int generatedAntimatter = 5 + world.rand.nextInt(acceleratorAntimatterDensityMultiplyer);
                            antimatter += generatedAntimatter;

                            // Reset energy consumption levels and destroy accelerated particle.
                            totalEnergyConsumed = 0;
                            entityParticle.setDead();
                            entityParticle = null;
                        }

                        // Plays sound of particle accelerating past the speed based on total velocity at the time of anti-matter creation.
                        if (entityParticle != null) {
                            //world.playSoundEffect(xCoord, yCoord, zCoord, Reference.PREFIX + "tile.accelerator", 1.5F, (float) (0.6 + (0.4 * (entityParticle.getParticleVelocity()) / TileAccelerator.clientParticleVelocity)));
                        }
                    }

                totalEnergyConsumed += energyStorage.extractEnergy(maxTransfer, false);
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

            if (world.getWorldTime() % 5 == 0) {
                Quantum.getPacketHandler().sendToReceivers(new PacketTileEntity(this), getPlayersUsing());
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
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        tagCompound.setFloat("totalEnergyConsumed", totalEnergyConsumed);
        tagCompound.setInteger("antimatter", antimatter);

        return tagCompound;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handlePacketData(ByteBuf dataStream) {
        super.handlePacketData(dataStream);

        if (world.isRemote) {
            totalEnergyConsumed = dataStream.readFloat();
            antimatter = dataStream.readInt();
            velocity = dataStream.readFloat();
            clientEnergy = dataStream.readLong();
        }
    }

    @Override
    public List<Object> getPacketData(List<Object> objects) {
        super.getPacketData(objects);

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

    /*
    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return new int[] { 0, 1, 2, 3 };
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStack, EnumFacing direction) {
        return isItemValidForSlot(index, itemStack) && index != 2 && index != 3; // TODO: Convert int to enum.
    }

    @Override
    public boolean canExtractItem(int index, ItemStack itemStack, EnumFacing direction) {
        return index == 2 || index == 3; // TODO: Convert int to enum.
    }
    */

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Converts antimatter storage into item if the condition are meet.
     */
    private void outputAntimatter() {
        // Do we have an empty cell in slot one
        ItemStack itemStackEmptyCell = inventory.getStackInSlot(1);

        if (OreDictionaryUtility.isEmptyCell(itemStackEmptyCell) && itemStackEmptyCell.stackSize > 0) {
            // Each cell can only hold 125mg of antimatter
            // TODO: maybe a config for this?
            if (antimatter >= 125) {
                ItemStack itemStack = inventory.getStackInSlot(2);

                if (itemStack != null) {
                    // If the output slot is not empty we must increase stack size
                    if (itemStack.getItem() == QuantumItems.itemAntimatterCell) {
                        ItemStack newStack = itemStack.copy();

                        if (newStack.stackSize < newStack.getMaxStackSize()) {
                            InventoryUtility.decrStackSize(inventory, 1);
                            antimatter -= 125;
                            newStack.stackSize++;
                            inventory.setStackInSlot(2, newStack);
                        }
                    }
                } else {
                    // Remove some of the internal reserves of anti-matter and use it to craft an individual item.
                    antimatter -= 125;
                    InventoryUtility.decrStackSize(inventory, 1);
                    inventory.setStackInSlot(2, new ItemStack(QuantumItems.itemAntimatterCell));
                }
            }
        }
    }

    private void calculateParticleDensity() {
        ItemStack itemToAccelerate = inventory.getStackInSlot(0);

        if (itemToAccelerate != null) {
            // Calculate block density multiplier if ore dictionary block.
            acceleratorAntimatterDensityMultiplyer = ConfigurationManager.General.acceleratorAntimatterDensityMultiplier;

            Block potentialBlock = Block.getBlockFromItem(itemToAccelerate.getItem());

            if (potentialBlock != null) {
                // Prevent negative numbers and disallow zero for density multiplier.
                acceleratorAntimatterDensityMultiplyer = (int) potentialBlock.getDefaultState().getBlockHardness(world, new BlockPos(0, 0, 0)) * ConfigurationManager.General.acceleratorAntimatterDensityMultiplier;

                if (acceleratorAntimatterDensityMultiplyer <= 0) {
                    acceleratorAntimatterDensityMultiplyer = ConfigurationManager.General.acceleratorAntimatterDensityMultiplier;
                }

                if (acceleratorAntimatterDensityMultiplyer > 1000) {
                    acceleratorAntimatterDensityMultiplyer = 1000 * ConfigurationManager.General.acceleratorAntimatterDensityMultiplier;
                }
            }
        }
    }

    // Get velocity for the particle and @return it as a float.
    public float getParticleVelocity() {
        if (entityParticle != null) {
            return (float) entityParticle.getParticleVelocity();
        }

        return 0;
    }
}