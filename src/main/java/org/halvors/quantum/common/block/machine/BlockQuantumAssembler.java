package org.halvors.quantum.common.block.machine;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.halvors.quantum.common.tile.machine.TileQuantumAssembler;

import javax.annotation.Nonnull;

public class BlockQuantumAssembler extends BlockMachineModel {
    public BlockQuantumAssembler() {
        super("quantum_assembler");
    }

    @Override
    @Nonnull
    public TileEntity createNewTileEntity(@Nonnull World world, int metadata) {
        return new TileQuantumAssembler();
    }
}
