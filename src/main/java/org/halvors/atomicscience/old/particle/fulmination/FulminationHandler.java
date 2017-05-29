package org.halvors.atomicscience.old.particle.fulmination;

import atomicscience.AtomicScience;
import calclavia.api.icbm.explosion.ExplosionEvent.DoExplosionEvent;
import calclavia.api.icbm.explosion.IExplosion;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeSubscribe;
import universalelectricity.api.energy.EnergyStorageHandler;
import universalelectricity.api.vector.Vector3;

public class FulminationHandler
{
    public static final FulminationHandler INSTANCE = new FulminationHandler();
    public static final List<TileFulmination> list = new ArrayList();

    public void register(TileFulmination tileEntity)
    {
        if (!list.contains(tileEntity)) {
            list.add(tileEntity);
        }
    }

    public void unregister(TileFulmination tileEntity)
    {
        list.remove(tileEntity);
    }

    @ForgeSubscribe
    public void BaoZha(ExplosionEvent.DoExplosionEvent evt)
    {
        float maxEnergyPerGenerator;
        if (evt.iExplosion != null) {
            if ((evt.iExplosion.getRadius() > 0.0F) && (evt.iExplosion.getEnergy() > 0L))
            {
                HashSet<TileFulmination> avaliableGenerators = new HashSet();
                for (TileFulmination tileEntity : list) {
                    if (tileEntity != null) {
                        if (!tileEntity.func_70320_p())
                        {
                            Vector3 tileDiDian = new Vector3(tileEntity);
                            tileDiDian.translate(0.5D);
                            double juLi = tileDiDian.distance(new Vector3(evt.x, evt.y, evt.z));
                            if ((juLi <= evt.iExplosion.getRadius()) && (juLi > 0.0D))
                            {
                                float miDu = evt.world.func_72842_a(Vec3.func_72443_a(evt.x, evt.y, evt.z), AtomicScience.blockFulmination.func_71872_e(evt.world, tileEntity.field_70329_l, tileEntity.field_70330_m, tileEntity.field_70327_n));
                                if (miDu < 1.0F) {
                                    avaliableGenerators.add(tileEntity);
                                }
                            }
                        }
                    }
                }
                float totalEnergy = (float)evt.iExplosion.getEnergy();
                maxEnergyPerGenerator = totalEnergy / avaliableGenerators.size();
                for (TileFulmination tileEntity : avaliableGenerators)
                {
                    float density = evt.world.func_72842_a(Vec3.func_72443_a(evt.x, evt.y, evt.z), AtomicScience.blockFulmination.func_71872_e(evt.world, tileEntity.field_70329_l, tileEntity.field_70330_m, tileEntity.field_70327_n));
                    double juLi = new Vector3(tileEntity).distance(new Vector3(evt.x, evt.y, evt.z));

                    long energy = Math.min(maxEnergyPerGenerator, maxEnergyPerGenerator / (juLi / evt.iExplosion.getRadius()));
                    energy = Math.max((1.0F - density) * (float)energy, 0.0F);
                    tileEntity.energy.receiveEnergy(energy, true);
                }
            }
        }
    }
}
