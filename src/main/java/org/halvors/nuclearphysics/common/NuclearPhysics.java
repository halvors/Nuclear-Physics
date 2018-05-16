package org.halvors.nuclearphysics.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.halvors.nuclearphysics.common.entity.EntityParticle;
import org.halvors.nuclearphysics.common.init.*;
import org.halvors.nuclearphysics.common.network.PacketHandler;
import org.halvors.nuclearphysics.common.science.grid.GridTicker;
import org.halvors.nuclearphysics.common.science.grid.ThermalGrid;

@Mod(modid = Reference.ID,
     name = Reference.NAME,
     version = Reference.VERSION,
	 dependencies = "after:" + Integration.BUILDCRAFT_CORE_ID + ";after:" + Integration.COFH_CORE_ID + ";after:" + Integration.MEKANISM_ID,
	 guiFactory = "org.halvors." + Reference.ID + ".client.gui.configuration.GuiConfiguationFactory")
public class NuclearPhysics {
	// The instance of your mod that Forge uses.
	@Instance(Reference.ID)
	private static NuclearPhysics instance;

	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide = "org.halvors." + Reference.ID + ".client.ClientProxy", serverSide = "org.halvors." + Reference.ID + ".common.CommonProxy")
	private static CommonProxy proxy;

	// Configuration
	private static Configuration configuration;

	// Network
	private static final PacketHandler packetHandler = new PacketHandler();

	// Logger
	private static final Logger logger = LogManager.getLogger(Reference.ID);

	// Creative Tab
	private static final CreativeTab creativeTab = new CreativeTab();

	static {
		FluidRegistry.enableUniversalBucket(); // Must be called before preInit
	}

	@EventHandler
	public void preInit(final FMLPreInitializationEvent event) {
		// Initialize configuration.
        configuration = new Configuration(event.getSuggestedConfigurationFile());

		// Load the configuration.
		ConfigurationManager.loadConfiguration(configuration);

		// Call functions for adding blocks, items, etc.
		ModCapabilities.registerCapabilities();
		ModEntities.registerEntities();
		ModMessages.registerMessages();
		ModRecipes.registerRecipes();
		ModWorldGenerators.registerWorldGenerators();

		// Calling proxy handler.
		proxy.preInit();
	}

	@EventHandler
	public void init(final FMLInitializationEvent event) {
		// Register the proxy as our GuiHandler to NetworkRegistry.
		NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);

		ForgeChunkManager.setForcedChunkLoadingCallback(this, (tickets, world) -> {
            for (Ticket ticket : tickets) {
                if (ticket.getType() == Type.ENTITY) {
					final Entity entity = ticket.getEntity();

					if (entity instanceof EntityParticle) {
						((EntityParticle) entity).updateTicket = ticket;
					}
                }
            }
        });

		// Calling proxy handler.
		proxy.init();
	}

	@EventHandler
	public void postInit(final FMLPostInitializationEvent event) {
		if (!GridTicker.getInstance().isAlive()) {
			GridTicker.getInstance().start();
		}

		// Register our grids.
		GridTicker.getInstance().addGrid(new ThermalGrid());

		// Initialize mod integration.
		Integration.initialize();

		// Calling proxy handler.
		proxy.postInit();
	}

	@EventHandler
	public void serverStopping(final FMLServerStoppingEvent event) {
		GridTicker.getInstance().interrupt();
	}

	public static NuclearPhysics getInstance() {
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