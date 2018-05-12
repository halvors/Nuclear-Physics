package org.halvors.nuclearphysics.common.tile.particle;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.ItemStackHandler;
import org.halvors.nuclearphysics.api.tile.IElectromagnet;
import org.halvors.nuclearphysics.common.ConfigurationManager.General;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.block.states.BlockStateMachine.EnumMachine;
import org.halvors.nuclearphysics.common.capabilities.energy.EnergyStorage;
import org.halvors.nuclearphysics.common.entity.EntityParticle;
import org.halvors.nuclearphysics.common.init.ModItems;
import org.halvors.nuclearphysics.common.init.ModSoundEvents;
import org.halvors.nuclearphysics.common.item.particle.ItemAntimatterCell;
import org.halvors.nuclearphysics.common.network.packet.PacketTileEntity;
import org.halvors.nuclearphysics.common.tile.TileInventoryMachine;
import org.halvors.nuclearphysics.common.utility.InventoryUtility;
import org.halvors.nuclearphysics.common.utility.OreDictionaryHelper;

import java.util.List;

public class TileParticleAccelerator extends TileInventoryMachine implements IElectromagnet {
    private static final String NBT_TOTAL_ENERGY_CONSUMED = "totalEnergyConsumed";
    private static final String NBT_ANTIMATTER_COUNT = "antimatterCount";
    private static final int ENERGY_PER_TICK = 19000;
    public static final float ANTIMATTER_CREATION_SPEED = 0.9F; // Speed by which a particle will turn into anitmatter.

    // Multiplier that is used to give extra anti-matter based on density (hardness) of a given ore.
    private int particleDensity = General.antimatterDensityMultiplier;

    // The amount of anti-matter stored within the accelerator. Measured in milligrams.
    private int antimatterCount = 0; // Synced

    // The total amount of energy consumed by this particle.
    public int totalEnergyConsumed = 0; // Synced

    private EntityParticle entityParticle = null;
    private float velocity = 0; // Synced
    private int lastSpawnTick = 0;

    public TileParticleAccelerator() {
        this(EnumMachine.PARTICLE_ACCELERATOR);
    }

    public TileParticleAccelerator(final EnumMachine type) {
        super(type);

        energyStorage = new EnergyStorage(ENERGY_PER_TICK * 40, ENERGY_PER_TICK);
        inventory = new ItemStackHandler(4) {
            @Override
            protected void onContentsChanged(final int slot) {
                super.onContentsChanged(slot);
                markDirty();
            }

            public boolean isItemValidForSlot(final int slot, final ItemStack itemStack) {
                switch (slot) {
                    case 0:
                        return true;

                    case 1:
                        return OreDictionaryHelper.isEmptyCell(itemStack);

                    case 2:
                        return itemStack.getItem() instanceof ItemAntimatterCell;

                    case 3:
                        return OreDictionaryHelper.isDarkmatterCell(itemStack);
                }

                return false;
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

    @Override
    public void readFromNBT(final NBTTagCompound tag) {
        super.readFromNBT(tag);

        totalEnergyConsumed = tag.getInteger(NBT_TOTAL_ENERGY_CONSUMED);
        antimatterCount = tag.getInteger(NBT_ANTIMATTER_COUNT);
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound tag) {
        super.writeToNBT(tag);

        tag.setInteger(NBT_TOTAL_ENERGY_CONSUMED, totalEnergyConsumed);
        tag.setInteger(NBT_ANTIMATTER_COUNT, antimatterCount);

        return tag;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void update() {
        super.update();

        if (!world.isRemote) {
            velocity = getParticleVelocity();

            outputAntimatter();

            // Check if redstone signal is currently being applied.
            final ItemStack itemStack = inventory.getStackInSlot(0);

            if (canFunction() && energyStorage.extractEnergy(ENERGY_PER_TICK, true) >= ENERGY_PER_TICK) {
                if (entityParticle == null) {
                    // Creates an accelerated particle if one needs to exist (on world load for example or player login).
                    if (itemStack != null && lastSpawnTick >= 40) {
                        final BlockPos spawnAcceleratedParticlePos = pos.offset(facing.getOpposite());

                        // Only render the particle if container within the proper environment for it.
                        if (EntityParticle.canSpawnParticle(world, spawnAcceleratedParticlePos)) {
                            // Spawn the particle.
                            totalEnergyConsumed = 0;
                            entityParticle = new EntityParticle(world, spawnAcceleratedParticlePos, pos, facing.getOpposite());
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
                        if (entityParticle.didCollide()) {
                            if (world.rand.nextFloat() <= General.darkMatterSpawnChance) {
                                inventory.insertItem(3, new ItemStack(ModItems.itemDarkMatterCell), false);
                            }
                        }

                        entityParticle = null;
                    } else if (velocity > ANTIMATTER_CREATION_SPEED) {
                        // Play sound of anti-matter being created.
                        world.playSound(null, pos, ModSoundEvents.ANTIMATTER, SoundCategory.BLOCKS, 2, 1 - world.rand.nextFloat() * 0.3F);

                        // Create anti-matter in the internal reserve.
                        int generatedAntimatter = 5 + world.rand.nextInt(particleDensity);
                        antimatterCount += generatedAntimatter;

                        // Reset energy consumption levels and destroy accelerated particle.
                        totalEnergyConsumed = 0;
                        entityParticle.setDead();
                        entityParticle = null;
                    }

                    // Plays sound of particle accelerating past the speed based on total velocity at the time of anti-matter creation.
                    if (entityParticle != null) {
                        world.playSound(null, pos, ModSoundEvents.ACCELERATOR, SoundCategory.BLOCKS, 1.5F, (float) (0.6 + (0.4 * (entityParticle.getVelocity()) / ANTIMATTER_CREATION_SPEED)));
                    }

                    energyUsed = energyStorage.extractEnergy(ENERGY_PER_TICK, false);
                    totalEnergyConsumed += energyUsed;
                }
            } else {
                if (entityParticle != null) {
                    entityParticle.setDead();
                }

                entityParticle = null;
                reset();
            }

            if (world.getWorldTime() % 5 == 0) {
                NuclearPhysics.getPacketHandler().sendToReceivers(new PacketTileEntity(this), this);
            }

            lastSpawnTick++;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handlePacketData(final ByteBuf dataStream) {
        super.handlePacketData(dataStream);

        if (world.isRemote) {
            totalEnergyConsumed = dataStream.readInt();
            antimatterCount = dataStream.readInt();
            velocity = dataStream.readFloat();
        }
    }

    @Override
    public List<Object> getPacketData(final List<Object> objects) {
        super.getPacketData(objects);

        objects.add(totalEnergyConsumed);
        objects.add(antimatterCount);
        objects.add(velocity);

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
        final ItemStack itemStackEmptyCell = inventory.getStackInSlot(1);

        if (OreDictionaryHelper.isEmptyCell(itemStackEmptyCell) && itemStackEmptyCell.stackSize > 0) {
            // Each cell can only hold 125mg of antimatter
            // TODO: maybe a config for this?
            if (antimatterCount >= 125) {
                final ItemStack itemStack = inventory.getStackInSlot(2);

                if (itemStack != null) {
                    // If the output slot is not empty we must increase stack size
                    if (itemStack.getItem() == ModItems.itemAntimatterCell) {
                        final ItemStack newStack = itemStack.copy();

                        if (newStack.stackSize < newStack.getMaxStackSize()) {
                            InventoryUtility.decrStackSize(inventory, 1);
                            antimatterCount -= 125;
                            newStack.stackSize++;
                            inventory.setStackInSlot(2, newStack);
                        }
                    }
                } else {
                    // Remove some of the internal reserves of anti-matter and use it to craft an individual item.
                    antimatterCount -= 125;
                    InventoryUtility.decrStackSize(inventory, 1);
                    inventory.setStackInSlot(2, new ItemStack(ModItems.itemAntimatterCell));
                }
            }
        }
    }

    private void calculateParticleDensity() {
        final ItemStack itemStack = inventory.getStackInSlot(0);

        if (itemStack != null) {
            final Item item = itemStack.getItem();

            if (item instanceof ItemBlock) {
                final IBlockState state = Block.getBlockFromItem(item).getDefaultState();

                // Prevent negative numbers and disallow zero for density multiplier.
                // We can give any BlockPos as argument, it's not used anyway.
                particleDensity = Math.round(state.getBlockHardness(world, pos)) * General.antimatterDensityMultiplier;
            }

            if (particleDensity < 1) {
                particleDensity = General.antimatterDensityMultiplier;
            }

            if (particleDensity > 1000) {
                particleDensity = 1000 * General.antimatterDensityMultiplier;
            }
        }
    }

    // Get velocity for the particle and @return it as a float.
    public float getParticleVelocity() {
        if (entityParticle != null) {
            return (float) entityParticle.getVelocity();
        }

        return 0;
    }

    public EntityParticle getEntityParticle() {
        return entityParticle;
    }

    public void setEntityParticle(final EntityParticle entityParticle) {
        this.entityParticle = entityParticle;
    }

    public int getAntimatterCount() {
        return antimatterCount;
    }

    public float getVelocity() {
        return velocity;
    }
}