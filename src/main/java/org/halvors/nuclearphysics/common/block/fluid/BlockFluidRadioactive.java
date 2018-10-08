package org.halvors.nuclearphysics.common.block.fluid;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import org.halvors.nuclearphysics.api.BlockPos;
import org.halvors.nuclearphysics.client.utility.RenderUtility;
import org.halvors.nuclearphysics.common.ConfigurationManager.General;
import org.halvors.nuclearphysics.common.init.ModPotions;
import org.halvors.nuclearphysics.common.type.EnumParticleType;

import java.util.Random;

public class BlockFluidRadioactive extends BlockFluidClassic {
    public BlockFluidRadioactive(final Fluid fluid, final Material material) {
        super(fluid, material);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(final World world, final int x, final int y, final int z, final Random random) {
        super.randomDisplayTick(world, x, y, z, random);

        if (General.allowRadioactiveOres && Minecraft.getMinecraft().gameSettings.particleSetting == 0) {
            if (random.nextInt(100) == 0) {
                final BlockPos pos = new BlockPos(x, y, z);
                RenderUtility.renderParticle(EnumParticleType.RADIOACTIVE, world, pos.getX() + random.nextFloat(), pos.getY() + 1, pos.getZ() + random.nextFloat(), (random.nextDouble() - 0.5) / 2, (random.nextDouble() - 0.5) / 2, (random.nextDouble() - 0.5) / 2);
            }
        }
    }

    @Override
    public void onEntityCollidedWithBlock(final World world, final int x, final int y, final int z, final Entity entity) {
        if (entity instanceof EntityLivingBase) {
            entity.attackEntityFrom(ModPotions.poisonRadiation.getDamageSource(), 3);
            ModPotions.poisonRadiation.poisonEntity((EntityLivingBase) entity, 4);
        }
    }
}