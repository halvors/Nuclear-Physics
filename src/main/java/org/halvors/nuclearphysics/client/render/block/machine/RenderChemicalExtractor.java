package org.halvors.nuclearphysics.client.render.block.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.halvors.nuclearphysics.client.render.block.RenderTile;
import org.halvors.nuclearphysics.common.tile.machine.TileChemicalExtractor;
import org.halvors.nuclearphysics.common.type.EnumResource;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderChemicalExtractor extends RenderTile<TileChemicalExtractor> {
    private static final IModelCustom model = AdvancedModelLoader.loadModel(ResourceUtility.getResource(EnumResource.MODEL, "chemical_extractor.obj"));
    private static final String[] modelPart = { "MainChamberRotates", "Magnet1Rotates", "Magnet2Rotates" };

    public RenderChemicalExtractor() {
        super("chemical_extractor");
    }

    @Override
    protected void render(final TileChemicalExtractor tile, final double x, final double y, final double z) {
        GL11.glPushMatrix();
        GL11.glTranslated(0, 0.4375, 0.314388);
        GL11.glRotated((float) Math.toDegrees(tile.rotation), 1, 0, 0);
        GL11.glTranslated(0, -0.4375, -0.314388);
        model.renderOnly(modelPart);
        GL11.glPopMatrix();

        model.renderAllExcept(modelPart);
    }
}