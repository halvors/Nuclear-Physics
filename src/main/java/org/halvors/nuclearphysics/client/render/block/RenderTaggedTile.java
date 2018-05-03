package org.halvors.nuclearphysics.client.render.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.api.tile.ITagRender;
import org.halvors.nuclearphysics.client.utility.RenderUtility;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

@SideOnly(Side.CLIENT)
public abstract class RenderTaggedTile<T extends TileEntity> extends RenderTile<T> {
    @Override
    protected void render(final T tile, final double x, final double y, final double z) {
        final BlockPos pos = tile.getPos();

        if (tile instanceof ITagRender && getPlayer().getDistance(pos.getX(), pos.getY(), pos.getZ()) <= RenderLiving.NAME_TAG_RANGE) {
            final HashMap<String, Integer> tags = new HashMap<>();
            final float height = ((ITagRender) tile).addInformation(tags, getPlayer());
            final EntityPlayer player = Minecraft.getMinecraft().player;

            if (player.getRidingEntity() == null) {
                final RayTraceResult rayTraceResult = player.rayTrace(8, 1);

                if (rayTraceResult != null) {
                    boolean isLooking = false;

                    for (int h = 0; h < height; h++) {
                        final BlockPos rayTracePos = rayTraceResult.getBlockPos();

                        if (rayTracePos.getX() == pos.getX() && rayTracePos.getY() == pos.getY() + h && rayTracePos.getZ() == pos.getZ()) {
                            isLooking = true;
                        }
                    }

                    if (isLooking) {
                        final Iterator<Entry<String, Integer>> it = tags.entrySet().iterator();
                        int i = 0;

                        while (it.hasNext()) {
                            final Entry<String, Integer> entry = it.next();

                            if (entry.getKey() != null) {
                                RenderUtility.renderFloatingText(entry.getKey(), new BlockPos(x, y, z).add(0.5, i * 0.25 + height, 0.5), entry.getValue());
                            }

                            i++;
                        }
                    }
                }
            }
        }
    }

    public EntityPlayer getPlayer() {
        final Entity entity = rendererDispatcher.entity;

        if (entity instanceof EntityPlayer) {
            return (EntityPlayer) entity;
        }

        return null;
    }
}
