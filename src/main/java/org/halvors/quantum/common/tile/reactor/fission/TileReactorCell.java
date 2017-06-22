package org.halvors.quantum.common.tile.reactor.fission;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.base.tile.ITileNetworkable;
import org.halvors.quantum.common.effect.poison.PoisonRadiation;
import org.halvors.quantum.common.event.PlasmaEvent;
import org.halvors.quantum.common.explosion.ReactorExplosion;
import org.halvors.quantum.common.multiblock.IMultiBlockStructure;
import org.halvors.quantum.common.multiblock.MultiBlockHandler;
import org.halvors.quantum.common.network.PacketHandler;
import org.halvors.quantum.common.network.packet.PacketTileEntity;
import org.halvors.quantum.common.tile.TileInventory;
import org.halvors.quantum.common.tile.reactor.fusion.TilePlasma;
import org.halvors.quantum.common.transform.vector.Vector3;
import org.halvors.quantum.common.transform.vector.VectorWorld;
import org.halvors.quantum.lib.thermal.ThermalGrid;
import org.halvors.quantum.lib.thermal.ThermalPhysics;

import java.util.ArrayList;
import java.util.List;

public class TileReactorCell extends TileInventory implements IMultiBlockStructure<TileReactorCell>, ITileNetworkable, IFluidHandler, IReactor {
    public static final int radius = 2;
    public static final int meltingPoint = 2000;
    private final int specificHeatCapacity = 1000;
    private final float mass = ThermalPhysics.getMass(1000, 7);
    public final FluidTank tank = new FluidTank(FluidContainerRegistry.BUCKET_VOLUME * 15);

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
    public void updateEntity() {
        super.updateEntity();

        if (worldObj.getTotalWorldTime() == 0) {
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

        if (getMultiBlock().isPrimary() && tank.getFluid() != null && tank.getFluid().getFluidID() == Quantum.fluidPlasma.getID()) {
            // Spawn plasma.
            FluidStack drain = tank.drain(FluidContainerRegistry.BUCKET_VOLUME, false);

            if (drain != null && drain.amount >= FluidContainerRegistry.BUCKET_VOLUME) {
                ForgeDirection spawnDir = ForgeDirection.getOrientation(worldObj.rand.nextInt(3) + 2);
                Vector3 spawnPos = new Vector3(this).translate(spawnDir, 2);
                spawnPos.translate(0, Math.max(worldObj.rand.nextInt(getHeight()) - 1, 0), 0);

                if (worldObj.isAirBlock(spawnPos.intX(), spawnPos.intY(), spawnPos.intZ())) {
                    MinecraftForge.EVENT_BUS.post(new PlasmaEvent.PlasmaSpawnEvent(worldObj, spawnPos.intX(), spawnPos.intY(), spawnPos.intZ(), TilePlasma.plasmaMaxTemperature));
                    tank.drain(FluidContainerRegistry.BUCKET_VOLUME, true);
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

                    if (fuelRod.getMetadata() >= fuelRod.getMaxDurability()) {
                        getMultiBlock().get().setInventorySlotContents(0, null);
                    }

                    // Emit radiation.
                    if (worldObj.getTotalWorldTime() % 20 == 0) {
                        if (worldObj.rand.nextFloat() > 0.65) {
                            List<EntityLiving> entities = worldObj.getEntitiesWithinAABB(EntityLiving.class, AxisAlignedBB.getBoundingBox(xCoord - radius * 2, yCoord - radius * 2, zCoord - radius * 2, xCoord + radius * 2, yCoord + radius * 2, zCoord + radius * 2));

                            for (EntityLiving entity : entities) {
                                PoisonRadiation.INSTANCE.poisonEntity(new Vector3(this), entity);
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
                    Vector3 checkAdjacent = new Vector3(this).translate(ForgeDirection.getOrientation(side));

                    if (checkAdjacent.getBlock(worldObj) == Quantum.blockControlRod) {
                        deltaT /= 1.1;
                    }
                }

                // Add heat to surrounding blocks in the thermal grid.
                ThermalGrid.addTemperature(new VectorWorld(this), deltaT);

                // Sound of lava flowing randomly plays when above temperature to boil water.
                if (worldObj.rand.nextInt(80) == 0 && getTemperature() >= ThermalPhysics.waterBoilTemperature) {
                    // TODO: Only do this is there is a water block nearby.
                    worldObj.playSoundEffect(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, "liquid.lava", 0.5F, 2.1F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.85F);
                }

                // Sounds of lava popping randomly plays when above temperature to boil water.
                if (worldObj.rand.nextInt(40) == 0 && getTemperature() >= ThermalPhysics.waterBoilTemperature) {
                    // TODO: Only do this is there is a water block nearby.
                    worldObj.playSoundEffect(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, "liquid.lavapop", 0.5F, 2.6F + (worldObj.rand.nextFloat() - worldObj.rand.nextFloat()) * 0.8F);
                }

                // Reactor cell plays random idle noises while operating and above temperature to boil water.
                if (worldObj.getWorldTime() % 100 == 0 && getTemperature() >= ThermalPhysics.waterBoilTemperature) {
                    float percentage = Math.min(getTemperature() / meltingPoint, 1.0F);
                    worldObj.playSoundEffect(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, Reference.PREFIX + "tile.reactorCell", percentage, 1.0F);
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
                VectorWorld leakPos = new VectorWorld(this).translate(worldObj.rand.nextInt(20) - 10, worldObj.rand.nextInt(20) - 10, worldObj.rand.nextInt(20) - 10);
                Block block = leakPos.getBlock();

                if (block == Blocks.grass) {
                    leakPos.setBlock(worldObj, Quantum.blockRadioactiveGrass);
                    tank.drain(FluidContainerRegistry.BUCKET_VOLUME, true);
                } else if (block == Blocks.air || block.isReplaceable(worldObj, leakPos.intX(), leakPos.intY(), leakPos.intZ())) {
                    if (tank.getFluid() != null) {
                        leakPos.setBlock(worldObj, tank.getFluid().getFluid().getBlock());
                        tank.drain(FluidContainerRegistry.BUCKET_VOLUME, true);
                    }
                }
            }


            if (worldObj.getTotalWorldTime() % 60 == 0 || shouldUpdate) {
                shouldUpdate = false;
                worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType());
                Quantum.getPacketHandler().sendToReceivers(new PacketTileEntity(this), this);
            }

            if (worldObj.isRemote) {
                // Particles of white smoke will rise from above the reactor chamber when above water boiling temperature.
                if (worldObj.rand.nextInt(5) == 0 && getTemperature() >= ThermalPhysics.waterBoilTemperature) {
                    worldObj.spawnParticle("cloud", xCoord + worldObj.rand.nextInt(2), yCoord + 1.0F, zCoord + worldObj.rand.nextInt(2), 0.0D, 0.1D, 0.0D);
                    worldObj.spawnParticle("bubble", xCoord + worldObj.rand.nextInt(5), yCoord, zCoord + worldObj.rand.nextInt(5), 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        temperature = nbt.getFloat("temperature");
        tank.readFromNBT(nbt);
        getMultiBlock().load(nbt);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);

        nbt.setFloat("temperature", temperature);
        tank.writeToNBT(nbt);
        getMultiBlock().save(nbt);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public Vector3[] getMultiBlockVectors() {
        List<Vector3> vectors = new ArrayList<>();
        Vector3 checkPosition = new Vector3(this);

        while (true) {
            TileEntity tileEntity = checkPosition.getTileEntity(worldObj);

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
        return worldObj;
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
    public void handlePacketData(ByteBuf dataStream) throws Exception {
        if (worldObj.isRemote) {
            temperature = dataStream.readFloat();

            if (dataStream.readBoolean()) {
                tank.setFluid(FluidStack.loadFluidStackFromNBT(PacketHandler.readNBTTag(dataStream)));
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
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        return getMultiBlock().get().tank.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        if (resource != null) {
            if (resource.isFluidEqual(tank.getFluid())) {
                tank.drain(resource.amount, doDrain);
            }
        }

        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return tank.drain(maxDrain, doDrain);
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return fluid.getID() == Quantum.fluidPlasma.getID();
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return fluid.getID() == Quantum.fluidToxicWaste.getID();
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
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
        return tank.getFluid() != null && tank.getFluid().getFluidID() == Quantum.fluidToxicWaste.getID() && tank.getFluid().amount >= tank.getCapacity();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public String getInventoryName() {
        return getBlockType().getLocalizedName();
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this && player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) <= 64;
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack itemStack) {
        if (getMultiBlock().isPrimary() && getMultiBlock().get().getStackInSlot(0) == null) {
            return itemStack.getItem() instanceof IReactorComponent;
        }

        return false;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemStack, int side) {
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
            tile = checkPosition.getTileEntity(worldObj);
        }

        return height;
    }

    public TileReactorCell getLowest() {
        TileReactorCell lowest = this;
        Vector3 checkPosition = new Vector3(this);

        while (true) {
            TileEntity tile = checkPosition.getTileEntity(worldObj);

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
        TileReactorCell tileEntity = getLowest();
        tileEntity.getMultiBlock().deconstruct();
        tileEntity.getMultiBlock().construct();

        boolean top = new Vector3(this).add(new Vector3(0.0D, 1.0D, 0.0D)).getTileEntity(worldObj) instanceof TileReactorCell;
        boolean bottom = new Vector3(this).add(new Vector3(0.0D, -1.0D, 0.0D)).getTileEntity(worldObj) instanceof TileReactorCell;

        if (top && bottom) {
            worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 1, 3);
        } else if (top) {
            worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 0, 3);
        } else {
            worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, 2, 3);
        }
    }

    private void meltDown() {
        // Make sure the reactor block is destroyed.
        worldObj.setBlock(xCoord, yCoord, zCoord, Blocks.air);

        // No need to destroy reactor cell since explosion will do that for us.
        ReactorExplosion reactorExplosion = new ReactorExplosion(worldObj, null, xCoord, yCoord, zCoord, 9.0F);
        reactorExplosion.explode();
    }
}
