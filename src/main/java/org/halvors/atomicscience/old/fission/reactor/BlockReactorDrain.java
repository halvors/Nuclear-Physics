package org.halvors.atomicscience.old.fission.reactor;

import calclavia.lib.prefab.block.BlockRotatable;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import universalelectricity.api.UniversalElectricity;

public class BlockReactorDrain extends BlockRotatable
{
    private Icon frontIcon;

    public BlockReactorDrain(int id) {
        super(id, UniversalElectricity.machine);
    }

    public void func_71860_a(World world, int x, int y, int z, EntityLivingBase entityLiving, ItemStack itemStack)
    {
        if ((MathHelper.func_76135_e((float)entityLiving.field_70165_t - x) < 2.0F) && (MathHelper.func_76135_e((float)entityLiving.field_70161_v - z) < 2.0F))
        {
            double d0 = entityLiving.field_70163_u + 1.82D - entityLiving.field_70129_M;
            if (d0 - y > 2.0D)
            {
                world.setBlockMetadataWithNotify(x, y, z, 1, 3);
                return;
            }
            if (y - d0 > 0.0D)
            {
                world.setBlockMetadataWithNotify(x, y, z, 0, 3);
                return;
            }
        }
        super.func_71860_a(world, x, y, z, entityLiving, itemStack);
    }

    public Icon func_71858_a(int side, int metadata)
    {
        if (side == metadata) {
            return this.frontIcon;
        }

        return this.field_94336_cN;
    }

    @SideOnly(Side.CLIENT)
    public void func_94332_a(IconRegister iconRegister)
    {
        super.func_94332_a(iconRegister);
        this.frontIcon = iconRegister.func_94245_a(func_71917_a().replace("tile.", "") + "_front");
    }

    public TileEntity func_72274_a(World var1)
    {
        return new TileReactorDrain();
    }
}
