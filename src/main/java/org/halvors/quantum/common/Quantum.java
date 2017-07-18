package org.halvors.quantum.common;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.halvors.quantum.common.ConfigurationManager.Integration;
import org.halvors.quantum.common.block.debug.BlockCreativeBuilder;
import org.halvors.quantum.common.entity.particle.EntityParticle;
import org.halvors.quantum.common.event.ExplosionEventHandler;
import org.halvors.quantum.common.event.PlayerEventHandler;
import org.halvors.quantum.common.event.ThermalEventHandler;
import org.halvors.quantum.common.grid.UpdateTicker;
import org.halvors.quantum.common.network.PacketHandler;
import org.halvors.quantum.common.schematic.SchematicAccelerator;
import org.halvors.quantum.common.schematic.SchematicBreedingReactor;
import org.halvors.quantum.common.schematic.SchematicFissionReactor;
import org.halvors.quantum.common.schematic.SchematicFusionReactor;
import org.halvors.quantum.common.grid.thermal.ThermalGrid;
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
		FluidRegistry.enableUniversalBucket();
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
		//QuantumFluids.register();
		QuantumBlocks.register();
		QuantumItems.register();
		registerEntities();
		registerRecipes();

		// Calling proxy handler.
		proxy.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		// Register event handlers.
		MinecraftForge.EVENT_BUS.register(this);

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
		OreDictionary.registerOre("cellWater.json", QuantumItems.itemWaterCell);
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

	private void registerEntities() {
		// Register entities.
		EntityRegistry.registerModEntity(EntityParticle.class, "Particle", 0, this, 80, 3, true);
	}

	private void registerRecipes() {
		// Register recipes.

		// Cells
		// Antimatter
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(QuantumItems.itemAntimatterCell, 1, 1), QuantumItems.itemAntimatterCell, QuantumItems.itemAntimatterCell, QuantumItems.itemAntimatterCell, QuantumItems.itemAntimatterCell, QuantumItems.itemAntimatterCell, QuantumItems.itemAntimatterCell, QuantumItems.itemAntimatterCell, QuantumItems.itemAntimatterCell));
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(QuantumItems.itemAntimatterCell, 8, 0), new ItemStack(QuantumItems.itemAntimatterCell, 1, 1)));

		// Breeder Fuel Rod
		GameRegistry.addRecipe(new ShapedOreRecipe(QuantumItems.itemBreederFuel, "CUC", "CUC", "CUC", 'U', "breederUranium", 'C', "cellEmpty"));

		// Empty Cell
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(QuantumItems.itemCell, 16), " T ", "TGT", " T ", 'T', "ingotTin", 'G', Blocks.GLASS));

		// Fissile Fuel
		GameRegistry.addRecipe(new ShapedOreRecipe(QuantumItems.itemFissileFuel, "CUC", "CUC", "CUC", 'U', "ingotUranium", 'C', "cellEmpty"));

		// Water Cell
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(QuantumItems.itemWaterCell), "cellEmpty", Items.WATER_BUCKET));

		// Hazmat
		//GameRegistry.addRecipe(new ShapedOreRecipe(itemHazmatMask, "SSS", "BAB", "SCS", 'A', Items.leather_helmet, 'C', UniversalRecipe.CIRCUIT_T1.get(Settings.allowAlternateRecipes), 'S', Blocks.cloth }));
		//GameRegistry.addRecipe(new ShapedOreRecipe(itemHazmatBody, "SSS", "BAB", "SCS", 'A', Items.leather_plate, 'C', UniversalRecipe.CIRCUIT_T1.get(Settings.allowAlternateRecipes), 'S', Block.cloth }));
		//GameRegistry.addRecipe(new ShapedOreRecipe(itemHazmatLeggings, "SSS", "BAB", "SCS", 'A', Items.leather_legs, 'C', UniversalRecipe.CIRCUIT_T1.get(Settings.allowAlternateRecipes), 'S', Block.cloth }));
		//GameRegistry.addRecipe(new ShapedOreRecipe(itemHazmatBoots, "SSS", "BAB", "SCS", 'A', Items.leather_boots, 'C', UniversalRecipe.CIRCUIT_T1.get(Settings.allowAlternateRecipes), 'S', Block.cloth }));

		// Particle Accelerator
		//GameRegistry.addRecipe(new ShapedOreRecipe(blockAccelerator, "SCS", "CMC", "SCS", 'M', UniversalRecipe.MOTOR.get(Settings.allowAlternateRecipes), 'C', UniversalRecipe.CIRCUIT_T3.get(Settings.allowAlternateRecipes), 'S', UniversalRecipe.PRIMARY_PLATE.get(Settings.allowAlternateRecipes)));

		// Chemical Extractor
		//GameRegistry.addRecipe(new ShapedOreRecipe(blockChemicalExtractor, "BSB", "MCM", "BSB", 'C', UniversalRecipe.CIRCUIT_T3.get(Settings.allowAlternateRecipes), 'S', UniversalRecipe.PRIMARY_PLATE.get(Settings.allowAlternateRecipes), 'B', UniversalRecipe.SECONDARY_METAL.get(Settings.allowAlternateRecipes), 'M', UniversalRecipe.MOTOR.get(Settings.allowAlternateRecipes)));

		// Control Rod
		GameRegistry.addRecipe(new ShapedOreRecipe(QuantumBlocks.blockControlRod, "I", "I", "I", 'I', Items.IRON_INGOT));

		// Turbine
		//GameRegistry.addRecipe(new ShapedOreRecipe(blockElectricTurbine, " B ", "BMB", " B ", 'B', UniversalRecipe.SECONDARY_PLATE.get(Settings.allowAlternateRecipes), 'M', UniversalRecipe.MOTOR.get(Settings.allowAlternateRecipes)));

		// Electromagnet
		//GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockElectromagnet, 2, 0), "BBB", "BMB", "BBB", 'B', UniversalRecipe.SECONDARY_METAL.get(Settings.allowAlternateRecipes), 'M', UniversalRecipe.MOTOR.get(Settings.allowAlternateRecipes)));

		// Electromagnet Glass
		GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(QuantumBlocks.blockElectromagnet, 1, 1), QuantumBlocks.blockElectromagnet, Blocks.GLASS));

		// Fulmination Generator
		//GameRegistry.addRecipe(new ShapedOreRecipe(blockFulmination, "OSO", "SCS", "OSO", 'O', Blocks.obsidian, 'C', UniversalRecipe.CIRCUIT_T2.get(Settings.allowAlternateRecipes), 'S', UniversalRecipe.PRIMARY_PLATE.get(Settings.allowAlternateRecipes)));

		// Gas Centrifuge
		//GameRegistry.addRecipe(new ShapedOreRecipe(blockGasCentrifuge, "BSB", "MCM", "BSB", 'C', UniversalRecipe.CIRCUIT_T2.get(Settings.allowAlternateRecipes), 'S', UniversalRecipe.PRIMARY_PLATE.get(Settings.allowAlternateRecipes), 'B', UniversalRecipe.SECONDARY_METAL.get(Settings.allowAlternateRecipes), 'M', UniversalRecipe.MOTOR.get(Settings.allowAlternateRecipes)));

		// Gas Funnel
		//GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockGasFunnel, 2), " B ", "B B", "B B", 'B', UniversalRecipe.SECONDARY_METAL.get(Settings.allowAlternateRecipes)));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(QuantumBlocks.blockGasFunnel, 2), " B ", "B B", "B B", 'B', "ingotIron"));

		// Nuclear Boiler
		//GameRegistry.addRecipe(new ShapedOreRecipe(blockNuclearBoiler, "S S", "FBF", "SMS", 'F', Block.furnaceIdle, 'S', UniversalRecipe.PRIMARY_PLATE.get(Settings.allowAlternateRecipes), 'B', Item.bucketEmpty, 'M', UniversalRecipe.MOTOR.get(Settings.allowAlternateRecipes)));

		// Siren
		//GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(blockSiren, 2), "NPN", 'N', Blocks.noteblock, 'P', UniversalRecipe.SECONDARY_PLATE.get(Settings.allowAlternateRecipes)));

		// Thermometer
		//GameRegistry.addRecipe(new ShapedOreRecipe(blockThermometer, "SSS", "GCG", "GSG", 'S', UniversalRecipe.PRIMARY_METAL.get(Settings.allowAlternateRecipes), 'G', Blocks.glass, 'C', UniversalRecipe.CIRCUIT_T1.get(Settings.allowAlternateRecipes)));

		// Plasma Heater
		//GameRegistry.addRecipe(new ShapedOreRecipe(blockPlasmaHeater, "CPC", "PFP", "CPC", 'P', UniversalRecipe.PRIMARY_PLATE.get(Settings.allowAlternateRecipes), 'F', blockReactorCell, 'C', UniversalRecipe.CIRCUIT_T3.get(Settings.allowAlternateRecipes)));

		// Quantum Assembler
		//GameRegistry.addRecipe(new ShapedOreRecipe(blockQuantumAssembler, "CCC", "SXS", "SSS", 'X', blockGasCentrifuge, 'C', UniversalRecipe.CIRCUIT_T3.get(Settings.allowAlternateRecipes), 'S', UniversalRecipe.PRIMARY_PLATE.get(Settings.allowAlternateRecipes)));

		// Fission Reactor
		//GameRegistry.addRecipe(new ShapedOreRecipe(blockReactorCell, "SCS", "MEM", "SCS", 'E', "cellEmpty", 'C', UniversalRecipe.CIRCUIT_T2.get(Settings.allowAlternateRecipes), 'S', UniversalRecipe.PRIMARY_PLATE.get(Settings.allowAlternateRecipes), 'M', UniversalRecipe.MOTOR.get(Settings.allowAlternateRecipes)));

		/*
		// IC2 Recipes
		if (Loader.isModLoaded("IC2") && Settings.allowAlternateRecipes)
		{
			OreDictionary.registerOre("cellEmpty", Items.getItem("cell"));

			// Check to make sure we have actually registered the Ore, otherwise tell the user about
			// it.
			String cellEmptyName = OreDictionary.getOreName(OreDictionary.getOreID("cellEmpty"));
			if (cellEmptyName == "Unknown")
			{
				ResonantInduction.LOGGER.info("Unable to register cellEmpty in OreDictionary!");
			}

			// IC2 exchangeable recipes
			GameRegistry.addRecipe(new ShapelessOreRecipe(itemYellowCake, Items.getItem("reactorUraniumSimple")));
			GameRegistry.addRecipe(new ShapelessOreRecipe(Items.getItem("cell"), itemCell));
			GameRegistry.addRecipe(new ShapelessOreRecipe(itemCell, "cellEmpty"));
		}
		*/
	}

	/*
	@SubscribeEvent
	public void onFillBucketEvent(FillBucketEvent event) {
		if (!event.getWorld().isRemote && event.getTarget() != null && event.getWorld().typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
			VectorWorld pos = new VectorWorld(event.getWorld(), event.getTarget());

			if (pos.getBlock() == blockToxicWaste) {
				pos.setBlock(Blocks.AIR);

				event.setFilledBucket(new ItemStack(itemBucketToxicWaste));
				event.setResult(Event.Result.ALLOW);
			}
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void preTextureHook(TextureStitchEvent.Pre event) {
		if (event.getMap().getTextureType() == 0) {
			RenderUtility.registerIcon(Reference.PREFIX + "atomic_edge", event.getMap());
			RenderUtility.registerIcon(Reference.PREFIX + "gasFunnel_edge", event.getMap());

			RenderUtility.registerIcon(Reference.PREFIX + "deuterium", event.getMap());
			RenderUtility.registerIcon(Reference.PREFIX + "steam", event.getMap());
			RenderUtility.registerIcon(Reference.PREFIX + "tritium", event.getMap());
			RenderUtility.registerIcon(Reference.PREFIX + "uraniumHexafluoride", event.getMap());
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void postTextureHook(TextureStitchEvent.Post event) {
		fluidToxicWaste.setIcons(blockToxicWaste.getIcon(0, 0));
		fluidPlasma.setIcons(blockPlasma.getIcon(0, 0));
		fluidDeuterium.setIcons(RenderUtility.loadedIconMap.get(Reference.PREFIX + "deuterium"));
		fluidSteam.setIcons(RenderUtility.loadedIconMap.get(Reference.PREFIX + "steam"));
		fluidTritium.setIcons(RenderUtility.loadedIconMap.get(Reference.PREFIX + "tritium"));
		fluidUraniumHexaflouride.setIcons(RenderUtility.loadedIconMap.get(Reference.PREFIX + "uraniumHexafluoride"));
	}
	*/

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