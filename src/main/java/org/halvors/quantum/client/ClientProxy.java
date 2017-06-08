package org.halvors.quantum.client;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.halvors.quantum.common.CommonProxy;
import org.halvors.quantum.common.debug.block.BlockCreativeBuilder;
import org.halvors.quantum.common.debug.gui.GuiCreativeBuilder;
import org.halvors.quantum.client.gui.reactor.fission.GuiReactorCell;
import org.halvors.quantum.common.tile.reactor.fission.TileReactorCell;
import org.halvors.quantum.common.transform.vector.Vector3;

/**
 * This is the client proxy used only by the client.
 *
 * @author halvors
 */
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy implements IGuiHandler {
	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		Block block = world.getBlock(x, y, z);

		if (block instanceof BlockCreativeBuilder) {
			return new GuiCreativeBuilder(new Vector3(x, y, z));
		}

		if (tileEntity instanceof TileReactorCell) {
			return new GuiReactorCell(player, (TileReactorCell) tileEntity);
		}

		return null;
	}
}