package org.halvors.nuclearphysics.common.tile.reactor;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
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
import org.halvors.nuclearphysics.common.effect.poison.PoisonRadiation;
import org.halvors.nuclearphysics.common.event.PlasmaEvent.PlasmaSpawnEvent;
import org.halvors.nuclearphysics.common.grid.thermal.ThermalGrid;
import org.halvors.nuclearphysics.common.grid.thermal.ThermalPhysics;
import org.halvors.nuclearphysics.common.init.ModBlocks;
import org.halvors.nuclearphysics.common.init.ModFluids;
import org.halvors.nuclearphysics.common.init.ModSoundEvents;
import org.halvors.nuclearphysics.common.network.packet.PacketTileEntity;
import org.halvors.nuclearphysics.common.tile.TileRotatable;
import org.halvors.nuclearphysics.common.tile.reactor.fusion.TilePlasma;
import org.halvors.nuclearphysics.common.utility.LanguageUtility;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class TileReactorCell extends TileRotatable implements ITickable, IReactor {
    private String name;

    public static final int radius = 2;
    public static final int meltingPoint = 2000;
    private final int specificHeatCapacity = 1000;
    private final float mass = ThermalPhysics.getMass(1000, 7);

    private float temperature = ThermalPhysics.roomTemperature; // Synced
    private float previousTemperature = temperature;

    private boolean shouldUpdate = false;

    private long internalEnergy = 0;
    private long previousInternalEnergy = 0;
    private int meltdownCounter = 0;
    private int meltdownCounterMaximum = 1000;

    private final IItemHandlerModifiable inventory = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            markDirty();
        }

        private boolean isItemValidForSlot(int slot, ItemStack itemStack) {
            return !ModFluids.fluidStackPlasma.isFluidEqual(tank.getFluid()) && inventory.getStackInSlot(0).isEmpty() && itemStack.getItem() instanceof IReactorComponent;
        }

        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
            if (!isItemValidForSlot(slot, stack)) {
                return stack;
            }

            return super.insertItem(slot, stack, simulate);
        }
    };

    private final LiquidTank tank = new LiquidTank(Fluid.BUCKET_VOLUME * 15) {
        @Override
        public int fill(FluidStack resource, boolean doFill) {
            if (resource.isFluidEqual(ModFluids.fluidStackPlasma)) {
                return super.fill(resource, doFill);
            }

            return 0;
        }

        @Override
        public FluidStack drain(FluidStack resource, boolean doDrain) {
            if (resource.isFluidEqual(ModFluids.fluidStackToxicWaste)) {
                return super.drain(resource, doDrain);
            }

            return null;
        }
    };

    public TileReactorCell() {
        this("reactor_cell");
    }

    public TileReactorCell(String name) {
        this.name = name;
    }

    @Override
    public void update() {
        FluidStack fluidStack = tank.getFluid();

        if (fluidStack != null && fluidStack.isFluidEqual(ModFluids.fluidStackPlasma)) {
            // Spawn plasma.
            FluidStack drain = tank.drainInternal(Fluid.BUCKET_VOLUME, false);

            if (drain != null && drain.amount >= Fluid.BUCKET_VOLUME) {
                EnumFacing spawnDir = EnumFacing.getFront(world.rand.nextInt(3) + 2);
                BlockPos spawnPos = pos.offset(spawnDir, 2);

                if (world.isAirBlock(spawnPos)) {
                    MinecraftForge.EVENT_BUS.post(new PlasmaSpawnEvent(world, spawnPos, TilePlasma.plasmaMaxTemperature));
                    tank.drainInternal(Fluid.BUCKET_VOLUME, true);
                }
            }
        } else {
            previousInternalEnergy = internalEnergy;

            // Handle cell rod interactions.
            ItemStack fuelRod = inventory.getStackInSlot(0);

            if (!fuelRod.isEmpty()) {
                if (fuelRod.getItem() instanceof IReactorComponent) {
                    // Activate rods.
                    IReactorComponent reactorComponent = (IReactorComponent) fuelRod.getItem();
                    reactorComponent.onReact(fuelRod, this);

                    if (fuelRod.getMetadata() >= fuelRod.getMaxDamage()) {
                        inventory.setStackInSlot(0, ItemStack.EMPTY);
                    }

                    // Emit radiation.
                    if (world.getTotalWorldTime() % 20 == 0) {
                        if (world.rand.nextFloat() > 0.65) {
                            List<EntityLiving> entities = world.getEntitiesWithinAABB(EntityLiving.class, new AxisAlignedBB(pos.getX() - radius * 2, pos.getY() - radius * 2, pos.getZ() - radius * 2, pos.getX() + radius * 2, pos.getY() + radius * 2, pos.getZ() + radius * 2));

                            for (EntityLiving entity : entities) {
                                PoisonRadiation.getInstance().poisonEntity(pos, entity);
                            }
                        }
                    }
                }
            }

            // Update the temperature from the thermal grid.
            temperature = ThermalGrid.getTemperature(world, pos);

            // Only a small percentage of the internal energy is used for temperature.
            if ((internalEnergy - previousInternalEnergy) > 0) {
                float deltaT = ThermalPhysics.getTemperatureForEnergy(mass, specificHeatCapacity, (long) ((internalEnergy - previousInternalEnergy) * 0.15));

                // Check control rods.
                for (EnumFacing side : EnumFacing.HORIZONTALS) {
                    BlockPos checkPos = pos.offset(side);

                    if (world.getBlockState(checkPos).getBlock() == ModBlocks.blockControlRod) {
                        deltaT /= 1.1;
                    }
                }

                // Add heat to surrounding blocks in the thermal grid.
                ThermalGrid.addTemperature(world, pos, deltaT);

                // Reactor cell plays random idle noises while operating and above temperature to boil water.
                if (world.getWorldTime() % 100 == 0 && temperature >= ThermalPhysics.waterBoilTemperature) {
                    float percentage = Math.min(temperature / meltingPoint, 1.0F);

                    world.playSound(null, pos, ModSoundEvents.REACTOR_CELL, SoundCategory.BLOCKS, percentage, 1);
                }

                if (previousTemperature != temperature && !shouldUpdate) {
                    shouldUpdate = true;
                    previousTemperature = temperature;
                }

                // If temperature is over the melting point of the reactor, either increase counter or melt down.
                if (previousTemperature >= meltingPoint) {
                    if (meltdownCounter < meltdownCounterMaximum) {
                        shouldUpdate = true;
                        meltdownCounter++;
                    } else if (meltdownCounter >= meltdownCounterMaximum) {
                        meltdownCounter = 0;
                        meltDown();

                        return;
                    }
                }

                // If reactor temperature is below meltingPoint and meltdownCounter is over 0, decrease it.
                if (previousTemperature < meltingPoint && meltdownCounter > 0) {
                    meltdownCounter--;
                }
            }

            internalEnergy = 0;

            if (isOverToxic()) {
                // Randomly leak toxic waste when it is too toxic.
                BlockPos leakPos = pos.add(world.rand.nextInt(20) - 10, world.rand.nextInt(20) - 10, world.rand.nextInt(20) - 10);
                Block block = world.getBlockState(leakPos).getBlock();

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

            if (world.isRemote && !fuelRod.isEmpty()) {
                // Particles of white smoke will rise from above the reactor chamber when above water boiling temperature.
                if (temperature >= ThermalPhysics.waterBoilTemperature) {
                    if (world.rand.nextInt(5) == 0) {
                        world.spawnParticle(EnumParticleTypes.CLOUD, pos.getX() + world.rand.nextInt(2), pos.getY() + 1, pos.getZ() + world.rand.nextInt(2), 0, 0.1, 0);
                    }
                }
            }
        }


        if (!world.isRemote) {
            if (world.getTotalWorldTime() % 60 == 0 || shouldUpdate) {
                shouldUpdate = false;
                world.notifyNeighborsOfStateChange(pos, blockType, false); // TODO!

                NuclearPhysics.getPacketHandler().sendToReceivers(new PacketTileEntity(this), this);
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        temperature = tag.getFloat("temperature");

        if (tag.getTagId("Inventory") == Constants.NBT.TAG_LIST) {
            NBTTagList tagList = tag.getTagList("Inventory", Constants.NBT.TAG_COMPOUND);

            for (int i = 0; i < tagList.tagCount(); i++) {
                NBTTagCompound slotTag = (NBTTagCompound) tagList.get(i);
                byte slot = slotTag.getByte("Slot");

                if (slot < inventory.getSlots()) {
                    inventory.setStackInSlot(slot, new ItemStack(slotTag));
                }
            }

            return;
        }

        CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.readNBT(inventory, null, tag.getTag("Slots"));
        CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.readNBT(tank, null, tag.getTag("tank"));
    }

    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        tag.setFloat("temperature", temperature);
        tag.setTag("Slots", CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(inventory, null));
        tag.setTag("tank", CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.writeNBT(tank, null));

        return tag;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return (T) inventory;
        } else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return (T) tank;
        }

        return super.getCapability(capability, facing);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handlePacketData(ByteBuf dataStream) {
        super.handlePacketData(dataStream);

        if (world.isRemote) {
            temperature = dataStream.readFloat();
            tank.handlePacketData(dataStream);
        }
    }

    @Override
    public List<Object> getPacketData(List<Object> objects) {
        super.getPacketData(objects);

        objects.add(temperature);
        tank.getPacketData(objects);

        return objects;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



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

        // No need to destroy reactor cell since explosion will do that for us.
        ReactorExplosion explosion = new ReactorExplosion(world, null, pos, 9);
        explosion.explode();
    }

    @Override
    public void heat(long energy) {
        internalEnergy = Math.max(internalEnergy + energy, 0);
    }

    @Override
    public float getTemperature() {
        return temperature;
    }

    @Override
    public boolean isOverToxic() {
        FluidStack fluidStack = tank.getFluid();

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
}