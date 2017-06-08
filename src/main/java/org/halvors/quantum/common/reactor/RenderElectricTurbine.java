package org.halvors.quantum.common.reactor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.apache.commons.lang3.ArrayUtils;
import org.halvors.quantum.common.Reference;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderElectricTurbine extends TileEntitySpecialRenderer {
    private static final IModelCustom modelSmall = AdvancedModelLoader.loadModel(new ResourceLocation(Reference.PREFIX + "models/turbineSmall.obj"));
    private static final IModelCustom modelLarge = AdvancedModelLoader.loadModel(new ResourceLocation(Reference.PREFIX + "models/turbineLarge.obj"));
    private static final ResourceLocation textureSmall = new ResourceLocation(Reference.PREFIX + "textures/models/turbineSmall.png");
    private static final ResourceLocation textureLarge = new ResourceLocation(Reference.PREFIX + "textures/models/turbineLarge.png");

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f) {
        TileTurbine tileTurbine = (TileTurbine) tileEntity;

        if (tileTurbine.getMultiBlock().isPrimary()) {
            GL11.glPushMatrix();
            GL11.glTranslated(x + 0.5D, y, z + 0.5D);

            if (tileTurbine.getMultiBlock().isConstructed()) {
                Minecraft.getMinecraft().renderEngine.bindTexture(textureLarge);

                String[] blades = { "Blade1", "Blade2", "Blade3", "Blade4", "Blade5", "Blade6" };
                String[] mediumBlades = { "MediumBlade1", "MediumBlade2", "MediumBlade3", "MediumBlade4", "MediumBlade5", "MediumBlade6" };
                String[] largeBlades = { "LargeBlade1", "LargeBlade2", "LargeBlade3", "LargeBlade4", "LargeBlade5", "LargeBlade6" };

                GL11.glPushMatrix();
                GL11.glRotated(Math.toDegrees(tileTurbine.rotation), 0.0D, 1.0D, 0.0D);
                modelLarge.renderOnly(blades);
                modelLarge.renderOnly(largeBlades);
                GL11.glPopMatrix();

                GL11.glPushMatrix();
                GL11.glRotated(-Math.toDegrees(tileTurbine.rotation), 0.0D, 1.0D, 0.0D);
                modelLarge.renderOnly(mediumBlades);
                GL11.glPopMatrix();

                modelLarge.renderAllExcept(ArrayUtils.addAll(ArrayUtils.addAll(blades, mediumBlades), largeBlades));
            } else {
                GL11.glScalef(1.0F, 1.1F, 1.0F);
                Minecraft.getMinecraft().renderEngine.bindTexture(textureSmall);

                String[] bladesA = new String[3];

                for (int i = 0; i < bladesA.length; i++) {
                    bladesA[i] = ("BLADE A" + (i + 1) + " SPINS");
                }

                String[] sheildsA = new String[6];

                for (int i = 0; i < sheildsA.length; i++) {
                    sheildsA[i] = ("SHIELD A" + (i + 1) + " SPINS");
                }

                String[] bladesB = new String[3];

                for (int i = 0; i < bladesB.length; i++) {
                    bladesB[i] = ("BLADE B" + (i + 1) + " SPINS");
                }

                String[] sheildsB = new String[6];

                for (int i = 0; i < sheildsB.length; i++) {
                    sheildsB[i] = ("SHIELD B" + (i + 1) + " SPINS");
                }

                String[] renderA = ArrayUtils.addAll(bladesA, sheildsA);
                String[] renderB = ArrayUtils.addAll(bladesB, sheildsB);

                GL11.glPushMatrix();
                GL11.glRotated(Math.toDegrees(tileTurbine.rotation), 0.0D, 1.0D, 0.0D);
                modelSmall.renderOnly(renderA);
                GL11.glPopMatrix();

                GL11.glPushMatrix();
                GL11.glRotated(-Math.toDegrees(tileTurbine.rotation), 0.0D, 1.0D, 0.0D);
                modelSmall.renderOnly(renderB);
                GL11.glPopMatrix();

                modelSmall.renderAllExcept(ArrayUtils.addAll(renderA, renderB));
            }

            GL11.glPopMatrix();
        }
    }
}
