package org.halvors.nuclearphysics.common.block.states;

import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.state.Property;
import net.minecraft.world.chunk.BlockStateContainer;
import org.halvors.nuclearphysics.common.block.BlockConnectedTexture;

public class BlockStateConnectedTexture extends BlockStateContainer {
    public static final Property<Boolean> CONNECTED_DOWN = Property<Boolean>.create("connected_down");
    public static final Property<Boolean> CONNECTED_UP = PropertyBool.create("connected_up");
    public static final Property<Boolean> CONNECTED_NORTH = PropertyBool.create("connected_north");
    public static final Property<Boolean> CONNECTED_SOUTH = PropertyBool.create("connected_south");
    public static final Property<Boolean> CONNECTED_WEST = PropertyBool.create("connected_west");
    public static final Property<Boolean> CONNECTED_EAST = PropertyBool.create("connected_east");

    public BlockStateConnectedTexture(final BlockConnectedTexture block) {
        super(block, CONNECTED_DOWN, CONNECTED_UP, CONNECTED_NORTH, CONNECTED_SOUTH, CONNECTED_WEST, CONNECTED_EAST);
    }

    public BlockStateConnectedTexture(final BlockConnectedTexture block, final PropertyEnum typeProperty) {
        super(block, CONNECTED_DOWN, CONNECTED_UP, CONNECTED_NORTH, CONNECTED_SOUTH, CONNECTED_WEST, CONNECTED_EAST, typeProperty);
    }
}