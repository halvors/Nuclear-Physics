package org.halvors.atomicscience.old.fusion;

import calclavia.api.atomicscience.PlasmaEvent.SpawnPlasmaEvent;
import calclavia.lib.config.Config;
import calclavia.lib.content.module.TileBase;
import calclavia.lib.prefab.vector.Cuboid;
import calclavia.lib.thermal.ThermalGrid;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.EventBus;
import universalelectricity.api.vector.Vector3;
import universalelectricity.api.vector.VectorWorld;

public class TilePlasma
        extends TileBase
{
    @Config
    public static int plasmaMaxTemperature = 1000000;
    private float temperature = plasmaMaxTemperature;

    public TilePlasma()
    {
        super(Material.field_76256_h);
        this.textureName = "plasma";
        this.isOpaqueCube = false;
    }

    public int getLightValue(IBlockAccess access)
    {
        return 7;
    }

    public boolean isSolid(IBlockAccess access, int side)
    {
        return false;
    }

    public Iterable<Cuboid> getCollisionBoxes()
    {
        return new ArrayList();
    }

    public ArrayList<ItemStack> getDrops(int metadata, int fortune)
    {
        return new ArrayList();
    }

    public int getRenderBlockPass()
    {
        return 1;
    }

    public void collide(Entity entity)
    {
        entity.func_70097_a(DamageSource.field_76372_a, 100.0F);
    }

    public void func_70316_g()
    {
        super.func_70316_g();
        ThermalGrid.instance().addTemperature(new VectorWorld(this), (this.temperature - ThermalGrid.instance().getTemperature(new VectorWorld(this))) * 0.1F);
        if (this.ticks % 20L == 0L)
        {
            this.temperature = ((float)(this.temperature / 1.5D));
            if (this.temperature <= plasmaMaxTemperature / 10)
            {
                this.field_70331_k.func_72832_d(this.field_70329_l, this.field_70330_m, this.field_70327_n, Block.field_72067_ar.field_71990_ca, 0, 3);
                return;
            }
            for (int i = 0; i < 6; i++) {
                if (this.field_70331_k.field_73012_v.nextFloat() <= 0.4D)
                {
                    Vector3 diDian = new Vector3(this);
                    diDian.translate(ForgeDirection.getOrientation(i));

                    TileEntity tileEntity = diDian.getTileEntity(this.field_70331_k);
                    if (!(tileEntity instanceof TilePlasma)) {
                        MinecraftForge.EVENT_BUS.post(new PlasmaEvent.SpawnPlasmaEvent(this.field_70331_k, diDian.intX(), diDian.intY(), diDian.intZ(), (int)this.temperature));
                    }
                }
            }
        }
    }

    public void setTemperature(int newTemperature)
    {
        this.temperature = newTemperature;
    }
}
