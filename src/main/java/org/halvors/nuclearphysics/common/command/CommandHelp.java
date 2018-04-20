package org.halvors.nuclearphysics.common.command;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import org.halvors.nuclearphysics.common.Reference;

import java.util.List;
import java.util.Map;

public class CommandHelp extends net.minecraft.command.CommandHelp {
    private final CommandMod commandMod;

    public CommandHelp(CommandMod commandMod) {
        this.commandMod = commandMod;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands." + Reference.ID + ".help.usage";
    }

    @Override
    protected Map<String, ICommand> getCommandMap(MinecraftServer server) {
        return commandMod.getCommandMap();
    }

    @Override
    protected List<ICommand> getSortedPossibleCommands(ICommandSender sender, MinecraftServer server) {
        return commandMod.getSortedPossibleCommands(sender, server);
    }
}