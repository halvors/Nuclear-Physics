package org.halvors.nuclearphysics.common.tile;

import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.nuclearphysics.api.BlockPos;
import org.halvors.nuclearphysics.common.capabilities.energy.EnergyStorage;

import java.util.*;

public class TileGenerator extends TileBase implements ITileNetwork, IEnergyProvider {
    private final List<BlockPos> targets = new ArrayList<>();
    private final Map<BlockPos, ForgeDirection> facings = new HashMap<>();

    private int targetStartingIndex;

    protected EnergyStorage energyStorage;

    public TileGenerator() {
        super();
    }

    @Override
    public void readFromNBT(final NBTTagCompound tag) {
        super.readFromNBT(tag);

        if (energyStorage != null) {
            energyStorage.readFromNBT(tag);
        }
    }

    @Override
    public void writeToNBT(final NBTTagCompound tag) {
        super.writeToNBT(tag);

        if (energyStorage != null) {
            energyStorage.writeToNBT(tag);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void updateEntity() {
        if (!worldObj.isRemote) {
            sendEnergyToTargets();

            if (worldObj.getWorldTime() % getTargetRefreshRate() == 0) {
                searchTargets();
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handlePacketData(final ByteBuf dataStream) {
        if (worldObj.isRemote) {
            energyStorage.setEnergyStored(dataStream.readInt());
        }
    }

    @Override
    public List<Object> getPacketData(final List<Object> objects) {
        objects.add(energyStorage.getEnergyStored());

        return objects;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public int extractEnergy(final ForgeDirection from, final int maxExtract, final boolean simulate) {
        return energyStorage.extractEnergy(maxExtract, simulate);
    }

    @Override
    public int getEnergyStored(final ForgeDirection from) {
        return energyStorage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored(final ForgeDirection from) {
        return energyStorage.getMaxEnergyStored();
    }

    @Override
    public boolean canConnectEnergy(final ForgeDirection from) {
        return true;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public EnumSet<ForgeDirection> getExtractingDirections() {
        return EnumSet.noneOf(ForgeDirection.class);
    }

    protected int getTargetRefreshRate() {
        return 20;
    }

    protected void searchTargets() {
        targets.clear();

        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            final BlockPos neighborPos = pos.offset(side);

            if (isValidTarget(neighborPos, side)) {
                targets.add(neighborPos);
                facings.put(neighborPos, side);
            }
        }
    }

    protected void sendEnergyToTargets() {
        if (targets.size() > 0 && energyStorage.getEnergyStored() > 0) {
            for (int i = 0; i < targets.size(); ++i) {
                final BlockPos pos = targets.get((targetStartingIndex + i) % targets.size());
                sendEnergyTo(pos, facings.get(pos));
            }

            targetStartingIndex = (targetStartingIndex + 1) % targets.size();
        }
    }

    protected boolean isValidTarget(final BlockPos pos, final ForgeDirection to) {
        final TileEntity tile = pos.getTileEntity(worldObj);

        if (tile instanceof IEnergyReceiver) {
            IEnergyReceiver energyReceiver = (IEnergyReceiver) tile;

            return energyReceiver.canConnectEnergy(to.getOpposite());
        }

        return false;
    }

    protected void sendEnergyTo(final BlockPos pos, final ForgeDirection to) {
        final TileEntity tile = pos.getTileEntity(worldObj);

        if (tile instanceof IEnergyReceiver) {
            final IEnergyReceiver energyReceiver = (IEnergyReceiver) tile;

            if (energyReceiver.canConnectEnergy(to.getOpposite())) {
                sendEnergyToRF(tile, to);
            }
        }
    }

    protected void sendEnergyToRF(final TileEntity tile, final ForgeDirection from) {
        if (tile instanceof IEnergyReceiver) {
            final IEnergyReceiver energyReceiver = (IEnergyReceiver) tile;

            if (energyReceiver.canConnectEnergy(from.getOpposite())) {
                energyStorage.extractEnergy(energyReceiver.receiveEnergy(from.getOpposite(), energyStorage.getEnergyStored(), false), false);
            }
        }
    }

    public EnergyStorage getEnergyStorage() {
        return energyStorage;
    }
}
