package org.halvors.quantum.client.render.particle;

/*
@SideOnly(Side.CLIENT)
public class RenderParticle extends Render {
    public RenderParticle(RenderManager renderManager) {
        super(renderManager);
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float f, float partialTick) {
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

        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z + 0.3);
        GL11.glScaled(0.15, 0.15, 0.15);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDepthMask(false);
        GL11.glPushMatrix();
        GL11.glTranslatef(0, -1, -2);

        for (int i1 = 0; i1 < (var41 + var41 * var41) / 2 * 60; ++i1) {
            GL11.glRotated(rand.nextFloat() * 360, 1, 0, 0);
            GL11.glRotated(rand.nextFloat() * 360, 0, 1, 0);
            GL11.glRotated(rand.nextFloat() * 360, 0, 0, 1);
            GL11.glRotated(rand.nextFloat() * 360, 1, 0, 0);
            GL11.glRotated(rand.nextFloat() * 360, 0, 1, 0);
            GL11.glRotated(rand.nextFloat() * 360 + var41 * 90, 0, 0, 1);
            tessellator.startDrawing(6);
            float var81 = rand.nextFloat() * 20 + 5 + var51 * 10;
            float var91 = rand.nextFloat() * 2 + 1 + var51 * 2;
            tessellator.setColorRGBA_I(16777215, (int) (255 * (1 - var51)));
            tessellator.addVertex(0, 0, 0);
            tessellator.setColorRGBA_I(0, 0);
            tessellator.addVertex(-0.866 * var91, var81, -0.5 * var91);
            tessellator.addVertex(0.866 * var91, var81, -0.5 * var91);
            tessellator.addVertex(0, var81, 1 * var91);
            tessellator.addVertex(-0.866 * var91, var81, -0.5 * var91);
            tessellator.draw();
        }

        GL11.glPopMatrix();
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glColor4f(1, 1, 1, 1);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        RenderHelper.enableStandardItemLighting();
        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return null;
    }
}
*/