package org.halvors.quantum.common;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.halvors.quantum.common.ConfigurationManager.Integration;
import org.halvors.quantum.common.block.debug.BlockCreativeBuilder;
import org.halvors.quantum.common.entity.EntityParticle;
import org.halvors.quantum.common.event.ExplosionEventHandler;
import org.halvors.quantum.common.event.PlayerEventHandler;
import org.halvors.quantum.common.event.ThermalEventHandler;
import org.halvors.quantum.common.grid.UpdateTicker;
import org.halvors.quantum.common.grid.thermal.ThermalGrid;
import org.halvors.quantum.common.network.PacketHandler;
import org.halvors.quantum.common.schematic.SchematicAccelerator;
import org.halvors.quantum.common.schematic.SchematicBreedingReactor;
import org.halvors.quantum.common.schematic.SchematicFissionReactor;
import org.halvors.quantum.common.schematic.SchematicFusionReactor;
import org.halvors.quantum.common.tile.particle.FulminationHandler;

/**
 * This is the Quantum class, which is the main class of this mod.
 *
 * @author halvors
 */
@Mod(modid = Reference.ID,
     name = Reference.NAME,
     version = Reference.VERSION,
     dependencies = "after:CoFHCore;" +
                    "after:Mekanism",
     guiFactory = "org.halvors." + Reference.ID + ".client.gui.configuration.GuiConfiguationFactory")
public class Quantum {
	// The instance of your mod that Forge uses.
	@Instance(Reference.ID)
	private static Quantum instance;

	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide = "org.halvors." + Reference.ID + ".client.ClientProxy", serverSide = "org.halvors." + Reference.ID + ".common.CommonProxy")
	private static CommonProxy proxy;

	// ConfigurationManager
	private static Configuration configuration;

	// Network
	private static final PacketHandler packetHandler = new PacketHandler();

	// Logger
	private static final Logger logger = LogManager.getLogger(Reference.ID);

	// Creative Tab
	private static final CreativeTabQuantum creativeTab = new CreativeTabQuantum();

	// Grids
	private static final ThermalGrid thermalGrid = new ThermalGrid();

	static {
		FluidRegistry.enableUniversalBucket(); // Must be called before preInit
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// Initialize configuration.
        configuration = new Configuration(event.getSuggestedConfigurationFile());

		// Load the configuration.
		ConfigurationManager.loadConfiguration(configuration);

		// Mod integration.
		logger.log(Level.INFO, "CoFHCore integration is " + (Integration.isCoFHCoreEnabled ? "enabled" : "disabled") + ".");
		logger.log(Level.INFO, "Mekanism integration is " + (Integration.isMekanismEnabled ? "enabled" : "disabled") + ".");

		// Call functions for adding blocks, items, etc.
		QuantumBlocks.register();
		QuantumItems.register();
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

		// Adde schematics to the creative builder.
		BlockCreativeBuilder.registerSchematic(new SchematicAccelerator());
		BlockCreativeBuilder.registerSchematic(new SchematicBreedingReactor());
		BlockCreativeBuilder.registerSchematic(new SchematicFissionReactor());
		BlockCreativeBuilder.registerSchematic(new SchematicFusionReactor());

		if (ConfigurationManager.General.allowOreDictionaryCompatibility) {
			OreDictionary.registerOre("ingotUranium", QuantumItems.itemUranium);
			OreDictionary.registerOre("dustUranium", QuantumItems.itemYellowCake);
		}

		OreDictionary.registerOre("oreUranium", new ItemStack(QuantumBlocks.blockUraniumOre));
		OreDictionary.registerOre("breederUranium", new ItemStack(QuantumItems.itemUranium, 1, 1));
		OreDictionary.registerOre("blockRadioactiveGrass", QuantumBlocks.blockRadioactiveGrass);
		OreDictionary.registerOre("cellEmpty", QuantumItems.itemCell);
		OreDictionary.registerOre("cellUranium", QuantumItems.itemFissileFuel);
		OreDictionary.registerOre("cellTritium", QuantumItems.itemTritiumCell);
		OreDictionary.registerOre("cellDeuterium", QuantumItems.itemDeuteriumCell);
		OreDictionary.registerOre("water_cell.json", QuantumItems.itemWaterCell);
		OreDictionary.registerOre("darkmatter", QuantumItems.itemDarkMatterCell);
		OreDictionary.registerOre("antimatterMilligram", new ItemStack(QuantumItems.itemAntimatterCell, 1, 0));
		OreDictionary.registerOre("antimatterGram", new ItemStack(QuantumItems.itemAntimatterCell, 1, 1));

		ForgeChunkManager.setForcedChunkLoadingCallback(this, (tickets, world) -> {
            for (ForgeChunkManager.Ticket ticket : tickets) {
                if (ticket.getType() == ForgeChunkManager.Type.ENTITY) {
                    if (ticket.getEntity() != null) {
                        if (ticket.getEntity() instanceof EntityParticle) {
                            ((EntityParticle) ticket.getEntity()).updateTicket = ticket;
                        }
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

	public static CreativeTabQuantum getCreativeTab() {
		return creativeTab;
	}
}