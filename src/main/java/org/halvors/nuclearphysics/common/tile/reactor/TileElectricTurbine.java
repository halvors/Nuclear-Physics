package org.halvors.nuclearphysics.common.tile.reactor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.*;
import org.halvors.nuclearphysics.api.fluid.IBoilHandler;
import org.halvors.nuclearphysics.common.ConfigurationManager;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.capabilities.energy.EnergyStorage;
import org.halvors.nuclearphysics.common.capabilities.fluid.GasTank;
import org.halvors.nuclearphysics.common.init.ModSounds;
import org.halvors.nuclearphysics.common.multiblock.ElectricTurbineMultiBlockHandler;
import org.halvors.nuclearphysics.common.multiblock.IMultiBlockStructure;
import org.halvors.nuclearphysics.common.network.packet.PacketTileEntity;
import org.halvors.nuclearphysics.common.tile.TileGenerator;
import org.halvors.nuclearphysics.common.type.Position;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 1 cubic meter of steam = 338260 J of energy
 *
 * The front of the turbine is where the output is.
 */
public class TileElectricTurbine extends TileGenerator implements IMultiBlockStructure<TileElectricTurbine>, IBoilHandler, IFluidHandler {
    private final int energyPerSteam = 40;
    private final int defaultTorque = 5000;
    private int torque = defaultTorque;

    private final GasTank tank = new GasTank(FluidContainerRegistry.BUCKET_VOLUME * 16);

    // Radius of large turbine?
    private int multiBlockRadius = 1;

    // The power of the turbine this tick. In joules/tick
    private int power = 0;

    // Max power in watts.
    private int maxPower = 128000;

    // Current rotation of the turbine in radians.
    public float rotation = 0;

    public int tier = 0; // Synced

    private float angularVelocity = 0; // Synced
    private float previousAngularVelocity = 0;

    // MutliBlock methods.
    private ElectricTurbineMultiBlockHandler multiBlock;

    public TileElectricTurbine() {
        energyStorage = new EnergyStorage(maxPower * 20);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return AxisAlignedBB.getBoundingBox(xCoord - multiBlockRadius, yCoord, zCoord - multiBlockRadius, xCoord + 1 + multiBlockRadius, yCoord + 1, zCoord + 1 + multiBlockRadius);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        multiBlockRadius = tag.getInteger("multiBlockRadius");
        getMultiBlock().readFromNBT(tag);
        tank.readFromNBT(tag.getCompoundTag("tank"));
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        tag.setInteger("multiBlockRadius", multiBlockRadius);
        getMultiBlock().writeToNBT(tag);
        tag.setTag("tank", tank.writeToNBT(new NBTTagCompound()));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void updateEntity() {
        super.updateEntity();

        if (getMultiBlock().isConstructed()) {
            torque = defaultTorque * 500 * getArea();
        } else {
            torque = defaultTorque * 500;
        }

        getMultiBlock().update();

        if (getMultiBlock().isPrimary()) {
            if (!worldObj.isRemote) {
                // Increase spin rate and consume steam.
                if (tank.getFluidAmount() > 0 && power < maxPower) {
                    FluidStack fluidStack = tank.drain((int) Math.ceil(Math.min(tank.getFluidAmount() * 0.1, getMaxPower() / energyPerSteam)), true);

                    if (fluidStack != null) {
                        power += fluidStack.amount * energyPerSteam;
                    }
                }

                ////////////////////////////////////////////////////////////////////////////////////////////////////////

                // Set angular velocity based on power and torque.
                angularVelocity = (float) ((power * 4 * 256) / torque);

                if (worldObj.getWorldTime() % 3 == 0 && previousAngularVelocity != angularVelocity) {
                    NuclearPhysics.getPacketHandler().sendToReceivers(new PacketTileEntity(this), this);
                    previousAngularVelocity = angularVelocity;
                }

                if (power > 0) {
                    energyStorage.receiveEnergy((int) (power * ConfigurationManager.General.turbineOutputMultiplier), false);
                }
            } else if (angularVelocity != 0) {
                if (worldObj.getWorldTime() % 26 == 0) {
                    // TODO: Tweak this volume, i suspect it is way to loud.
                    double maxVelocity = (getMaxPower() / torque) * 4;
                    float percentage =Math.min(angularVelocity * 4 / (float) maxVelocity, 1);

                    worldObj.playSoundEffect(xCoord, yCoord, zCoord, ModSounds.ELECTRIC_TURBINE, percentage, 1);
                }

                // Update rotation.
                rotation = (float) ((rotation + angularVelocity / 20) % (Math.PI * 2));
            }
        } else if (tank.getFluidAmount() > 0) {
            int amount = getMultiBlock().get().tank.fill(tank.getFluid(), false);

            getMultiBlock().get().tank.fill(tank.drain(amount, true), true);
        }

        if (!worldObj.isRemote) {
            if (worldObj.getTotalWorldTime() % 60 == 0 && getMultiBlock().isConstructed()) {
                NuclearPhysics.getPacketHandler().sendToReceivers(new PacketTileEntity(this), this);
            }

            power = 0;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public Position[] getMultiBlockVectors() {
        Set<Position> positions = new HashSet<>();

        EnumFacing dir = EnumFacing.UP;
        int xMulti = dir.getFrontOffsetX() != 0 ? 0 : 1;
        int yMulti = dir.getFrontOffsetY() != 0 ? 0 : 1;
        int zMulti = dir.getFrontOffsetZ() != 0 ? 0 : 1;

        for (int x = -multiBlockRadius; x <= multiBlockRadius; x++) {
            for (int y = -multiBlockRadius; y <= multiBlockRadius; y++) {
                for (int z = -multiBlockRadius; z <= multiBlockRadius; z++) {
                    positions.add(new Position(x * xMulti, y * yMulti, z * zMulti));
                }
            }
        }

        return positions.toArray(new Position[0]);
    }

    @Override
    public World getWorldObject() {
        return worldObj;
    }

    @Override
    public void onMultiBlockChanged() {
        if (!worldObj.isRemote) {
            NuclearPhysics.getPacketHandler().sendToReceivers(new PacketTileEntity(this), this);
        }

        // Notify neighbor blocks of when multiblock is formed.
        worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, blockType);
    }

    @Override
    public Position getPosition() {
        return new Position(this);
    }

    @Override
    public ElectricTurbineMultiBlockHandler getMultiBlock() {
        if (multiBlock == null) {
            multiBlock = new ElectricTurbineMultiBlockHandler(this);
        }

        return multiBlock;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handlePacketData(ByteBuf dataStream) {
        super.handlePacketData(dataStream);

        if (worldObj.isRemote) {
            getMultiBlock().handlePacketData(dataStream);
            tier = dataStream.readInt();
            angularVelocity = dataStream.readFloat();
            tank.handlePacketData(dataStream);
        }
    }

    @Override
    public List<Object> getPacketData(List<Object> objects) {
        super.getPacketData(objects);

        getMultiBlock().getPacketData(objects);
        objects.add(tier);
        objects.add(angularVelocity);
        tank.getPacketData(objects);

        return objects;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public EnumSet<ForgeDirection> getExtractingDirections() {
        if (getMultiBlock().isPrimary()) {
            return EnumSet.of(ForgeDirection.UP);
        }

        return EnumSet.noneOf(ForgeDirection.class);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean canConnectEnergy(ForgeDirection from) {
        return from == ForgeDirection.UP && getMultiBlock().isPrimary();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public int receiveGas(ForgeDirection from, FluidStack fluidStack, boolean doTransfer) {
        if (from == ForgeDirection.DOWN && getMultiBlock().isPrimary()) {
            return tank.fill(fluidStack, doTransfer);
        }

        return 0;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        if (from == ForgeDirection.DOWN && getMultiBlock().isPrimary()) {
            return tank.fill(resource, doFill);
        }

        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        return null;
    }

    @Override
    public boolean canFill(ForgeDirection from, Fluid fluid) {
        return from == ForgeDirection.DOWN;
    }

    @Override
    public boolean canDrain(ForgeDirection from, Fluid fluid) {
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        return new FluidTankInfo[] { tank.getInfo() };
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private int getMaxPower() {
        if (getMultiBlock().isConstructed()) {
            return maxPower * getArea();
        }

        return maxPower;
    }

    private int getArea() {
        return (int) ((multiBlockRadius + 0.5) * 2 * (multiBlockRadius + 0.5) * 2);
    }
}
