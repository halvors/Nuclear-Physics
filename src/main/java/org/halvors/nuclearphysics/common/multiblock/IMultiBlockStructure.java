package org.halvors.nuclearphysics.common.multiblock;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public interface IMultiBlockStructure<W extends IMultiBlockStructure> extends IMultiBlock {
    IWorld getWorldObject();

    void onMultiBlockChanged();

    BlockPos getPosition();

    MultiBlockHandler<W> getMultiBlock();
}
