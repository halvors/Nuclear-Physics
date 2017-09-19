package org.halvors.nuclearphysics.client.render.block.reactor;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.event.TextureEventHandler;
import org.halvors.nuclearphysics.client.event.TextureEventHandler.FluidType;
import org.halvors.nuclearphysics.client.render.block.Model3D;
import org.halvors.nuclearphysics.client.render.block.RenderTile;
import org.halvors.nuclearphysics.common.init.ModFluids;
import org.halvors.nuclearphysics.common.tile.reactor.TileReactorCell;

@SideOnly(Side.CLIENT)
public class RenderReactorCell extends RenderTile<TileReactorCell> {
    private static final Model3D modelPlasma = new Model3D(0.26, 0.1, 0.26, 0.74, 0.9, 0.74);
    private static final Model3D modelFissileFuel = new Model3D(0.26, 0.1, 0.26, 0.74, 0.9, 0.74);

    @Override
    public void render(TileReactorCell tile, double x, double y, double z, float partialTicks, int destroyStage) {
        // Render fissile fuel inside reactor.
        IFluidTank tank = tile.getTank();
        FluidStack fluidStack = tank.getFluid();
        ItemStack itemStack = tile.getInventory().getStackInSlot(0);

        if (fluidStack != null && fluidStack.isFluidEqual(ModFluids.fluidStackPlasma) && tank.getFluidAmount() > 0) {
            GlStateManager.pushMatrix();

            // Make glass and fuel transparent.
            GlStateManager.enableBlend();
            GlStateManager.disableAlpha();

            if (modelPlasma.getTexture() == null) {
                modelPlasma.setTexture(TextureEventHandler.getFluidTexture(fluidStack.getFluid(), FluidType.STILL));
            }

            modelPlasma.render();

            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();

            GlStateManager.popMatrix();
        } else if (itemStack != null) {
            GlStateManager.pushMatrix();

            // Make glass transparent.
            GlStateManager.disableAlpha();

            if (modelFissileFuel.getTexture() == null) {
                modelFissileFuel.setTexture(TextureEventHandler.getTexture("reactor_fissile_material"));
            }

            modelFissileFuel.render();

            GlStateManager.enableAlpha();

            GlStateManager.popMatrix();
        }
    }
}