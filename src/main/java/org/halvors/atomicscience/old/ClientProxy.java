package org.halvors.atomicscience.old;

import atomicscience.fission.reactor.GuiReactorCell;
import atomicscience.fission.reactor.RenderReactorCell;
import atomicscience.fission.reactor.TileReactorCell;
import atomicscience.fusion.RenderPlasmaHeater;
import atomicscience.fusion.TilePlasmaHeater;
import atomicscience.particle.accelerator.EntityParticle;
import atomicscience.particle.accelerator.GuiAccelerator;
import atomicscience.particle.accelerator.RenderParticle;
import atomicscience.particle.accelerator.TileAccelerator;
import atomicscience.particle.quantum.GuiQuantumAssembler;
import atomicscience.particle.quantum.RenderQuantumAssembler;
import atomicscience.particle.quantum.TileQuantumAssembler;
import atomicscience.process.RenderChemicalExtractor;
import atomicscience.process.TileChemicalExtractor;
import atomicscience.process.fission.GuiCentrifuge;
import atomicscience.process.fission.GuiChemicalExtractor;
import atomicscience.process.fission.GuiNuclearBoiler;
import atomicscience.process.fission.RenderCentrifuge;
import atomicscience.process.fission.RenderNuclearBoiler;
import atomicscience.process.fission.TileCentrifuge;
import atomicscience.process.fission.TileNuclearBoiler;
import atomicscience.process.sensor.RenderThermometer;
import atomicscience.process.sensor.TileThermometer;
import atomicscience.process.turbine.RenderElectricTurbine;
import atomicscience.process.turbine.TileElectricTurbine;
import calclavia.lib.render.block.BlockRenderingHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.EventBus;

public class ClientProxy
        extends CommonProxy
{
    public void preInit()
    {
        MinecraftForge.EVENT_BUS.register(SoundHandler.INSTANCE);
        RenderingRegistry.registerBlockHandler(new BlockRenderingHandler());
    }

    public int getArmorIndex(String armor)
    {
        return RenderingRegistry.addNewArmourRendererPrefix(armor);
    }

    public void init()
    {
        super.init();
        ClientRegistry.bindTileEntitySpecialRenderer(TileCentrifuge.class, new RenderCentrifuge());
        ClientRegistry.bindTileEntitySpecialRenderer(TilePlasmaHeater.class, new RenderPlasmaHeater());
        ClientRegistry.bindTileEntitySpecialRenderer(TileNuclearBoiler.class, new RenderNuclearBoiler());
        ClientRegistry.bindTileEntitySpecialRenderer(TileElectricTurbine.class, new RenderElectricTurbine());
        ClientRegistry.bindTileEntitySpecialRenderer(TileThermometer.class, new RenderThermometer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileChemicalExtractor.class, new RenderChemicalExtractor());
        ClientRegistry.bindTileEntitySpecialRenderer(TileQuantumAssembler.class, new RenderQuantumAssembler());
        ClientRegistry.bindTileEntitySpecialRenderer(TileReactorCell.class, new RenderReactorCell());

        RenderingRegistry.registerEntityRenderingHandler(EntityParticle.class, new RenderParticle());
    }

    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        TileEntity tileEntity = world.func_72796_p(x, y, z);
        Block block = Block.field_71973_m[world.func_72798_a(x, y, z)];
        if ((tileEntity instanceof TileCentrifuge)) {
            return new GuiCentrifuge(player.field_71071_by, (TileCentrifuge)tileEntity);
        }
        if ((tileEntity instanceof TileChemicalExtractor)) {
            return new GuiChemicalExtractor(player.field_71071_by, (TileChemicalExtractor)tileEntity);
        }
        if ((tileEntity instanceof TileAccelerator)) {
            return new GuiAccelerator(player.field_71071_by, (TileAccelerator)tileEntity);
        }
        if ((tileEntity instanceof TileQuantumAssembler)) {
            return new GuiQuantumAssembler(player.field_71071_by, (TileQuantumAssembler)tileEntity);
        }
        if ((tileEntity instanceof TileNuclearBoiler)) {
            return new GuiNuclearBoiler(player.field_71071_by, (TileNuclearBoiler)tileEntity);
        }
        if ((tileEntity instanceof TileReactorCell)) {
            return new GuiReactorCell(player.field_71071_by, (TileReactorCell)tileEntity);
        }
        return null;
    }

    public boolean isFancyGraphics()
    {
        return Minecraft.func_71410_x().field_71474_y.field_74347_j;
    }
}
