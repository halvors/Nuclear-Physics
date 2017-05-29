package org.halvors.atomicscience.old.particle.fulmination;

import calclavia.lib.content.module.TileRender;
import calclavia.lib.prefab.tile.TileElectrical;
import calclavia.lib.render.ConnectedTextureRenderer;
import java.util.EnumSet;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ForgeDirection;
import universalelectricity.api.electricity.IVoltageOutput;
import universalelectricity.api.energy.EnergyStorageHandler;

public class TileFulmination
        extends TileElectrical
        implements IVoltageOutput
{
    private static final long DIAN = 10000000000000L;

    public TileFulmination()
    {
        super(Material.field_76243_f);
        this.energy = new EnergyStorageHandler(10000000000000L);
        this.blockHardness = 50.0F;
        this.blockResistance = 25000.0F;
    }

    public void initiate()
    {
        super.initiate();
        FulminationHandler.INSTANCE.register(this);
    }

    public void func_70316_g()
    {
        super.func_70316_g();
        produce();

        this.energy.extractEnergy(1L, true);
    }

    public void func_70313_j()
    {
        FulminationHandler.INSTANCE.unregister(this);
        super.initiate();
    }

    public long onReceiveEnergy(ForgeDirection from, long receive, boolean doReceive)
    {
        return 0L;
    }

    public EnumSet<ForgeDirection> getInputDirections()
    {
        return EnumSet.noneOf(ForgeDirection.class);
    }

    public EnumSet<ForgeDirection> getOutputDirections()
    {
        return EnumSet.allOf(ForgeDirection.class);
    }

    public long getVoltageOutput(ForgeDirection side)
    {
        return 10000000000L;
    }

    protected TileRender newRenderer()
    {
        return new ConnectedTextureRenderer(this, "atomicscience:atomic_edge");
    }
}
