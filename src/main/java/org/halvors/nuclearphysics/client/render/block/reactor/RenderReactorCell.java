package org.halvors.nuclearphysics.client.render.block.reactor;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.event.TextureEventHandler;
import org.halvors.nuclearphysics.client.event.TextureEventHandler.FluidType;
import org.halvors.nuclearphysics.client.render.block.Model3D;
import org.halvors.nuclearphysics.client.render.block.OBJModelContainer;
import org.halvors.nuclearphysics.client.utility.RenderUtility;
import org.halvors.nuclearphysics.common.block.reactor.BlockReactorCell.EnumReactorCell;
import org.halvors.nuclearphysics.common.block.states.BlockStateReactorCell;
import org.halvors.nuclearphysics.common.init.ModFluids;
import org.halvors.nuclearphysics.common.tile.reactor.TileReactorCell;
import org.halvors.nuclearphysics.common.utility.ResourceUtility;
import org.halvors.nuclearphysics.common.utility.type.ResourceType;

import java.util.Arrays;

@SideOnly(Side.CLIENT)
public class RenderReactorCell extends TileEntitySpecialRenderer<TileReactorCell> {
    private static final OBJModelContainer model = new OBJModelContainer(ResourceUtility.getResource(ResourceType.MODEL, "reactor_cell.obj"), Arrays.asList("BackLeftSpike", "BackRightSpike", "Base", "BaseDepth", "BaseWidth", "BottomPad", "FrontLeftSpike", "FrontRightSpike", "HatCover", "HatDepth", "HatMiddle", "HatTop", "HatWidth", "MiddleBackLeft", "MiddleBackRight", "MiddleFrontLeft", "MiddleFrontRight", "MiddlePBack", "MiddlePFront", "MiddlePLeft", "MiddlePRight", "OPBackLeft", "OPBackRight", "OPFrontLeft", "OPFrontRight", "OPLeftBack", "OPLeftFront1", "OPLeftFront2", "OPRightBack", "TopBase", "TopBaseDepth", "TopBaseWidth"));
    private static final OBJModelContainer modelTop = new OBJModelContainer(ResourceUtility.getResource(ResourceType.MODEL, "reactor_cell_top.obj"), Arrays.asList("BackLeftSpike", "BackRightSpike", "FrontLeftSpike", "FrontRightSpike", "HatCover", "HatDepth", "HatMiddle", "HatTop", "HatWidth", "MiddleBackLeft", "MiddleBackRight", "MiddleFrontLeft", "MiddleFrontRight", "MiddlePBack", "MiddlePFront", "MiddlePLeft", "MiddlePRight", "OPBackLeft", "OPBackRight", "OPFrontLeft", "OPFrontRight", "OPLeftBack", "OPLeftFront1", "OPLeftFront2", "OPRightBack", "TopBase", "TopBaseDepth", "TopBaseWidth"));
    private static final OBJModelContainer modelMiddle = new OBJModelContainer(ResourceUtility.getResource(ResourceType.MODEL, "reactor_cell_middle.obj"));
    private static final OBJModelContainer modelBottom = new OBJModelContainer(ResourceUtility.getResource(ResourceType.MODEL, "reactor_cell_bottom.obj"));
    private static final Model3D modelPlasma = new Model3D(0.26, 0.1, 0.26, 0.74, 0.9, 0.74);
    private static final Model3D modelFissileFuel = new Model3D(0.26, 0.1, 0.26, 0.74, 0.9, 0.74);

    @Override
    public void renderTileEntityAt(TileReactorCell tile, double x, double y, double z, float partialTicks, int destroyStage) {
        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        GlStateManager.pushMatrix();

        // Translate to the location of our tile entity
        GlStateManager.translate(x, y, z);
        GlStateManager.disableRescaleNormal();

        // Rotate block based on direction.
        RenderUtility.rotateBlockBasedOnDirection(tile.getFacing());

        EnumReactorCell type = tile.getWorld().getBlockState(tile.getPos()).getValue(BlockStateReactorCell.TYPE);

        switch (type) {
            case NORMAL:
                model.render();
                break;

            case TOP:
                modelTop.render();
                break;

            case MIDDLE:
                modelMiddle.render();
                break;

            case BOTTOM:
                modelBottom.render();
                break;
        }

        // Render fissile fuel inside reactor.
        if (tile.getMultiBlock().isPrimary()) {
            IFluidTank tank = tile.getTank();
            FluidStack fluidStack = tank.getFluid();
            ItemStack itemStack = tile.getMultiBlock().get().getInventory().getStackInSlot(0);

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
                float height = tile.getHeight() * (((float) itemStack.getMaxDamage() - itemStack.getMetadata()) / (float) itemStack.getMaxDamage());

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

        GlStateManager.popMatrix();
    }
}