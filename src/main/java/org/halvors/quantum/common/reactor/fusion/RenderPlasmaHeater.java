package org.halvors.quantum.common.reactor.fusion;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.lib.utility.RenderUtility;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderPlasmaHeater extends TileEntitySpecialRenderer {
    private final IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation(Reference.PREFIX + "models/fusionReactor.obj"));
    private final ResourceLocation texture =new ResourceLocation(Reference.PREFIX + "textures/models/fusionReactor.png");

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f) {
        if (tileEntity instanceof TilePlasmaHeater) {
            TilePlasmaHeater tilePlasmaHeater = (TilePlasmaHeater) tileEntity;

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