package org.halvors.nuclearphysics.client.render.block;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.client.utility.RenderUtility;
import org.halvors.nuclearphysics.common.tile.ITileRotatable;

@SideOnly(Side.CLIENT)
public abstract class RenderTile<T extends TileEntity> extends TileEntitySpecialRenderer<T> {
    @Override
    public void renderTileEntityAt(T tile, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushMatrix();

        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        // Translate to the location of our tile entity
        GlStateManager.translate(x, y, z);
        GlStateManager.disableRescaleNormal();

        // Rotate block based on direction.
        if (tile instanceof ITileRotatable) {
            RenderUtility.rotateBlockBasedOnDirection(((ITileRotatable) tile).getFacing());
        }

        render(tile, x, y, z);

        GlStateManager.popMatrix();
    }

    protected abstract void render(T tile, double x, double y, double z);
}
