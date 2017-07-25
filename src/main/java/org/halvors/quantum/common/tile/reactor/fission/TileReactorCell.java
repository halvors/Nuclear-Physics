package org.halvors.quantum.common.tile.reactor.fission;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.api.item.IReactorComponent;
import org.halvors.quantum.api.tile.IReactor;
import org.halvors.quantum.common.Quantum;
import org.halvors.quantum.common.QuantumBlocks;
import org.halvors.quantum.common.QuantumFluids;
import org.halvors.quantum.common.block.reactor.fission.BlockReactorCell;
import org.halvors.quantum.common.block.reactor.fission.BlockReactorCell.EnumReactorCell;
import org.halvors.quantum.common.effect.explosion.ReactorExplosion;
import org.halvors.quantum.common.effect.poison.PoisonRadiation;
import org.halvors.quantum.common.event.PlasmaEvent;
import org.halvors.quantum.common.grid.thermal.ThermalGrid;
import org.halvors.quantum.common.grid.thermal.ThermalPhysics;
import org.halvors.quantum.common.multiblock.IMultiBlockStructure;
import org.halvors.quantum.common.multiblock.MultiBlockHandler;
import org.halvors.quantum.common.network.PacketHandler;
import org.halvors.quantum.common.network.packet.PacketTileEntity;
import org.halvors.quantum.common.tile.ITileNetwork;
import org.halvors.quantum.common.tile.TileInventory;
import org.halvors.quantum.common.tile.reactor.fusion.TilePlasma;
import org.halvors.quantum.common.utility.transform.vector.Vector3;
import org.halvors.quantum.common.utility.transform.vector.VectorWorld;

import java.util.ArrayList;
import java.util.List;

public class TileReactorCell extends TileInventory implements ITickable, IMultiBlockStructure<TileReactorCell>, IFluidHandler, IReactor {
    public static final int radius = 2;
    public static final int meltingPoint = 2000;
    private final int specificHeatCapacity = 1000;
    private final float mass = ThermalPhysics.getMass(1000, 7);
    public final FluidTank tank = new FluidTank(Fluid.BUCKET_VOLUME * 15);

    private float temperature = ThermalPhysics.roomTemperature; // Synced
    private float previousTemperature = temperature;

    private boolean shouldUpdate = false;

    private long internalEnergy = 0;
    private long previousInternalEnergy = 0;
    private int meltdownCounter = 0;
    private int meltdownCounterMaximum = 1000;

    private MultiBlockHandler<TileReactorCell> multiBlock;

    public TileReactorCell() {

    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        if (getMultiBlock().isPrimary() && getMultiBlock().isConstructed()) {
            return INFINITE_EXTENT_AABB;
        }

        return super.getRenderBoundingBox();
    }

    @Override
    public void update() {
        if (world.getTotalWorldTime() == 0) {
            updatePositionStatus();
        }

        // Move fuel rod down into the primary cell block if possible.
        if (!getMultiBlock().isPrimary()) {
            if (getStackInSlot(0) != null) {
                if (getMultiBlock().get().getStackInSlot(0) == null) {
                    getMultiBlock().get().setInventorySlotContents(0, getStackInSlot(0));
                    setInventorySlotContents(0, null);
                }
            }

            if (tank.getFluidAmount() > 0) {
                getMultiBlock().get().tank.fill(tank.drain(tank.getCapacity(), true), true);
            }
        }

        if (getMultiBlock().isPrimary() && tank.getFluid() != null && tank.getFluid().equals(QuantumFluids.plasma)) {
            // Spawn plasma.
            FluidStack drain = tank.drain(Fluid.BUCKET_VOLUME, false);

            if (drain != null && drain.amount >= Fluid.BUCKET_VOLUME) {
                EnumFacing spawnDir = EnumFacing.getFront(world.rand.nextInt(3) + 2);
                Vector3 spawnPos = new Vector3(this).translate(spawnDir, 2);
                spawnPos.translate(0, Math.max(world.rand.nextInt(getHeight()) - 1, 0), 0);

                BlockPos pos = new BlockPos(spawnPos.intX(), spawnPos.intY(), spawnPos.intZ());

                if (world.isAirBlock(pos)) {
                    MinecraftForge.EVENT_BUS.post(new PlasmaEvent.PlasmaSpawnEvent(world, pos, TilePlasma.plasmaMaxTemperature));
                    tank.drain(Fluid.BUCKET_VOLUME, true);
                }
            }
        } else {
            previousInternalEnergy = internalEnergy;

            // Handle cell rod interactions.
            ItemStack fuelRod = getMultiBlock().get().getStackInSlot(0);

            if (fuelRod != null) {
                if (fuelRod.getItem() instanceof IReactorComponent) {
                    // Activate rods.
                    IReactorComponent reactorComponent = (IReactorComponent) fuelRod.getItem();
                    reactorComponent.onReact(fuelRod, this);

                    if (fuelRod.getMetadata() >= fuelRod.getMaxDamage()) {
                        getMultiBlock().get().setInventorySlotContents(0, null);
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
            temperature = ThermalGrid.getTemperature(new VectorWorld(this));

            // Only a small percentage of the internal energy is used for temperature.
            if ((internalEnergy - previousInternalEnergy) > 0) {
                float deltaT = ThermalPhysics.getTemperatureForEnergy(mass, specificHeatCapacity, (long) ((internalEnergy - previousInternalEnergy) * 0.15));

                // Check control rods.
                for (int side = 2; side < 6; side++) {
                    Vector3 checkAdjacent = new Vector3(this).translate(EnumFacing.getFront(side));

                    if (checkAdjacent.getBlock(world) == QuantumBlocks.blockControlRod) {
                        deltaT /= 1.1;
                    }
                }

                // Add heat to surrounding blocks in the thermal grid.
                ThermalGrid.addTemperature(new VectorWorld(this), deltaT);

                // Sound of lava flowing randomly plays when above temperature to boil water.
                if (world.rand.nextInt(80) == 0 && getTemperature() >= ThermalPhysics.waterBoilTemperature) {
                    // TODO: Only do this is there is a water block nearby.
                    //world.playSoundEffect(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, "liquid.lava", 0.5F, 2.1F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.85F);
                }

                // Sounds of lava popping randomly plays when above temperature to boil water.
                if (world.rand.nextInt(40) == 0 && getTemperature() >= ThermalPhysics.waterBoilTemperature) {
                    // TODO: Only do this is there is a water block nearby.
                    //world.playSoundEffect(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, "liquid.lavapop", 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
                }

                // Reactor cell plays random idle noises while operating and above temperature to boil water.
                if (world.getWorldTime() % 100 == 0 && getTemperature() >= ThermalPhysics.waterBoilTemperature) {
                    float percentage = Math.min(getTemperature() / meltingPoint, 1.0F);
                    //world.playSoundEffect(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, Reference.PREFIX + "tile.reactorCell", percentage, 1.0F);
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
                    world.setBlockState(leakPos, QuantumBlocks.blockRadioactiveGrass.getDefaultState());
                    tank.drain(Fluid.BUCKET_VOLUME, true);
                } else if (block == Blocks.AIR || block.isReplaceable(world, leakPos)) {
                    if (tank.getFluid() != null) {
                        world.setBlockState(leakPos, tank.getFluid().getFluid().getBlock().getDefaultState());
                        tank.drain(Fluid.BUCKET_VOLUME, true);
                    }
                }
            }

            if (world.getTotalWorldTime() % 60 == 0 || shouldUpdate) {
                shouldUpdate = false;
                world.notifyNeighborsOfStateChange(pos, blockType);

                Quantum.getPacketHandler().sendToReceivers(new PacketTileEntity(this), this);
            }

            if (world.isRemote) {
                // Particles of white smoke will rise from above the reactor chamber when above water boiling temperature.
                if (world.rand.nextInt(5) == 0 && getTemperature() >= ThermalPhysics.waterBoilTemperature) {
                    world.spawnParticle(EnumParticleTypes.CLOUD, pos.getX() + world.rand.nextInt(2), pos.getY() + 1, pos.getZ() + world.rand.nextInt(2), 0.0D, 0.1D, 0.0D);
                    world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, pos.getX() + world.rand.nextInt(5), pos.getY(), pos.getZ() + world.rand.nextInt(5), 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);

        temperature = tagCompound.getFloat("temperature");
        tank.readFromNBT(tagCompound);
        getMultiBlock().load(tagCompound);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);

        tagCompound.setFloat("temperature", temperature);
        tank.writeToNBT(tagCompound);
        getMultiBlock().save(tagCompound);

        return tagCompound;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public Vector3[] getMultiBlockVectors() {
        List<Vector3> vectors = new ArrayList<>();
        Vector3 checkPosition = new Vector3(this);

        while (true) {
            TileEntity tileEntity = checkPosition.getTileEntity(world);

            if (tileEntity instanceof TileReactorCell) {
                vectors.add(checkPosition.clone().subtract(getPosition()));
            } else {
                break;
            }

            checkPosition.y++;
        }

        return vectors.toArray(new Vector3[0]);
    }

    @Override
    public World getWorldObject() {
        return world;
    }

    @Override
    public void onMultiBlockChanged() {

    }

    @Override
    public Vector3 getPosition() {
        return new Vector3(this);
    }

    @Override
    public MultiBlockHandler<TileReactorCell> getMultiBlock() {
        if (multiBlock == null) {
            multiBlock = new MultiBlockHandler<>(this);
        }

        return multiBlock;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handlePacketData(ByteBuf dataStream) {
        if (world.isRemote) {
            temperature = dataStream.readFloat();

            if (dataStream.readBoolean()) {
                tank.setFluid(FluidStack.loadFluidStackFromNBT(PacketHandler.readNBT(dataStream)));
            }
        }
    }

    @Override
    public List<Object> getPacketData(List<Object> objects) {
        objects.add(temperature);

        if (tank.getFluid() != null) {
            objects.add(true);

            NBTTagCompound compoundTank = new NBTTagCompound();
            tank.getFluid().writeToNBT(compoundTank);
            objects.add(compoundTank);
        } else {
            objects.add(false);
        }

        return objects;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public int fill(EnumFacing from, FluidStack resource, boolean doFill) {
        return getMultiBlock().get().tank.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(EnumFacing from, FluidStack resource, boolean doDrain) {
        if (resource != null) {
            if (resource.isFluidEqual(tank.getFluid())) {
                tank.drain(resource.amount, doDrain);
            }
        }

        return null;
    }

    @Override
    public FluidStack drain(EnumFacing from, int maxDrain, boolean doDrain) {
        return tank.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(EnumFacing from, Fluid fluid) {
        return fluid.equals(QuantumFluids.plasma);
    }

    @Override
    public boolean canDrain(EnumFacing from, Fluid fluid) {
        return fluid.equals(QuantumFluids.fluidToxicWaste);
    }

    @Override
    public FluidTankInfo[] getTankInfo(EnumFacing from) {
        return new FluidTankInfo[] { tank.getInfo() };
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
        return tank.getFluid() != null && tank.getFluid().equals(QuantumFluids.fluidToxicWaste) && tank.getFluid().amount >= tank.getCapacity();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return world.getTileEntity(pos) == this && player.getDistanceSq(pos.add(0.5, 0.5, 0.5)) <= 64;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack itemStack) {
        if (getMultiBlock().isPrimary() && getMultiBlock().get().getStackInSlot(0) == null) {
            return itemStack.getItem() instanceof IReactorComponent;
        }

        return false;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemStack, EnumFacing side) {
        return isItemValidForSlot(slot, itemStack);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public int getHeight() {
        int height = 0;
        TileEntity tile = this;
        Vector3 checkPosition = new Vector3(this);

        while (tile instanceof TileReactorCell) {
            height++;
            checkPosition.y++;
            tile = checkPosition.getTileEntity(world);
        }

        return height;
    }

    public TileReactorCell getLowest() {
        TileReactorCell lowest = this;
        Vector3 checkPosition = new Vector3(this);

        while (true) {
            TileEntity tile = checkPosition.getTileEntity(world);

            if (tile instanceof TileReactorCell) {
                lowest = (TileReactorCell) tile;
            } else {
                break;
            }

            checkPosition.y--;
        }

        return lowest;
    }

    public void updatePositionStatus() {
        TileReactorCell tile = getLowest();
        tile.getMultiBlock().deconstruct();
        tile.getMultiBlock().construct();

        boolean top = new Vector3(this).add(new Vector3(0, 1, 0)).getTileEntity(world) instanceof TileReactorCell;
        boolean bottom = new Vector3(this).add(new Vector3(0, -1, 0)).getTileEntity(world) instanceof TileReactorCell;
        IBlockState state = world.getBlockState(pos);

        if (top && bottom) {
            world.setBlockState(pos, state.withProperty(BlockReactorCell.type, EnumReactorCell.MIDDLE), 3);
        } else if (top) {
            world.setBlockState(pos, state.withProperty(BlockReactorCell.type, EnumReactorCell.BOTTOM), 3);
        } else {
            world.setBlockState(pos, state.withProperty(BlockReactorCell.type, EnumReactorCell.TOP), 3);
        }
    }

    private void meltDown() {
        // Make sure the reactor block is destroyed.
        world.setBlockToAir(pos);

        // No need to destroy reactor cell since explosion will do that for us.
        ReactorExplosion reactorExplosion = new ReactorExplosion(world, null, pos, 9.0F);
        reactorExplosion.explode();
    }
}
