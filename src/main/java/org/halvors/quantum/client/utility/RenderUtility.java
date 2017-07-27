package org.halvors.quantum.client.utility;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3i;
import org.halvors.quantum.common.Quantum;

public class RenderUtility {
    public static void rotateBlockBasedOnDirection(EnumFacing direction) {
        switch (direction) {
            case SOUTH:
                GlStateManager.translate(1, 0, 1);
                GlStateManager.rotate(180, 0, 1, 0);
                break;

            case WEST:
                GlStateManager.translate(0, 0, 1);
                GlStateManager.rotate(90, 0, 1, 0);
                break;

            case EAST:
                GlStateManager.translate(1, 0, 0);
                GlStateManager.rotate(270, 0, 1, 0);
                break;
        }
    }
}
