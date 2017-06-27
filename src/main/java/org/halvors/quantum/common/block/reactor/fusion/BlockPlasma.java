package org.halvors.quantum.common.block.reactor.fusion;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import org.halvors.quantum.common.block.BlockTextured;
import org.halvors.quantum.common.tile.reactor.fusion.TilePlasma;
import org.halvors.quantum.common.utility.transform.vector.Cuboid;

import java.util.ArrayList;

public class BlockPlasma extends BlockTextured {
    public BlockPlasma() {
        super("plasma", Material.lava);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public int getRenderBlockPass() {
        return 1;
    }

    @Override
    public int getLightValue(IBlockAccess access, int x, int y, int z) {
        return 7;
    }

    @Override
    public boolean isBlockSolid(IBlockAccess access, int x, int y, int z, int side) {
        return false;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
        return new Cuboid().toAABB();
    }

    @Override
    public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int metadata, int fortune) {
        return new ArrayList<>();
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        entity.attackEntityFrom(DamageSource.inFire, 100);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metadata) {
        return new TilePlasma();
    }
}
