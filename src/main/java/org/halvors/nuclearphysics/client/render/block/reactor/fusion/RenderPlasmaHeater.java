package org.halvors.nuclearphysics.client.render.block.reactor.fusion;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.halvors.nuclearphysics.client.render.block.RenderTaggedTile;
import org.halvors.nuclearphysics.common.tile.reactor.fusion.TilePlasmaHeater;
import org.halvors.nuclearphysics.common.type.Resource;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderPlasmaHeater extends RenderTaggedTile<TilePlasmaHeater> {
    private static final IModelCustom model = AdvancedModelLoader.loadModel(ResourceUtility.getResource(Resource.MODEL, "plasma_heater.obj"));
    private static final String[] modelPart = { "rrot", "srot" };

    public RenderPlasmaHeater() {
        super("plasma_heater");
    }

    @Override
    protected void render(TilePlasmaHeater tile, double x, double y, double z) {
        GL11.glPushMatrix();
        GL11.glTranslated(0.5, 0, 0.5);
        GL11.glRotated((float) Math.toDegrees(tile.rotation), 0, 1, 0);
        GL11.glTranslated(-0.5, 0, -0.5);
        model.renderOnly(modelPart);
        GL11.glPopMatrix();

        model.renderAllExcept(modelPart);

        GL11.glPopMatrix();
        GL11.glPushMatrix();

        super.render(tile, x, y, z);
    }
}