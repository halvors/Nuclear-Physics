package org.halvors.quantum.client.render.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.halvors.quantum.client.utility.render.RenderUtility;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.tile.machine.TileChemicalExtractor;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderChemicalExtractor extends TileEntitySpecialRenderer {
    public static final IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation(Reference.PREFIX + "models/chemicalExtractor.obj"));
    public static final ResourceLocation texture = new ResourceLocation(Reference.PREFIX + "textures/models/chemicalExtractor.png");

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTick) {
        if (tile instanceof TileChemicalExtractor) {
            TileChemicalExtractor tileChemicalExtractor = (TileChemicalExtractor) tile;

            GL11.glPushMatrix();
            GL11.glTranslated(x + 0.5F, y, z + 0.5F);

            if (tileChemicalExtractor.getWorld() != null) {
                RenderUtility.rotateBlockBasedOnDirection(tileChemicalExtractor.getDirection());
            }

            RenderUtility.bind(texture);

            GL11.glPushMatrix();
            GL11.glTranslated(0.1875F, 0.4375F, 0);
            GL11.glRotated(Math.toDegrees(tileChemicalExtractor.rotation), 0, 0, 1);
            GL11.glTranslated(-0.1875F, -0.4375F, 0);
            model.renderOnly("MainChamberRotates", "Magnet1Rotates", "Magnet2Rotates");
            GL11.glPopMatrix();

            model.renderAllExcept("MainChamberRotates", "Magnet1Rotates", "Magnet2Rotates");
            GL11.glPopMatrix();
        }
    }
}