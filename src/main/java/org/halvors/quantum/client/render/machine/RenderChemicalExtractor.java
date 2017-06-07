package org.halvors.quantum.client.render.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.tile.machine.TileChemicalExtractor;
import org.halvors.quantum.lib.utility.RenderUtility;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderChemicalExtractor extends TileEntitySpecialRenderer {
    public static final IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation(Reference.PREFIX + "models/chemicalExtractor.obj"));
    public static final ResourceLocation texture = new ResourceLocation(Reference.PREFIX + "textures/models/chemicalExtractor.png");
    private static int count = 0;

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f) {
        if (tileEntity instanceof TileChemicalExtractor) {
            TileChemicalExtractor tileChemicalExtractor = (TileChemicalExtractor) tileEntity;

            GL11.glPushMatrix();
            GL11.glTranslated(x + 0.5, y, z + 0.5);

            if (tileChemicalExtractor.getWorld() != null) {
                RenderUtility.rotateBlockBasedOnDirection(tileChemicalExtractor.getDirection());
            }

            bindTexture(texture);

            GL11.glPushMatrix();

            // TODO: Rotate chambers.
            //GL11.glRotatef((float) Math.toDegrees(count), 0, 0, 0);
            //model.renderOnlyAroundPivot(Math.toDegrees(tileChemicalExtractor.rotation), 0, 0, 1, "MAIN CHAMBER-ROTATES", "MAGNET 1-ROTATES", "MAGNET 2-ROTATES");
            model.renderOnly("Main ChamberRotates", "Magnet1Rotates", "Magnet2Rotates");

            GL11.glPopMatrix();

            model.renderAllExcept("Main ChamberRotates", "Magnet1Rotates", "Magnet2Rotates");

            GL11.glPopMatrix();

            if (count > 1000) {
                count++;
            }
        }
    }
}