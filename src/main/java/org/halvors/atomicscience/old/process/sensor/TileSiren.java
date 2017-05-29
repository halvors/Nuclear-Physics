package org.halvors.atomicscience.old.process.sensor;

import calclavia.lib.content.module.TileBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.UniversalElectricity;
import universalelectricity.api.vector.Vector3;
import universalelectricity.api.vector.VectorWorld;

public class TileSiren
        extends TileBlock
{
    public TileSiren()
    {
        super(UniversalElectricity.machine);
    }

    public void onWorldJoin()
    {
        scheduelTick(1);
    }

    public void onNeighborChanged()
    {
        scheduelTick(1);
    }

    public void func_70316_g()
    {
        int metadata = this.field_70331_k.func_72805_g(x(), y(), z());
        if (this.field_70331_k.func_94577_B(x(), y(), z()) > 0)
        {
            float volume = 0.5F;
            for (int i = 0; i < 6; i++)
            {
                Vector3 check = position().translate(ForgeDirection.getOrientation(i));
                int blockID = check.getBlockID(this.field_70331_k);
                if (blockID == blockID()) {
                    volume *= 1.5F;
                }
            }
            this.field_70331_k.func_72908_a(x(), y(), z(), "atomicscience:alarm", volume, 1.0F - 0.18F * (metadata / 15.0F));
            scheduelTick(30);
        }
    }

    protected boolean configure(EntityPlayer player, int side, Vector3 hit)
    {
        int metadata = world().func_72805_g(x(), y(), z());
        if (player.func_70093_af()) {
            metadata--;
        } else {
            metadata++;
        }
        metadata = Math.max(metadata % 16, 0);

        world().func_72921_c(x(), y(), z(), metadata, 2);
        return true;
    }
}
