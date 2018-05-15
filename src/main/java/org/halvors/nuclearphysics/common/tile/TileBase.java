package org.halvors.nuclearphysics.common.tile;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.network.packet.PacketTileEntity;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TileBase extends TileEntity implements ITileNetwork {
    public TileBase() {

    }

    @Override
    @Nonnull
    public NBTTagCompound getUpdateTag() {
        // Forge writes only x/y/z/id info to a new NBT Tag Compound. This is fine, we have a custom network system
        // to send other data so we don't use this one (yet).
        return super.getUpdateTag();
    }

    @Override
    public void handleUpdateTag(@Nonnull final NBTTagCompound tag) {
        // The super implementation of handleUpdateTag is to call this readFromNBT. But, the given TagCompound
        // only has x/y/z/id data, so our readFromNBT will set a bunch of default values which are wrong.
        // So simply call the super's readFromNBT, to let Forge do whatever it wants, but don't treat this like
        // a full NBT object, don't pass it to our custom read methods.
        super.readFromNBT(tag);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void handlePacketData(ByteBuf dataStream) {

    }

    @Override
    public List<Object> getPacketData(List<Object> objects) {
        return null;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void notifyBlockUpdate() {
        NuclearPhysics.getPacketHandler().sendToReceivers(new PacketTileEntity(this), this);
    }
}
