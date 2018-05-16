package org.halvors.nuclearphysics.common.tile.reactor;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import org.halvors.nuclearphysics.api.item.IReactorComponent;
import org.halvors.nuclearphysics.api.tile.IReactor;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.capabilities.fluid.LiquidTank;
import org.halvors.nuclearphysics.common.effect.explosion.ReactorExplosion;
import org.halvors.nuclearphysics.common.event.PlasmaEvent.PlasmaSpawnEvent;
import org.halvors.nuclearphysics.common.init.ModBlocks;
import org.halvors.nuclearphysics.common.init.ModFluids;
import org.halvors.nuclearphysics.common.init.ModPotions;
import org.halvors.nuclearphysics.common.init.ModSoundEvents;
import org.halvors.nuclearphysics.common.network.packet.PacketTileEntity;
import org.halvors.nuclearphysics.common.science.grid.ThermalGrid;
import org.halvors.nuclearphysics.common.science.physics.ThermalPhysics;
import org.halvors.nuclearphysics.common.tile.TileRotatable;
import org.halvors.nuclearphysics.common.tile.reactor.fusion.TilePlasma;
import org.halvors.nuclearphysics.common.utility.InventoryUtility;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class TileReactorCell extends TileRotatable implements ITickable, IReactor {
    private static final String NBT_TEMPERATURE = "temperature";
    private static final String NBT_SLOTS = "slots";
    private static final String NBT_TANK = "tank";
    private static final int RADIUS = 2;
    public static final int MELTING_POINT = 2000;

    private String name;

    private final int specificHeatCapacity = 1000;
    private final double mass = ThermalPhysics.getMass(1000, 7);

    private double temperature = ThermalPhysics.ROOM_TEMPERATURE; // Synced
    private double previousTemperature = temperature;

    private boolean shouldUpdate = false;

    private long internalEnergy = 0;
    private long previousInternalEnergy = 0;
    private int meltdownCounter = 0;
    private int meltdownCounterMaximum = 1000;

    private final IItemHandlerModifiable inventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(final int slot) {
            super.onContentsChanged(slot);
            markDirty();
        }

        private boolean isItemValidForSlot(final int slot, final ItemStack itemStack) {
            return !ModFluids.fluidStackPlasma.isFluidEqual(tank.getFluid()) && inventory.getStackInSlot(0) == null && itemStack.getItem() instanceof IReactorComponent;
        }

        @Override
        public ItemStack insertItem(final int slot, final ItemStack stack, final boolean simulate) {
            if (!isItemValidForSlot(slot, stack)) {
                return stack;
            }

            return super.insertItem(slot, stack, simulate);
        }
    };

    private final LiquidTank tank = new LiquidTank(Fluid.BUCKET_VOLUME * 15) {
        @Override
        public int fill(final FluidStack resource, final boolean doFill) {
            if (resource.isFluidEqual(ModFluids.fluidStackPlasma)) {
                return super.fill(resource, doFill);
            }

            return 0;
        }

        @Override
        public FluidStack drain(final FluidStack resource, final boolean doDrain) {
            if (resource.isFluidEqual(ModFluids.fluidStackToxicWaste)) {
                return super.drain(resource, doDrain);
            }

            return null;
        }
    };

    public TileReactorCell() {
        this("reactor_cell");
    }

    public TileReactorCell(final String name) {
        this.name = name;
    }

    @Override
    public void readFromNBT(final NBTTagCompound tag) {
        super.readFromNBT(tag);

        temperature = tag.getDouble(NBT_TEMPERATURE);
        InventoryUtility.readFromNBT(tag, inventory);

        CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(inventory, null, tag.getTag(NBT_SLOTS));
        CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.readNBT(tank, null, tag.getTag(NBT_TANK));
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(final NBTTagCompound tag) {
        super.writeToNBT(tag);

        tag.setDouble(NBT_TEMPERATURE, temperature);
        tag.setTag(NBT_SLOTS, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(inventory, null));
        tag.setTag(NBT_TANK, CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.writeNBT(tank, null));

        return tag;
    }

    @Override
    public boolean hasCapability(@Nonnull final Capability<?> capability, @Nullable final EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nonnull
    public <T> T getCapability(@Nonnull final Capability<T> capability, @Nullable final EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) inventory;
        } else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return (T) tank;
        }

        return super.getCapability(capability, facing);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void update() {
        // TODO: Should we do this for fusion reactors as well?
        // Reactor cell plays random idle noises while operating with temperature above boiling water temperature.
        if (world.getWorldTime() % 100 == 0 && temperature >= ThermalPhysics.WATER_BOIL_TEMPERATURE) {
            world.playSound(null, pos, ModSoundEvents.REACTOR_CELL, SoundCategory.BLOCKS, (float) Math.min(temperature / MELTING_POINT, 1), 1);
        }

        if (!world.isRemote) {
            final FluidStack fluidStack = tank.getFluid();

            // Nuclear fusion.
            if (fluidStack != null && fluidStack.isFluidEqual(ModFluids.fluidStackPlasma)) {
                final FluidStack drain = tank.drainInternal(Fluid.BUCKET_VOLUME, false);

                if (drain != null && drain.amount >= Fluid.BUCKET_VOLUME) {
                    final EnumFacing spawnDir = EnumFacing.getFront(world.rand.nextInt(4) + 2);
                    final BlockPos spawnPos = pos.offset(spawnDir, 2);

                    if (world.isAirBlock(spawnPos)) {
                    	final PlasmaSpawnEvent event = new PlasmaSpawnEvent(world, spawnPos, TilePlasma.PLASMA_MAX_TEMPERATURE);
                        MinecraftForge.EVENT_BUS.post(event);

                        // Spawn plasma.
                        if (!event.isCanceled()) {
                        	world.setBlockState(spawnPos, ModFluids.plasma.getBlock().getDefaultState(), 2);
                        	tank.drainInternal(Fluid.BUCKET_VOLUME, true);
                        }
                    } else {
                    	final TileEntity tile = world.getTileEntity(spawnPos);

                        // Do plasma boost.
                    	if (tile instanceof TilePlasma) {
                    	    final TilePlasma tilePlasma = (TilePlasma) tile;
                            final int increaseTemperature = TilePlasma.PLASMA_MAX_TEMPERATURE / 10;

                    		if (TilePlasma.PLASMA_MAX_TEMPERATURE - tilePlasma.getTemperature() > increaseTemperature) {
                                tilePlasma.setTemperature(tilePlasma.getTemperature() + increaseTemperature);
                    			tank.drain(100, true);
                    		}
                    	}
                    }
                }
            } else { // Nuclear fission.
                previousInternalEnergy = internalEnergy;

                // Handle cell rod interactions.
                final ItemStack fuelRod = inventory.getStackInSlot(0);

                if (fuelRod != null && fuelRod.getItem() instanceof IReactorComponent) {
                    // Activate rods.
                    final IReactorComponent reactorComponent = (IReactorComponent) fuelRod.getItem();
                    reactorComponent.onReact(fuelRod, this);

                    if (fuelRod.getMetadata() >= fuelRod.getMaxDamage()) {
                        inventory.setStackInSlot(0, null);
                    }

                    // Emit radiation.
                    if (world.getTotalWorldTime() % 20 == 0 && world.rand.nextFloat() > 0.65) {
                        final List<EntityLiving> entities = world.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(pos.getX() - RADIUS * 2, pos.getY() - RADIUS * 2, pos.getZ() - RADIUS * 2, pos.getX() + RADIUS * 2, pos.getY() + RADIUS * 2, pos.getZ() + RADIUS * 2));

                        for (EntityLiving entity : entities) {
                            ModPotions.poisonRadiation.poisonEntity(entity);
                        }
                    }
                }

                // Update the temperature from the thermal grid.
                temperature = ThermalGrid.getTemperature(world, pos);

                // Only a small percentage of the internal energy is used for temperature.
                if ((internalEnergy - previousInternalEnergy) > 0) {
                    double deltaTemperature = ThermalPhysics.getTemperatureForEnergy(mass, specificHeatCapacity, (long) ((internalEnergy - previousInternalEnergy) * 0.15));

                    // Check control rods.
                    for (EnumFacing side : EnumFacing.HORIZONTALS) {
                        final BlockPos checkPos = pos.offset(side);

                        if (world.getBlockState(checkPos).getBlock() == ModBlocks.blockControlRod) {
                            deltaTemperature /= 2;
                        }
                    }

                    // Add heat to surrounding blocks in the thermal grid.
                    ThermalGrid.addTemperature(world, pos, deltaTemperature);

                    if (previousTemperature != temperature && !shouldUpdate) {
                        shouldUpdate = true;
                        previousTemperature = temperature;
                    }

                    // If temperature is over the melting point of the reactor, either increase counter or melt down.
                    if (previousTemperature >= MELTING_POINT) {
                        if (meltdownCounter < meltdownCounterMaximum) {
                            meltdownCounter++;
                            shouldUpdate = true;
                        } else {
                            meltdownCounter = 0;
                            meltDown();

                            return;
                        }
                    }

                    // If reactor temperature is below melting point and meltdownCounter is over 0, decrease it.
                    if (previousTemperature < MELTING_POINT && meltdownCounter > 0) {
                        meltdownCounter--;
                    }
                }

                internalEnergy = 0;

                if (isOverToxic()) {
                    // Randomly leak toxic waste when it is too toxic.
                    final BlockPos leakPos = pos.add(world.rand.nextInt(20) - 10, world.rand.nextInt(20) - 10, world.rand.nextInt(20) - 10);
                    final Block block = world.getBlockState(leakPos).getBlock();

                    if (block == Blocks.GRASS) {
                        world.setBlockState(leakPos, ModBlocks.blockRadioactiveGrass.getDefaultState());
                        tank.drainInternal(Fluid.BUCKET_VOLUME, true);
                    } else if (world.isAirBlock(leakPos) || block.isReplaceable(world, leakPos)) {
                        if (fluidStack != null) {
                            world.setBlockState(leakPos, fluidStack.getFluid().getBlock().getDefaultState());
                            tank.drainInternal(Fluid.BUCKET_VOLUME, true);
                        }
                    }
                }
            }

            if (world.getTotalWorldTime() % 60 == 0 || shouldUpdate) {
                shouldUpdate = false;
                world.notifyNeighborsOfStateChange(pos, getBlockType());

                NuclearPhysics.getPacketHandler().sendToReceivers(new PacketTileEntity(this), this);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handlePacketData(final ByteBuf dataStream) {
        super.handlePacketData(dataStream);

        if (world.isRemote) {
            temperature = dataStream.readDouble();
            tank.handlePacketData(dataStream);
        }
    }

    @Override
    public List<Object> getPacketData(final List<Object> objects) {
        super.getPacketData(objects);

        objects.add(temperature);
        tank.getPacketData(objects);

        return objects;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void heat(final long energy) {
        internalEnergy = Math.max(internalEnergy + energy, 0);
    }

    @Override
    public double getTemperature() {
        return temperature;
    }

    @Override
    public boolean isOverToxic() {
        final FluidStack fluidStack = tank.getFluid();

        return fluidStack != null && fluidStack.amount == tank.getCapacity() && fluidStack.isFluidEqual(ModFluids.fluidStackToxicWaste);
    }

    @Override
    public FluidTank getTank() {
        return tank;
    }

    @Override
    public World getWorldObject() {
        return world;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public String getName() {
        return name;
    }

    public String getLocalizedName() {
        return LanguageUtility.transelate(getBlockType().getUnlocalizedName() + ".name");
    }

    public IItemHandlerModifiable getInventory() {
        return inventory;
    }

    private void meltDown() {
        // Make sure the reactor block is destroyed.
        world.setBlockToAir(pos);

        // Create the explosion.
        final ReactorExplosion explosion = new ReactorExplosion(world, null, pos, 9);
        explosion.explode();
    }
}