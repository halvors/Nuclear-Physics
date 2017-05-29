package org.halvors.atomicscience.old.fusion;

import calclavia.lib.render.RenderTaggedTile;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderPlasmaHeater
        extends RenderTaggedTile
{
    public static final IModelCustom MODEL = AdvancedModelLoader.loadModel("/assets/atomicscience/models/fusionReactor.tcn");
    public static final ResourceLocation TEXTURE = new ResourceLocation("atomicscience", "models/fusionReactor.png");

    public void func_76894_a(TileEntity t, double x, double y, double z, float f)
    {
        TilePlasmaHeater tileEntity = (TilePlasmaHeater)t;
        if (tileEntity.field_70331_k != null) {
            super.func_76894_a(t, x, y, z, f);
        }
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5D, y + 0.5D, z + 0.5D);

        func_110628_a(TEXTURE);

        GL11.glPushMatrix();
        GL11.glRotated(Math.toDegrees(tileEntity.rotation), 0.0D, 1.0D, 0.0D);
        MODEL.renderOnly(new String[] { "rrot", "srot" });
        GL11.glPopMatrix();

        MODEL.renderAllExcept(new String[] { "rrot", "srot" });
        GL11.glPopMatrix();
    }
}
