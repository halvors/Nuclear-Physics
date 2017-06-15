package org.halvors.quantum;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.*;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.halvors.quantum.client.render.machine.RenderCentrifuge;
import org.halvors.quantum.client.render.machine.RenderChemicalExtractor;
import org.halvors.quantum.client.render.machine.RenderNuclearBoiler;
import org.halvors.quantum.client.render.machine.RenderQuantumAssembler;
import org.halvors.quantum.client.render.reactor.RenderElectricTurbine;
import org.halvors.quantum.client.render.reactor.fission.RenderReactorCell;
import org.halvors.quantum.client.render.reactor.fission.RenderThermometer;
import org.halvors.quantum.client.render.reactor.fusion.RenderFusionReactor;
import org.halvors.quantum.common.CommonProxy;
import org.halvors.quantum.common.ConfigurationManager;
import org.halvors.quantum.common.ConfigurationManager.Integration;
import org.halvors.quantum.common.QuantumCreativeTab;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.base.IUpdatableMod;
import org.halvors.quantum.common.block.*;
import org.halvors.quantum.common.block.machine.BlockCentrifuge;
import org.halvors.quantum.common.block.machine.BlockChemicalExtractor;
import org.halvors.quantum.common.block.machine.BlockNuclearBoiler;
import org.halvors.quantum.common.block.machine.BlockQuantumAssembler;
import org.halvors.quantum.common.block.particle.BlockAccelerator;
import org.halvors.quantum.common.block.reactor.BlockElectricTurbine;
import org.halvors.quantum.common.block.reactor.BlockGasFunnel;
import org.halvors.quantum.common.block.reactor.fission.BlockControlRod;
import org.halvors.quantum.common.block.reactor.fission.BlockReactorCell;
import org.halvors.quantum.common.block.reactor.fission.BlockSiren;
import org.halvors.quantum.common.block.reactor.fission.BlockThermometer;
import org.halvors.quantum.common.block.reactor.fusion.BlockFusionReactor;
import org.halvors.quantum.common.block.reactor.fusion.BlockPlasma;
import org.halvors.quantum.common.debug.block.BlockCreativeBuilder;
import org.halvors.quantum.common.entity.particle.EntityParticle;
import org.halvors.quantum.common.event.PlayerEventHandler;
import org.halvors.quantum.common.event.ThermalEventHandler;
import org.halvors.quantum.common.item.ItemCell;
import org.halvors.quantum.common.item.ItemRadioactive;
import org.halvors.quantum.common.item.ItemScrewdriver;
import org.halvors.quantum.common.item.armor.ItemArmorHazmat;
import org.halvors.quantum.common.item.particle.ItemAntimatter;
import org.halvors.quantum.common.item.reactor.fission.ItemBreederFuel;
import org.halvors.quantum.common.item.reactor.fission.ItemBucketToxicWaste;
import org.halvors.quantum.common.item.reactor.fission.ItemFissileFuel;
import org.halvors.quantum.common.item.reactor.fission.ItemUranium;
import org.halvors.quantum.common.schematic.SchematicAccelerator;
import org.halvors.quantum.common.schematic.SchematicBreedingReactor;
import org.halvors.quantum.common.schematic.SchematicFissionReactor;
import org.halvors.quantum.common.schematic.SchematicFusionReactor;
import org.halvors.quantum.common.tile.machine.TileCentrifuge;
import org.halvors.quantum.common.tile.machine.TileChemicalExtractor;
import org.halvors.quantum.common.tile.machine.TileNuclearBoiler;
import org.halvors.quantum.common.tile.machine.TileQuantumAssembler;
import org.halvors.quantum.common.tile.particle.FulminationHandler;
import org.halvors.quantum.common.tile.particle.TileAccelerator;
import org.halvors.quantum.common.tile.particle.TileFulmination;
import org.halvors.quantum.common.tile.reactor.TileElectricTurbine;
import org.halvors.quantum.common.tile.reactor.TileGasFunnel;
import org.halvors.quantum.common.tile.reactor.fission.TileReactorCell;
import org.halvors.quantum.common.tile.reactor.fission.TileSiren;
import org.halvors.quantum.common.tile.reactor.fission.TileThermometer;
import org.halvors.quantum.common.tile.reactor.fusion.TileFusionReactor;
import org.halvors.quantum.common.tile.reactor.fusion.TilePlasma;
import org.halvors.quantum.common.transform.vector.Vector3;
import org.halvors.quantum.common.transform.vector.VectorWorld;
import org.halvors.quantum.common.updater.UpdateManager;
import org.halvors.quantum.lib.event.PlasmaEvent;
import org.halvors.quantum.lib.grid.UpdateTicker;
import org.halvors.quantum.lib.render.BlockRenderingHandler;
import org.halvors.quantum.lib.render.RenderUtility;
import org.halvors.quantum.lib.thermal.ThermalGrid;
import org.halvors.quantum.lib.tile.BlockDummy;
import org.halvors.quantum.lib.tile.TileBlock;

import java.util.List;

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
public class Quantum implements IUpdatableMod {
	// The instance of your mod that Forge uses.
	@Instance(value = Reference.ID)
	private static Quantum instance;

	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide = "org.halvors." + Reference.ID + ".client.ClientProxy", serverSide = "org.halvors." + Reference.ID + ".common.CommonProxy")
	private static CommonProxy proxy;

	// Logger
	private static final Logger logger = LogManager.getLogger(Reference.ID);

	// ConfigurationManager
	private static Configuration configuration;

	// Grids
	public static ThermalGrid thermalGrid = new ThermalGrid();

	// Creative Tab
	private static final QuantumCreativeTab creativeTab = new QuantumCreativeTab();

	// Fluids
	public static final Fluid fluidDeuterium = new Fluid("deuterium").setGaseous(true);
	public static final Fluid fluidUraniumHexaflouride = new Fluid("uraniumhexafluoride").setGaseous(true);
	public static final Fluid fluidPlasma = new Fluid("plasma").setGaseous(true);
	public static final Fluid fluidSteam = new Fluid("steam").setGaseous(true);
	public static final Fluid fluidTritium = new Fluid("tritium").setGaseous(true);
	public static final Fluid fluidToxicWaste = new Fluid("toxicwaste");

	public static FluidStack fluidStackDeuterium;
	public static FluidStack fluidStackUraniumHexaflouride;
	public static FluidStack fluidStackSteam;
	public static FluidStack fluidStackTritium;
	public static FluidStack fluidStackToxicWaste;
	public static FluidStack fluidStackWater;

	// Blocks
	public static Block blockAccelerator;
	public static Block blockChemicalExtractor;
	public static Block blockCentrifuge;
	public static Block blockControlRod;
	public static TileBlock blockElectromagnet;
	public static TileBlock blockFulmination;
	public static Block blockFusionReactor;
	public static Block blockGasFunnel;
	public static Block blockNuclearBoiler;
	public static Block blockSiren;
	public static Block blockThermometer;
	public static Block blockUraniumOre;
	public static Block blockPlasma;
	public static Block blockQuantumAssembler;
	public static Block blockRadioactiveGrass;
	public static Block blockReactorCell;
	public static BlockFluidClassic blockToxicWaste;
	public static Block blockElectricTurbine;

	public static Block blockCreativeBuilder;

	// Items
	// Cells
	public static Item itemAntimatter;
	public static Item itemBreedingRod;
	public static Item itemCell;
	public static Item itemDarkMatter;
	public static Item itemDeuteriumCell;
	public static Item itemFissileFuel;
	public static Item itemTritiumCell;
	public static Item itemWaterCell;

	// Buckets
	public static Item itemBucketToxicWaste;

	// Uranium
	public static Item itemUranium;
	public static Item itemYellowCake;

	// Hazmat
	public static ItemArmor itemHazmatMask;
	public static ItemArmor itemHazmatBody;
	public static ItemArmor itemHazmatLeggings;
	public static ItemArmor itemHazmatBoots;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// Initialize configuration.
        configuration = new Configuration(event.getSuggestedConfigurationFile());

		// Load the configuration.
		ConfigurationManager.loadConfiguration(configuration);

		// Check for updates.
		FMLCommonHandler.instance().bus().register(new UpdateManager(this, Reference.RELEASE_URL, Reference.DOWNLOAD_URL));

		// Mod integration.
		logger.log(Level.INFO, "CoFHCore integration is " + (Integration.isCoFHCoreEnabled ? "enabled" : "disabled") + ".");
		logger.log(Level.INFO, "Mekanism integration is " + (Integration.isMekanismEnabled ? "enabled" : "disabled") + ".");
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		// Register event handlers.
		MinecraftForge.EVENT_BUS.register(this);
		FMLCommonHandler.instance().bus().register(new PlayerEventHandler());
		MinecraftForge.EVENT_BUS.register(new ThermalEventHandler());

		// Register the proxy as our GuiHandler to NetworkRegistry.
		NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);

		// Register block rendering handler.
		RenderingRegistry.registerBlockHandler(new BlockRenderingHandler()); // TODO: Only register client side?

		// Call functions for adding blocks, items, etc.
		registerFluids();
		registerBlocks();
		registerTileEntities();
		registerTileEntitySpecialRenders();
		registerItems();
		registerFluidContainers();
		registerEntities();
		registerRecipes();

		// Register event buses. TODO: Move this to a custom event handler?
		MinecraftForge.EVENT_BUS.register(itemAntimatter);
		MinecraftForge.EVENT_BUS.register(FulminationHandler.INSTANCE);

		// Adde schematics to the creative builder.
		BlockCreativeBuilder.registerSchematic(new SchematicAccelerator());
		BlockCreativeBuilder.registerSchematic(new SchematicBreedingReactor());
		BlockCreativeBuilder.registerSchematic(new SchematicFissionReactor());
		BlockCreativeBuilder.registerSchematic(new SchematicFusionReactor());

		/** Cell registry. */
		if (ConfigurationManager.General.allowOreDictionaryCompatibility) {
			OreDictionary.registerOre("ingotUranium", itemUranium);
			OreDictionary.registerOre("dustUranium", itemYellowCake);
		}

		OreDictionary.registerOre("oreUranium", new ItemStack(blockUraniumOre));
		OreDictionary.registerOre("breederUranium", new ItemStack(itemUranium, 1, 1));
		OreDictionary.registerOre("blockRadioactiveGrass", blockRadioactiveGrass);
		OreDictionary.registerOre("cellEmpty", itemCell);
		OreDictionary.registerOre("cellUranium", itemFissileFuel);
		OreDictionary.registerOre("cellTritium", itemTritiumCell);
		OreDictionary.registerOre("cellDeuterium", itemDeuteriumCell);
		OreDictionary.registerOre("cellWater", itemWaterCell);
		OreDictionary.registerOre("darkmatter", itemDarkMatter);
		OreDictionary.registerOre("antimatterMilligram", new ItemStack(itemAntimatter, 1, 0));
		OreDictionary.registerOre("antimatterGram", new ItemStack(itemAntimatter, 1, 1));

		ForgeChunkManager.setForcedChunkLoadingCallback(this, new ForgeChunkManager.LoadingCallback() {
			@Override
			public void ticketsLoaded(List<ForgeChunkManager.Ticket> tickets, World world) {
				for (ForgeChunkManager.Ticket ticket : tickets) {
					if (ticket.getType() == ForgeChunkManager.Type.ENTITY) {
						if (ticket.getEntity() != null) {
							if (ticket.getEntity() instanceof EntityParticle) {
								((EntityParticle) ticket.getEntity()).updateTicket = ticket;
							}
						}
					}
				}
			}
		});
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		if (!UpdateTicker.getInstance().isAlive()) {
			UpdateTicker.getInstance().start();
		}

		// Register grids.
		UpdateTicker.addNetwork(thermalGrid);
	}

	private void registerFluids() {
		// Register fluids.
		FluidRegistry.registerFluid(fluidDeuterium);
		FluidRegistry.registerFluid(fluidUraniumHexaflouride);
		FluidRegistry.registerFluid(fluidPlasma);
		FluidRegistry.registerFluid(fluidSteam);
		FluidRegistry.registerFluid(fluidTritium);
		FluidRegistry.registerFluid(fluidToxicWaste);

		fluidStackDeuterium = new FluidStack(fluidDeuterium, 0);
		fluidStackUraniumHexaflouride = new FluidStack(fluidUraniumHexaflouride, 0);
		fluidStackSteam = new FluidStack(fluidSteam, 0);
		fluidStackTritium = new FluidStack(fluidTritium, 0);
		fluidStackToxicWaste = new FluidStack(fluidToxicWaste, 0);
		fluidStackWater = new FluidStack(FluidRegistry.WATER, 0);
	}

	private void registerBlocks() {
		// Register blocks.
		blockAccelerator = new BlockAccelerator();
		blockChemicalExtractor = new BlockChemicalExtractor();
		blockCentrifuge = new BlockCentrifuge();
		blockControlRod = new BlockControlRod();
		blockElectricTurbine = new BlockElectricTurbine();

		blockElectromagnet = new TileElectromagnet();
		blockElectromagnet.block = new BlockDummy(Reference.DOMAIN, Quantum.getCreativeTab(), blockElectromagnet);

		blockFulmination = new TileFulmination();
		blockFulmination.block = new BlockDummy(Reference.DOMAIN, Quantum.getCreativeTab(), blockFulmination);

		blockFusionReactor = new BlockFusionReactor();
		blockGasFunnel = new BlockGasFunnel();
		blockNuclearBoiler = new BlockNuclearBoiler();
		blockSiren = new BlockSiren();
		blockThermometer = new BlockThermometer();
		blockUraniumOre = new BlockUraniumOre();
		blockPlasma = new BlockPlasma();
		fluidPlasma.setBlock(blockPlasma);
		blockQuantumAssembler = new BlockQuantumAssembler();
		blockRadioactiveGrass = new BlockRadioactiveGrass();
		blockReactorCell = new BlockReactorCell();
		blockToxicWaste = new BlockToxicWaste();

		blockCreativeBuilder = new BlockCreativeBuilder();

		GameRegistry.registerBlock(blockAccelerator, "blockAccelerator;");
		GameRegistry.registerBlock(blockChemicalExtractor, "blockChemicalExtractor");
		GameRegistry.registerBlock(blockCentrifuge, "blockCentrifuge");
		GameRegistry.registerBlock(blockControlRod, "blockControlRod");
		GameRegistry.registerBlock(blockElectricTurbine, "blockElectricTurbine");
		GameRegistry.registerBlock(blockElectromagnet.block, "blockElectromagnet");
		GameRegistry.registerBlock(blockFulmination.block, "blockFulmination");
		GameRegistry.registerBlock(blockFusionReactor, "blockFusionReactor");
		GameRegistry.registerBlock(blockGasFunnel, "blockGasFunnel");
		GameRegistry.registerBlock(blockNuclearBoiler, "blockNuclearBoiler");
		GameRegistry.registerBlock(blockSiren, "blockSiren");
		GameRegistry.registerBlock(blockThermometer, "blockThermometer");
		GameRegistry.registerBlock(blockUraniumOre, "blockUraniumOre");
		GameRegistry.registerBlock(blockPlasma, "blockPlasma");
		GameRegistry.registerBlock(blockQuantumAssembler, "blockQuantumAssembler");
		GameRegistry.registerBlock(blockRadioactiveGrass, "blockRadioactiveGrass");
		GameRegistry.registerBlock(blockReactorCell, "blockReactorCell");
		GameRegistry.registerBlock(blockToxicWaste, "blockToxicWaste");

		GameRegistry.registerBlock(blockCreativeBuilder, "blockCreativeBuilder");
	}

	private void registerTileEntities() {
		// Register tile entities.
		GameRegistry.registerTileEntity(TileAccelerator.class, "tileAccelerator");
		GameRegistry.registerTileEntity(TileChemicalExtractor.class, "tileChemicalExtractor");
		GameRegistry.registerTileEntity(TileCentrifuge.class, "tileCentrifuge");
		GameRegistry.registerTileEntity(TileElectricTurbine.class, "tileElectricTurbine");
		GameRegistry.registerTileEntity(TileElectromagnet.class, "tileElectromagnet");
		GameRegistry.registerTileEntity(TileFulmination.class, "tileFulmination");
		GameRegistry.registerTileEntity(TileGasFunnel.class, "tileGasFunnel");
		GameRegistry.registerTileEntity(TileNuclearBoiler.class, "tileNuclearBoiler");
		GameRegistry.registerTileEntity(TileSiren.class, "tileSiren");
		GameRegistry.registerTileEntity(TileThermometer.class, "tileThermometer");
		GameRegistry.registerTileEntity(TilePlasma.class, "tilePlasma");
		GameRegistry.registerTileEntity(TileQuantumAssembler.class, "tileQuantumAssembler");
		GameRegistry.registerTileEntity(TileFusionReactor.class, "tileFusionCore");
		GameRegistry.registerTileEntity(TileReactorCell.class, "tileReactorCell");
	}

	private void registerTileEntitySpecialRenders() {
		// Register special renderers.
		ClientRegistry.bindTileEntitySpecialRenderer(TileChemicalExtractor.class, new RenderChemicalExtractor());
		ClientRegistry.bindTileEntitySpecialRenderer(TileCentrifuge.class, new RenderCentrifuge());
		ClientRegistry.bindTileEntitySpecialRenderer(TileElectricTurbine.class, new RenderElectricTurbine());
		ClientRegistry.bindTileEntitySpecialRenderer(TileNuclearBoiler.class, new RenderNuclearBoiler());
		ClientRegistry.bindTileEntitySpecialRenderer(TileThermometer.class, new RenderThermometer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileQuantumAssembler.class, new RenderQuantumAssembler());
		ClientRegistry.bindTileEntitySpecialRenderer(TileFusionReactor.class, new RenderFusionReactor());
		ClientRegistry.bindTileEntitySpecialRenderer(TileReactorCell.class, new RenderReactorCell());
	}

	private void registerItems() {
		// Register items.
		// Cells
		itemAntimatter = new ItemAntimatter();
		itemBreedingRod = new ItemBreederFuel();
		itemCell = new ItemCell("cellEmpty");
		itemDarkMatter = new ItemCell("darkMatter");
		itemDeuteriumCell = new ItemCell("cellDeuterium");
		itemFissileFuel = new ItemFissileFuel();
		itemTritiumCell = new ItemCell("cellTritium");
		itemWaterCell = new ItemCell("cellWater");

		GameRegistry.registerItem(itemAntimatter, "antimatter");
		GameRegistry.registerItem(itemBreedingRod, "rodBreedingFuel");
		GameRegistry.registerItem(itemCell, "cellEmpty");
		GameRegistry.registerItem(itemDarkMatter, "darkMatter");
		GameRegistry.registerItem(itemDeuteriumCell, "cellDeuterium");
		GameRegistry.registerItem(itemFissileFuel, "rodFissileFuel");
		GameRegistry.registerItem(itemTritiumCell, "cellTritium");
		GameRegistry.registerItem(itemWaterCell, "cellWater");

		// Buckets
		itemBucketToxicWaste = new ItemBucketToxicWaste();

		GameRegistry.registerItem(itemBucketToxicWaste, "bucketToxicWaste");

		// Uranium
		itemUranium = new ItemUranium();
		itemYellowCake = new ItemRadioactive("yellowcake");

		GameRegistry.registerItem(itemUranium, "uranium");
		GameRegistry.registerItem(itemYellowCake, "yellowcake");

		// Hazmat
		itemHazmatMask = new ItemArmorHazmat("hazmatMask", 0);
		itemHazmatBody = new ItemArmorHazmat("hazmatBody", 1);
		itemHazmatLeggings = new ItemArmorHazmat("hazmatLeggings", 2);
		itemHazmatBoots = new ItemArmorHazmat("hazmatBoots", 3);

		GameRegistry.registerItem(itemHazmatMask, "itemHazmatMask");
		GameRegistry.registerItem(itemHazmatBody, "itemHazmatBody");
		GameRegistry.registerItem(itemHazmatLeggings, "itemHazmatLeggings");
		GameRegistry.registerItem(itemHazmatBoots, "itemHazmatBoots");

		// Debug
		GameRegistry.registerItem(new ItemScrewdriver(), "itemScrewdriver");
	}

	private void registerFluidContainers() {
		// Register fluid containers.
		FluidContainerRegistry.registerFluidContainer(new FluidStack(FluidRegistry.getFluid("deuterium"), 200), new ItemStack(itemDeuteriumCell), new ItemStack(itemCell));
		FluidContainerRegistry.registerFluidContainer(new FluidStack(FluidRegistry.getFluid("tritium"), 200), new ItemStack(itemTritiumCell), new ItemStack(itemCell));
		FluidContainerRegistry.registerFluidContainer(FluidRegistry.getFluid("toxicwaste"), new ItemStack(itemBucketToxicWaste), new ItemStack(Items.bucket));
		FluidContainerRegistry.registerFluidContainer(FluidRegistry.WATER, new ItemStack(itemWaterCell), new ItemStack(itemCell));
	}

	private void registerEntities() {
		// Register entities.
		EntityRegistry.registerModEntity(EntityParticle.class, "Particle", 0, this, 80, 3, true);
	}

	private void registerRecipes() {

	}

	@SubscribeEvent
	public void fillBucketEvent(FillBucketEvent event) {
		if (!event.world.isRemote && event.target != null && event.target.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
			VectorWorld pos = new VectorWorld(event.world, event.target);

			if (pos.getBlock() == blockToxicWaste) {
				pos.setBlock(Blocks.air);

				event.result = new ItemStack(itemBucketToxicWaste);
				event.setResult(Event.Result.ALLOW);
			}
		}
	}

	@SubscribeEvent
	public void onPlasmaSpawnEvent(PlasmaEvent.PlasmaSpawnEvent event) {
		Vector3 position = new Vector3(event.x, event.y, event.z);
		Block block = position.getBlock(event.world);

		if (block != null) {
			TileEntity tile = position.getTileEntity(event.world);

			if (block == Blocks.bedrock || block == Blocks.iron_block) {
				return;
			}

			if (tile instanceof TilePlasma) {
				((TilePlasma) tile).setTemperature(event.temperature);

				return;
			}

			if (tile instanceof IElectromagnet) {
				return;
			}
		}

		position.setBlock(event.world, blockPlasma);

		TileEntity tile = position.getTileEntity(event.world);

		if (tile instanceof TilePlasma) {
			((TilePlasma) tile).setTemperature(event.temperature);
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void preTextureHook(TextureStitchEvent.Pre event) {
		if (event.map.getTextureType() == 0) {
			RenderUtility.registerIcon(Reference.PREFIX + "atomic_edge", event.map);
		}
	}

	public static Quantum getInstance() {
		return instance;
	}

	public static CommonProxy getProxy() {
		return proxy;
	}

	public static Logger getLogger() {
		return logger;
	}

	public static QuantumCreativeTab getCreativeTab() {
		return creativeTab;
	}

	public static Configuration getConfiguration() {
		return configuration;
	}

	@Override
	public String getModId() {
		return Reference.ID;
	}

	@Override
	public String getModName() {
		return Reference.NAME;
	}

	@Override
	public String getModVersion() {
		return Reference.VERSION;
	}
}