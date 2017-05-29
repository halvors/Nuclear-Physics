package org.halvors.atomicscience.common.base.tile;

import net.minecraft.entity.player.EntityPlayer;

public interface ITileOwnable {
	/**
	 * Check if this ownable has an owner.
	 * @return true if this ownable has an owner.
	 */
	boolean hasOwner();

	/**
	 * Check if the player is owning this.
	 * @param player the player to check if is the owner of this.
	 * @return true if the player is the owner of this.
	 */
	boolean isOwner(EntityPlayer player);

	/**
	 * Get the player owning this.
	 * @return player
	 */
	EntityPlayer getOwner();

	/**
	 * Get the name of the player owning this.
	 * @return name
	 */
	String getOwnerName();

	/**
	 * Sets the player owning this.
	 * @param player the player going to be set as owner of this.
	 */
	void setOwner(EntityPlayer player);
}
