package org.halvors.quantum.common;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.halvors.quantum.common.block.BlockRadioactiveGrass;
import org.halvors.quantum.common.block.BlockUraniumOre;
import org.halvors.quantum.common.block.debug.BlockCreativeBuilder;
import org.halvors.quantum.common.block.machine.*;
import org.halvors.quantum.common.block.particle.BlockFulmination;
import org.halvors.quantum.common.block.reactor.BlockElectricTurbine;
import org.halvors.quantum.common.block.reactor.BlockGasFunnel;
import org.halvors.quantum.common.block.reactor.fission.BlockControlRod;
import org.halvors.quantum.common.block.reactor.fission.BlockReactorCell;
import org.halvors.quantum.common.block.reactor.fission.BlockSiren;
import org.halvors.quantum.common.block.reactor.fission.BlockThermometer;
import org.halvors.quantum.common.block.reactor.fusion.BlockElectromagnet;
import org.halvors.quantum.common.block.reactor.fusion.BlockPlasma;
import org.halvors.quantum.common.block.reactor.fusion.BlockPlasmaHeater;
import org.halvors.quantum.common.item.block.ItemBlockMetadata;
import org.halvors.quantum.common.item.block.ItemBlockThermometer;
import org.halvors.quantum.common.tile.machine.TileChemicalExtractor;
import org.halvors.quantum.common.tile.machine.TileGasCentrifuge;
import org.halvors.quantum.common.tile.machine.TileNuclearBoiler;
import org.halvors.quantum.common.tile.machine.TileQuantumAssembler;
import org.halvors.quantum.common.tile.particle.TileAccelerator;
import org.halvors.quantum.common.tile.particle.TileFulmination;
import org.halvors.quantum.common.tile.reactor.TileElectricTurbine;
import org.halvors.quantum.common.tile.reactor.TileGasFunnel;
import org.halvors.quantum.common.tile.reactor.fission.TileReactorCell;
import org.halvors.quantum.common.tile.reactor.fission.TileSiren;
import org.halvors.quantum.common.tile.reactor.fission.TileThermometer;
import org.halvors.quantum.common.tile.reactor.fusion.TileElectromagnet;
import org.halvors.quantum.common.tile.reactor.fusion.TilePlasma;
import org.halvors.quantum.common.tile.reactor.fusion.TilePlasmaHeater;

public class QuantumBlocks {
    public static Block blockAccelerator = new BlockAccelerator();
    //public static Block blockChemicalExtractor = new BlockChemicalExtractor();
    public static Block blockControlRod = new BlockControlRod();
    public static Block blockElectricTurbine = new BlockElectricTurbine();
    public static Block blockElectromagnet = new BlockElectromagnet();
    public static Block blockFulmination = new BlockFulmination();
    //public static Block blockGasCentrifuge = new BlockGasCentrifuge();
    public static Block blockGasFunnel = new BlockGasFunnel();
    public static Block blockModelMachine = new BlockMachineModel();
    //public static Block blockNuclearBoiler = new BlockNuclearBoiler();
    public static Block blockSiren = new BlockSiren();
    public static Block blockThermometer = new BlockThermometer();
    public static Block blockUraniumOre = new BlockUraniumOre();
    public static Block blockPlasma = new BlockPlasma();
    public static Block blockPlasmaHeater = new BlockPlasmaHeater();
    //public static Block blockQuantumAssembler = new BlockQuantumAssembler();
    public static Block blockRadioactiveGrass = new BlockRadioactiveGrass();
    public static Block blockReactorCell = new BlockReactorCell();

    public static Block blockCreativeBuilder = new BlockCreativeBuilder();

    // Register blocks.
    public static void register() {
        register(blockAccelerator);
        register(blockControlRod);
        register(blockElectricTurbine);
        register(blockElectromagnet, new ItemBlockMetadata(blockElectromagnet));
        register(blockFulmination);
        register(blockGasFunnel);
        register(blockModelMachine, new ItemBlockMetadata(blockModelMachine));
        register(blockSiren);
        register(blockThermometer, new ItemBlockThermometer(blockThermometer));
        register(blockUraniumOre);
        //register(blockPlasma);
        //QuantumFluids.plasma.setBlock(blockPlasma);
        register(blockPlasmaHeater);
        register(blockRadioactiveGrass);
        register(blockReactorCell);

        register(blockCreativeBuilder);

        GameRegistry.registerTileEntity(TileAccelerator.class, "tileAccelerator");
        GameRegistry.registerTileEntity(TileChemicalExtractor.class, "tileChemicalExtractor");
        GameRegistry.registerTileEntity(TileElectricTurbine.class, "tileElectricTurbine");
        GameRegistry.registerTileEntity(TileElectromagnet.class, "tileElectromagnet");
        GameRegistry.registerTileEntity(TileFulmination.class, "tileFulmination");
        GameRegistry.registerTileEntity(TileGasCentrifuge.class, "tileGasCentrifuge");
        GameRegistry.registerTileEntity(TileGasFunnel.class, "tileGasFunnel");
        GameRegistry.registerTileEntity(TileNuclearBoiler.class, "tileNuclearBoiler");
        GameRegistry.registerTileEntity(TileSiren.class, "tileSiren");
        GameRegistry.registerTileEntity(TileThermometer.class, "tileThermometer");
        GameRegistry.registerTileEntity(TilePlasma.class, "tilePlasma");
        GameRegistry.registerTileEntity(TilePlasmaHeater.class, "tilePlasmaHeater");
        GameRegistry.registerTileEntity(TileQuantumAssembler.class, "tileQuantumAssembler");
        GameRegistry.registerTileEntity(TileReactorCell.class, "tileReactorCell");
    }

    private static <T extends Block> T register(T block, ItemBlock itemBlock) {
        GameRegistry.register(block);
        GameRegistry.register(itemBlock);

        /*
        if (block instanceof BlockQuantum) {
            ((BlockQuantum) block).registerItemModel(itemBlock);
        } else if (block instanceof BlockContainerQuantum) {
            ((BlockContainerQuantum) block).registerItemModel(itemBlock);
        }
        */

        return block;
    }

    private static <T extends Block> T register(T block) {
        ItemBlock itemBlock = new ItemBlock(block);
        itemBlock.setRegistryName(block.getRegistryName());

        return register(block, itemBlock);
    }
}
