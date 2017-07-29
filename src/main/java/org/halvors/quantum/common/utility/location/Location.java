package org.halvors.quantum.common.utility.location;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.halvors.quantum.common.utility.transform.vector.Vector3;
import org.halvors.quantum.common.utility.transform.vector.VectorWorld;

public class Location {
	private World world;
	private BlockPos pos;

	public Location(World world, BlockPos pos) {
		this.world = world;
		this.pos = pos;
	}

	public Location(Entity entity) {
		this.world = entity.getEntityWorld();
		this.pos = entity.getPosition();
	}

	public Location(TileEntity tile) {
		this.world = tile.getWorld();
		this.pos = tile.getPos();
	}

	public World getWorld() {
		return world;
	}

	public BlockPos getPos() {
		return pos;
	}

	public IBlockState getBlockState() {
		return world.getBlockState(pos);
	}

	public TileEntity getTileEntity() {
		return world.getTileEntity(pos);
	}
}
