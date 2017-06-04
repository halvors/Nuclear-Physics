package org.halvors.quantum.common.updater;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.*;
import org.halvors.quantum.common.ConfigurationManager.General;
import org.halvors.quantum.common.base.IUpdatableMod;

public class UpdateManager {
    private static final ChatStyle description = new ChatStyle();
    private static final ChatStyle version = new ChatStyle();
    private static final ChatStyle modname = new ChatStyle();
    private static final ChatStyle download = new ChatStyle();
    private static final ChatStyle white = new ChatStyle();
    private static int pollOffset = 0;

    static {
        description.setColor(EnumChatFormatting.GRAY);
        version.setColor(EnumChatFormatting.AQUA);
        modname.setColor(EnumChatFormatting.GOLD);
        download.setColor(EnumChatFormatting.GREEN);
        white.setColor(EnumChatFormatting.WHITE);

        ChatStyle tooltip = new ChatStyle();
        tooltip.setColor(EnumChatFormatting.YELLOW);
        IChatComponent message = new ChatComponentTranslation("tooltip.clickToDownload").setChatStyle(tooltip);
        download.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, message));
    }

    private final IUpdatableMod mod;
    private final UpdateThread updateThread;
    private final String downloadUrl;

    private boolean isNotificationDisplayed;
    private int lastPoll = 400;

    public UpdateManager(IUpdatableMod mod, String releaseUrl, String downloadUrl) {
        this.mod = mod;
        this.updateThread = new UpdateThread(mod, releaseUrl, downloadUrl);
        this.downloadUrl = downloadUrl;

        updateThread.start();
        lastPoll += (pollOffset += 140);
    }

    @SubscribeEvent
    public void onTick(PlayerTickEvent event) {
        if (event.phase == Phase.END) {
            if (lastPoll > 0) {
                --lastPoll;
            } else {
                lastPoll = 400;

                if (!isNotificationDisplayed && updateThread.isCheckCompleted()) {
                    isNotificationDisplayed = true;
                    FMLCommonHandler.instance().bus().unregister(this);

                    if (updateThread.isNewVersionAvailable()) {
                        if (!General.enableUpdateNotice && !updateThread.isCriticalUpdate()) {
                            return;
                        }

                        ModVersion newModVersion = updateThread.getNewModVersion();
                        EntityPlayer player = event.player;

                        // Display notification message.
                        ChatStyle modData = modname.createShallowCopy();
                        modData.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(newModVersion.getModVersion().toString()).setChatStyle(version)));

                        IChatComponent notificationChatMessage = new ChatComponentText("");
                        notificationChatMessage.appendText("[");
                        notificationChatMessage.appendSibling(new ChatComponentText(mod.getModName()).setChatStyle(modData));
                        notificationChatMessage.appendText("] ");
                        notificationChatMessage.appendSibling(new ChatComponentTranslation("tooltip.newVersionAvailable").setChatStyle(white));
                        notificationChatMessage.appendText(":");

                        // Display description.
                        IChatComponent descriptionChatMessage = new ChatComponentText("");

                        if (!StringUtils.isNullOrEmpty(downloadUrl)) {
                            ChatStyle downloadData = download.createShallowCopy();
                            downloadData.setChatClickEvent(new ClickEvent(Action.OPEN_URL, downloadUrl));

                            descriptionChatMessage.appendText("[");
                            descriptionChatMessage.appendSibling(new ChatComponentTranslation("tooltip.download").setChatStyle(downloadData));
                            descriptionChatMessage.appendText("] ");
                        }

                        descriptionChatMessage.appendSibling(new ChatComponentText(newModVersion.getDescription()).setChatStyle(description));

                        // Send the chat messages to the player.
                        player.addChatMessage(notificationChatMessage);
                        player.addChatMessage(descriptionChatMessage);
                    }
                }
            }
        }
    }
}
