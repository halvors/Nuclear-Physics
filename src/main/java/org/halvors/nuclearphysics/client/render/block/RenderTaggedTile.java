package org.halvors.nuclearphysics.client.render.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.nuclearphysics.api.tile.ITagRender;
import org.halvors.nuclearphysics.client.utility.RenderUtility;
import org.halvors.nuclearphysics.common.utility.position.Position;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

@SideOnly(Side.CLIENT)
public abstract class RenderTaggedTile<T extends TileEntity> extends TileEntitySpecialRenderer<T> {
    @Override
    public void renderTileEntityAt(T tile, double x, double y, double z, float partialTicks, int destroyStage) {
        BlockPos pos = tile.getPos();

        if (tile instanceof ITagRender && getPlayer().getDistance(pos.getX(), pos.getY(), pos.getZ()) <= RenderLiving.NAME_TAG_RANGE) {
            HashMap<String, Integer> tags = new HashMap<>();
            float height = ((ITagRender) tile).addInformation(tags, getPlayer());

            EntityPlayer player = Minecraft.getMinecraft().player;

            if (player.getRidingEntity() == null) {
                RayTraceResult rayTraceResult = player.rayTrace(8, 1);

                if (rayTraceResult != null) {
                    boolean isLooking = false;

                    for (int h = 0; h < height; h++) {
                        BlockPos rayTracePos = rayTraceResult.getBlockPos();

                        if (rayTracePos.getX() == pos.getX() && rayTracePos.getY() == pos.getY() + h && rayTracePos.getZ() == pos.getZ()) {
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
        Entity entity = rendererDispatcher.entity;

        if (entity instanceof EntityPlayer) {
            return (EntityPlayer) entity;
        }

        return null;
    }
}
