package org.halvors.nuclearphysics.client.render.block.process;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.render.block.OBJModelContainer;
import org.halvors.nuclearphysics.client.render.block.RenderTile;
import org.halvors.nuclearphysics.common.tile.process.TileGasCentrifuge;
import org.halvors.nuclearphysics.common.type.Resource;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;

import java.util.Arrays;

@SideOnly(Side.CLIENT)
public class RenderGasCentrifuge extends RenderTile<TileGasCentrifuge> {
    private static final OBJModelContainer modelPart = new OBJModelContainer(ResourceUtility.getResource(Resource.MODEL, "gas_centrifuge.obj"), Arrays.asList("C", "JROT", "KROT", "LROT", "MROT"));
    private static final OBJModelContainer model = new OBJModelContainer(ResourceUtility.getResource(Resource.MODEL, "gas_centrifuge.obj"), Arrays.asList("A", "B", "D", "E", "F", "G", "H", "I"));

    @Override
    protected void render(TileGasCentrifuge tile, double x, double y, double z) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.5, 0, 0.5);
        GlStateManager.rotate((float) Math.toDegrees(tile.rotation), 0, 1, 0);
        GlStateManager.translate(-0.5, 0, -0.5);
        modelPart.render();
        GlStateManager.popMatrix();

        model.render();
    }
}