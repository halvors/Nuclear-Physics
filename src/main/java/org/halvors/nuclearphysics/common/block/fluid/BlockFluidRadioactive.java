package org.halvors.nuclearphysics.common.block.fluid;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import org.halvors.nuclearphysics.common.ConfigurationManager.General;
import org.halvors.nuclearphysics.common.init.ModPotions;

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
                EntitySmokeFX fx = new EntitySmokeFX(world, x + random.nextFloat(), y + 1, z + random.nextFloat(), (random.nextDouble() - 0.5) / 2, (random.nextDouble() - 0.5) / 2, (random.nextDouble() - 0.5) / 2);
                fx.setRBGColorF(0.2F, 0.8F, 0);
                Minecraft.getMinecraft().effectRenderer.addEffect(fx);
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