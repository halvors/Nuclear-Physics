package org.halvors.quantum.common.block.reactor.fission;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.api.item.IReactorComponent;
import org.halvors.quantum.client.render.BlockRenderingHandler;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.block.BlockRotatable;
import org.halvors.quantum.common.tile.reactor.fission.TileReactorCell;
import org.halvors.quantum.common.utility.InventoryUtility;
import org.halvors.quantum.common.utility.transform.vector.Vector3;

public class BlockReactorCell extends BlockRotatable {
    public BlockReactorCell() {
        super("reactorCell", Material.IRON);

        setTextureName(Reference.PREFIX + "machine");
        setHardness(1.0F);
        setResistance(1.0F);
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public int getRenderType() {
        return BlockRenderingHandler.getInstance().getRenderId();
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int facing, float playerX, float playerY, float playerZ) {
        TileReactorCell tile = (TileReactorCell) world.getTileEntity(x, y, z);

        if (player.inventory.getCurrentItem() != null) {
            if (tile.getStackInSlot(0) == null) {
                if (player.inventory.getCurrentItem().getItem() instanceof IReactorComponent) {
                    ItemStack itemStack = player.inventory.getCurrentItem().copy();
                    itemStack.stackSize = 1;
                    tile.setInventorySlotContents(0, itemStack);
                    player.inventory.decrStackSize(player.inventory.currentItem, 1);

                    return true;
                }
            }
        } else if (player.isSneaking() && tile.getStackInSlot(0) != null) {
            InventoryUtility.dropItemStack(world, new Vector3(player), tile.getStackInSlot(0), 0);
            tile.setInventorySlotContents(0, null);

            return true;
        } else {
            player.openGui(Quantum.getInstance(), 0, world, tile.getPos().getX(), tile.getPos().getY(), tile.getPos().getZ());

            return true;
        }

        return false;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack) {
        TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

        if (tileEntity instanceof TileReactorCell) {
            ((TileReactorCell) tileEntity).updatePositionStatus();
        }
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));

        if (tile instanceof TileReactorCell) {
            ((TileReactorCell) tile).updatePositionStatus();
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileReactorCell();
    }
}
