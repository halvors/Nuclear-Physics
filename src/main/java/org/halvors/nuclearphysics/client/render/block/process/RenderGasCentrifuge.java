package org.halvors.nuclearphysics.client.render.block.process;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.halvors.nuclearphysics.client.render.block.RenderTile;
import org.halvors.nuclearphysics.common.tile.process.TileGasCentrifuge;
import org.halvors.nuclearphysics.common.type.Resource;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderGasCentrifuge extends RenderTile<TileGasCentrifuge> {
    private static final IModelCustom model = AdvancedModelLoader.loadModel(ResourceUtility.getResource(Resource.MODEL, "gas_centrifuge.obj"));
    private static final String[] modelPart = { "C", "JROT", "KROT", "LROT", "MROT" };

    public RenderGasCentrifuge() {
        super("gas_centrifuge");
    }

    @Override
    protected void render(TileGasCentrifuge tile, double x, double y, double z) {
        GL11.glPushMatrix();
        GL11.glTranslated(0.5, 0, 0.5);
        GL11.glRotated((float) Math.toDegrees(tile.rotation), 0, 1, 0);
        GL11.glTranslated(-0.5, 0, -0.5);
        model.renderOnly(modelPart);
        GL11.glPopMatrix();

        model.renderAllExcept(modelPart);
    }
}