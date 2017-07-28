package org.halvors.quantum.common.multiblock;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import org.halvors.quantum.api.nbt.ISaveObject;
import org.halvors.quantum.common.utility.transform.vector.Vector3;

import java.lang.ref.WeakReference;
import java.util.LinkedHashSet;
import java.util.Set;

/** A reference-based multiblock structure uses a central block as the "primary block" and have all
 * the blocks around it be "dummy blocks". This handler should be extended. Every single block will
 * have a reference of this object.
 *
 * @author Calclavia */
public class MultiBlockHandler<W extends IMultiBlockStructure> implements ISaveObject {
    /** The main block used for reference */
    protected WeakReference<W> prim = null;
    /** The relative primary block position to be loaded in once the tile is initiated. */
    protected Vector3 newPrimary = null;
    protected final W self;
    protected Class<? extends W> wrapperClass;

    public MultiBlockHandler(W wrapper) {
        self = wrapper;
        wrapperClass = (Class<? extends W>) wrapper.getClass();
    }

    public void update() {
        if (self.getWorldObject() != null && newPrimary != null) {
            W checkWrapper = getWrapperAt(newPrimary.clone().translate(self.getPosition()));

            if (checkWrapper != null) {
                newPrimary = null;

                if (checkWrapper != getPrimary()) {
                    prim = new WeakReference<>(checkWrapper);
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
        Vector3[] vectors = self.getMultiBlockVectors();

        for (Vector3 vector : vectors) {
            W checkWrapper = getWrapperAt(vector.translate(self.getPosition()));

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

                prim = new WeakReference<>(self);

                for (W structure : structures) {
                    structure.getMultiBlock().prim = prim;
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
                        structure.getMultiBlock().prim = null;
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


    public W getWrapperAt(Vector3 position) {
        TileEntity tile = position.getTileEntity(self.getWorldObject());

        if (tile != null && wrapperClass.isAssignableFrom(tile.getClass())) {
            return (W) tile;
        }

        return null;
    }

    public boolean isConstructed() {
        return prim != null;
    }

    public boolean isPrimary() {
        return !isConstructed() || getPrimary() == self;
    }

    public W getPrimary() {
        return prim == null ? null : prim.get();
    }

    public W get()
    {
        return getPrimary() != null ? getPrimary() : self;
    }

    /** Only the primary wrapper of the multiblock saves and loads data. */
    @Override
    public void load(NBTTagCompound tag) {
        if (tag.hasKey("primaryMultiBlock")) {
            newPrimary = new Vector3(tag.getCompoundTag("primaryMultiBlock"));
            update();
        } else {
            prim = null;
        }
    }

    @Override
    public NBTTagCompound save(NBTTagCompound tag) {
        if (isConstructed()) {
            tag.setTag("primaryMultiBlock", getPrimary().getPosition().subtract(self.getPosition()).writeToNBT(new NBTTagCompound()));
        }

        return tag;
    }
}
