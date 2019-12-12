package org.halvors.nuclearphysics.api.tile;

import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;

/**
 * Applied to TileEntities to render a tag above them.
 */
public interface ITagRender {
    /** Gets the list of strings to render above the object.
     * @param player The player this list will display for
     * @param map HashMap of strings followed by there color Example {"Hello World",0x88FF88}
     * @return The HEIGHT in which the render should happen.
     */
    float addInformation(HashMap<String, Integer> map, PlayerEntity player);
}

