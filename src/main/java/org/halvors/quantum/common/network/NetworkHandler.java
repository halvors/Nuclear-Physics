package org.halvors.quantum.common.network;

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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.debug.packet.PacketCreativeBuilder;
import org.halvors.quantum.common.network.packet.*;
import org.halvors.quantum.common.utility.PlayerUtils;
import org.halvors.quantum.common.utility.location.Range;

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
		networkWrapper.registerMessage(PacketTileEntity.PacketTileEntityMessage.class, PacketTileEntity.class, 1, Side.CLIENT);
		networkWrapper.registerMessage(PacketCreativeBuilder.PacketCreativeBuilderMessage.class, PacketCreativeBuilder.class, 2, Side.CLIENT);
		networkWrapper.registerMessage(PacketCreativeBuilder.PacketCreativeBuilderMessage.class, PacketCreativeBuilder.class, 2, Side.SERVER);
		networkWrapper.registerMessage(PacketTileRedstoneControl.PacketTileRedstoneControlMessage.class, PacketTileRedstoneControl.class, 3, Side.SERVER);
		networkWrapper.registerMessage(PacketTileRedstoneControl.PacketTileRedstoneControlMessage.class, PacketTileRedstoneControl.class, 3, Side.CLIENT);
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
                writeString(dataStream, (String) object);
            } else if (object instanceof ItemStack) {
                writeItemStack(dataStream, (ItemStack) object);
            } else if (object instanceof NBTTagCompound) {
                writeNBTTag(dataStream, (NBTTagCompound) object);
            }
        } catch (Exception e) {
            Quantum.getLogger().error("An error occurred when sending packet data.");
            e.printStackTrace();
        }
    }

    public static void writeObjects(List<Object> objects, ByteBuf dataStream) {
        for (Object object : objects) {
            writeObject(object, dataStream);
        }
    }

	public static void writeString(ByteBuf dataStream, String s) {
		dataStream.writeInt(s.getBytes().length);
		dataStream.writeBytes(s.getBytes());
	}

	public static String readString(ByteBuf dataStream) {
		return new String(dataStream.readBytes(dataStream.readInt()).array());
	}

	public static void writeItemStack(ByteBuf dataStream, ItemStack stack) {
		dataStream.writeInt(stack != null ? Item.getIdFromItem(stack.getItem()) : -1);

		if (stack != null) {
			dataStream.writeInt(stack.stackSize);
			dataStream.writeInt(stack.getMetadata());

			if(stack.getTagCompound() != null && stack.getItem().getShareTag()) {
				dataStream.writeBoolean(true);
				writeNBTTag(dataStream, stack.getTagCompound());
			} else {
				dataStream.writeBoolean(false);
			}
		}
	}

	public static ItemStack readItemStack(ByteBuf dataStream) {
		int id = dataStream.readInt();

		if (id >= 0) {
			ItemStack stack = new ItemStack(Item.getItemById(id), dataStream.readInt(), dataStream.readInt());

			if (dataStream.readBoolean()) {
				stack.setTagCompound(readNBTTag(dataStream));
			}

			return stack;
		}

		return null;
	}

	public static void writeNBTTag(ByteBuf dataStream, NBTTagCompound tagCompound) {
		try {
			byte[] buffer = CompressedStreamTools.compress(tagCompound);

			dataStream.writeInt(buffer.length);
			dataStream.writeBytes(buffer);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static NBTTagCompound readNBTTag(ByteBuf dataStream) {
		try {
			byte[] buffer = new byte[dataStream.readInt()];
			dataStream.readBytes(buffer);

			return CompressedStreamTools.decompress(buffer, new NBTSizeTracker(2097152L));
		} catch(Exception e) {
			e.printStackTrace();
		}

		return null;
	}
}
