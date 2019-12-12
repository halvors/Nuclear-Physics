package org.halvors.nuclearphysics.common.tile;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import org.halvors.nuclearphysics.NuclearPhysics;
import org.halvors.nuclearphysics.common.network.packet.PacketTileEntity;

import java.util.Arrays;
import java.util.List;

public class TileRotatable extends TileBase implements ITileNetwork, ITileRotatable {
    private static final String NBT_DIRECTION = "direction";

    protected Direction direction = Direction.NORTH;

    public TileRotatable(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);

        if (compound.contains(NBT_DIRECTION)) {
            direction = Direction.byIndex(compound.getInt(NBT_DIRECTION));
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        if (direction != null) {
            compound.putInt(NBT_DIRECTION, direction.ordinal());
        }

        return super.write(compound);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handlePacketData(final ByteBuf dataStream) {
        if (world.isRemote) {
            direction = Direction.byIndex(dataStream.readInt());
        }
    }

    @Override
    public List<Object> getPacketData(final List<Object> objects) {
        objects.add(direction.ordinal());

        return objects;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public boolean canSetDirection(Direction direction) {
        return Arrays.asList(Direction.DOWN, Direction.EAST, Direction.NORTH, Direction.SOUTH).contains(direction);
    }

    @Override
    public Direction getDirection() {
        return direction;
    }

    @Override
    public void setDirection(Direction direction) {
        this.direction = direction;

        NuclearPhysics.getPacketHandler().sendToReceivers(new PacketTileEntity(this), this);
    }
}
