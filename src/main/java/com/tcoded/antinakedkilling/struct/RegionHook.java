package com.tcoded.antinakedkilling.struct;

import org.bukkit.entity.Player;

import java.util.UUID;

public interface RegionHook {

    /**
     * Get the name of the region hook
     * @return The name of the region hook
     */
    String getName();

    /**
     * Check if the provided player is in a danger region
     * @param player The player to check
     * @return True if the player is in a danger region, false otherwise
     */
    boolean isInDangerRegion(Player player);

}
