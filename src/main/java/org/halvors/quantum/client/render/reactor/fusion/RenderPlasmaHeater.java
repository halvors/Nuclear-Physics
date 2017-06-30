package org.halvors.quantum.client.render.reactor.fusion;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.client.utility.render.RenderUtility;
import org.halvors.quantum.common.tile.reactor.fusion.TilePlasmaHeater;
import org.halvors.quantum.common.utility.ResourceUtility;
import org.halvors.quantum.common.utility.type.ResourceType;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderPlasmaHeater extends TileEntitySpecialRenderer {
    private static final IModelCustom model = AdvancedModelLoader.loadModel(ResourceUtility.getResource(ResourceType.MODEL, "plasmaHeater.obj"));
    private static final ResourceLocation texture = ResourceUtility.getResource(ResourceType.TEXTURE_MODELS, "plasmaHeater.png");

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks, int destroyStage) {
        if (tile instanceof TilePlasmaHeater) {
            TilePlasmaHeater tilePlasmaHeater = (TilePlasmaHeater) tile;

            GL11.glPushMatrix();
            GL11.glTranslated(x + 0.5, y, z + 0.5);

            RenderUtility.bind(texture);

            GL11.glPushMatrix();
            GL11.glRotated(Math.toDegrees(tilePlasmaHeater.rotation), 0, 1, 0);
            model.renderOnly("rrot", "srot");
            GL11.glPopMatrix();

            model.renderAllExcept("rrot", "srot");
            GL11.glPopMatrix();
        }
    }
}