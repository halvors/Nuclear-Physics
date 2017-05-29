package org.halvors.atomicscience.old.process;

import calclavia.lib.render.RenderUtility;
import calclavia.lib.render.model.TechneAdvancedModel;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderChemicalExtractor
        extends TileEntitySpecialRenderer
{
    public static final TechneAdvancedModel MODEL = (TechneAdvancedModel)AdvancedModelLoader.loadModel("/assets/atomicscience/models/chemicalExtractor.tcn");
    public static final ResourceLocation TEXTURE = new ResourceLocation("atomicscience", "models/chemicalExtractor.png");

    public void render(TileChemicalExtractor tileEntity, double x, double y, double z, float f)
    {
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5D, y + 0.5D, z + 0.5D);
        if (tileEntity.field_70331_k != null) {
            RenderUtility.rotateBlockBasedOnDirection(tileEntity.getDirection());
        }
        func_110628_a(TEXTURE);

        GL11.glPushMatrix();
        MODEL.renderOnlyAroundPivot(Math.toDegrees(tileEntity.rotation), 0.0D, 0.0D, 1.0D, new String[] { "MAIN CHAMBER-ROTATES", "MAGNET 1-ROTATES", "MAGNET 2-ROTATES" });
        GL11.glPopMatrix();

        MODEL.renderAllExcept(new String[] { "MAIN CHAMBER-ROTATES", "MAGNET 1-ROTATES", "MAGNET 2-ROTATES" });
        GL11.glPopMatrix();
    }

    public void func_76894_a(TileEntity tileEntity, double var2, double var4, double var6, float var8)
    {
        render((TileChemicalExtractor)tileEntity, var2, var4, var6, var8);
    }
}
