package org.halvors.quantum.client.utility;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumFacing;

public class RenderUtility {
    public static void rotateBlockBasedOnDirection(EnumFacing direction) {
        switch (direction) {
            case NORTH:
                GlStateManager.translate(0, 0, 1);
                GlStateManager.rotate(90, 0, 1, 0);
                break;

            case SOUTH:
                GlStateManager.translate(1, 0, 0);
                GlStateManager.rotate(-90, 0, 1, 0);
                break;

            case WEST:
                GlStateManager.translate(1, 0, 1);
                GlStateManager.rotate(-180, 0, 1, 0);
                break;
        }
    }
}
