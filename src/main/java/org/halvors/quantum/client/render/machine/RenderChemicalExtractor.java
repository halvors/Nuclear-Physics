package org.halvors.quantum.client.render.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.halvors.quantum.client.utility.render.RenderUtility;
import org.halvors.quantum.common.tile.machine.TileChemicalExtractor;
import org.halvors.quantum.common.utility.ResourceUtility;
import org.halvors.quantum.common.utility.type.ResourceType;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderChemicalExtractor extends TileEntitySpecialRenderer {
    private static final IModelCustom model = AdvancedModelLoader.loadModel(ResourceUtility.getResource(ResourceType.MODEL, "chemicalExtractor.obj"));
    private static final ResourceLocation texture = ResourceUtility.getResource(ResourceType.TEXTURE_MODELS, "chemicalExtractor.png");

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTick) {
        if (tile instanceof TileChemicalExtractor) {
            TileChemicalExtractor tileChemicalExtractor = (TileChemicalExtractor) tile;

            GL11.glPushMatrix();
            GL11.glTranslated(x + 0.5, y, z + 0.5);

            if (tileChemicalExtractor.getWorld() != null) {
                RenderUtility.rotateBlockBasedOnDirection(tileChemicalExtractor.getDirection());
            }

            RenderUtility.bind(texture);

            GL11.glPushMatrix();
            GL11.glTranslated(0.1875, 0.4375, 0);
            GL11.glRotated(Math.toDegrees(tileChemicalExtractor.rotation), 0, 0, 1);
            GL11.glTranslated(-0.1875, -0.4375, 0);
            model.renderOnly("MainChamberRotates", "Magnet1Rotates", "Magnet2Rotates");
            GL11.glPopMatrix();

            model.renderAllExcept("MainChamberRotates", "Magnet1Rotates", "Magnet2Rotates");
            GL11.glPopMatrix();
        }
    }
}