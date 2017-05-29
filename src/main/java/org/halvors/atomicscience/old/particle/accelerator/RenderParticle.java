package org.halvors.atomicscience.old.particle.accelerator;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderParticle
        extends Render
{
    public void func_76986_a(Entity entity, double x, double y, double z, float var8, float var9)
    {
        Tessellator tessellator = Tessellator.field_78398_a;

        float par2 = entity.field_70173_aa;
        while (par2 > 200.0F) {
            par2 -= 100.0F;
        }
        RenderHelper.func_74518_a();
        float var41 = (5.0F + par2) / 200.0F;
        float var51 = 0.0F;
        if (var41 > 0.8F) {
            var51 = (var41 - 0.8F) / 0.2F;
        }
        Random rand = new Random(432L);

        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y, (float)z);
        GL11.glScalef(0.15F, 0.15F, 0.15F);
        GL11.glDisable(3553);
        GL11.glShadeModel(7425);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 1);
        GL11.glDisable(3008);
        GL11.glEnable(2884);
        GL11.glDepthMask(false);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, -1.0F, -2.0F);
        for (int i1 = 0; i1 < (var41 + var41 * var41) / 2.0F * 60.0F; i1++)
        {
            GL11.glRotatef(rand.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(rand.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(rand.nextFloat() * 360.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(rand.nextFloat() * 360.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(rand.nextFloat() * 360.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(rand.nextFloat() * 360.0F + var41 * 90.0F, 0.0F, 0.0F, 1.0F);
            tessellator.func_78371_b(6);
            float var81 = rand.nextFloat() * 20.0F + 5.0F + var51 * 10.0F;
            float var91 = rand.nextFloat() * 2.0F + 1.0F + var51 * 2.0F;
            tessellator.func_78384_a(16777215, (int)(255.0F * (1.0F - var51)));
            tessellator.func_78377_a(0.0D, 0.0D, 0.0D);
            tessellator.func_78384_a(0, 0);
            tessellator.func_78377_a(-0.866D * var91, var81, -0.5F * var91);
            tessellator.func_78377_a(0.866D * var91, var81, -0.5F * var91);
            tessellator.func_78377_a(0.0D, var81, 1.0F * var91);
            tessellator.func_78377_a(-0.866D * var91, var81, -0.5F * var91);
            tessellator.func_78381_a();
        }
        GL11.glPopMatrix();
        GL11.glDepthMask(true);
        GL11.glDisable(2884);
        GL11.glDisable(3042);
        GL11.glShadeModel(7424);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(3553);
        GL11.glEnable(3008);
        RenderHelper.func_74519_b();
        GL11.glPopMatrix();
    }

    protected ResourceLocation func_110775_a(Entity entity)
    {
        return null;
    }
}
