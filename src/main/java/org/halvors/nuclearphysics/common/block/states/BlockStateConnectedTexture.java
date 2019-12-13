package org.halvors.nuclearphysics.common.block.states;

import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IProperty;
import net.minecraft.world.chunk.BlockStateContainer;
import org.halvors.nuclearphysics.common.block.BlockConnectedTexture;

public class BlockStateConnectedTexture extends BlockStateContainer {
    public static final IProperty<Boolean> CONNECTED_DOWN = BooleanProperty.create("connected_down");
    public static final IProperty<Boolean> CONNECTED_UP = BooleanProperty.create("connected_up");
    public static final IProperty<Boolean> CONNECTED_NORTH = BooleanProperty.create("connected_north");
    public static final IProperty<Boolean> CONNECTED_SOUTH = BooleanProperty.create("connected_south");
    public static final IProperty<Boolean> CONNECTED_WEST = BooleanProperty.create("connected_west");
    public static final IProperty<Boolean> CONNECTED_EAST = BooleanProperty.create("connected_east");

    public BlockStateConnectedTexture(final BlockConnectedTexture block) {
        super(block, CONNECTED_DOWN, CONNECTED_UP, CONNECTED_NORTH, CONNECTED_SOUTH, CONNECTED_WEST, CONNECTED_EAST);
    }

    public BlockStateConnectedTexture(final BlockConnectedTexture block, final IProperty<Enum> typeProperty) {
        super(block, CONNECTED_DOWN, CONNECTED_UP, CONNECTED_NORTH, CONNECTED_SOUTH, CONNECTED_WEST, CONNECTED_EAST, typeProperty);
    }
}