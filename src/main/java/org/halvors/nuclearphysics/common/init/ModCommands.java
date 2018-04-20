package org.halvors.nuclearphysics.common.init;

import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.halvors.nuclearphysics.common.command.CommandGrid;
import org.halvors.nuclearphysics.common.command.CommandHelp;
import org.halvors.nuclearphysics.common.command.CommandMod;

public class ModCommands {

    /**
     * Register the commands.
     *
     * @param event The server starting event
     */
    public static void registerCommands(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandMod());
    }

    /**
     * Register the sub-commands of the {@code /np} command.
     *
     * @param commandMod The /np command
     */
    public static void registerSubCommands(CommandMod commandMod) {
        commandMod.addSubcommand(new CommandHelp(commandMod));
        commandMod.addSubcommand(new CommandGrid());
    }
}
