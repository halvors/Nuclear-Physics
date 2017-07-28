package org.halvors.quantum.common.tile.reactor;

import cofh.api.energy.EnergyStorage;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.common.ConfigurationManager;
import org.halvors.quantum.common.Quantum;
import org.halvors.quantum.common.QuantumFluids;
import org.halvors.quantum.common.fluid.tank.FluidTankStrict;
import org.halvors.quantum.common.multiblock.ElectricTurbineMultiBlockHandler;
import org.halvors.quantum.common.multiblock.IMultiBlockStructure;
import org.halvors.quantum.common.network.packet.PacketTileEntity;
import org.halvors.quantum.common.tile.ITileNetwork;
import org.halvors.quantum.common.tile.TileElectric;
import org.halvors.quantum.common.utility.transform.vector.Vector3;

import javax.annotation.Nonnull;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** TileElectricTurbine
 *
 * 1 cubic meter of steam = 338260 J of energy
 *
 * The front of the turbine is where the output is.
 */
public class TileElectricTurbine extends TileElectric implements ITickable, IMultiBlockStructure<TileElectricTurbine>, ITileNetwork {
    // Amount of energy per liter of steam. Boil Water Energy = 327600 + 2260000 = 2587600
    //protected final int energyPerSteam = 2647600 / 1000;
    protected final int energyPerSteam = 52000;
    protected final int defaultTorque = 5000;
    protected int torque = defaultTorque;
    private final FluidTankStrict tank = new FluidTankStrict(QuantumFluids.fluidStackSteam, Fluid.BUCKET_VOLUME * 100);

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
        setEnergyStorage(new EnergyStorage(maxPower * 20));
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
                    power += tank.drain((int) Math.ceil(Math.min(tank.getFluidAmount() * 0.1, getMaxPower() / energyPerSteam)), true).amount * energyPerSteam;
                }

                // Set angular velocity based on power and torque.
                angularVelocity = (power * 4) / torque;

                if (world.getWorldTime() % 3 == 0 && previousAngularVelocity != angularVelocity) {
                    Quantum.getPacketHandler().sendToReceivers(new PacketTileEntity(this), this);
                    previousAngularVelocity = angularVelocity;
                }

                if (power > 0) {
                    energyStorage.receiveEnergy((int) (power * ConfigurationManager.General.turbineOutputMultiplier), false);
                    produce();
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
            getMultiBlock().get().tank.fill(tank.drain(getMultiBlock().get().tank.fill(tank.getFluid(), false), true), true);
        }

        if (!world.isRemote) {
            power = 0;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        multiBlockRadius = tag.getInteger("multiBlockRadius");
        getMultiBlock().load(tag);
        tank.readFromNBT(tag);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        tag = super.writeToNBT(tag);

        tag.setInteger("multiBlockRadius", multiBlockRadius);
        tag = getMultiBlock().save(tag);
        tag = tank.writeToNBT(tag);

        return tag;
    }

    @Override
    public boolean canConnectEnergy(EnumFacing from) {
        return getMultiBlock().isPrimary() && from == EnumFacing.UP;
    }

    @Override
    public EnumSet<EnumFacing> getReceivingDirections() {
        return EnumSet.noneOf(EnumFacing.class);
    }

    @Override
    public EnumSet<EnumFacing> getExtractingDirections()
    {
        return EnumSet.of(EnumFacing.UP);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public Vector3[] getMultiBlockVectors() {
        Set<Vector3> vectors = new HashSet<>();

        EnumFacing facing = EnumFacing.NORTH;
        int xMulti = facing.getFrontOffsetX() != 0 ? 0 : 1;
        int yMulti = facing.getFrontOffsetY() != 0 ? 0 : 1;
        int zMulti = facing.getFrontOffsetZ() != 0 ? 0 : 1;

        for (int x = -multiBlockRadius; x <= multiBlockRadius; x++) {
            for (int y = -multiBlockRadius; y <= multiBlockRadius; y++) {
                for (int z = -multiBlockRadius; z <= multiBlockRadius; z++) {
                    vectors.add(new Vector3(x * xMulti, y * yMulti, z * zMulti));
                }
            }
        }

        return vectors.toArray(new Vector3[0]);
    }

    @Override
    public World getWorldObject() {
        return world;
    }

    @Override
    public void onMultiBlockChanged() {
        world.notifyNeighborsOfStateChange(pos, getBlockType());

        // TODO: Update render?
        //world.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    public Vector3 getPosition()
    {
        return new Vector3(this);
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
            tier = dataStream.readInt();
            angularVelocity = dataStream.readFloat();
            tank.handlePacketData(dataStream);
        }
    }

    @Override
    public List<Object> getPacketData(List<Object> objects) {
        objects.add(tier);
        objects.add(angularVelocity);
        objects.addAll(tank.getPacketData(objects));

        return objects;
    }

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
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nonnull
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nonnull EnumFacing facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            if (facing == EnumFacing.DOWN) {
                return (T) getMultiBlock().get().tank;
            }
        }

        return super.getCapability(capability, facing);
    }
}
