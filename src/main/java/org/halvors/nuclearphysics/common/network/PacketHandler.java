package org.halvors.nuclearphysics.common.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.Reference;
import org.halvors.nuclearphysics.common.type.Range;
import org.halvors.nuclearphysics.common.utility.PlayerUtility;

import java.util.List;
import java.util.Set;

/**
 * This is the PacketHandler which is responsible for registering the packet that we are going to use.
 *
 * @author halvors
 */
public class PacketHandler {
	public static final SimpleNetworkWrapper networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.ID);

	public void sendTo(IMessage message, EntityPlayerMP player) {
		networkWrapper.sendTo(message, player);
	}

	public void sendToAll(IMessage message) {
		networkWrapper.sendToAll(message);
	}

	public void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint point) {
		networkWrapper.sendToAllAround(message, point);
	}

	public void sendToDimension(IMessage message, int dimensionId) {
		networkWrapper.sendToDimension(message, dimensionId);
	}

	public void sendToServer(IMessage message) {
		networkWrapper.sendToServer(message);
	}

	/**
	 * Send this message to all players within a defined AABB cuboid.
	 * @param message - the message to send
	 * @param cuboid - the AABB cuboid to send the packet in
	 * @param dimensionId - the dimension the cuboid is in
	 */
	public void sendToCuboid(IMessage message, AxisAlignedBB cuboid, int dimensionId) {
		if (cuboid != null) {
			for (EntityPlayerMP player : PlayerUtility.getPlayers()) {
				if (player.dimension == dimensionId && cuboid.isVecInside(Vec3.createVectorHelper(player.posX, player.posY, player.posZ))) {
					sendTo(message, player);
				}
			}
		}
	}

	public void sendToReceivers(IMessage message, Set<EntityPlayer> playerList) {
		for (EntityPlayer player : playerList) {
			sendTo(message, (EntityPlayerMP) player);
		}
	}

	public void sendToReceivers(IMessage message, Range range) {
		for (EntityPlayerMP player : PlayerUtility.getPlayers()) {
			if (player.getEntityWorld().equals(range.getWorld()) && Range.getChunkRange(player).intersects(range)) {
				sendTo(message, player);
			}
		}
	}

	public void sendToReceivers(IMessage message, Entity entity) {
		sendToReceivers(message, new Range(entity));
	}

	public void sendToReceivers(IMessage message, TileEntity tileEntity) {
		sendToReceivers(message, new Range(tileEntity));
	}

	public static Packet getPacketFrom(IMessage message) {
		return networkWrapper.getPacketFrom(message);
	}

	public static EntityPlayer getPlayer(MessageContext context) {
		return NuclearPhysics.getProxy().getPlayer(context);
	}

	public static World getWorld(MessageContext context) {
		return getPlayer(context).getEntityWorld();
	}

    public static void writeObject(Object object, ByteBuf dataStream) {
        try {
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
            NuclearPhysics.getLogger().error("An error occurred when sending packet data.");
            e.printStackTrace();
        }
    }

    public static void writeObjects(List<Object> objects, ByteBuf dataStream) {
        for (Object object : objects) {
            writeObject(object, dataStream);
        }
    }

	public static String readString(ByteBuf input) {
		return ByteBufUtils.readUTF8String(input);
	}

	public static ItemStack readStack(ByteBuf input) {
		return ByteBufUtils.readItemStack(input);
	}

	public static NBTTagCompound readNBT(ByteBuf input) {
		return ByteBufUtils.readTag(input);
	}
}
