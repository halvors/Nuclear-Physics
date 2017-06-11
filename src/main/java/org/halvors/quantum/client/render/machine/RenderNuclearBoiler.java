package org.halvors.quantum.client.render.machine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.tile.machine.TileNuclearBoiler;
import org.halvors.quantum.lib.utility.RenderUtility;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderNuclearBoiler extends TileEntitySpecialRenderer {
    private static final IModelCustom model = AdvancedModelLoader.loadModel(new ResourceLocation(Reference.PREFIX + "models/nuclearBoiler.obj"));
    private static final ResourceLocation texture = new ResourceLocation(Reference.PREFIX + "textures/models/nuclearBoiler.png");

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float f) {
        if (tileEntity instanceof TileNuclearBoiler) {
            TileNuclearBoiler tileNuclearBoiler = (TileNuclearBoiler) tileEntity;

            GL11.glPushMatrix();
            GL11.glTranslated(x + 0.5F, y, z + 0.5F);
            GL11.glRotatef(90, 0, 1, 0);

            if (tileNuclearBoiler.getWorld() != null) {
                RenderUtility.rotateBlockBasedOnDirection(tileNuclearBoiler.getDirection());
            }

            RenderUtility.bind(texture);

            model.renderOnly("FUEL BAR SUPPORT 1 ROTATES", "FUEL BAR 1 ROTATES");
            model.renderOnly("FUEL BAR SUPPORT 2 ROTATES", "FUEL BAR 2 ROTATES");
            //model.renderOnlyAroundPivot(Math.toDegrees(tileNuclearBoiler.rotation), 0, 1, 0, "FUEL BAR SUPPORT 1 ROTATES", "FUEL BAR 1 ROTATES");
            //model.renderOnlyAroundPivot(-Math.toDegrees(tileNuclearBoiler.rotation), 0, 1, 0, "FUEL BAR SUPPORT 2 ROTATES", "FUEL BAR 2 ROTATES");
            model.renderAllExcept("FUEL BAR SUPPORT 1 ROTATES", "FUEL BAR SUPPORT 2 ROTATES", "FUEL BAR 1 ROTATES", "FUEL BAR 2 ROTATES");
            GL11.glPopMatrix();
        }
    }
}