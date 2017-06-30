package org.halvors.quantum.common.updater;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.StringUtils;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.halvors.quantum.common.ConfigurationManager.General;
import org.halvors.quantum.common.IUpdatableMod;

public class UpdateManager {
    private static final Style description = new Style();
    private static final Style version = new Style();
    private static final Style modname = new Style();
    private static final Style download = new Style();
    private static final Style white = new Style();
    private static int pollOffset = 0;

    static {
        description.setColor(TextFormatting.GRAY);
        version.setColor(TextFormatting.AQUA);
        modname.setColor(TextFormatting.GOLD);
        download.setColor(TextFormatting.GREEN);
        white.setColor(TextFormatting.WHITE);

        Style tooltip = new Style();
        tooltip.setColor(TextFormatting.YELLOW);
        ITextComponent message = new TextComponentTranslation("tooltip.clickToDownload").setStyle(tooltip);
        download.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, message));
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
    public void onTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
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
                        Style modData = modname.createShallowCopy();
                        modData.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString(newModVersion.getModVersion().toString()).setStyle(version)));

                        ITextComponent notificationChatMessage = new TextComponentString("");
                        notificationChatMessage.appendText("[");
                        notificationChatMessage.appendSibling(new TextComponentString(mod.getModName()).setStyle(modData));
                        notificationChatMessage.appendText("] ");
                        notificationChatMessage.appendSibling(new TextComponentTranslation("tooltip.newVersionAvailable").setStyle(white));
                        notificationChatMessage.appendText(":");

                        // Display description.
                        ITextComponent descriptionChatMessage = new TextComponentString("");

                        if (!StringUtils.isNullOrEmpty(downloadUrl)) {
                            Style downloadData = download.createShallowCopy();
                            downloadData.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, downloadUrl));

                            descriptionChatMessage.appendText("[");
                            descriptionChatMessage.appendSibling(new TextComponentTranslation("tooltip.download").setStyle(downloadData));
                            descriptionChatMessage.appendText("] ");
                        }

                        descriptionChatMessage.appendSibling(new TextComponentString(newModVersion.getDescription()).setStyle(description));

                        // Send the chat messages to the player.
                        player.sendMessage(notificationChatMessage);
                        player.sendMessage(descriptionChatMessage);
                    }
                }
            }
        }
    }
}
