package org.halvors.nuclearphysics;

import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.halvors.nuclearphysics.common.item.group.ItemGroupMain;
import org.halvors.nuclearphysics.setup.ClientSetup;
import org.halvors.nuclearphysics.setup.CommonSetup;

/*@Mod(modid = Reference.ID,
     name = Reference.NAME,
     version = Reference.VERSION,
	 dependencies = "after:" + Integration.BUILDCRAFT_CORE_ID + ";after:" + Integration.COFH_CORE_ID + ";after:" + Integration.MEKANISM_ID,
	 acceptedMinecraftVersions = "[1.12,1.13)",
	 guiFactory = "org.halvors." + Reference.ID + ".client.gui.configuration.GuiConfiguationFactory")
*/
@Mod(NuclearPhysics.ID)
public final class NuclearPhysics {
	public static final String ID = "nuclearphysics";

	//public static final NetworkHandler NETWORK_HANDLER = new NetworkHandler();
	public static final ItemGroup MAIN_GROUP = new ItemGroupMain();
	//public static final ServerConfig SERVER_CONFIG = new ServerConfig();
	//public static final ClientConfig CLIENT_CONFIG = new ClientConfig();

	public NuclearPhysics() {
		DistExecutor.runWhenOn(Dist.CLIENT, () -> ClientSetup::new);


		//ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, SERVER_CONFIG.getSpec());
		//ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CLIENT_CONFIG.getSpec());

		CommonSetup commonSetup = new CommonSetup();

		FMLJavaModLoadingContext.get().getModEventBus().addListener(commonSetup::onCommonSetup);
		FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Block.class, commonSetup::onRegisterBlocks);
		FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(TileEntityType.class, commonSetup::onRegisterTiles);
		FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Item.class, commonSetup::onRegisterItems);
		FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(IRecipeSerializer.class, commonSetup::onRegisterRecipeSerializers);
		FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(ContainerType.class, commonSetup::onRegisterContainers);
	}

	// 1.12.2 ///////////////////////////////////////////////////////////////////

	/*
	// The instance of your mod that Forge uses.
	//@Instance(Reference.ID)
	private static NuclearPhysics instance;

	// Says where the client and server 'proxy' code is loaded.
	//@SidedProxy(clientSide = "org.halvors." + Reference.ID + ".client.ClientProxy", serverSide = "org.halvors." + Reference.ID + ".common.CommonProxy")
	//private static CommonProxy proxy;
	public static CommonProxy proxy = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);

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

	public NuclearPhysics() {
		instance = this;

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::preInit);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::postInit);
	}

	public void preInit(FMLCommonSetupEvent event) {
		// Initialize configuration.
        configuration = new Configuration(event.getSuggestedConfigurationFile());

		// Load the configuration.
		ConfigurationManager.loadConfiguration(configuration);

		// Call functions for adding blocks, items, etc.
		ModCapabilities.registerCapabilities();
		ModMessages.registerMessages();
		ModRecipes.registerRecipes();
		ModWorldGenerators.registerWorldGenerators();

		// Calling proxy handler.
		proxy.preInit();
	}

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

	public static CreativeTab getCreativeTab() {
		return creativeTab;
	}
	*/
}