package org.halvors.quantum.common.tile.reactor;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.common.ConfigurationManager;
import org.halvors.quantum.common.Quantum;
import org.halvors.quantum.common.QuantumFluids;
import org.halvors.quantum.common.fluid.tank.FluidTankQuantum;
import org.halvors.quantum.common.multiblock.ElectricTurbineMultiBlockHandler;
import org.halvors.quantum.common.multiblock.IMultiBlockStructure;
import org.halvors.quantum.common.network.packet.PacketTileEntity;
import org.halvors.quantum.common.tile.ITileNetwork;
import org.halvors.quantum.common.tile.TileGenerator;
import org.halvors.quantum.common.utility.location.Position;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** TileElectricTurbine
 *
 * 1 cubic meter of steam = 338260 J of energy
 *
 * The front of the turbine is where the output is.
 */
public class TileElectricTurbine extends TileGenerator implements IMultiBlockStructure<TileElectricTurbine>, ITileNetwork {
    // Amount of energy per liter of steam. Boil Water Energy = 327600 + 2260000 = 2587600
    //protected final int energyPerSteam = 2647600 / 1000;
    protected final int energyPerSteam = 52000;
    protected final int defaultTorque = 5000;
    protected int torque = defaultTorque;

    private final FluidTankQuantum tank = new FluidTankQuantum(QuantumFluids.fluidStackSteam, Fluid.BUCKET_VOLUME * 100) {
        /*
        @Override
        public boolean canFill() {
            return false;
        }
        */
    };

    // Radius of large turbine?
    public int multiBlockRadius = 1;

    // The power of the turbine this tick. In joules/tick
    public int power = 0;

    // Current rotation of the turbine in radians.
    public float rotation = 0;

    public int tier = 0; // Synced

    // Max power in watts.
    protected int maxPower = 5000000;
    protected float angularVelocity = 0; // Synced
    protected float previousAngularVelocity = 0;

    // MutliBlock methods.
    private ElectricTurbineMultiBlockHandler multiBlock;

    public TileElectricTurbine() {
        energyGeneration = maxPower * 20;
        energyStorage = new EnergyStorage(maxPower * 20);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(pos.getX() - multiBlockRadius, pos.getY() - multiBlockRadius, pos.getZ() - multiBlockRadius, pos.getX() + 1 + multiBlockRadius, pos.getY() + 1 + multiBlockRadius, pos.getZ() + 1 + multiBlockRadius);
    }

    @Override
    public void update() {
        if (getMultiBlock().isConstructed()) {
            torque = defaultTorque * 500 * getArea();
        } else {
            torque = defaultTorque * 500;
        }

        getMultiBlock().update();

        if (getMultiBlock().isPrimary()) {
            if (!world.isRemote) {
                // Increase spin rate and consume steam.
                if (tank.getFluidAmount() > 0 && power < maxPower) {
                    FluidStack fluidStackSteam = tank.drain((int) Math.ceil(Math.min(tank.getFluidAmount() * 0.1, getMaxPower() / energyPerSteam)), true);

                    if (fluidStackSteam != null) {
                        power += fluidStackSteam.amount * energyPerSteam;
                    }
                }

                // Set angular velocity based on power and torque.
                angularVelocity = (power * 4) / torque;

                if (world.getWorldTime() % 3 == 0 && previousAngularVelocity != angularVelocity) {
                    Quantum.getPacketHandler().sendToReceivers(new PacketTileEntity(this), this);
                    previousAngularVelocity = angularVelocity;
                }

                if (power > 0) {
                    energyStorage.receiveEnergy((int) (power * ConfigurationManager.General.turbineOutputMultiplier), false);
                    generateEnergy();
                }
            }

            if (angularVelocity != 0) {
                if (world.getWorldTime() % 26 == 0) {
                    double maxVelocity = (getMaxPower() / torque) * 4;

                    float percentage = angularVelocity * 4 / (float) maxVelocity;

                    // TODO: Is this working?
                    //world.playSound(pos.getX(), pos.getY(), pos.getZ(), new SoundEvent(new ResourceLocation(Reference.PREFIX + "tile.electricTurbine")), SoundCategory.AMBIENT, percentage, 1);

                    //world.playSoundEffect(xCoord, yCoord, zCoord, Reference.PREFIX + "tile.electricTurbine", percentage, 1);
                }

                // Update rotation.
                rotation = (float) ((rotation + angularVelocity / 20) % (Math.PI * 2));
            }
        } else if (tank.getFluidAmount() > 0) {
            int amount = getMultiBlock().get().tank.fillInternal(tank.getFluid(), false);

            getMultiBlock().get().tank.fillInternal(tank.drainInternal(amount, true), true);
        }

        if (!world.isRemote) {
            if (world.getTotalWorldTime() % 60 == 0 && getMultiBlock().isConstructed()) {
                Quantum.getPacketHandler().sendToReceivers(new PacketTileEntity(this), this);
            }

            power = 0;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        multiBlockRadius = tag.getInteger("multiBlockRadius");
        getMultiBlock().readFromNBT(tag);
        CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.readNBT(tank, null, tag.getTag("tank"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag = super.writeToNBT(tag);

        tag.setInteger("multiBlockRadius", multiBlockRadius);
        tag = getMultiBlock().writeToNBT(tag);
        tag.setTag("tank", CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.writeNBT(tank, null));

        return tag;
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
        return world;
    }

    @Override
    public void onMultiBlockChanged() {
        if (!world.isRemote) {
            Quantum.getPacketHandler().sendToReceivers(new PacketTileEntity(this), this);
        }

        //world.notifyNeighborsOfStateChange(pos, getBlockType());

        // TODO: Update render?
        //world.markBlockForUpdate(xCoord, yCoord, zCoord);
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
        if (world.isRemote) {
            getMultiBlock().handlePacketData(dataStream);
            tier = dataStream.readInt();
            angularVelocity = dataStream.readFloat();
            tank.handlePacketData(dataStream);
        }
    }

    @Override
    public List<Object> getPacketData(List<Object> objects) {
        getMultiBlock().getPacketData(objects);
        objects.add(tier);
        objects.add(angularVelocity);
        tank.getPacketData(objects);

        return objects;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private int getMaxPower() {
        if (getMultiBlock().isConstructed()) {
            return maxPower * getArea();
        }

        return maxPower;
    }

    private int getArea() {
        return (int) (((multiBlockRadius + 0.5) * 2) * ((multiBlockRadius + 0.5) * 2));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nonnull EnumFacing facing) {
        return (capability == CapabilityEnergy.ENERGY && getMultiBlock().isPrimary() && facing == EnumFacing.UP) || (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && getMultiBlock().isPrimary() && facing == EnumFacing.DOWN) || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nonnull
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nonnull EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY) {
            if (getMultiBlock().isPrimary() && facing == EnumFacing.UP) {
                return (T) getMultiBlock().get().getEnergyStorage();
            }
        } else if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            if (getMultiBlock().isPrimary() && facing == EnumFacing.DOWN) {
                return (T) getMultiBlock().get().tank;
            }
        }

        return super.getCapability(capability, facing);
    }
}
