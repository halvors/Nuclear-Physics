package org.halvors.nuclearphysics.common.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import org.halvors.nuclearphysics.common.Reference;
import org.halvors.nuclearphysics.common.grid.UpdateTicker;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class CommandGrid extends CommandBase {
    @Override
    public String getName() {
        return "grid";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "message." + Reference.ID + ".grid.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 2) {
            throw new WrongUsageException(getUsage(sender));
        }

        /*
        final EntityLivingBase entity = getEntity(server, sender, args[0], EntityLivingBase.class);
        final float amount = (float) parseDouble(args[1], -Float.MAX_VALUE, Float.MAX_VALUE);
        final IMaxHealth maxHealth = CapabilityMaxHealth.getMaxHealth(entity);
        if (maxHealth != null) {
            processEntity(entity, maxHealth, amount);
        }
        */

        //notifyCommandListener(sender, this, "message." + Reference.ID + ".grid", entity.getDisplayName());

        if (args[0].equalsIgnoreCase("info")) {
            sender.sendMessage(new TextComponentString("[Universal Electricity Grid] Tick rate: " + (UpdateTicker.getInstance().isPaused() ? "Paused" : (UpdateTicker.getInstance().getDeltaTime() > 0 ? 1 / (double) UpdateTicker.getInstance().getDeltaTime() : 0) * 1000 + "/s")));
            sender.sendMessage(new TextComponentString("[Universal Electricity Grid] Grids running: " + UpdateTicker.getInstance().getUpdaterCount()));
        } else if (args[0].equalsIgnoreCase("pause")) {
            UpdateTicker.getInstance().setPaused(!UpdateTicker.getInstance().isPaused());
            sender.sendMessage(new TextComponentString("[Universal Electricity Grid] Ticking grids running state: " + !UpdateTicker.getInstance().isPaused()));
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        return args.length == 1 ? getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames()) : Collections.emptyList();
    }
}
