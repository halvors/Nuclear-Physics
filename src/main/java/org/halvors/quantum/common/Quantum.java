package org.halvors.quantum.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.halvors.quantum.common.ConfigurationManager.Integration;
import org.halvors.quantum.common.entity.EntityParticle;
import org.halvors.quantum.common.event.handler.ExplosionEventHandler;
import org.halvors.quantum.common.event.handler.PlayerEventHandler;
import org.halvors.quantum.common.event.handler.ThermalEventHandler;
import org.halvors.quantum.common.grid.UpdateTicker;
import org.halvors.quantum.common.grid.thermal.ThermalGrid;
import org.halvors.quantum.common.init.QuantumEntities;
import org.halvors.quantum.common.init.QuantumRecipes;
import org.halvors.quantum.common.network.PacketHandler;
import org.halvors.quantum.common.event.handler.FulminationHandler;
import org.halvors.quantum.common.world.WorldGenerator;

/**
 * This is the Quantum class, which is the main class of this mod.
 *
 * @author halvors
 */
@Mod(modid = Reference.ID,
     name = Reference.NAME,
     version = Reference.VERSION,
     dependencies = "after:Mekanism",
     guiFactory = "org.halvors." + Reference.ID + ".atomic.client.gui.configuration.GuiConfiguationFactory")
public class Quantum {
	// The instance of your mod that Forge uses.
	@Instance(Reference.ID)
	private static Quantum instance;

	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide = "org.halvors." + Reference.ID + ".atomic.client.ClientProxy", serverSide = "org.halvors." + Reference.ID + ".atomic.common.CommonProxy")
	private static CommonProxy proxy;

	// ConfigurationManager
	private static Configuration configuration;

	// Network
	private static final PacketHandler packetHandler = new PacketHandler();

	// Logger
	private static final Logger logger = LogManager.getLogger(Reference.ID);

	// Creative Tab
	private static final CreativeTabs creativeTab = new CreativeTabQuantum();

	// Grids
	private static final ThermalGrid thermalGrid = new ThermalGrid();

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// Initialize configuration.
        configuration = new Configuration(event.getSuggestedConfigurationFile());

		// Load the configuration.
		ConfigurationManager.loadConfiguration(configuration);

		// Mod integration.
		logger.log(Level.INFO, "Mekanism integration is " + (Integration.isMekanismEnabled ? "enabled" : "disabled") + ".");

		// Call functions for adding blocks, items, etc.
		QuantumEntities.register();
		QuantumRecipes.register();

		// Calling proxy handler.
		proxy.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		// Register event handlers.
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());
		MinecraftForge.EVENT_BUS.register(new ThermalEventHandler());
		MinecraftForge.EVENT_BUS.register(new ExplosionEventHandler());

		// Register the proxy as our GuiHandler to NetworkRegistry.
		NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);

		// Register event buses. TODO: Move this to a custom event handler?
		MinecraftForge.EVENT_BUS.register(FulminationHandler.INSTANCE);

		//Register the mod's world generators
		GameRegistry.registerWorldGenerator(new WorldGenerator(), 1);

		// TODO: Add support for this? Make sure to return something in OreDictionaryHelper still if disabled.
		if (ConfigurationManager.General.allowOreDictionaryCompatibility) {

		}

		ForgeChunkManager.setForcedChunkLoadingCallback(this, (tickets, world) -> {
            for (Ticket ticket : tickets) {
                if (ticket.getType() == ForgeChunkManager.Type.ENTITY) {
					Entity entity = ticket.getEntity();

					if (entity != null && entity instanceof EntityParticle) {
						((EntityParticle) entity).updateTicket = ticket;
					}
                }
            }
        });

		// Register packets.
		packetHandler.init();

		// Calling proxy handler.
		proxy.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		if (!UpdateTicker.getInstance().isAlive()) {
			UpdateTicker.getInstance().start();
		}

		// Register grids.
		UpdateTicker.addNetwork(thermalGrid);

		// Calling proxy handler.
		proxy.postInit();
	}

	public static Quantum getInstance() {
		return instance;
	}

	public static CommonProxy getProxy() {
		return proxy;
	}

	public static Configuration getConfiguration() {
		return configuration;
	}

	public static PacketHandler getPacketHandler() {
		return packetHandler;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static CreativeTabs getCreativeTab() {
		return creativeTab;
	}
}