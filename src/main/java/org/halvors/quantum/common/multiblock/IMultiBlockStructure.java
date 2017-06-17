package org.halvors.quantum.common.multiblock;

import net.minecraft.world.World;
import org.halvors.quantum.common.transform.vector.Vector3;

public interface IMultiBlockStructure<W extends IMultiBlockStructure> extends IMultiBlock {
    World getWorldObject();

    void onMultiBlockChanged();

    Vector3 getPosition();

    MultiBlockHandler<W> getMultiBlock();
}
