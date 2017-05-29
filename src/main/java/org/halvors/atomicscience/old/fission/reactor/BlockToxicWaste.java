package org.halvors.atomicscience.old.fission.reactor;

import calclavia.lib.prefab.poison.Poison;
import calclavia.lib.prefab.poison.PoisonRadiation;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.FluidRegistry;
import universalelectricity.api.vector.Vector3;

public class BlockToxicWaste
        extends BlockFluidClassic
{
    public BlockToxicWaste(int id)
    {
        super(id, FluidRegistry.getFluid("toxicwaste"), Material.field_76244_g);
        setTickRate(20);
    }

    public void func_71862_a(World par1World, int x, int y, int z, Random par5Random)
    {
        super.func_71862_a(par1World, x, y, z, par5Random);
        if (par5Random.nextInt(100) == 0)
        {
            double d5 = x + par5Random.nextFloat();
            double d7 = y + this.field_72022_cl;
            double d6 = z + par5Random.nextFloat();
            par1World.func_72869_a("suspended", d5, d7, d6, 0.0D, 0.0D, 0.0D);
        }
        if (par5Random.nextInt(200) == 0) {
            par1World.func_72980_b(x, y, z, "liquid.lava", 0.2F + par5Random.nextFloat() * 0.2F, 0.9F + par5Random.nextFloat() * 0.15F, false);
        }
    }

    public void func_71869_a(World par1World, int x, int y, int z, Entity entity)
    {
        if ((entity instanceof EntityLivingBase))
        {
            entity.func_70097_a(DamageSource.field_82727_n, 3.0F);
            PoisonRadiation.INSTANCE.poisonEntity(new Vector3(x, y, z), (EntityLivingBase)entity, 4);
        }
    }
}
