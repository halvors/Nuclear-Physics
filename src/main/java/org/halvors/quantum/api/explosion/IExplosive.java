package org.halvors.quantum.api.explosion;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.halvors.quantum.api.ITier;

/** An interface used to find various types of explosive's information.
 *
 * @author Calclavia */
public interface IExplosive extends ITier {
    /** @return Gets the explosive's ID. */
    int getId();

    /** @return The unique name key in the ICBM language file. */
    String getUnlocalizedName();

    /** @return Gets the specific translated name of the block versions of the explosive. */
    String getExplosiveName();

    /** @return Gets the specific translated name of the grenade versions of the explosive. */
    String getGrenadeName();

    /** @return Gets the specific translated name of the missile versions of the explosive. */
    String getMissileName();

    /** @return Gets the specific translated name of the minecart versions of the explosive. */
    String getMinecartName();

    /** @return The tier of the explosive. */
    @Override
    int getTier();

    /** Creates a new explosion at a given location.
     *
     * @param world The world in which the explosion takes place.
     * @param x The X-Coord
     * @param y The Y-Coord
     * @param z The Z-Coord
     * @param entity Entity that caused the explosion. */
    void createExplosion(World world, double x, double y, double z, Entity entity);

    @SideOnly(Side.CLIENT)
    ModelBase getBlockModel();

    @SideOnly(Side.CLIENT)
    IModel getMissileModel();

    @SideOnly(Side.CLIENT)
    ResourceLocation getBlockResource();
}
