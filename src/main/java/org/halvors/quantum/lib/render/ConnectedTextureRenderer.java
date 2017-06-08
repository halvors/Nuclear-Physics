package org.halvors.quantum.lib.render;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.quantum.common.transform.vector.Vector3;
import org.halvors.quantum.lib.tile.TileRender;
import org.halvors.quantum.lib.utility.RenderUtility;
import org.halvors.quantum.lib.utility.WorldUtility;
import org.halvors.quantum.lib.utility.RenderBlockUtility;
import org.lwjgl.opengl.GL11;

public class ConnectedTextureRenderer extends TileRender {
    private TileEntity tile;
    private String edgeTexture;

    public ConnectedTextureRenderer(TileEntity tile, String edgeTexture) {
        this.tile = tile;
        this.edgeTexture = edgeTexture;
    }

    @Override
    public boolean renderItem(ItemStack itemStack) {
        GL11.glPushMatrix();
        GL11.glTranslated(0.5, 0.5, 0.5);
        RenderBlockUtility.tessellateBlockWithConnectedTextures(itemStack.getMetadata(), tile.getBlockType(), null, RenderUtility.getIcon(edgeTexture));
        GL11.glPopMatrix();

        return true;
    }

    @Override
    public boolean renderStatic(RenderBlocks renderer, Vector3 position) {
        World world = tile.getWorld();
        int x = tile.xCoord;
        int y = tile.yCoord;
        int z = tile.zCoord;
        byte sideMap = 0;

        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
            Vector3 check = new Vector3(tile).translate(direction);
            TileEntity checkTile = check.getTileEntity(world);

            if (checkTile != null && checkTile.getClass() == tile.getClass() && check.getBlockMetadata(world) == tile.getBlockMetadata()) {
                sideMap = WorldUtility.setEnableSide(sideMap, direction, true);
            }
        }

        RenderBlockUtility.tessellateBlockWithConnectedTextures(sideMap, world, x, y, z, tile.getBlockType(), null, RenderUtility.getIcon(edgeTexture));

        return true;
    }
}