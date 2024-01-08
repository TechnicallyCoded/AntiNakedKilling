package com.tcoded.antinakedkilling.struct;

import java.util.UUID;

public interface CombatHook {

    /**
     * Get the name of the combat hook
     * @return The name of the combat hook
     */
    String getName();

    /**
     * Check if the provided player is in combat
     * @return True if the player is in combat, false otherwise
     */
    boolean isInCombat(UUID uuid);

    /**
     * Get the other player in combat with the one provided
     * @param uuid The UUID of the player to get the combat target of
     * @return The UUID of the player the provided player is in combat with, or null if not in combat
     */
    UUID getCombatTarget(UUID uuid);
}
