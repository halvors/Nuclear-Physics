package org.halvors.nuclearphysics.common.init;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.IForgeRegistry;
import net.minecraftforge.oredict.OreDictionary;
import org.halvors.nuclearphysics.common.Reference;
import org.halvors.nuclearphysics.common.block.BlockNuclearPhysics;
import org.halvors.nuclearphysics.common.block.BlockRadioactiveGrass;
import org.halvors.nuclearphysics.common.block.BlockUraniumOre;
import org.halvors.nuclearphysics.common.block.debug.BlockCreativeBuilder;
import org.halvors.nuclearphysics.common.block.machine.BlockMachine;
import org.halvors.nuclearphysics.common.block.machine.BlockMachine.EnumMachine;
import org.halvors.nuclearphysics.common.block.machine.BlockMachineModel;
import org.halvors.nuclearphysics.common.block.machine.BlockMachineModel.EnumMachineModel;
import org.halvors.nuclearphysics.common.block.particle.BlockFulmination;
import org.halvors.nuclearphysics.common.block.reactor.BlockElectricTurbine;
import org.halvors.nuclearphysics.common.block.reactor.BlockGasFunnel;
import org.halvors.nuclearphysics.common.block.reactor.fission.BlockControlRod;
import org.halvors.nuclearphysics.common.block.reactor.fission.BlockReactorCell;
import org.halvors.nuclearphysics.common.block.reactor.fission.BlockSiren;
import org.halvors.nuclearphysics.common.block.reactor.fission.BlockThermometer;
import org.halvors.nuclearphysics.common.block.reactor.fusion.BlockElectromagnet;
import org.halvors.nuclearphysics.common.item.block.ItemBlockMetadata;
import org.halvors.nuclearphysics.common.item.block.ItemBlockThermometer;
import org.halvors.nuclearphysics.common.item.block.ItemBlockTooltip;
import org.halvors.nuclearphysics.common.tile.particle.TileFulmination;
import org.halvors.nuclearphysics.common.tile.reactor.TileElectricTurbine;
import org.halvors.nuclearphysics.common.tile.reactor.TileGasFunnel;
import org.halvors.nuclearphysics.common.tile.reactor.fission.TileReactorCell;
import org.halvors.nuclearphysics.common.tile.reactor.fission.TileSiren;
import org.halvors.nuclearphysics.common.tile.reactor.fission.TileThermometer;
import org.halvors.nuclearphysics.common.tile.reactor.fusion.TileElectromagnet;
import org.halvors.nuclearphysics.common.tile.reactor.fusion.TilePlasma;

import java.util.HashSet;
import java.util.Set;

public class ModBlocks {
    public static Block blockControlRod = new BlockControlRod();
    public static Block blockElectricTurbine = new BlockElectricTurbine();
    public static Block blockElectromagnet = new BlockElectromagnet();
    public static Block blockFulmination = new BlockFulmination();
    public static Block blockGasFunnel = new BlockGasFunnel();
    public static Block blockMachine = new BlockMachine();
    public static Block blockMachineModel = new BlockMachineModel();
    public static Block blockSiren = new BlockSiren();
    public static Block blockThermometer = new BlockThermometer();
    public static Block blockUraniumOre = new BlockUraniumOre();
    public static Block blockRadioactiveGrass = new BlockRadioactiveGrass();
    public static Block blockReactorCell = new BlockReactorCell();

    public static Block blockCreativeBuilder = new BlockCreativeBuilder();

    @EventBusSubscriber
    public static class RegistrationHandler {
        public static final Set<ItemBlock> ITEM_BLOCKS = new HashSet<>();

        /**
         * Register this mod's {@link Block}s.
         *
         * @param event The event
         */
        @SubscribeEvent
        public static void registerBlocks(final Register<Block> event) {
            final IForgeRegistry<Block> registry = event.getRegistry();

            final Block[] blocks = {
                    blockControlRod,
                    blockElectricTurbine,
                    blockElectromagnet,
                    blockFulmination,
                    blockGasFunnel,
                    blockMachine,
                    blockMachineModel,
                    blockSiren,
                    blockThermometer,
                    blockUraniumOre,
                    blockRadioactiveGrass,
                    blockReactorCell,
                    blockCreativeBuilder
            };

            registry.registerAll(blocks);
        }

        /**
         * Register this mod's {@link ItemBlock}s.
         *
         * @param event The event
         */
        @SubscribeEvent
        public static void registerItemBlocks(final Register<Item> event) {
            final ItemBlock[] items = {
                    new ItemBlockTooltip(blockControlRod),
                    new ItemBlockTooltip(blockElectricTurbine),
                    new ItemBlockMetadata(blockElectromagnet),
                    new ItemBlockTooltip(blockFulmination),
                    new ItemBlockTooltip(blockGasFunnel),
                    new ItemBlockMetadata(blockMachine),
                    new ItemBlockMetadata(blockMachineModel),
                    new ItemBlockTooltip(blockSiren),
                    new ItemBlockThermometer(blockThermometer),
                    new ItemBlockTooltip(blockUraniumOre),
                    new ItemBlockTooltip(blockRadioactiveGrass),
                    new ItemBlockTooltip(blockReactorCell),
                    new ItemBlockTooltip(blockCreativeBuilder)
            };

            final IForgeRegistry<Item> registry = event.getRegistry();

            for (final ItemBlock item : items) {
                final BlockNuclearPhysics block = (BlockNuclearPhysics) item.getBlock();
                //final ResourceLocation registryName = Preconditions.checkNotNull(block.getRegistryName(), "Block %s has null registry name", block);
                //registry.register(item.setRegistryName(registryName));
                registry.register(item);

                block.registerItemModel(item);
                block.registerBlockModel();

                ITEM_BLOCKS.add(item);
            }

            registerTileEntities();

            OreDictionary.registerOre("oreUranium", blockUraniumOre);
            OreDictionary.registerOre("blockRadioactiveGrass", blockRadioactiveGrass);
        }
    }

    private static void registerTileEntities() {
        for (EnumMachine type : EnumMachine.values()) {
            registerTile(type.getTileClass());
        }

        for (EnumMachineModel type : EnumMachineModel.values()) {
            registerTile(type.getTileClass());
        }

        registerTile(TileElectricTurbine.class);
        registerTile(TileElectromagnet.class);
        registerTile(TileFulmination.class);
        registerTile(TileGasFunnel.class);
        registerTile(TileSiren.class);
        registerTile(TileThermometer.class);
        registerTile(TilePlasma.class);
        registerTile(TileReactorCell.class);
    }

    private static void registerTile(Class<? extends TileEntity> tileClass) {
        String name = tileClass.getSimpleName().replaceAll("(.)(\\p{Lu})", "$1_$2").toLowerCase();

        GameRegistry.registerTileEntity(tileClass, Reference.PREFIX + name);
    }
}
