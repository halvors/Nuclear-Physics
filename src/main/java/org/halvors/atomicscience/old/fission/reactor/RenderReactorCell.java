package org.halvors.atomicscience.old.fission.reactor;

import calclavia.lib.render.RenderUtility;
import calclavia.lib.render.block.ModelCube;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderReactorCell
        extends TileEntitySpecialRenderer
{
    public static final IModelCustom MODEL_TOP = AdvancedModelLoader.loadModel("/assets/atomicscience/models/reactorCellTop.tcn");
    public static final IModelCustom MODEL_MIDDLE = AdvancedModelLoader.loadModel("/assets/atomicscience/models/reactorCellMiddle.tcn");
    public static final IModelCustom MODEL_BOTTOM = AdvancedModelLoader.loadModel("/assets/atomicscience/models/reactorCellBottom.tcn");
    public static final ResourceLocation TEXTURE_TOP = new ResourceLocation("atomicscience", "models/reactorCellTop.png");
    public static final ResourceLocation TEXTURE_MIDDLE = new ResourceLocation("atomicscience", "models/reactorCellMiddle.png");
    public static final ResourceLocation TEXTURE_BOTTOM = new ResourceLocation("atomicscience", "models/reactorCellBottom.png");
    public static final ResourceLocation TEXTURE_FISSILE = new ResourceLocation("atomicscience", "models/fissileMaterial.png");

    public void func_76894_a(TileEntity t, double x, double y, double z, float f)
    {
        TileReactorCell tileEntity = (TileReactorCell)t;

        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5D, y + 0.5D, z + 0.5D);

        int meta = 2;
        if (tileEntity.field_70331_k != null) {
            meta = tileEntity.func_70322_n();
        }
        boolean hasBelow = (tileEntity.field_70331_k != null) && ((t.field_70331_k.func_72796_p(t.field_70329_l, t.field_70330_m - 1, t.field_70327_n) instanceof TileReactorCell));
        switch (meta)
        {
            case 0:
                func_110628_a(TEXTURE_BOTTOM);
                MODEL_BOTTOM.renderAll();
                break;
            case 1:
                func_110628_a(TEXTURE_MIDDLE);
                GL11.glTranslatef(0.0F, 0.075F, 0.0F);
                GL11.glScalef(1.0F, 1.15F, 1.0F);
                MODEL_MIDDLE.renderAll();
                break;
            case 2:
                func_110628_a(TEXTURE_TOP);
                if (hasBelow)
                {
                    GL11.glScalef(1.0F, 1.32F, 1.0F);
                }
                else
                {
                    GL11.glTranslatef(0.0F, 0.1F, 0.0F);
                    GL11.glScalef(1.0F, 1.2F, 1.0F);
                }
                if (hasBelow) {
                    MODEL_TOP.renderAllExcept(new String[] { "BottomPad", "BaseDepth", "BaseWidth", "Base" });
                } else {
                    MODEL_TOP.renderAll();
                }
                break;
        }
        GL11.glPopMatrix();
        if (tileEntity.func_70301_a(0) != null)
        {
            float height = tileEntity.getHeight() * ((tileEntity.func_70301_a(0).func_77958_k() - tileEntity.func_70301_a(0).func_77960_j()) / tileEntity.func_70301_a(0).func_77958_k());

            GL11.glPushMatrix();
            GL11.glTranslatef((float)x + 0.5F, (float)y + 0.5F * height, (float)z + 0.5F);
            GL11.glScalef(0.4F, 0.9F * height, 0.4F);
            func_110628_a(TEXTURE_FISSILE);
            RenderUtility.disableLighting();
            ModelCube.INSTNACE.render();
            RenderUtility.enableLighting();
            GL11.glPopMatrix();
        }
    }
}
