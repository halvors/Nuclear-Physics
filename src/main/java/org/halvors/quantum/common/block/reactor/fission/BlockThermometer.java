package org.halvors.quantum.common.block.reactor.fission;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.block.BlockRotatable;
import org.halvors.quantum.common.item.block.ItemBlockSaved;
import org.halvors.quantum.common.item.block.ItemBlockThermometer;
import org.halvors.quantum.common.tile.reactor.fission.TileThermometer;
import org.halvors.quantum.common.utility.InventoryUtility;
import org.halvors.quantum.common.utility.WrenchUtility;
import org.halvors.quantum.common.utility.transform.vector.Vector3;

import java.util.ArrayList;

public class BlockThermometer extends BlockRotatable {
    private static IIcon iconSide;

    public BlockThermometer() {
        super("thermometer", Material.piston);

        setTextureName(Reference.PREFIX + "thermometer");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return side == 1 || side == 0 ? super.getIcon(side, meta) : iconSide;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        super.registerIcons(iconRegister);

        iconSide = iconRegister.registerIcon(Reference.PREFIX + "machine");
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int facing, float playerX, float playerY, float playerZ) {
        TileEntity tile = world.getTileEntity(x, y, z);

        if (tile instanceof TileThermometer) {
            TileThermometer tileThermometer = (TileThermometer) tile;

            if (WrenchUtility.hasUsableWrench(player, x, y, z)) {
                if (player.isSneaking()) {
                    tileThermometer.setThreshold(tileThermometer.getThershold() - 10);
                } else {
                    tileThermometer.setThreshold(tileThermometer.getThershold() + 10);
                }

                return true;
            }

            if (player.isSneaking()) {
                tileThermometer.setThreshold(tileThermometer.getThershold() + 100);
            } else {
                tileThermometer.setThreshold(tileThermometer.getThershold() - 100);
            }

            return true;
        }

        return false;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack) {
        TileEntity tile = world.getTileEntity(x, y, z);

        // Fetch saved coordinates from ItemBlockThermometer and apply them to the block.
        if (tile instanceof TileThermometer) {
            TileThermometer tileThermometer = (TileThermometer) tile;
            ItemBlockThermometer itemBlockThermometer = (ItemBlockThermometer) itemStack.getItem();
            tileThermometer.setTrackCoordinate(itemBlockThermometer.getSavedCoord(itemStack));
        }
    }

    @Override
    public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z, boolean willHarvest) {
        Block block = world.getBlock(x, y, z);

        if (!player.capabilities.isCreativeMode && !world.isRemote && willHarvest) {
            ItemStack stack = ItemBlockSaved.getItemStackWithNBT(block, world, x, y, z);
            InventoryUtility.dropItemStack(world, new Vector3(x, y, z), stack);
        }

        return world.setBlockToAir(x, y, z);
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public int isProvidingStrongPower(IBlockAccess access, int x, int y, int z, int side) {
        TileEntity tile = access.getTileEntity(x, y, z);

        if (tile instanceof TileThermometer) {
            TileThermometer tileThermometer = (TileThermometer) tile;

            return tileThermometer.isProvidingPower ? 15 : 0;
        }

        return 0;
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        return new ArrayList<>();
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TileThermometer();
    }
}
