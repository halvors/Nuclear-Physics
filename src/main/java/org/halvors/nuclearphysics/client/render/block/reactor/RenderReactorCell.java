package org.halvors.nuclearphysics.client.render.block.reactor;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.event.TextureEventHandler;
import org.halvors.nuclearphysics.client.event.TextureEventHandler.EnumFluidType;
import org.halvors.nuclearphysics.client.render.block.Model3D;
import org.halvors.nuclearphysics.client.render.block.RenderTile;
import org.halvors.nuclearphysics.common.init.ModFluids;
import org.halvors.nuclearphysics.common.tile.reactor.TileReactorCell;

@SideOnly(Side.CLIENT)
public class RenderReactorCell extends RenderTile<TileReactorCell> {
    private static final Model3D modelPlasma = new Model3D(0.26, 0.1, 0.26, 0.74, 0.9, 0.74);
    private static final Model3D modelFissileFuel = new Model3D(0.26, 0.1, 0.26, 0.74, 0.9, 0.74);

    @Override
    protected void render(final TileReactorCell tile, final double x, final double y, final double z) {
        // Render fissile fuel inside reactor.
        final IFluidTank tank = tile.getTank();
        final FluidStack fluidStack = tank.getFluid();
        final ItemStack itemStack = tile.getInventory().getStackInSlot(0);

        if (fluidStack != null && fluidStack.isFluidEqual(ModFluids.fluidStackPlasma) && tank.getFluidAmount() > 0) {
            renderFuel(modelPlasma, TextureEventHandler.getFluidTexture(fluidStack.getFluid(), EnumFluidType.STILL), true);
        } else if (itemStack != null) {
            renderFuel(modelFissileFuel, TextureEventHandler.getTexture("reactor_fissile_material"), false);
        }
    }

    private void renderFuel(final Model3D model, final TextureAtlasSprite texture, final boolean isTransparent) {
        GlStateManager.pushMatrix();

        // Make fuel transparent.
        if (isTransparent) {
            GlStateManager.enableBlend();
        }

        // Make glass transparent.
        GlStateManager.disableAlpha();

        if (model.getTexture() == null) {
            model.setTexture(texture);
        }

        model.render();

        if (isTransparent) {
            GlStateManager.disableBlend();
        }

        GlStateManager.enableAlpha();

        GlStateManager.popMatrix();
    }
}