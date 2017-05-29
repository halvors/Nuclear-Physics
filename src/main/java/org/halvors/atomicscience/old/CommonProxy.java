package org.halvors.atomicscience.old;

import atomicscience.fission.reactor.ContainerReactorCell;
import atomicscience.fission.reactor.TileReactorCell;
import atomicscience.fusion.ContainerNuclearBoiler;
import atomicscience.particle.accelerator.ContainerAccelerator;
import atomicscience.particle.accelerator.TileAccelerator;
import atomicscience.particle.quantum.ContainerQuantumAssembler;
import atomicscience.particle.quantum.TileQuantumAssembler;
import atomicscience.process.ContainerChemicalExtractor;
import atomicscience.process.TileChemicalExtractor;
import atomicscience.process.fission.ContainerCentrifuge;
import atomicscience.process.fission.TileCentrifuge;
import atomicscience.process.fission.TileNuclearBoiler;
import calclavia.lib.prefab.ProxyBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class CommonProxy
        extends ProxyBase
{
    public int getArmorIndex(String armor)
    {
        return 0;
    }

    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        return null;
    }

    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tileEntity = world.func_72796_p(x, y, z);
        if ((tileEntity instanceof TileCentrifuge)) {
            return new ContainerCentrifuge(player.field_71071_by, (TileCentrifuge)tileEntity);
        }
        if ((tileEntity instanceof TileChemicalExtractor)) {
            return new ContainerChemicalExtractor(player.field_71071_by, (TileChemicalExtractor)tileEntity);
        }
        if ((tileEntity instanceof TileAccelerator)) {
            return new ContainerAccelerator(player.field_71071_by, (TileAccelerator)tileEntity);
        }
        if ((tileEntity instanceof TileQuantumAssembler)) {
            return new ContainerQuantumAssembler(player.field_71071_by, (TileQuantumAssembler)tileEntity);
        }
        if ((tileEntity instanceof TileNuclearBoiler)) {
            return new ContainerNuclearBoiler(player.field_71071_by, (TileNuclearBoiler)tileEntity);
        }
        if ((tileEntity instanceof TileReactorCell)) {
            return new ContainerReactorCell(player, (TileReactorCell)tileEntity);
        }
        return null;
    }

    public boolean isFancyGraphics()
    {
        return false;
    }
}
