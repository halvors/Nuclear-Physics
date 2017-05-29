package org.halvors.atomicscience.old.process.turbine;

import calclavia.api.resonantinduction.IBoilHandler;
import calclavia.lib.content.module.TileBase;
import calclavia.lib.content.module.TileRender;
import calclavia.lib.render.ConnectedTextureRenderer;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileFunnel
        extends TileBase
        implements IBoilHandler
{
    private static Icon iconTop;
    private final FluidTank tank = new FluidTank(16000);

    public TileFunnel()
    {
        super(Material.field_76243_f);
    }

    public Icon getIcon(int side, int meta)
    {
        return (side == 1) || (side == 0) ? iconTop : super.getIcon(side, meta);
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        super.registerIcons(iconRegister);
        iconTop = iconRegister.func_94245_a(this.domain + this.name + "_top");
    }

    public void func_70316_g()
    {
        super.func_70316_g();
        if (this.tank.getFluidAmount() > 0)
        {
            TileEntity tileEntity = this.field_70331_k.func_72796_p(this.field_70329_l, this.field_70330_m + 1, this.field_70327_n);
            if ((tileEntity instanceof IFluidHandler))
            {
                IFluidHandler handler = (IFluidHandler)tileEntity;
                if (handler.canFill(ForgeDirection.DOWN, this.tank.getFluid().getFluid()))
                {
                    FluidStack drainedStack = this.tank.drain(this.tank.getCapacity(), false);
                    if (drainedStack != null) {
                        this.tank.drain(handler.fill(ForgeDirection.DOWN, drainedStack, true), true);
                    }
                }
            }
        }
    }

    public void func_70307_a(NBTTagCompound tag)
    {
        super.func_70307_a(tag);
        this.tank.writeToNBT(tag);
    }

    public void func_70310_b(NBTTagCompound tag)
    {
        super.func_70310_b(tag);
        this.tank.readFromNBT(tag);
    }

    public int fill(ForgeDirection from, FluidStack resource, boolean doFill)
    {
        return this.tank.fill(resource, doFill);
    }

    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain)
    {
        return this.tank.drain(maxDrain, doDrain);
    }

    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain)
    {
        if ((resource == null) || (!resource.isFluidEqual(this.tank.getFluid()))) {
            return null;
        }
        return this.tank.drain(resource.amount, doDrain);
    }

    public boolean canFill(ForgeDirection from, Fluid fluid)
    {
        if ((fluid.isGaseous()) && (from == ForgeDirection.DOWN)) {
            return true;
        }
        return false;
    }

    public boolean canDrain(ForgeDirection from, Fluid fluid)
    {
        if ((fluid.isGaseous()) && (from == ForgeDirection.UP)) {
            return true;
        }
        return false;
    }

    public FluidTankInfo[] getTankInfo(ForgeDirection from)
    {
        return new FluidTankInfo[] { this.tank.getInfo() };
    }

    protected TileRender newRenderer()
    {
        return new ConnectedTextureRenderer(this, "atomicscience:funnel_edge");
    }
}
