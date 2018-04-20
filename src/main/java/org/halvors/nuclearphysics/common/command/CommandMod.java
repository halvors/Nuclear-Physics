package org.halvors.nuclearphysics.common.command;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.command.CommandTreeBase;
import org.halvors.nuclearphysics.common.Reference;

import java.util.List;
import java.util.stream.Collectors;

public class CommandMod extends CommandTreeBase {
    @Override
    public String getName() {
        return Reference.ID;
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands." + Reference.ID + ".usage";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    public List<ICommand> getSortedPossibleCommands(ICommandSender sender, MinecraftServer server) {
        return getSubCommands().stream()
                .filter(command -> command.checkPermission(server, sender)).sorted().collect(Collectors.toList());
    }
}
