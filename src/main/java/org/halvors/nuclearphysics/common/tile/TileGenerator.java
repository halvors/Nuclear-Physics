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

public class TileGenerator extends TileBase implements ITickable, IEnergyStorage {
    private final List<BlockPos> mTargets = Lists.newArrayList();
    private final Map<BlockPos, EnumFacing> mFacings = new HashMap<>();
    private int mTargetStartingIndex;

    protected EnergyStorage energyStorage;

    public TileGenerator() {

    }

    public TileGenerator(int capacity) {
        energyStorage = new EnergyStorage(capacity);
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

    public EnergyStorage getEnergyStorage() {
        return energyStorage;
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

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return 0;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return energyStorage.extractEnergy(maxExtract, simulate);
    }

    @Override
    public int getEnergyStored() {
        return energyStorage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored() {
        return energyStorage.getMaxEnergyStored();
    }

    @Override
    public boolean canExtract() {
        return energyStorage.canExtract();
    }

    @Override
    public boolean canReceive() {
        return energyStorage.canReceive();
    }
}
