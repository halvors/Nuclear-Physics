package org.halvors.nuclearphysics.api.tile;

import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;

/**
 * Applied to TileEntities to render a tag above them.
 */
public interface ITagRender {
    /**
     * Gets the list of strings to render above the object.
     *
     * @param player The player this list will display for
     * @param map HashMap of strings followed by their color.
     * @return The height in which the render should happen.
     */
    float addInformation(HashMap<String, Integer> map, EntityPlayer player);
}

