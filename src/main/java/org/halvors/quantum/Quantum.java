package org.halvors.quantum;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.*;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.halvors.quantum.client.render.machine.RenderCentrifuge;
import org.halvors.quantum.client.render.reactor.RenderElectricTurbine;
import org.halvors.quantum.client.render.reactor.RenderReactorCell;
import org.halvors.quantum.common.CommonProxy;
import org.halvors.quantum.common.ConfigurationManager;
import org.halvors.quantum.common.ConfigurationManager.Integration;
import org.halvors.quantum.common.QuantumCreativeTab;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.base.IUpdatableMod;
import org.halvors.quantum.common.block.*;
import org.halvors.quantum.common.block.machine.BlockCentrifuge;
import org.halvors.quantum.common.block.reactor.BlockElectricTurbine;
import org.halvors.quantum.common.event.PlayerEventHandler;
import org.halvors.quantum.common.item.*;
import org.halvors.quantum.common.item.armor.ItemArmorHazmat;
import org.halvors.quantum.common.block.reactor.BlockControlRod;
import org.halvors.quantum.common.tile.machine.TileCentrifuge;
import org.halvors.quantum.common.tile.reactor.TileElectricTurbine;
import org.halvors.quantum.common.tile.machine.TileEntityElectricityMeter;
import org.halvors.quantum.common.tile.reactor.TilePlasma;
import org.halvors.quantum.common.tile.reactor.TileReactorCell;
import org.halvors.quantum.common.transform.vector.VectorWorld;
import org.halvors.quantum.common.updater.UpdateManager;
import org.halvors.quantum.lib.tile.BlockDummy;
import org.halvors.quantum.lib.tile.TileBlock;

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
	public static Quantum instance;

	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide = "org.halvors." + Reference.ID + ".client.ClientProxy", serverSide = "org.halvors." + Reference.ID + ".common.CommonProxy")
	public static CommonProxy proxy;

	// Logger
	private static final Logger logger = LogManager.getLogger(Reference.ID);

	// ConfigurationManager
	private static Configuration configuration;

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
	public static final Block blockMachine = new BlockMachine();

	public static Block blockCentrifuge;
	public static Block blockControlRod;
	public static Block blockElectromagnet;
	public static Block blockUraniumOre;
	public static TileBlock blockPlasma;
	public static Block blockRadioactiveGrass;
	public static TileBlock blockReactorCell;
	public static BlockFluidClassic blockToxicWaste;
	public static Block blockElectricTurbine;

	//blockNuclearBoiler = contentRegistry.createTile(BlockNuclearBoiler.class, TileNuclearBoiler.class);
	//blockChemicalExtractor = contentRegistry.createTile(BlockChemicalExtractor.class, TileChemicalExtractor.class);
	//blockFusionCore = contentRegistry.createTile(BlockPlasmaHeater.class, TilePlasmaHeater.class);
	//blockThermometer = contentRegistry.newBlock(TileThermometer.class);
	//public static final Block blockPlasma = new TilePlasma();
	//blockSiren = contentRegistry.newBlock(TileSiren.class);
	//blockSteamFunnel = contentRegistry.newBlock(TileFunnel.class);
	//blockAccelerator = contentRegistry.createTile(BlockAccelerator.class, TileAccelerator.class);
	//blockFulmination = contentRegistry.newBlock(TileFulmination.class);
	//blockQuantumAssembler = contentRegistry.newBlock(TileQuantumAssembler.class);

	// Items
	//public static final ItemQuantum itemMultimeter = new ItemMultimeter();

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
		// Register the our EventHandler.
		FMLCommonHandler.instance().bus().register(new PlayerEventHandler());

		// Register the proxy as our GuiHandler to NetworkRegistry.
		NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);

		// Register event bus.
		MinecraftForge.EVENT_BUS.register(this);

		// Call functions for adding blocks, items, etc.
		registerFluids();
		registerBlocks();
		registerItems();
		registerFluidContainers();
		registerTileEntities();
		registerRecipes();
	}

	private void registerFluids() {
		// Register fluids.
		FluidRegistry.registerFluid(fluidDeuterium);
		FluidRegistry.registerFluid(fluidUraniumHexaflouride);
		FluidRegistry.registerFluid(fluidPlasma);
		FluidRegistry.registerFluid(fluidSteam);
		FluidRegistry.registerFluid(fluidTritium);
		FluidRegistry.registerFluid(fluidToxicWaste);

		fluidStackDeuterium = new FluidStack(FluidRegistry.getFluid("deuterium"), 0);
		fluidStackUraniumHexaflouride = new FluidStack(fluidUraniumHexaflouride, 0);
		fluidStackSteam = new FluidStack(FluidRegistry.getFluid("steam"), 0);
		fluidStackTritium = new FluidStack(FluidRegistry.getFluid("tritium"), 0);
		fluidStackToxicWaste = new FluidStack(FluidRegistry.getFluid("toxicwaste"), 0);
		fluidStackWater = new FluidStack(FluidRegistry.WATER, 0);
		fluidStackWater = new FluidStack(FluidRegistry.WATER, 0);
	}

	private void registerBlocks() {
		// Register blocks.
		GameRegistry.registerBlock(blockMachine, ItemBlockMachine.class, "blockMachine");

		blockCentrifuge = new BlockCentrifuge();
		blockControlRod = new BlockControlRod();
		blockElectromagnet = new BlockElectromagnet().setCreativeTab(Quantum.getCreativeTab());
		blockUraniumOre = new BlockUraniumOre();

		blockPlasma = new TilePlasma();
		blockPlasma.block = new BlockDummy(Reference.DOMAIN, Quantum.getCreativeTab(), blockPlasma);

		blockRadioactiveGrass = new BlockRadioactiveGrass();

		blockReactorCell = new TileReactorCell();
		blockReactorCell.block = new BlockDummy(Reference.DOMAIN, Quantum.getCreativeTab(), blockReactorCell);

		blockToxicWaste = new BlockToxicWaste();
		blockElectricTurbine = new BlockElectricTurbine();

		GameRegistry.registerBlock(blockCentrifuge, "blockCentrifuge");
		GameRegistry.registerBlock(blockControlRod, "blockControlRod");
		GameRegistry.registerBlock(blockElectromagnet, "blockElectromagnet");
		GameRegistry.registerBlock(blockUraniumOre, "blockUraniumOre");
		GameRegistry.registerBlock(blockPlasma.getBlockType(), "blockPlasma");
		GameRegistry.registerBlock(blockRadioactiveGrass, "blockRadioactiveGrass");
		GameRegistry.registerBlock(blockReactorCell.getBlockType(), "blockReactorCell");
		GameRegistry.registerBlock(blockToxicWaste, "blockToxicWaste");
		GameRegistry.registerBlock(blockElectricTurbine, "blockElectricTurbine");
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
		//fluidPlasma.setBlock(blockPlasma);
		FluidContainerRegistry.registerFluidContainer(new FluidStack(FluidRegistry.getFluid("deuterium"), 200), new ItemStack(itemDeuteriumCell), new ItemStack(itemCell));
		FluidContainerRegistry.registerFluidContainer(new FluidStack(FluidRegistry.getFluid("tritium"), 200), new ItemStack(itemTritiumCell), new ItemStack(itemCell));
		FluidContainerRegistry.registerFluidContainer(FluidRegistry.getFluid("toxicwaste"), new ItemStack(itemBucketToxicWaste), new ItemStack(Items.bucket));
		FluidContainerRegistry.registerFluidContainer(FluidRegistry.WATER, new ItemStack(itemWaterCell), new ItemStack(itemCell));
	}

	private void registerTileEntities() {
		// Register tile entities.
		GameRegistry.registerTileEntity(TileEntityElectricityMeter.class, "tileElectricityMeter");
		GameRegistry.registerTileEntity(TileCentrifuge.class, "tileCentrifuge");
		GameRegistry.registerTileEntity(TileElectricTurbine.class, "tileElectricTurbine");
		GameRegistry.registerTileEntity(TileReactorCell.class, "tileReactorCell");

		// Register special renderers.
		ClientRegistry.bindTileEntitySpecialRenderer(TileCentrifuge.class, new RenderCentrifuge());
		ClientRegistry.bindTileEntitySpecialRenderer(TileElectricTurbine.class, new RenderElectricTurbine());
		ClientRegistry.bindTileEntitySpecialRenderer(TileReactorCell.class, new RenderReactorCell());
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