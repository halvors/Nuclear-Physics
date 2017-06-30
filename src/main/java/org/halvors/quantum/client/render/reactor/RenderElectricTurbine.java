package org.halvors.quantum.client.render.reactor;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.ArrayUtils;
import org.halvors.quantum.client.utility.render.RenderUtility;
import org.halvors.quantum.common.tile.reactor.TileElectricTurbine;
import org.halvors.quantum.common.utility.ResourceUtility;
import org.halvors.quantum.common.utility.type.ResourceType;
import org.lwjgl.opengl.GL11;

/*
@SideOnly(Side.CLIENT)
public class RenderElectricTurbine extends TileEntitySpecialRenderer<TileElectricTurbine> {
    private static IModel modelSmall;
    private static IModel modelLarge;
    private static final ResourceLocation textureSmall = ResourceUtility.getResource(ResourceType.TEXTURE_MODELS, "electricTurbineSmall.png");
    private static final ResourceLocation textureLarge = ResourceUtility.getResource(ResourceType.TEXTURE_MODELS, "electricTurbineLarge.png");

    public RenderElectricTurbine() throws Exception {
        modelSmall = OBJLoader.INSTANCE.loadModel(ResourceUtility.getResource(ResourceType.MODEL, "electricTurbineSmall.obj"));
        modelLarge = OBJLoader.INSTANCE.loadModel(ResourceUtility.getResource(ResourceType.MODEL, "electricTurbineLarge.obj"));
    }

    @Override
    public void renderTileEntityAt(TileElectricTurbine tile, double x, double y, double z, float partialTicks, int destroyStage) {
            if (tile.getMultiBlock().isPrimary()) {
                GL11.glPushMatrix();
                GL11.glTranslated(x + 0.5, y, z + 0.5);

                if (tile.getMultiBlock().isConstructed()) {
                    RenderUtility.bind(textureLarge);

                    String[] blades = {"Blade1", "Blade2", "Blade3", "Blade4", "Blade5", "Blade6"};
                    String[] mediumBlades = {"MediumBlade1", "MediumBlade2", "MediumBlade3", "MediumBlade4", "MediumBlade5", "MediumBlade6"};
                    String[] largeBlades = {"LargeBlade1", "LargeBlade2", "LargeBlade3", "LargeBlade4", "LargeBlade5", "LargeBlade6"};

                    GL11.glPushMatrix();
                    GL11.glRotated(Math.toDegrees(tile.rotation), 0, 1, 0);
                    modelLarge.renderOnly(blades);
                    modelLarge.renderOnly(largeBlades);
                    GL11.glPopMatrix();

                    GL11.glPushMatrix();
                    GL11.glRotated(-Math.toDegrees(tile.rotation), 0, 1, 0);
                    modelLarge.renderOnly(mediumBlades);
                    GL11.glPopMatrix();

                    modelLarge.renderAllExcept(ArrayUtils.addAll(ArrayUtils.addAll(blades, mediumBlades), largeBlades));
                } else {
                    GL11.glScaled(1, 1.1, 1);
                    RenderUtility.bind(textureSmall);

                    String[] blades = {"Blade1", "Blade2", "Blade3"};
                    String[] mediumBlades = {"MediumBlade1", "MediumBlade2", "MediumBlade3"};
                    String[] shields = {"Shield1", "Shield2", "Shield3", "Shield4", "Shield5", "Shield6"};
                    String[] mediumShields = {"MediumShield1", "MediumShield2", "MediumShield3", "MediumShield4", "MediumShield5", "MediumShield6"};

                    GL11.glPushMatrix();
                    GL11.glRotated(Math.toDegrees(tile.rotation), 0, 1, 0);
                    modelSmall.renderOnly(blades);
                    modelSmall.renderOnly(shields);
                    GL11.glPopMatrix();

                    GL11.glPushMatrix();
                    GL11.glRotated(-Math.toDegrees(tile.rotation), 0, 1, 0);
                    modelSmall.renderOnly(mediumBlades);
                    modelSmall.renderOnly(mediumShields);
                    GL11.glPopMatrix();

                    modelSmall.renderAllExcept(ArrayUtils.addAll(ArrayUtils.addAll(blades, shields), ArrayUtils.addAll(mediumShields, mediumBlades)));
                }

                GL11.glPopMatrix();
            }
        }
    }
}
*/
