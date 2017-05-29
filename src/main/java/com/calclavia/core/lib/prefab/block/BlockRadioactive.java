package com.calclavia.core.lib.prefab.block;

import com.calclavia.core.lib.prefab.poison.Poison;
import com.calclavia.core.lib.prefab.poison.PoisonRadiation;
import com.calclavia.core.lib.transform.vector.Vector3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockMycelium;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.World;

public class BlockRadioactive
        extends Block
{
    public static int RECOMMENDED_ID = 3768;
    public boolean canSpread = true;
    public float radius = 5.0F;
    public int amplifier = 2;
    public boolean canWalkPoison = true;
    public boolean isRandomlyRadioactive = true;
    public boolean spawnParticle = true;
    private Icon iconTop;
    private Icon iconBottom;

    public BlockRadioactive(int id, Material material)
    {
        super(id, material);
        func_71907_b(true);
        func_71848_c(0.2F);
        func_71900_a(0.1F);
    }

    public BlockRadioactive(int id)
    {
        this(id, Material.field_76246_e);
    }

    public Icon func_71858_a(int side, int metadata)
    {
        return side == 0 ? this.iconBottom : side == 1 ? this.iconTop : this.field_94336_cN;
    }

    @SideOnly(Side.CLIENT)
    public void func_94332_a(IconRegister iconRegister)
    {
        super.func_94332_a(iconRegister);
        this.iconTop = iconRegister.registerIcon(func_71917_a().replace("tile.", "") + "_top");
        this.iconBottom = iconRegister.registerIcon(func_71917_a().replace("tile.", "") + "_bottom");
    }

    public void func_71921_a(World world, int x, int y, int z, EntityPlayer par5EntityPlayer)
    {
        if (world.rand.nextFloat() > 0.8D) {
            func_71847_b(world, x, y, z, world.rand);
        }
    }

    public void func_71847_b(World world, int x, int y, int z, Random rand)
    {
        if (!world.field_72995_K)
        {
            if (this.isRandomlyRadioactive)
            {
                AxisAlignedBB bounds = AxisAlignedBB.func_72330_a(x - this.radius, y - this.radius, z - this.radius, x + this.radius, y + this.radius, z + this.radius);
                List<EntityLivingBase> entitiesNearby = world.func_72872_a(EntityLivingBase.class, bounds);
                for (EntityLivingBase entity : entitiesNearby) {
                    PoisonRadiation.INSTANCE.poisonEntity(new Vector3(x, y, z), entity, this.amplifier);
                }
            }
            if (this.canSpread)
            {
                for (int i = 0; i < 4; i++)
                {
                    int newX = x + rand.nextInt(3) - 1;
                    int newY = y + rand.nextInt(5) - 3;
                    int newZ = z + rand.nextInt(3) - 1;
                    int blockID = world.func_72798_a(newX, newY, newZ);
                    if ((rand.nextFloat() > 0.4D) && ((blockID == Block.field_72050_aA.field_71990_ca) || (blockID == Block.field_71980_u.field_71990_ca))) {
                        world.func_72921_c(newX, newY, newZ, this.field_71990_ca, 2);
                    }
                }
                if (rand.nextFloat() > 0.85D) {
                    world.func_72921_c(x, y, z, Block.field_71994_by.field_71990_ca, 2);
                }
            }
        }
    }

    public void func_71891_b(World par1World, int x, int y, int z, Entity par5Entity)
    {
        if (((par5Entity instanceof EntityLiving)) && (this.canWalkPoison)) {
            PoisonRadiation.INSTANCE.poisonEntity(new Vector3(x, y, z), (EntityLiving)par5Entity);
        }
    }

    public int func_71925_a(Random par1Random)
    {
        return 0;
    }

    @SideOnly(Side.CLIENT)
    public void func_71862_a(World world, int x, int y, int z, Random par5Random)
    {
        if (this.spawnParticle) {
            if (Minecraft.getMinecraft().gameSettings.particleSetting == 0)
            {
                int radius = 3;
                for (int i = 0; i < 2; i++)
                {
                    Vector3 diDian = new Vector3(x, y, z);

                    diDian.x += Math.random() * radius - radius / 2;
                    diDian.y += Math.random() * radius - radius / 2;
                    diDian.z += Math.random() * radius - radius / 2;

                    EntitySmokeFX fx = new EntitySmokeFX(world, diDian.x, diDian.y, diDian.z, (Math.random() - 0.5D) / 2.0D, (Math.random() - 0.5D) / 2.0D, (Math.random() - 0.5D) / 2.0D);
                    fx.setRBGColorF(0.2F, 0.8F, 0.0F);
                    Minecraft.getMinecraft().effectRenderer.addEffect(fx);
                }
            }
        }
    }
}
