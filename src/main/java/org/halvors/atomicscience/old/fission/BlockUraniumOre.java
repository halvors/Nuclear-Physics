package org.halvors.atomicscience.old.fission;

import com.calclavia.core.lib.prefab.block.BlockRadioactive;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import org.halvors.atomicscience.old.Settings;
import org.halvors.atomicscience.old.TabAS;

public class BlockUraniumOre extends BlockRadioactive
{
    public BlockUraniumOre(int id)
    {
        super(id, Material.field_76246_e);
        func_71864_b("atomicscience:oreUranium");
        func_71884_a(field_71976_h);
        setCreativeTab(TabAS.INSTANCE);
        func_71848_c(2.0F);
        func_111022_d("atomicscience:oreUranium");

        this.isRandomlyRadioactive = Settings.allowRadioactiveOres;
        this.canWalkPoison = Settings.allowRadioactiveOres;
        this.canSpread = false;
        this.radius = 1.0F;
        this.amplifier = 0;
        this.spawnParticle = false;
    }

    @SideOnly(Side.CLIENT)
    public void func_71862_a(World world, int x, int y, int z, Random par5Random)
    {
        if (Settings.allowRadioactiveOres) {
            super.func_71862_a(world, x, y, z, par5Random);
        }
    }

    public Icon func_71858_a(int side, int metadata)
    {
        return this.field_94336_cN;
    }

    public int func_71925_a(Random par1Random)
    {
        return 1;
    }
}
