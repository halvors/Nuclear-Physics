package org.halvors.quantum.atomic.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import org.halvors.quantum.atomic.common.Quantum;
import org.halvors.quantum.atomic.common.Reference;
import org.halvors.quantum.atomic.common.network.packet.PacketConfiguration;
import org.halvors.quantum.core.network.packet.PacketCreativeBuilder;
import org.halvors.quantum.atomic.common.network.packet.PacketTileEntity;
import org.halvors.quantum.atomic.common.utility.PlayerUtility;
import org.halvors.quantum.atomic.common.utility.location.Range;

import java.util.List;
import java.util.Set;

/**
 * This is the PacketHandler which is responsible for registering the packet that we are going to use.
 *
 * @author halvors
 */
public class PacketHandler {
	private final SimpleNetworkWrapper networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(Reference.ID);

	public void init() {
		// Register packets.
		networkWrapper.registerMessage(PacketConfiguration.PacketConfigurationMessage.class, PacketConfiguration.class, 0, Side.CLIENT);
		networkWrapper.registerMessage(PacketTileEntity.PacketTileEntityMessage.class, PacketTileEntity.class, 1, Side.CLIENT);
		networkWrapper.registerMessage(PacketCreativeBuilder.PacketCreativeBuilderMessage.class, PacketCreativeBuilder.class, 2, Side.SERVER);
	}

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
				if (player.dimension == dimensionId && cuboid.isVecInside(new Vec3d(player.posX, player.posY, player.posZ))) {
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
			if (player.dimension == range.getDimensionId() && Range.getChunkRange(player).intersects(range)) {
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

	public static void handlePacket(Runnable runnable, EntityPlayer player) {
		Quantum.getProxy().handlePacket(runnable, player);
	}

	public Packet getPacketFrom(IMessage message) {
		return networkWrapper.getPacketFrom(message);
	}

	public static EntityPlayer getPlayer(MessageContext context) {
		return Quantum.getProxy().getPlayer(context);
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
                writeString(dataStream, (String) object);
            } else if (object instanceof ItemStack) {
                writeStack(dataStream, (ItemStack) object);
            } else if (object instanceof NBTTagCompound) {
                writeNBT(dataStream, (NBTTagCompound) object);
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

	public static void writeString(ByteBuf output, String s) {
		ByteBufUtils.writeUTF8String(output, s);
	}

	public static String readString(ByteBuf input) {
		return ByteBufUtils.readUTF8String(input);
	}

	public static void writeStack(ByteBuf output, ItemStack stack) {
		ByteBufUtils.writeItemStack(output, stack);
	}

	public static ItemStack readStack(ByteBuf input) {
		return ByteBufUtils.readItemStack(input);
	}

	public static void writeNBT(ByteBuf output, NBTTagCompound tagCompound) {
		ByteBufUtils.writeTag(output, tagCompound);
	}

	public static NBTTagCompound readNBT(ByteBuf input) {
		return ByteBufUtils.readTag(input);
	}
}
