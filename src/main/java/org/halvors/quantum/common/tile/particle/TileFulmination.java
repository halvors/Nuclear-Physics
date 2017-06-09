package org.halvors.quantum.common.tile.particle;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.quantum.common.Reference;
import org.halvors.quantum.lib.prefab.tile.TileElectrical;
import org.halvors.quantum.lib.render.ConnectedTextureRenderer;
import org.halvors.quantum.lib.tile.TileRender;
import universalelectricity.api.electricity.IVoltageOutput;
import universalelectricity.api.energy.EnergyStorageHandler;

import java.util.EnumSet;

public class TileFulmination extends TileElectrical implements IVoltageOutput {
    private static final long energyCapacity = 10000000000000L;

    public TileFulmination() {
        super(Material.iron);

        energy = new EnergyStorageHandler(energyCapacity);
        blockHardness = 10;
        blockResistance = 25000;
    }

    @Override
    public void initiate() {
        super.initiate();

        FulminationHandler.INSTANCE.register(this);
    }

    @Override
    public void invalidate() {
        FulminationHandler.INSTANCE.unregister(this);

        super.initiate();
    }

    @Override
    public void updateEntity() {
        super.updateEntity();

        produce();

        // Slowly lose energy.
        energy.extractEnergy(1, true);
    }

    @Override
    public long onReceiveEnergy(ForgeDirection from, long receive, boolean doReceive)
    {
        return 0;
    }

    @Override
    public EnumSet<ForgeDirection> getInputDirections() {
        return EnumSet.noneOf(ForgeDirection.class);
    }

    @Override
    public EnumSet<ForgeDirection> getOutputDirections() {
        return EnumSet.allOf(ForgeDirection.class);
    }

    @Override
    public long getVoltageOutput(ForgeDirection side)
    {
        return 10000000000L;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockAccess access, int x, int y, int z, int side) {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected TileRender newRenderer() {
        return new ConnectedTextureRenderer(this, Reference.PREFIX + "atomic_edge");
    }
}
