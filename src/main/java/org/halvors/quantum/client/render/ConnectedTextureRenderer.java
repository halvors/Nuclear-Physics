package org.halvors.quantum.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.quantum.client.utility.render.BlockRenderUtility;
import org.halvors.quantum.client.utility.render.RenderUtility;
import org.halvors.quantum.common.transform.vector.Vector3;
import org.halvors.quantum.common.utility.WorldUtility;
import org.lwjgl.opengl.GL11;

public class ConnectedTextureRenderer implements IBlockRenderer {
    private Block block;
    private IIcon edgeTexture;

    public ConnectedTextureRenderer(Block block, String edgeTexture) {
        this.block = block;
        this.edgeTexture = RenderUtility.getIcon(edgeTexture);
    }

    @Override
    public boolean renderItem(ItemStack itemStack) {
        GL11.glPushMatrix();
        BlockRenderUtility.tessellateBlockWithConnectedTextures(itemStack.getMetadata(), block, null, edgeTexture);
        GL11.glPopMatrix();

        return true;
    }

    @Override
    public boolean renderStatic(RenderBlocks renderer, IBlockAccess access, int x, int y, int z) {
        TileEntity tile = access.getTileEntity(x, y, z);
        byte sideMap = 0;

        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
            Vector3 check = new Vector3(x, y, z).translate(direction);
            TileEntity checkTile = check.getTileEntity(access);

            if (checkTile != null && checkTile.getClass() == tile.getClass() && check.getBlockMetadata(access) == tile.getBlockMetadata()) {
                sideMap = WorldUtility.setEnableSide(sideMap, direction, true);
            }
        }

        BlockRenderUtility.tessellateBlockWithConnectedTextures(sideMap, access, x, y, z, block, null, edgeTexture);

        return true;
    }
}