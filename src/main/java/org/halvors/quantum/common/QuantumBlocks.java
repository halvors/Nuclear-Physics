package org.halvors.quantum.common;

import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import org.halvors.quantum.common.block.BlockQuantum;
import org.halvors.quantum.common.block.BlockRadioactiveGrass;
import org.halvors.quantum.common.block.BlockUraniumOre;
import org.halvors.quantum.common.block.debug.BlockCreativeBuilder;
import org.halvors.quantum.common.block.machine.BlockMachine;
import org.halvors.quantum.common.block.machine.BlockMachine.EnumMachine;
import org.halvors.quantum.common.block.machine.BlockMachineModel;
import org.halvors.quantum.common.block.machine.BlockMachineModel.EnumMachineModel;
import org.halvors.quantum.common.block.particle.BlockFulmination;
import org.halvors.quantum.common.block.reactor.BlockElectricTurbine;
import org.halvors.quantum.common.block.reactor.BlockGasFunnel;
import org.halvors.quantum.common.block.reactor.fission.BlockControlRod;
import org.halvors.quantum.common.block.reactor.fission.BlockReactorCell;
import org.halvors.quantum.common.block.reactor.fission.BlockSiren;
import org.halvors.quantum.common.block.reactor.fission.BlockThermometer;
import org.halvors.quantum.common.block.reactor.fusion.BlockElectromagnet;
import org.halvors.quantum.common.block.reactor.fusion.BlockPlasma;
import org.halvors.quantum.common.item.block.ItemBlockMetadata;
import org.halvors.quantum.common.item.block.ItemBlockThermometer;
import org.halvors.quantum.common.tile.particle.TileFulmination;
import org.halvors.quantum.common.tile.reactor.TileElectricTurbine;
import org.halvors.quantum.common.tile.reactor.TileGasFunnel;
import org.halvors.quantum.common.tile.reactor.fission.TileReactorCell;
import org.halvors.quantum.common.tile.reactor.fission.TileSiren;
import org.halvors.quantum.common.tile.reactor.fission.TileThermometer;
import org.halvors.quantum.common.tile.reactor.fusion.TileElectromagnet;
import org.halvors.quantum.common.tile.reactor.fusion.TilePlasma;

public class QuantumBlocks {
    public static BlockQuantum blockControlRod = new BlockControlRod();
    public static BlockQuantum blockElectricTurbine = new BlockElectricTurbine();
    public static BlockQuantum blockElectromagnet = new BlockElectromagnet();
    public static BlockQuantum blockFulmination = new BlockFulmination();
    public static BlockQuantum blockGasFunnel = new BlockGasFunnel();
    public static BlockQuantum blockMachine = new BlockMachine();
    public static BlockQuantum blockMachineModel = new BlockMachineModel();
    public static BlockQuantum blockSiren = new BlockSiren();
    public static BlockQuantum blockThermometer = new BlockThermometer();
    public static BlockQuantum blockUraniumOre = new BlockUraniumOre();
    public static BlockQuantum blockPlasma = new BlockPlasma();
    public static BlockQuantum blockRadioactiveGrass = new BlockRadioactiveGrass();
    public static BlockQuantum blockReactorCell = new BlockReactorCell();

    public static BlockQuantum blockCreativeBuilder = new BlockCreativeBuilder();

    // Register blocks.
    public static void register() {
        register(blockControlRod);
        register(blockElectricTurbine);
        register(blockElectromagnet, new ItemBlockMetadata(blockElectromagnet));
        register(blockFulmination);
        register(blockGasFunnel);
        register(blockMachine, new ItemBlockMetadata(blockMachine));
        register(blockMachineModel, new ItemBlockMetadata(blockMachineModel));
        register(blockSiren);
        register(blockThermometer, new ItemBlockThermometer(blockThermometer));
        register(blockUraniumOre, "oreUranium");
        //register(blockPlasma);
        //QuantumFluids.plasma.setBlock(blockPlasma);
        register(blockRadioactiveGrass, "blockRadioactiveGrass");
        register(blockReactorCell);

        register(blockCreativeBuilder);

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

    private static <T extends BlockQuantum> T register(T block, String name) {
        register(block);
        OreDictionary.registerOre(name, block);

        return block;
    }

    private static <T extends BlockQuantum> T register(T block) {
        ItemBlock itemBlock = new ItemBlock(block);
        itemBlock.setRegistryName(block.getRegistryName());

        return register(block, itemBlock);
    }

    private static <T extends BlockQuantum> T register(T block, ItemBlock itemBlock) {
        GameRegistry.register(block);
        GameRegistry.register(itemBlock);

        block.registerItemModel(itemBlock);
        block.registerBlockModel();

        return block;
    }

    private static Class<? extends TileEntity> registerTile(Class<? extends TileEntity> tileClass) {
        String name = tileClass.getSimpleName().replaceAll("(.)(\\p{Lu})", "$1_$2").toLowerCase();

        GameRegistry.registerTileEntity(tileClass, name);

        return tileClass;
    }
}
