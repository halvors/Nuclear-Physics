package org.halvors.quantum.common.multiblock;

import net.minecraft.world.World;
import org.halvors.quantum.common.utility.position.Position;

public interface IMultiBlockStructure<W extends IMultiBlockStructure> extends IMultiBlock {
    World getWorldObject();

    void onMultiBlockChanged();

    Position getPosition();

    MultiBlockHandler<W> getMultiBlock();
}
