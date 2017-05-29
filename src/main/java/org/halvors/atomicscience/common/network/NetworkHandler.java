package org.halvors.atomicscience.common.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.NetworkRegistry.TargetPoint;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.halvors.electrometrics.common.Reference;
import org.halvors.electrometrics.common.network.packet.*;
import org.halvors.electrometrics.common.tile.TileEntity;
import org.halvors.electrometrics.common.util.PlayerUtils;
import org.halvors.electrometrics.common.util.location.Range;

import java.util.List;

/**
 * This is the NetworkHandler which is responsible for registering the packet that we are going to use.
 *
 * @author halvors
 */
public class NetworkHandler {
	private static final SimpleNetworkWrapper networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.ID);

	static {
		// Register packets.
		networkWrapper.registerMessage(PacketConfiguration.PacketConfigurationMessage.class, PacketConfiguration.class, 0, Side.CLIENT);
		networkWrapper.registerMessage(PacketRequestData.PacketRequestDataMessage.class, PacketRequestData.class, 1, Side.SERVER);
		networkWrapper.registerMessage(PacketTileEntity.PacketTileEntityMessage.class, PacketTileEntity.class, 2, Side.SERVER);
		networkWrapper.registerMessage(PacketTileEntity.PacketTileEntityMessage.class, PacketTileEntity.class, 2, Side.CLIENT);
		networkWrapper.registerMessage(PacketTileEntityElectricityMeter.PacketTileEntityElectricityMeterMessage.class, PacketTileEntityElectricityMeter.class, 3, Side.SERVER);
		networkWrapper.registerMessage(PacketTileEntityElectricityMeter.PacketTileEntityElectricityMeterMessage.class, PacketTileEntityElectricityMeter.class, 3, Side.CLIENT);
		networkWrapper.registerMessage(PacketTileRedstoneControl.PacketTileRedstoneControlMessage.class, PacketTileRedstoneControl.class, 4, Side.SERVER);
		networkWrapper.registerMessage(PacketTileRedstoneControl.PacketTileRedstoneControlMessage.class, PacketTileRedstoneControl.class, 4, Side.CLIENT);
	}

	public static SimpleNetworkWrapper getNetworkWrapper() {
		return networkWrapper;
	}

	public static EntityPlayer getPlayer(MessageContext context) {
		return context.side.isClient() ? PlayerUtils.getClientPlayer() : context.getServerHandler().playerEntity;
	}

	public static World getWorld(MessageContext context) {
		return getPlayer(context).worldObj;
	}

	public static Packet getPacketFrom(IMessage message) {
		return networkWrapper.getPacketFrom(message);
	}

	public static void sendTo(IMessage message, EntityPlayerMP player) {
		networkWrapper.sendTo(message, player);
	}

	public static void sendToAll(IMessage message) {
		networkWrapper.sendToAll(message);
	}

	public static void sendToAllAround(IMessage message, TargetPoint point) {
		networkWrapper.sendToAllAround(message, point);
	}

	public static void sendToDimension(IMessage message, int dimensionId) {
		networkWrapper.sendToDimension(message, dimensionId);
	}

	public static void sendToServer(IMessage message) {
		networkWrapper.sendToServer(message);
	}

	/**
	 * Send this message to all players within a defined AABB cuboid.
	 * @param message - the message to send
	 * @param cuboid - the AABB cuboid to send the packet in
	 * @param dimensionId - the dimension the cuboid is in
	 */
	public static void sendToCuboid(IMessage message, AxisAlignedBB cuboid, int dimensionId) {
		if (cuboid != null) {
			for (EntityPlayerMP player : PlayerUtils.getPlayers()) {
				if (player.dimension == dimensionId && cuboid.isVecInside(Vec3.createVectorHelper(player.posX, player.posY, player.posZ))) {
					sendTo(message, player);
				}
			}
		}
	}

	public static void sendToReceivers(IMessage message, Range range) {
		for (EntityPlayerMP player : PlayerUtils.getPlayers()) {
			if (player.dimension == range.getDimensionId() && Range.getChunkRange(player).intersects(range)) {
				sendTo(message, player);
			}
		}
	}

	public static void sendToReceivers(IMessage message, Entity entity) {
		sendToReceivers(message, new Range(entity));
	}

	public static void sendToReceivers(IMessage message, TileEntity tileEntity) {
		sendToReceivers(message, new Range(tileEntity));
	}

    public static void writeObject(Object object, ByteBuf dataStream) {
        try {
            // Language types.
            if (object instanceof Boolean) {
                dataStream.writeBoolean((Boolean) object);
            } else if (object instanceof Byte) {
                dataStream.writeByte((Byte) object);
            } else if (object instanceof byte[]) {
                dataStream.writeBytes((byte[]) object);
            } else if (object instanceof Double) {
                dataStream.writeDouble((Double) object);
            } else if (object instanceof Float) {
                dataStream.writeFloat((Float) object);
            } else if (object instanceof Integer) {
                dataStream.writeInt((Integer) object);
            } else if (object instanceof int[]) {
                for (int i : (int[]) object) {
                    dataStream.writeInt(i);
                }
            } else if (object instanceof Long) {
                dataStream.writeLong((Long) object);
            } else if (object instanceof String) {
                ByteBufUtils.writeUTF8String(dataStream, (String) object);
            } else if (object instanceof ItemStack) {
                ByteBufUtils.writeItemStack(dataStream, (ItemStack) object);
            } else if (object instanceof NBTTagCompound) {
                ByteBufUtils.writeTag(dataStream, (NBTTagCompound) object);
            }
        } catch (Exception e) {
            org.halvors.electrometrics.AtomicScience.getLogger().error("An error occurred when sending packet data.");
            e.printStackTrace();
        }
    }

    public static void writeObjects(List<Object> objects, ByteBuf dataStream) {
        for (Object object : objects) {
            writeObject(object, dataStream);
        }
    }
}
