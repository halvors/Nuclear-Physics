package org.halvors.nuclearphysics.client.render.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import org.halvors.nuclearphysics.api.tile.ITagRender;
import org.halvors.nuclearphysics.client.utility.RenderUtility;
import org.halvors.nuclearphysics.common.type.Position;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

@SideOnly(Side.CLIENT)
public abstract class RenderTaggedTile<T extends TileEntity> extends RenderTile<T> {
    public RenderTaggedTile(String name) {
        super(name);
    }

    @Override
    protected void render(T tile, double x, double y, double z) {
        if (tile instanceof ITagRender && getPlayer().getDistance(tile.xCoord, tile.yCoord, tile.zCoord) <= RenderLiving.NAME_TAG_RANGE) {
            HashMap<String, Integer> tags = new HashMap<>();
            float height = ((ITagRender) tile).addInformation(tags, getPlayer());

            EntityPlayer player = Minecraft.getMinecraft().thePlayer;

            if (player.ridingEntity == null) {
                MovingObjectPosition rayTraceResult = player.rayTrace(8, 1);

                if (rayTraceResult != null) {
                    boolean isLooking = false;

                    for (int h = 0; h < height; h++) {
                        Position rayTracePos = new Position(rayTraceResult.blockX, rayTraceResult.blockY, rayTraceResult.blockZ);

                        if (rayTracePos.getX() == tile.xCoord && rayTracePos.getY() == tile.yCoord + h && rayTracePos.getZ() == tile.zCoord) {
                            isLooking = true;
                        }
                    }

                    if (isLooking) {
                        Iterator<Entry<String, Integer>> it = tags.entrySet().iterator();
                        int i = 0;

                        while (it.hasNext()) {
                            Entry<String, Integer> entry = it.next();

                            if (entry.getKey() != null) {
                                RenderUtility.renderFloatingText(entry.getKey(), new Position(x, y, z).translate(0.5, i * 0.25 + height, 0.5), entry.getValue());
                            }

                            i++;
                        }
                    }
                }
            }
        }
    }

    public EntityPlayer getPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }
}
