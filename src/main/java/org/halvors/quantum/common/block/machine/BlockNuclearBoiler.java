package org.halvors.quantum.common.block.machine;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.halvors.quantum.common.tile.machine.TileNuclearBoiler;

import javax.annotation.Nonnull;

public class BlockNuclearBoiler extends BlockMachine {
    public BlockNuclearBoiler() {
        super("nuclear_boiler");
    }

    @Override
    @Nonnull
    public TileEntity createNewTileEntity(@Nonnull World world, int metadata) {
        return new TileNuclearBoiler();
    }
}
