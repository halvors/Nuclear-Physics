package org.halvors.nuclearphysics.common.multiblock;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import org.halvors.nuclearphysics.api.nbt.ISaveObject;
import org.halvors.nuclearphysics.common.type.Position;

import java.lang.ref.WeakReference;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/** A reference-based multiblock structure uses a central block as the "primary block" and have all
 * the blocks around it be "dummy blocks". This handler should be extended. Every single block will
 * have a reference of this object.
 */
public class MultiBlockHandler<W extends IMultiBlockStructure> implements ISaveObject {
    protected final W self;

    /** The main block used for reference */
    protected WeakReference<W> primary = null;

    /** The relative primary block position to be loaded in once the tile is initiated. */
    protected Position newPrimary = null;
    protected Class<? extends W> wrapperClass;

    @SuppressWarnings("unchecked")
    public MultiBlockHandler(W wrapper) {
        self = wrapper;
        wrapperClass = (Class<? extends W>) wrapper.getClass();
    }

    public void update() {
        if (self.getWorldObject() != null && newPrimary != null) {
            W checkWrapper = getWrapperAt(newPrimary.clone().add(self.getPosition()).getPos());

            if (checkWrapper != null) {
                newPrimary = null;

                if (checkWrapper != getPrimary()) {
                    primary = new WeakReference<>(checkWrapper);
                    self.onMultiBlockChanged();
                }
            }
        }
    }

    /** Try to construct the structure, otherwise, deconstruct it.
     *
     * @return True if operation is successful. */
    public boolean toggleConstruct() {
        return construct() || deconstruct();

    }

    /** Gets the structure blocks of the multiblock.
     *
     * @return Null if structure cannot be created. */
    public Set<W> getStructure() {
        Set<W> structure = new LinkedHashSet<>();
        Position[] positions = self.getMultiBlockVectors();

        for (Position position : positions) {
            W checkWrapper = getWrapperAt(position.add(self.getPosition()).getPos());

            if (checkWrapper != null) {
                structure.add(checkWrapper);
            } else {
                structure.clear();

                return null;
            }
        }

        return structure;
    }

    /** Called to construct the multiblock structure. Example: Wrenching the center block or checking
     * if a placement was done correct. Note that this block will become the PRIMARY block.
     *
     * @return True if the construction was successful. */
    public boolean construct() {
        if (!isConstructed()) {
            Set<W> structures = getStructure();

            if (structures != null) {
                for (W structure : structures) {
                    if (structure.getMultiBlock().isConstructed()) {
                        return false;
                    }
                }

                primary = new WeakReference<>(self);

                for (W structure : structures) {
                    structure.getMultiBlock().primary = primary;
                }

                for (W structure : structures) {
                    structure.onMultiBlockChanged();
                }

                return true;
            }
        }

        return false;
    }

    public boolean deconstruct() {
        if (isConstructed()) {
            if (isPrimary()) {
                Set<W> structures = getStructure();

                if (structures != null) {
                    for (W structure : structures) {
                        structure.getMultiBlock().primary = null;
                    }

                    for (W structure : structures) {
                        structure.onMultiBlockChanged();
                    }
                }
            } else {
                getPrimary().getMultiBlock().deconstruct();
            }

            return true;
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    public W getWrapperAt(BlockPos pos) {
        TileEntity tile = self.getWorldObject().getTileEntity(pos);

        if (tile != null && wrapperClass.isAssignableFrom(tile.getClass())) {
            return (W) tile;
        }

        return null;
    }

    public boolean isConstructed() {
        return primary != null;
    }

    public boolean isPrimary() {
        return !isConstructed() || getPrimary() == self;
    }

    public W getPrimary() {
        return primary == null ? null : primary.get();
    }

    public W get()
    {
        return getPrimary() != null ? getPrimary() : self;
    }

    /** Only the primary wrapper of the multiblock saves and loads data. */
    @Override
    public void readFromNBT(NBTTagCompound tag) {
        if (tag.hasKey("primaryMultiBlock")) {
            newPrimary = new Position(tag.getCompoundTag("primaryMultiBlock"));
            update();
        } else {
            primary = null;
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        if (isConstructed()) {
            tag.setTag("primaryMultiBlock", getPrimary().getPosition().subtract(self.getPosition()).writeToNBT(new NBTTagCompound()));
        }

        return tag;
    }

    public void handlePacketData(ByteBuf dataStream) {
        if (dataStream.readBoolean()) {
            newPrimary = new Position(dataStream);
            update();
        } else {
            primary = null;
        }
    }

    public List<Object> getPacketData(List<Object> objects) {
        if (isConstructed()) {
            objects.add(true);
            getPrimary().getPosition().subtract(self.getPosition()).getPacketData(objects);
        } else {
            objects.add(false);
        }

        return objects;
    }
}
