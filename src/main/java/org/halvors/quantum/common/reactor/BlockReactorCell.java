package org.halvors.quantum.common.reactor;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.transform.vector.Cuboid;
import org.halvors.quantum.common.transform.vector.Vector3;
import org.halvors.quantum.lib.render.block.BlockRenderingHandler;
import org.halvors.quantum.lib.utility.inventory.InventoryUtility;

public class BlockReactorCell extends BlockContainer {
    public BlockReactorCell() {
        super(Material.iron);

        setUnlocalizedName("reactor");
        setTextureName(Reference.PREFIX + "machine");
        setCreativeTab(Quantum.getCreativeTab());
        setHardness(1.0F);
        setResistance(1.0F);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isOpaqueCube() {
        return false;
    }

    /*
    @Override
    @SideOnly(Side.CLIENT)
    public int getRenderType() {
        return BlockRenderingHandler.ID; //-1
    }
    */

    @Override
    public boolean shouldSideBeRendered(IBlockAccess block, int x, int y, int z, int side) {
        return side == 0 && Cuboid.full().min.y > 0.0D;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int facing, float playerX, float playerY, float playerZ) {
        if (world.isRemote) {
            TileReactorCell tileEntity = (TileReactorCell) world.getTileEntity(x, y, z);

            if (!player.isSneaking()) {
                if (tileEntity.getStackInSlot(0) != null) {
                    InventoryUtility.dropItemStack(world, new Vector3(player), tileEntity.getStackInSlot(0), 0);
                    tileEntity.setInventorySlotContents(0, null);

                    return true;
                }

                if (player.inventory.getCurrentItem() != null) {
                    if (player.inventory.getCurrentItem().getItem() instanceof IReactorComponent) {
                        ItemStack itemStack = player.inventory.getCurrentItem().copy();
                        itemStack.stackSize = 1;
                        tileEntity.setInventorySlotContents(0, itemStack);
                        player.inventory.decrStackSize(player.inventory.currentItem, 1);
                    }

                    return true;
                }
            }

            player.openGui(Quantum.getInstance(), 0, world, tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord);

            return true;
        }

        return false;
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileReactorCell();
    }
}
