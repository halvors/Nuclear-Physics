package org.halvors.quantum.client.render.particle;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.common.entity.particle.EntityParticle;
import org.lwjgl.opengl.GL11;

import java.util.Random;

@SideOnly(Side.CLIENT)
public class RenderParticle extends Render<EntityParticle> {
    public RenderParticle(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(EntityParticle entity, double x, double y, double z, float f, float partialTick) {
        Tessellator tessellator = Tessellator.getInstance();
        float age = entity.ticksExisted;

        while (age > 200) {
            age -= 100;
        }

        RenderHelper.disableStandardItemLighting();
        float var41 = (5 + age) / 200;
        float var51 = 0;

        if (var41 > 0.8) {
            var51 = (var41 - 0.8F) / 0.2F;
        }

        Random rand = new Random(432);

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z + 0.3);
        GlStateManager.scale(0.15, 0.15, 0.15);
        GlStateManager.disableTexture2D();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GlStateManager.disableAlpha();
        GlStateManager.enableCull();
        GlStateManager.depthMask(false);
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, -1, -2);

        for (int i1 = 0; i1 < (var41 + var41 * var41) / 2 * 60; ++i1) {
            GlStateManager.rotate(rand.nextFloat() * 360, 1, 0, 0);
            GlStateManager.rotate(rand.nextFloat() * 360, 0, 1, 0);
            GlStateManager.rotate(rand.nextFloat() * 360, 0, 0, 1);
            GlStateManager.rotate(rand.nextFloat() * 360, 1, 0, 0);
            GlStateManager.rotate(rand.nextFloat() * 360, 0, 1, 0);
            GlStateManager.rotate(rand.nextFloat() * 360 + var41 * 90, 0, 0, 1);

            VertexBuffer vertexBuffer = tessellator.getBuffer();
            vertexBuffer.begin(6, DefaultVertexFormats.POSITION_TEX_NORMAL);
            float var81 = rand.nextFloat() * 20 + 5 + var51 * 10;
            float var91 = rand.nextFloat() * 2 + 1 + var51 * 2;
            //tessellator.setColorRGBA_I(16777215, (int) (255 * (1 - var51)));
            vertexBuffer.pos(0, 0, 0).endVertex();
            //tessellator.setColorRGBA_I(0, 0);
            vertexBuffer.pos(-0.866 * var91, var81, -0.5 * var91).endVertex();
            vertexBuffer.pos(0.866 * var91, var81, -0.5 * var91).endVertex();
            vertexBuffer.pos(0, var81, 1 * var91).endVertex();
            vertexBuffer.pos(-0.866 * var91, var81, -0.5 * var91).endVertex();

            tessellator.draw();
        }

        GlStateManager.popMatrix();
        GlStateManager.depthMask(true);
        GlStateManager.disableCull();
        GlStateManager.disableBlend();
        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.enableTexture2D();
        GlStateManager.enableAlpha();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityParticle entity) {
        return null;
    }
}