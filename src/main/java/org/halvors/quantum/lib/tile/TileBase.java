package org.halvors.quantum.lib.tile;

import java.util.HashSet;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.quantum.lib.IPlayerUsing;

public abstract class TileBase extends TileBlock implements IPlayerUsing {
    private final HashSet<EntityPlayer> playersUsing = new HashSet<>();
    protected long ticks = 0L;

    public TileBase(String name, Material material) {
        super(name, material);
    }

    public TileBase(Material material) {
        super(material);
    }

    public TileBlock tile() {
        return this;
    }

    public void initiate() {

    }

    @Override
    public void updateEntity() {
        if (ticks == 0L) {
            initiate();
        }

        if (ticks >= Long.MAX_VALUE) {
            ticks = 1L;
        }

        ticks ++;
    }

    @Override
    public Packet getDescriptionPacket() {
        // TODO: Fix this.
        //References.PACKET_ANNOTATION.getPacket(this);

        return null;
    }

    @Override
    public HashSet<EntityPlayer> getPlayersUsing() {
        return this.playersUsing;
    }

    @Override
    public ForgeDirection getDirection() {
        return ForgeDirection.getOrientation(worldObj.getBlockMetadata(xCoord, yCoord, zCoord));
    }

    @Override
    public void setDirection(ForgeDirection direction) {
        this.worldObj.setBlockMetadataWithNotify(xCoord, yCoord, zCoord, direction.ordinal(), 3);
    }
}
