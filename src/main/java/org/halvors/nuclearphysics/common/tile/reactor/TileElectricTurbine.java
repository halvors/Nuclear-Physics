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
import org.halvors.nuclearphysics.api.BlockPos;
import org.halvors.nuclearphysics.api.fluid.IBoilHandler;
import org.halvors.nuclearphysics.common.ConfigurationManager.General;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.capabilities.energy.EnergyStorage;
import org.halvors.nuclearphysics.common.capabilities.fluid.GasTank;
import org.halvors.nuclearphysics.common.init.ModSounds;
import org.halvors.nuclearphysics.common.multiblock.ElectricTurbineMultiBlockHandler;
import org.halvors.nuclearphysics.common.multiblock.IMultiBlockStructure;
import org.halvors.nuclearphysics.common.network.packet.PacketTileEntity;
import org.halvors.nuclearphysics.common.tile.TileGenerator;

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
    private static final String NBT_MULTI_BLOCK_RADIUS = "multiBlockRadius";
    private static final String NBT_TANK = "tank";
    private static final int ENERGY_PER_STEAM = 40;
    private static final int DEFAULT_TORQUE = 5000;

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
    public void readFromNBT(final NBTTagCompound tag) {
        super.readFromNBT(tag);

        multiBlockRadius = tag.getInteger(NBT_MULTI_BLOCK_RADIUS);
        getMultiBlock().readFromNBT(tag);
        tank.readFromNBT(tag.getCompoundTag(NBT_TANK));
    }

    @Override
    public void writeToNBT(final NBTTagCompound tag) {
        super.writeToNBT(tag);

        tag.setInteger(NBT_MULTI_BLOCK_RADIUS, multiBlockRadius);
        getMultiBlock().writeToNBT(tag);
        tag.setTag(NBT_TANK, tank.writeToNBT(new NBTTagCompound()));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void updateEntity() {
        super.updateEntity();

        int torque = DEFAULT_TORQUE * 500;

        if (getMultiBlock().isConstructed()) {
            torque *= getArea();
        }

        getMultiBlock().update();

        if (getMultiBlock().isPrimary()) {
            if (!worldObj.isRemote) {
                // Increase spin rate and consume steam.
                if (tank.getFluidAmount() > 0 && power < maxPower) {
                    final FluidStack fluidStack = tank.drain((int) Math.ceil(Math.min(tank.getFluidAmount() * 0.1, getMaxPower() / ENERGY_PER_STEAM)), true);

                    if (fluidStack != null) {
                        power += fluidStack.amount * ENERGY_PER_STEAM;
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
                    energyStorage.receiveEnergy((int) (power * General.turbineOutputMultiplier), false);
                }
            } else if (angularVelocity != 0) {
                if (worldObj.getWorldTime() % 26 == 0) {
                    // TODO: Tweak this volume, i suspect it is way to loud.
                    final double maxVelocity = (getMaxPower() / torque) * 4;
                    final float percentage =Math.min(angularVelocity * 4 / (float) maxVelocity, 1);

                    worldObj.playSoundEffect(xCoord, yCoord, zCoord, ModSounds.ELECTRIC_TURBINE, percentage, 1);
                }

                // Update rotation.
                rotation = (float) ((rotation + angularVelocity / 20) % (Math.PI * 2));
            }
        } else if (tank.getFluidAmount() > 0) {
            final int amount = getMultiBlock().get().tank.fill(tank.getFluid(), false);

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
    public BlockPos[] getMultiBlockVectors() {
        final Set<BlockPos> positions = new HashSet<>();

        final EnumFacing dir = EnumFacing.UP;
        final int xMulti = dir.getFrontOffsetX() != 0 ? 0 : 1;
        final int yMulti = dir.getFrontOffsetY() != 0 ? 0 : 1;
        final int zMulti = dir.getFrontOffsetZ() != 0 ? 0 : 1;

        for (int x = -multiBlockRadius; x <= multiBlockRadius; x++) {
            for (int y = -multiBlockRadius; y <= multiBlockRadius; y++) {
                for (int z = -multiBlockRadius; z <= multiBlockRadius; z++) {
                    positions.add(new BlockPos(x * xMulti, y * yMulti, z * zMulti));
                }
            }
        }

        return positions.toArray(new BlockPos[0]);
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
        worldObj.notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType());
    }

    @Override
    public BlockPos getPosition() {
        return pos;
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
    public void handlePacketData(final ByteBuf dataStream) {
        super.handlePacketData(dataStream);

        if (worldObj.isRemote) {
            getMultiBlock().handlePacketData(dataStream);
            tier = dataStream.readInt();
            angularVelocity = dataStream.readFloat();
            tank.handlePacketData(dataStream);
        }
    }

    @Override
    public List<Object> getPacketData(final List<Object> objects) {
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
    public boolean canConnectEnergy(final ForgeDirection from) {
        return from == ForgeDirection.UP && getMultiBlock().isPrimary();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public int receiveGas(final ForgeDirection from, final FluidStack fluidStack, final boolean doTransfer) {
        if (from == ForgeDirection.DOWN && getMultiBlock().isPrimary()) {
            return tank.fill(fluidStack, doTransfer);
        }

        return 0;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public int fill(final ForgeDirection from, final FluidStack resource, final boolean doFill) {
        if (from == ForgeDirection.DOWN && getMultiBlock().isPrimary()) {
            return tank.fill(resource, doFill);
        }

        return 0;
    }

    @Override
    public FluidStack drain(final ForgeDirection from, final FluidStack resource, final boolean doDrain) {
        return null;
    }

    @Override
    public FluidStack drain(final ForgeDirection from, final int maxDrain, final boolean doDrain) {
        return null;
    }

    @Override
    public boolean canFill(final ForgeDirection from, final Fluid fluid) {
        return from == ForgeDirection.DOWN;
    }

    @Override
    public boolean canDrain(final ForgeDirection from, final Fluid fluid) {
        return false;
    }

    @Override
    public FluidTankInfo[] getTankInfo(final ForgeDirection from) {
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
