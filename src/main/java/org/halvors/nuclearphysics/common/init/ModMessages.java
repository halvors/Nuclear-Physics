package org.halvors.nuclearphysics.common.init;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.relauncher.Side;
import org.halvors.nuclearphysics.common.NuclearPhysics;
import org.halvors.nuclearphysics.common.network.packet.PacketConfiguration;
import org.halvors.nuclearphysics.common.network.packet.PacketCreativeBuilder;
import org.halvors.nuclearphysics.common.network.packet.PacketTileEntity;

public class ModMessages {
    // Start the IDs at 1 so any unregistered messages (ID 0) throw a more obvious exception when received
    private static int messageId = 1;

    public static void registerMessages() {
        registerMessage(PacketConfiguration.PacketConfigurationMessage.class, PacketConfiguration.class, Side.CLIENT);
        registerMessage(PacketTileEntity.PacketTileEntityMessage.class, PacketTileEntity.class, Side.CLIENT);
        registerMessage(PacketCreativeBuilder.PacketCreativeBuilderMessage.class, PacketCreativeBuilder.class, Side.SERVER);
    }

    private static <REQ extends IMessage, REPLY extends IMessage> void registerMessage(final Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, final Class<REQ> requestMessageType, final Side receivingSide) {
        NuclearPhysics.getPacketHandler().networkWrapper.registerMessage(messageHandler, requestMessageType, messageId++, receivingSide);
    }
}
