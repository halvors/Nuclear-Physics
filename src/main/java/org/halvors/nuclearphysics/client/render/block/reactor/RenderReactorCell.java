package org.halvors.nuclearphysics.client.render.block.reactor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import org.halvors.nuclearphysics.client.render.ModelCube;
import org.halvors.nuclearphysics.client.utility.RenderUtility;
import org.halvors.nuclearphysics.common.init.ModFluids;
import org.halvors.nuclearphysics.common.tile.reactor.TileReactorCell;
import org.halvors.nuclearphysics.common.type.Resource;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

@SideOnly(Side.CLIENT)
public class RenderReactorCell extends TileEntitySpecialRenderer {
    private static final IModelCustom model = AdvancedModelLoader.loadModel(ResourceUtility.getResource(Resource.MODEL, "reactor_cell.obj"));
    private static final ResourceLocation texture = ResourceUtility.getResource(Resource.TEXTURE_MODELS, "reactor_cell.png");
    private static final ResourceLocation textureFissile = ResourceUtility.getResource(Resource.TEXTURE_MODELS, "reactor_fissile_material.png");
    private static final ResourceLocation texturePlasma = ResourceUtility.getResource(Resource.TEXTURE_BLOCKS, "fluids/plasma_still.png");

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
        if (tile instanceof TileReactorCell) {
            TileReactorCell tileReactorCell = (TileReactorCell) tile;

            GL11.glPushMatrix();

            bindTexture(texture);

            // Translate to the location of our tile entity
            GL11.glTranslated(x, y, z);
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);

            // Rotate block based on direction.
            RenderUtility.rotateBlockBasedOnDirection(tileReactorCell.getFacing());

            model.renderAll();

            // Render fissile fuel inside reactor.
            IFluidTank tank = tileReactorCell.getTank();
            FluidStack fluidStack = tank.getFluid();
            ItemStack itemStack = tileReactorCell.getStackInSlot(0);

            if (fluidStack != null && fluidStack.isFluidEqual(ModFluids.fluidStackPlasma) && tank.getFluidAmount() > 0) {
                GL11.glPushMatrix();

                bindTexture(texturePlasma);

                // Make glass and fuel transparent.
                GL11.glEnable(GL11.GL_BLEND);

                GL11.glTranslated(0.5, 0, 0.5);
                GL11.glScaled(0.48, 1.8, 0.48);
                ModelCube.instance.render();

                GL11.glDisable(GL11.GL_BLEND);

                GL11.glPopMatrix();
            } else if (itemStack != null) {
                GL11.glPushMatrix();

                bindTexture(textureFissile);

                GL11.glTranslated(0.5, 0, 0.5);
                GL11.glScaled(0.48, 1.8, 0.48);
                RenderHelper.disableStandardItemLighting();
                ModelCube.instance.render();
                RenderHelper.enableStandardItemLighting();

                GL11.glPopMatrix();
            }

            GL11.glPopMatrix();
        }
    }
}