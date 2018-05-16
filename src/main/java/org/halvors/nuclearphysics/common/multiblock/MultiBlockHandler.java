package org.halvors.nuclearphysics.common.multiblock;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import org.halvors.nuclearphysics.api.nbt.ISaveObject;
import org.halvors.nuclearphysics.common.utility.VectorUtility;

import java.lang.ref.WeakReference;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/** A reference-based multiblock structure uses a central block as the "primary block" and have all
 * the blocks around it be "dummy blocks". This handler should be extended. Every single block will
 * have a reference of this object.
 */
public class MultiBlockHandler<W extends IMultiBlockStructure> implements ISaveObject {
    private static final String NBT_PRIMARY_MULTIBLOCK = "primaryMultiBlock";

    protected final W self;

    /** The main block used for reference */
    protected WeakReference<W> primary = null;

    /** The relative primary block position to be loaded in once the tile is initiated. */
    protected BlockPos newPrimary = null;
    protected final Class<? extends W> wrapperClass;

    @SuppressWarnings("unchecked")
    public MultiBlockHandler(final W wrapper) {
        self = wrapper;
        wrapperClass = (Class<? extends W>) wrapper.getClass();
    }

    public void update() {
        if (self.getWorldObject() != null && newPrimary != null) {
            final W checkWrapper = getWrapperAt(newPrimary.add(self.getPosition()));

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
        final Set<W> structure = new LinkedHashSet<>();
        final BlockPos[] positions = self.getMultiBlockVectors();

        for (final BlockPos position : positions) {
            final W checkWrapper = getWrapperAt(position.add(self.getPosition()));

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
            final Set<W> structures = getStructure();

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
                final Set<W> structures = getStructure();

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
    public W getWrapperAt(final BlockPos pos) {
        final TileEntity tile = self.getWorldObject().getTileEntity(pos);

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
    public void readFromNBT(final NBTTagCompound tag) {
        if (tag.hasKey(NBT_PRIMARY_MULTIBLOCK)) {
            newPrimary = VectorUtility.readFromNBT(tag.getCompoundTag(NBT_PRIMARY_MULTIBLOCK));
            update();
        } else {
            primary = null;
        }
    }

    @Override
    public NBTTagCompound writeToNBT(final NBTTagCompound tag) {
        if (isConstructed()) {
            tag.setTag(NBT_PRIMARY_MULTIBLOCK, VectorUtility.writeToNBT(getPrimary().getPosition().subtract(self.getPosition()), new NBTTagCompound()));
        }

        return tag;
    }

    public void handlePacketData(final ByteBuf dataStream) {
        if (dataStream.readBoolean()) {
            newPrimary = VectorUtility.handlePacketData(dataStream);
            update();
        } else {
            primary = null;
        }
    }

    public List<Object> getPacketData(final List<Object> objects) {
        if (isConstructed()) {
            objects.add(true);
            VectorUtility.getPacketData(getPrimary().getPosition().subtract(self.getPosition()), objects);
        } else {
            objects.add(false);
        }

        return objects;
    }
}
