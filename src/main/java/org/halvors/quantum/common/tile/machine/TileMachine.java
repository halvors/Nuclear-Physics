package org.halvors.quantum.common.tile.machine;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import org.halvors.quantum.common.block.BlockRotatable;
import org.halvors.quantum.common.tile.ITileRotatable;
import org.halvors.quantum.common.tile.TileElectricInventory;

public class TileMachine extends TileElectricInventory implements ITileRotatable {
    public TileMachine(int maxSlots) {
        super(maxSlots);
    }

    @Override
    public EnumFacing getDirection() {
        IBlockState state = world.getBlockState(pos);

        return EnumFacing.getHorizontal(state.getBlock().getMetaFromState(state));
    }

    @Override
    public void setDirection(EnumFacing direction) {
        world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockRotatable.facing, direction), 2);
    }
}
