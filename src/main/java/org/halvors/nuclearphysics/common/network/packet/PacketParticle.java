package org.halvors.nuclearphysics.common.network.packet;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;
import org.halvors.nuclearphysics.common.network.PacketHandler;

public class PacketParticle implements IMessage {
    private String particleName;
    private double x, y, z, velocityX, velocityY, velocityZ;

    public PacketParticle() {

    }

    public PacketParticle(String particleName, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        this.particleName = particleName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
    }

    @Override
    public void fromBytes(ByteBuf dataStream) {
        this.particleName = ByteBufUtils.readUTF8String(dataStream);
        this.x = dataStream.readDouble();
        this.y = dataStream.readDouble();
        this.z = dataStream.readDouble();
        this.velocityX = dataStream.readDouble();
        this.velocityY = dataStream.readDouble();
        this.velocityZ = dataStream.readDouble();
    }

    @Override
    public void toBytes(ByteBuf dataStream) {
        ByteBufUtils.writeUTF8String(dataStream, particleName);
        dataStream.writeDouble(x);
        dataStream.writeDouble(y);
        dataStream.writeDouble(z);
        dataStream.writeDouble(velocityX);
        dataStream.writeDouble(velocityY);
        dataStream.writeDouble(velocityZ);
    }

    public static class PacketParticleMessage implements IMessageHandler<PacketParticle, IMessage> {
        @Override
        public IMessage onMessage(PacketParticle message, MessageContext messageContext) {
            World world = PacketHandler.getWorld(messageContext);

            if (world != null) {
                // Spawn the particle, this is done on the client.
                world.spawnParticle(message.particleName, message.x, message.y, message.z, message.velocityX, message.velocityY, message.velocityZ);
            }

            return null;
        }
    }
}