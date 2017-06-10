package org.halvors.quantum.client.render.reactor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.apache.commons.lang3.ArrayUtils;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.tile.reactor.TileElectricTurbine;
import org.halvors.quantum.lib.utility.RenderUtility;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderElectricTurbine extends TileEntitySpecialRenderer {
    private static final IModelCustom modelSmall = AdvancedModelLoader.loadModel(new ResourceLocation(Reference.PREFIX + "models/turbineSmall.obj"));
    private static final IModelCustom modelLarge = AdvancedModelLoader.loadModel(new ResourceLocation(Reference.PREFIX + "models/turbineLarge.obj"));
    private static final ResourceLocation textureSmall = new ResourceLocation(Reference.PREFIX + "textures/models/turbineSmall.png");
    private static final ResourceLocation textureLarge = new ResourceLocation(Reference.PREFIX + "textures/models/turbineLarge.png");

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f) {
        TileElectricTurbine tileTurbine = (TileElectricTurbine) tileEntity;

        if (tileTurbine.getMultiBlock().isPrimary()) {
            GL11.glPushMatrix();
            GL11.glTranslated(x + 0.5F, y, z + 0.5F);

            if (tileTurbine.getMultiBlock().isConstructed()) {
                RenderUtility.bind(textureLarge);

                String[] blades = { "Blade1", "Blade2", "Blade3", "Blade4", "Blade5", "Blade6" };
                String[] mediumBlades = { "MediumBlade1", "MediumBlade2", "MediumBlade3", "MediumBlade4", "MediumBlade5", "MediumBlade6" };
                String[] largeBlades = { "LargeBlade1", "LargeBlade2", "LargeBlade3", "LargeBlade4", "LargeBlade5", "LargeBlade6" };

                GL11.glPushMatrix();
                GL11.glRotated(Math.toDegrees(tileTurbine.rotation), 0, 1, 0);
                modelLarge.renderOnly(blades);
                modelLarge.renderOnly(largeBlades);
                GL11.glPopMatrix();

                GL11.glPushMatrix();
                GL11.glRotated(-Math.toDegrees(tileTurbine.rotation), 0, 1, 0);
                modelLarge.renderOnly(mediumBlades);
                GL11.glPopMatrix();

                modelLarge.renderAllExcept(ArrayUtils.addAll(ArrayUtils.addAll(blades, mediumBlades), largeBlades));
            } else {
                GL11.glScalef(1.0F, 1.1F, 1.0F);
                RenderUtility.bind(textureSmall);

                String[] blades = { "Blade1", "Blade2", "Blade3" };
                String[] mediumBlades = { "MediumBlade1", "MediumBlade2", "MediumBlade3" };
                String[] shields = { "Shield1", "Shield2", "Shield3", "Shield4", "Shield5", "Shield6" };
                String[] mediumShields = { "MediumShield1", "MediumShield2", "MediumShield3", "MediumShield4", "MediumShield5", "MediumShield6" };

                GL11.glPushMatrix();
                GL11.glRotated(Math.toDegrees(tileTurbine.rotation), 0, 1, 0);
                modelSmall.renderOnly(blades);
                modelSmall.renderOnly(shields);
                GL11.glPopMatrix();

                GL11.glPushMatrix();
                GL11.glRotated(-Math.toDegrees(tileTurbine.rotation), 0, 1, 0);
                modelSmall.renderOnly(mediumBlades);
                modelSmall.renderOnly(mediumShields);
                GL11.glPopMatrix();

                modelSmall.renderAllExcept(ArrayUtils.addAll(ArrayUtils.addAll(blades, shields), ArrayUtils.addAll(mediumShields, mediumBlades)));
            }

            GL11.glPopMatrix();
        }
    }
}
