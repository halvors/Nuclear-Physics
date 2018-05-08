package org.halvors.nuclearphysics.client.render.block.reactor.fusion;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.render.block.OBJModelContainer;
import org.halvors.nuclearphysics.client.render.block.RenderTaggedTile;
import org.halvors.nuclearphysics.common.tile.reactor.fusion.TilePlasmaHeater;
import org.halvors.nuclearphysics.common.type.EnumResource;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;

import java.util.Arrays;

@SideOnly(Side.CLIENT)
public class RenderPlasmaHeater extends RenderTaggedTile<TilePlasmaHeater> {
    private static final OBJModelContainer MODEL_PART = new OBJModelContainer(ResourceUtility.getResource(EnumResource.MODEL, "plasma_heater.obj"), Arrays.asList("rrot", "srot"));
    private static final OBJModelContainer MODEL = new OBJModelContainer(ResourceUtility.getResource(EnumResource.MODEL, "plasma_heater.obj"), Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "t", "u", "v", "w", "x", "y"));

    @Override
    protected void render(final TilePlasmaHeater tile, final double x, final double y, final double z) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.5, 0, 0.5);
        GlStateManager.rotate((float) Math.toDegrees(tile.rotation), 0, 1, 0);
        GlStateManager.translate(-0.5, 0, -0.5);
        MODEL_PART.render();
        GlStateManager.popMatrix();

        MODEL.render();

        GlStateManager.popMatrix();
        GlStateManager.pushMatrix();

        super.render(tile, x, y, z);
    }
}