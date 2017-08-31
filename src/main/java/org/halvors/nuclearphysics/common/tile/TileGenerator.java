package org.halvors.nuclearphysics.common.tile;

import com.google.common.collect.Lists;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TileGenerator extends TileQuantum implements ITickable {
    private final List<BlockPos> mTargets = Lists.newArrayList();
    private final Map<BlockPos, EnumFacing> mFacings = new HashMap<>();
    private int mTargetStartingIndex;

    protected EnergyStorage energyStorage;

    public TileGenerator() {

    }

    public TileGenerator(int capacity) {

        energyStorage = new EnergyStorage(capacity, 0, capacity);
    }

    @Override
    public void update() {
        sendEnergyToTargets();

        if (world.getWorldTime() % getTargetRefreshRate() == 0) {
            searchTargets();
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        if (energyStorage != null) {
            CapabilityEnergy.ENERGY.readNBT(energyStorage, null, tag.getTag("storedEnergy"));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        if (energyStorage != null) {
            tag.setTag("storedEnergy", CapabilityEnergy.ENERGY.writeNBT(energyStorage, null));
        }

        return tag;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nonnull EnumFacing facing) {
        return (capability == CapabilityEnergy.ENERGY && getExtractingDirections().contains(facing)) || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nonnull
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nonnull EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY && getExtractingDirections().contains(facing)) {
            return (T) energyStorage;
        }

        return super.getCapability(capability, facing);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public EnumSet<EnumFacing> getExtractingDirections() {
        return EnumSet.noneOf(EnumFacing.class);
    }

    protected int getTargetRefreshRate() {
        return 20;
    }

    protected void searchTargets() {
        mTargets.clear();

        for (EnumFacing direction : EnumFacing.VALUES) {
            BlockPos neighbor = pos.offset(direction);

            if (isValidTarget(neighbor, direction)) {
                mTargets.add(neighbor);
                mFacings.put(neighbor, direction);
            }
        }
    }

    protected void sendEnergyToTargets() {
        if (mTargets.size() > 0 && energyStorage.getEnergyStored() > 0) {
            for (int i = 0; i < mTargets.size(); ++i) {
                BlockPos pos = mTargets.get((mTargetStartingIndex + i) % mTargets.size());
                sendEnergyTo(pos, mFacings.get(pos));
            }

            mTargetStartingIndex = (mTargetStartingIndex + 1) % mTargets.size();
        }
    }

    protected boolean isValidTarget(BlockPos pos, EnumFacing to) {
        TileEntity tile = world.getTileEntity(pos);

        if (tile != null && tile.hasCapability(CapabilityEnergy.ENERGY, to.getOpposite())) {
            return true;
        }

        return false;
    }

    protected void sendEnergyTo(BlockPos pos, EnumFacing to) {
        TileEntity tile = world.getTileEntity(pos);

        if (tile == null) {
            return;
        }

        if (tile.hasCapability(CapabilityEnergy.ENERGY, to.getOpposite())) {
            sendEnergyToFE(tile, to);
        }
    }

    protected void sendEnergyToFE(TileEntity tile, EnumFacing pFrom) {
        if (tile != null && tile.hasCapability(CapabilityEnergy.ENERGY, pFrom.getOpposite())) {
            IEnergyStorage ies = tile.getCapability(CapabilityEnergy.ENERGY, pFrom.getOpposite());
            energyStorage.extractEnergy(ies.receiveEnergy(energyStorage.getEnergyStored(), false), false);
        }
    }

    /*
    protected int produce() {
        int totalUsed = 0;

        // Send energy to available receivers.
        for (EnumFacing direction : getExtractingDirections()) {
            if (energyStorage.getEnergyStored() > 0) {
                TileEntity tileEntity = new Vector3(this).translate(direction).getTileEntity(world);

                if (tileEntity != null && tileEntity instanceof IEnergyReceiver) {
                    IEnergyReceiver tileReceiver = (IEnergyReceiver) tileEntity;
                    int used = extractEnergy(direction, tileReceiver.receiveEnergy(direction.getOpposite(), energyStorage.extractEnergy(energyStorage.getMaxExtract(), true), false), false);
                    totalUsed += energyStorage.extractEnergy(used, false);
                }
            }
        }

        return totalUsed;
    }
    */

    public EnergyStorage getEnergyStorage() {
        return energyStorage;
    }
}
