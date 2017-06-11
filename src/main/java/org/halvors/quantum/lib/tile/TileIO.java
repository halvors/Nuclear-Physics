package org.halvors.quantum.lib.tile;

import net.minecraft.block.material.Material;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import org.halvors.quantum.lib.IIO;

import java.util.EnumSet;

public abstract class TileIO extends TileBase implements IIO {
    protected short ioMap = 364;
    protected boolean saveIOMap = false;

    public TileIO(Material material) {
        super(material);
    }

    @Override
    public EnumSet<ForgeDirection> getInputDirections() {
        EnumSet<ForgeDirection> dirs = EnumSet.noneOf(ForgeDirection.class);

        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
            if (getIO(direction) == 1) {
                dirs.add(direction);
            }
        }

        return dirs;
    }

    @Override
    public EnumSet<ForgeDirection> getOutputDirections() {
        EnumSet<ForgeDirection> dirs = EnumSet.noneOf(ForgeDirection.class);

        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
            if (getIO(direction) == 2) {
                dirs.add(direction);
            }
        }

        return dirs;
    }

    @Override
    public void setIO(ForgeDirection dir, int type) {
        String currentIO = getIOMapBase3();
        StringBuilder str = new StringBuilder(currentIO);
        str.setCharAt(dir.ordinal(), Integer.toString(type).charAt(0));
        this.ioMap = Short.parseShort(str.toString(), 3);
    }

    @Override
    public int getIO(ForgeDirection dir) {
        String currentIO = getIOMapBase3();
        return Integer.parseInt("" + currentIO.charAt(dir.ordinal()));
    }

    public String getIOMapBase3() {
        String currentIO = Integer.toString(this.ioMap, 3);
        while (currentIO.length() < 6) {
            currentIO = "0" + currentIO;
        }
        return currentIO;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        if (saveIOMap && nbt.hasKey("ioMap")) {
            this.ioMap = nbt.getShort("ioMap");
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);

        if (this.saveIOMap) {
            nbt.setShort("ioMap", this.ioMap);
        }
    }
}
