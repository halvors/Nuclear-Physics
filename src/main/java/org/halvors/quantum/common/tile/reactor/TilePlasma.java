package org.halvors.quantum.common.tile.reactor;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.quantum.common.transform.vector.Cuboid;
import org.halvors.quantum.common.transform.vector.Vector3;
import org.halvors.quantum.common.transform.vector.VectorWorld;
import org.halvors.quantum.lib.event.PlasmaEvent;
import org.halvors.quantum.lib.thermal.ThermalGrid;
import org.halvors.quantum.lib.tile.TileBase;

import java.util.ArrayList;

public class TilePlasma extends TileBase {
    public static int plasmaMaxTemperature = 1000000;
    private float temperature = plasmaMaxTemperature;

    public TilePlasma() {
        super(Material.lava);

        textureName = "plasma";
        isOpaqueCube = false;
    }

    @Override
    public int getLightValue(IBlockAccess access) {
        return 7;
    }

    @Override
    public boolean isSolid(IBlockAccess access, int side) {
        return false;
    }

    @Override
    public Iterable<Cuboid> getCollisionBoxes() {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<ItemStack> getDrops(int metadata, int fortune)  {
        return new ArrayList<>();
    }

    @Override
    public int getRenderBlockPass() {
        return 1;
    }

    @Override
    public void collide(Entity entity) {
        entity.attackEntityFrom(DamageSource.inFire, 100.0F);
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        ThermalGrid.addTemperature(new VectorWorld(this), (temperature - ThermalGrid.getTemperature(new VectorWorld(this))) * 0.1F);

        if (ticks % 20L == 0L) {
            temperature = (float) (temperature / 1.5D);

            if (temperature <= plasmaMaxTemperature / 10) {
                worldObj.setBlock(xCoord, yCoord, zCoord, block, 0, 3);

                return;
            }

            for (int i = 0; i < 6; i++) {
                if (worldObj.rand.nextFloat() <= 0.4D) {
                    Vector3 pos = new Vector3(this);
                    pos.translate(ForgeDirection.getOrientation(i));

                    TileEntity tileEntity = pos.getTileEntity(worldObj);

                    if (!(tileEntity instanceof TilePlasma)) {
                        MinecraftForge.EVENT_BUS.post(new PlasmaEvent.SpawnPlasmaEvent(worldObj, pos.intX(), pos.intY(), pos.intZ(), (int) temperature));
                    }
                }
            }
        }
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }
}
