package org.halvors.nuclearphysics.client.render.block.process;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.apache.commons.lang3.ArrayUtils;
import org.halvors.nuclearphysics.client.render.block.RenderTile;
import org.halvors.nuclearphysics.common.tile.process.TileNuclearBoiler;
import org.halvors.nuclearphysics.common.type.Resource;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderNuclearBoiler extends RenderTile<TileNuclearBoiler> {
    private static final IModelCustom model = AdvancedModelLoader.loadModel(ResourceUtility.getResource(Resource.MODEL, "nuclear_boiler.obj"));
    private static final String[] modelPart1 = { "FuelBarSupport1Rotates", "FuelBar1Rotates" };
    private static final String[] modelPart2 = { "FuelBarSupport2Rotates", "FuelBar2Rotates" };

    public RenderNuclearBoiler() {
        super("nuclear_boiler");
    }

    @Override
    protected void render(TileNuclearBoiler tile, double x, double y, double z) {
        GL11.glPushMatrix();
        GL11.glTranslated(0.687042, 0, 0.1875);
        GL11.glRotated((float) Math.toDegrees(tile.rotation), 0, 1, 0);
        GL11.glTranslated(-0.687042, 0, -0.1875);
        model.renderOnly(modelPart1);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslated(0.312958, 0, 0.1875);
        GL11.glRotated((float) -Math.toDegrees(tile.rotation), 0, 1, 0);
        GL11.glTranslated(-0.312958, 0, -0.1875);
        model.renderOnly(modelPart2);
        GL11.glPopMatrix();

        model.renderAllExcept(ArrayUtils.addAll(modelPart1, modelPart2));
    }
}