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
import org.halvors.nuclearphysics.common.type.EnumResource;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class RenderReactorCell extends TileEntitySpecialRenderer {
    private static final IModelCustom MODEL = AdvancedModelLoader.loadModel(ResourceUtility.getResource(EnumResource.MODEL, "reactor_cell.obj"));
    private static final ResourceLocation TEXTURE = ResourceUtility.getResource(EnumResource.TEXTURE_MODELS, "reactor_cell.png");
    private static final ResourceLocation TEXTURE_FISSILE = ResourceUtility.getResource(EnumResource.TEXTURE_MODELS, "reactor_fissile_material.png");
    private static final ResourceLocation TEXTURE_PLASMA = ResourceUtility.getResource(EnumResource.TEXTURE_BLOCKS, "fluids/plasma_still.png");

    @Override
    public void renderTileEntityAt(final TileEntity tile, final double x, final double y, final double z, final float partialTicks) {
        if (tile instanceof TileReactorCell) {
            final TileReactorCell tileReactorCell = (TileReactorCell) tile;

            GL11.glPushMatrix();

            bindTexture(TEXTURE);

            // Translate to the location of our tile entity
            GL11.glTranslated(x, y, z);
            //GL11.glDisable(GL12.GL_RESCALE_NORMAL);

            // Rotate block based on direction.
            RenderUtility.rotateBlockBasedOnDirection(tileReactorCell.getFacing());

            MODEL.renderAll();

            // Render fissile fuel inside reactor.
            final IFluidTank tank = tileReactorCell.getTank();
            final FluidStack fluidStack = tank.getFluid();
            final ItemStack itemStack = tileReactorCell.getStackInSlot(0);

            if (fluidStack != null && fluidStack.isFluidEqual(ModFluids.fluidStackPlasma) && tank.getFluidAmount() > 0) {
                renderFuel(TEXTURE_PLASMA, true);
            } else if (itemStack != null) {
                renderFuel(TEXTURE_FISSILE, false);
            }

            GL11.glPopMatrix();
        }
    }

    private void renderFuel(final ResourceLocation texture, final boolean isTransparent) {
        GL11.glPushMatrix();

        bindTexture(texture);

        // Make glass and fuel transparent?
        if (isTransparent) {
            GL11.glEnable(GL11.GL_BLEND);
        } else {
            RenderHelper.disableStandardItemLighting();
        }

        GL11.glTranslated(0.5, 0.5, 0.5);
        GL11.glScaled(0.48, 0.9, 0.48);
        ModelCube.instance.render();

        if (isTransparent) {
            GL11.glDisable(GL11.GL_BLEND);
        } else {
            RenderHelper.enableStandardItemLighting();
        }

        GL11.glPopMatrix();
    }
}
