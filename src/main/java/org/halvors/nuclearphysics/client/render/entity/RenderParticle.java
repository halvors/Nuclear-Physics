package org.halvors.nuclearphysics.client.render.entity;

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
import org.halvors.nuclearphysics.common.entity.EntityParticle;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import java.util.Random;

@SideOnly(Side.CLIENT)
public class RenderParticle extends Render<EntityParticle> {
    public RenderParticle(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(@Nonnull EntityParticle entity, double x, double y, double z, float entityYaw, float partialTicks) {
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexBuffer = tessellator.getBuffer();

        float age = entity.ticksExisted;

        while (age > 200) {
            age -= 100;
        }

        RenderHelper.disableStandardItemLighting();
        float f = (5 + age) / 200;
        float f1 = 0;

        if (f > 0.8) {
            f1 = (f - 0.8F) / 0.2F;
        }

        Random random = new Random(432L);

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z + 0.3);
        GlStateManager.scale(0.15, 0.15, 0.15);

        GlStateManager.disableTexture2D();
        GlStateManager.shadeModel(GL11.GL_SMOOTH);
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
        GlStateManager.disableAlpha();
        GlStateManager.enableCull();
        GlStateManager.depthMask(false);
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.0F, -1.0F, -2.0F);

        for (int i = 0; i < (f + f * f) / 2 * 60; i++) {
            GlStateManager.rotate(random.nextFloat() * 360, 1, 0, 0);
            GlStateManager.rotate(random.nextFloat() * 360, 0, 1, 0);
            GlStateManager.rotate(random.nextFloat() * 360, 0, 0, 1);
            GlStateManager.rotate(random.nextFloat() * 360, 1, 0, 0);
            GlStateManager.rotate(random.nextFloat() * 360, 0, 1, 0);
            GlStateManager.rotate(random.nextFloat() * 360 + f * 90, 0, 0, 1);
            float f2 = random.nextFloat() * 20 + 5 + f1 * 10;
            float f3 = random.nextFloat() * 2 + 1 + f1 * 2;

            vertexBuffer.begin(6, DefaultVertexFormats.POSITION_COLOR);
            vertexBuffer.pos(0, 0, 0).color(255, 255, 255, (int) (255 * (1 - f1))).endVertex();
            vertexBuffer.pos(-0.866 * f3, f2, -0.5 * f3).color(255, 255, 255, 0).endVertex();
            vertexBuffer.pos(0.866 * f3, f2, -0.5 * f3).color(255, 255, 255, 0).endVertex();
            vertexBuffer.pos(0,  f2, 1 * f3).color(255, 255, 255, 0).endVertex();
            vertexBuffer.pos(-0.866 * f3, f2, -0.5 * f3).color(255, 255, 255, 0).endVertex();

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