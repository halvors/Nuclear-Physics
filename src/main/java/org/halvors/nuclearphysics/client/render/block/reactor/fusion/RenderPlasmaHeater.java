package org.halvors.nuclearphysics.client.render.block.reactor.fusion;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.halvors.nuclearphysics.client.render.block.RenderTaggedTile;
import org.halvors.nuclearphysics.common.tile.reactor.fusion.TilePlasmaHeater;
import org.halvors.nuclearphysics.common.type.EnumResource;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderPlasmaHeater extends RenderTaggedTile<TilePlasmaHeater> {
    private static final IModelCustom MODEL = AdvancedModelLoader.loadModel(ResourceUtility.getResource(EnumResource.MODEL, "plasma_heater.obj"));
    private static final String[] MODEL_PART = { "rrot", "srot" };

    public RenderPlasmaHeater() {
        super("plasma_heater");
    }

    @Override
    protected void render(final TilePlasmaHeater tile, final double x, final double y, final double z) {
        GL11.glPushMatrix();
        GL11.glTranslated(0.5, 0, 0.5);
        GL11.glRotated((float) Math.toDegrees(tile.rotation), 0, 1, 0);
        GL11.glTranslated(-0.5, 0, -0.5);
        MODEL.renderOnly(MODEL_PART);
        GL11.glPopMatrix();

        MODEL.renderAllExcept(MODEL_PART);

        GL11.glPopMatrix();
        GL11.glPushMatrix();

        super.render(tile, x, y, z);
    }
}