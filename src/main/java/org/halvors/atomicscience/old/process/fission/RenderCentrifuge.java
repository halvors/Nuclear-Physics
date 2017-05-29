package org.halvors.atomicscience.old.process.fission;

import calclavia.lib.render.RenderUtility;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderCentrifuge
        extends TileEntitySpecialRenderer
{
    public static final IModelCustom MODEL = AdvancedModelLoader.loadModel("/assets/atomicscience/models/centrifuge.tcn");
    public static final ResourceLocation TEXTURE = new ResourceLocation("atomicscience", "models/centrifuge.png");

    public void render(TileCentrifuge tileEntity, double x, double y, double z, float f)
    {
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5D, y + 0.5D, z + 0.5D);
        if (tileEntity.field_70331_k != null) {
            RenderUtility.rotateBlockBasedOnDirection(tileEntity.getDirection());
        }
        func_110628_a(TEXTURE);

        GL11.glPushMatrix();
        GL11.glRotated(Math.toDegrees(tileEntity.rotation), 0.0D, 1.0D, 0.0D);
        MODEL.renderOnly(new String[] { "C", "JROT", "KROT", "LROT", "MROT" });
        GL11.glPopMatrix();

        MODEL.renderAllExcept(new String[] { "C", "JROT", "KROT", "LROT", "MROT" });
        GL11.glPopMatrix();
    }

    public void func_76894_a(TileEntity tileEntity, double var2, double var4, double var6, float var8)
    {
        render((TileCentrifuge)tileEntity, var2, var4, var6, var8);
    }
}
