package org.halvors.quantum.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import org.halvors.quantum.Quantum;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.common.poison.PoisonRadiation;
import org.halvors.quantum.common.transform.vector.Vector3;

import java.util.Random;

public class BlockToxicWaste extends BlockFluidClassic {
    public BlockToxicWaste() {
        super(Quantum.fluidToxicWaste, Material.water);

        setTickRate(20);

        setUnlocalizedName("toxicWaste");
        setTextureName(Reference.PREFIX + "toxicWaste");
        setCreativeTab(Quantum.getCreativeTab());
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        if (entity instanceof EntityLivingBase) {
            entity.attackEntityFrom(DamageSource.wither, 3);
            PoisonRadiation.INSTANCE.poisonEntity(new Vector3(x, y, z), (EntityLivingBase) entity, 4);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World world, int x, int y, int z, Random random) {
        super.randomDisplayTick(world, x, y, z, random);

        if (random.nextInt(100) == 0) {
            world.spawnParticle("suspended", x + random.nextFloat(), y + maxY, z + random.nextFloat(), 0, 0, 0);
        }

        if (random.nextInt(200) == 0) {
            world.playSound(x, y, z, "liquid.lava", 0.2F + random.nextFloat() * 0.2F, 0.9F + random.nextFloat() * 0.15F, false);
        }
    }
}