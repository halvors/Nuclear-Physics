package org.halvors.quantum.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.effect.poison.PoisonRadiation;
import org.halvors.quantum.common.utility.transform.vector.Vector3;

import java.util.Random;

public class BlockToxicWaste extends BlockFluidClassic {
    public BlockToxicWaste() {
        super(Quantum.fluidToxicWaste, Material.WATER);

        setUnlocalizedName("toxicWaste");
        setRegistryName(Reference.ID, "toxicWaste");
        setCreativeTab(Quantum.getCreativeTab());
        setTickRate(20);
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        if (entity instanceof EntityLivingBase) {
            entity.attackEntityFrom(DamageSource.wither, 3);
            PoisonRadiation.getInstance().poisonEntity(pos, (EntityLivingBase) entity, 4);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random random) {
        super.randomDisplayTick(state, world, pos, random);

        if (random.nextInt(100) == 0) {
            // TODO: Check if Y paramter should be pos.getY() + maxY?
            world.spawnParticle(EnumParticleTypes.SUSPENDED, pos.getX() + random.nextFloat(), pos.getY(), pos.getZ() + random.nextFloat(), 0, 0, 0);
        }

        if (random.nextInt(200) == 0) {
            //world.playSound(x, y, z, "liquid.lava", 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
        }
    }
}