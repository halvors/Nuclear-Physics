package org.halvors.nuclearphysics.common.init;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.relauncher.Side;
import org.halvors.nuclearphysics.common.network.PacketHandler;
import org.halvors.nuclearphysics.common.network.packet.*;
import org.halvors.nuclearphysics.common.network.packet.PacketConfiguration.PacketConfigurationMessage;
import org.halvors.nuclearphysics.common.network.packet.PacketCreativeBuilder.PacketCreativeBuilderMessage;
import org.halvors.nuclearphysics.common.network.packet.PacketParticle.PacketParticleMessage;
import org.halvors.nuclearphysics.common.network.packet.PacketRedstoneControl.PacketRedstoneControlMessage;
import org.halvors.nuclearphysics.common.network.packet.PacketTileEntity.PacketTileEntityMessage;

public class ModMessages {
    // Start the IDs at 1 so any unregistered messages (ID 0) throw a more obvious exception when received
    private static int messageId = 1;

    public static void registerMessages() {
        registerMessage(PacketConfigurationMessage.class, PacketConfiguration.class, Side.CLIENT);
        registerMessage(PacketTileEntityMessage.class, PacketTileEntity.class, Side.CLIENT);
        registerMessage(PacketRedstoneControlMessage.class, PacketRedstoneControl.class, Side.SERVER);
        registerMessage(PacketCreativeBuilderMessage.class, PacketCreativeBuilder.class, Side.SERVER);
        registerMessage(PacketParticleMessage.class, PacketParticle.class, Side.CLIENT);
    }

    private static <REQ extends IMessage, REPLY extends IMessage> void registerMessage(final Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, final Class<REQ> requestMessageType, final Side receivingSide) {
        PacketHandler.networkWrapper.registerMessage(messageHandler, requestMessageType, messageId++, receivingSide);
    }
}
