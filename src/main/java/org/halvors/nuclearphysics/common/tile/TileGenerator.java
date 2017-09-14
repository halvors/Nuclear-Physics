package org.halvors.nuclearphysics.common.tile;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import org.halvors.nuclearphysics.common.capabilities.energy.EnergyStorage;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class TileGenerator extends TileBase implements ITickable, ITileNetwork, IEnergyStorage {
    private final List<BlockPos> targets = new ArrayList<>();
    private final Map<BlockPos, EnumFacing> facings = new HashMap<>();
    private int targetStartingIndex;

    protected EnergyStorage energyStorage;

    public TileGenerator() {

    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        CapabilityEnergy.ENERGY.readNBT(energyStorage, null, tag.getTag("storedEnergy"));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        if (energyStorage != null) {
            tag.setTag("storedEnergy", CapabilityEnergy.ENERGY.writeNBT(energyStorage, null));
        }

        return tag;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return (capability == CapabilityEnergy.ENERGY && getExtractingDirections().contains(facing)) || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Override
    @Nonnull
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY && getExtractingDirections().contains(facing)) {
            return (T) energyStorage;
        }

        return super.getCapability(capability, facing);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void update() {
        if (!world.isRemote) {
            sendEnergyToTargets();

            if (world.getWorldTime() % getTargetRefreshRate() == 0) {
                searchTargets();
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handlePacketData(ByteBuf dataStream) {
        if (world.isRemote) {
            energyStorage.setEnergyStored(dataStream.readInt());
        }
    }

    @Override
    public List<Object> getPacketData(List<Object> objects) {
        objects.add(energyStorage.getEnergyStored());

        return objects;
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

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public EnumSet<EnumFacing> getExtractingDirections() {
        return EnumSet.noneOf(EnumFacing.class);
    }

    protected int getTargetRefreshRate() {
        return 20;
    }

    protected void searchTargets() {
        targets.clear();

        for (EnumFacing side : EnumFacing.values()) {
            BlockPos neighbor = pos.offset(side);

            if (isValidTarget(neighbor, side)) {
                targets.add(neighbor);
                facings.put(neighbor, side);
            }
        }
    }

    protected void sendEnergyToTargets() {
        if (targets.size() > 0 && energyStorage.getEnergyStored() > 0) {
            for (int i = 0; i < targets.size(); ++i) {
                BlockPos pos = targets.get((targetStartingIndex + i) % targets.size());
                sendEnergyTo(pos, facings.get(pos));
            }

            targetStartingIndex = (targetStartingIndex + 1) % targets.size();
        }
    }

    protected boolean isValidTarget(BlockPos pos, EnumFacing to) {
        TileEntity tile = world.getTileEntity(pos);

        return tile != null && tile.hasCapability(CapabilityEnergy.ENERGY, to.getOpposite());

    }

    protected void sendEnergyTo(BlockPos pos, EnumFacing to) {
        TileEntity tile = world.getTileEntity(pos);

        if (tile != null && tile.hasCapability(CapabilityEnergy.ENERGY, to.getOpposite())) {
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
}
