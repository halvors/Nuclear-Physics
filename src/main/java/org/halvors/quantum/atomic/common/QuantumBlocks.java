package org.halvors.quantum.atomic.common;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import org.halvors.quantum.atomic.common.block.BlockQuantum;
import org.halvors.quantum.atomic.common.block.BlockRadioactiveGrass;
import org.halvors.quantum.atomic.common.block.BlockUraniumOre;
import org.halvors.quantum.atomic.common.block.debug.BlockCreativeBuilder;
import org.halvors.quantum.atomic.common.block.machine.BlockMachine;
import org.halvors.quantum.atomic.common.block.machine.BlockMachine.EnumMachine;
import org.halvors.quantum.atomic.common.block.machine.BlockMachineModel;
import org.halvors.quantum.atomic.common.block.machine.BlockMachineModel.EnumMachineModel;
import org.halvors.quantum.atomic.common.block.particle.BlockFulmination;
import org.halvors.quantum.atomic.common.block.reactor.BlockElectricTurbine;
import org.halvors.quantum.atomic.common.block.reactor.fission.BlockControlRod;
import org.halvors.quantum.atomic.common.block.reactor.fission.BlockReactorCell;
import org.halvors.quantum.atomic.common.block.reactor.fission.BlockSiren;
import org.halvors.quantum.atomic.common.block.reactor.fission.BlockThermometer;
import org.halvors.quantum.atomic.common.block.reactor.fusion.BlockElectromagnet;
import org.halvors.quantum.atomic.common.block.reactor.fusion.BlockPlasma;
import org.halvors.quantum.atomic.common.item.block.ItemBlockMetadata;
import org.halvors.quantum.atomic.common.item.block.ItemBlockThermometer;
import org.halvors.quantum.atomic.common.tile.particle.TileFulmination;
import org.halvors.quantum.atomic.common.tile.reactor.TileElectricTurbine;
import org.halvors.quantum.atomic.common.tile.reactor.fission.TileReactorCell;
import org.halvors.quantum.atomic.common.tile.reactor.fission.TileSiren;
import org.halvors.quantum.atomic.common.tile.reactor.fission.TileThermometer;
import org.halvors.quantum.atomic.common.tile.reactor.fusion.TileElectromagnet;
import org.halvors.quantum.atomic.common.tile.reactor.fusion.TilePlasma;

public class QuantumBlocks {
    public static Block blockCreativeBuilder = new BlockCreativeBuilder();

    public static Block blockControlRod = new BlockControlRod();
    public static Block blockElectricTurbine = new BlockElectricTurbine();
    public static Block blockElectromagnet = new BlockElectromagnet();
    public static Block blockFulmination = new BlockFulmination();
    public static Block blockMachine = new BlockMachine();
    public static Block blockMachineModel = new BlockMachineModel();
    public static Block blockSiren = new BlockSiren();
    public static Block blockThermometer = new BlockThermometer();
    public static Block blockUraniumOre = new BlockUraniumOre();
    public static Block blockPlasma = new BlockPlasma();
    public static Block blockRadioactiveGrass = new BlockRadioactiveGrass();
    public static Block blockReactorCell = new BlockReactorCell();

    // Register blocks.
    public static void register() {
        // Basic Components
        register(blockCreativeBuilder);

        register(blockControlRod);
        register(blockElectricTurbine);
        register(blockElectromagnet, new ItemBlockMetadata(blockElectromagnet));
        register(blockFulmination);
        register(blockMachine, new ItemBlockMetadata(blockMachine));
        register(blockMachineModel, new ItemBlockMetadata(blockMachineModel));
        register(blockSiren);
        register(blockThermometer, new ItemBlockThermometer(blockThermometer));
        register(blockUraniumOre, "oreUranium");
        //register(blockPlasma);
        //QuantumFluids.plasma.setBlock(blockPlasma);
        register(blockRadioactiveGrass, "blockRadioactiveGrass");
        register(blockReactorCell);

        for (EnumMachine type : EnumMachine.values()) {
            GameRegistry.registerTileEntity(type.getTileClass(), getNameFromClass(type.getTileClass()));
        }

        for (EnumMachineModel type : EnumMachineModel.values()) {
            GameRegistry.registerTileEntity(type.getTileClass(), getNameFromClass(type.getTileClass()));
        }

        GameRegistry.registerTileEntity(TileElectricTurbine.class, "tile_electric_turbine");
        GameRegistry.registerTileEntity(TileElectromagnet.class, "tile_electromagnet");
        GameRegistry.registerTileEntity(TileFulmination.class, "tile_fulmination");
        GameRegistry.registerTileEntity(TileSiren.class, "tile_siren");
        GameRegistry.registerTileEntity(TileThermometer.class, "tile_thermometer");
        GameRegistry.registerTileEntity(TilePlasma.class, "tile_plasma");
        GameRegistry.registerTileEntity(TileReactorCell.class, "tile_reactor_cell");
    }

    private static String getNameFromClass(Class clazz) {
        return clazz.getSimpleName().replaceAll("(.)(\\p{Lu})", "$1_$2").toLowerCase();
    }

    private static <T extends Block> T register(T block, String name) {
        register(block);
        OreDictionary.registerOre(name, block);

        return block;
    }

    private static <T extends Block> T register(T block) {
        ItemBlock itemBlock = new ItemBlock(block);
        itemBlock.setRegistryName(block.getRegistryName());

        return register(block, itemBlock);
    }

    private static <T extends Block> T register(T block, ItemBlock itemBlock) {
        GameRegistry.register(block);
        GameRegistry.register(itemBlock);

        if (block instanceof BlockQuantum) {
            ((BlockQuantum) block).registerItemModel(itemBlock);
        }

        return block;
    }
}
