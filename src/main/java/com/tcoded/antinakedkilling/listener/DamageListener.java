package com.tcoded.antinakedkilling.listener;

import com.tcoded.antinakedkilling.AntiNakedKilling;
import com.tcoded.antinakedkilling.manager.CombatHookManager;
import com.tcoded.antinakedkilling.manager.RegionHookManager;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

public class DamageListener implements Listener {

    private final AntiNakedKilling plugin;
    private final CombatHookManager combatManager;
    private final RegionHookManager regionManager;

    public DamageListener(AntiNakedKilling plugin) {
        this.plugin = plugin;
        this.combatManager = plugin.getCombatManager();
        this.regionManager = plugin.getRegionManager();
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerHit(EntityDamageByEntityEvent event) {
        // Ignore cancelled interactions
        if (event.isCancelled()) return;

        // Get the attacker who dealt the damage
        Entity attackerEntity = event.getDamager();
        // Get the victim who took the damage
        Entity victimEntity = event.getEntity();

        // If the victim entity is not a player, then stop
        if (!(victimEntity instanceof Player)) return;
        Player victim = (Player) victimEntity;

        // Get the effective attacker (e.g. if the attacker is a projectile, get the shooter)
        Entity effectiveAttacker = getEffectiveAttacker(attackerEntity);

        // Should we allow players to attack naked players?
        if (effectiveAttacker instanceof HumanEntity && isPlayerDamageAllowed()) {
            return; // don't cancel
        }

        // Should we allow mobs to attack naked players?
        if (effectiveAttacker instanceof Mob && isMobDamageAllowed()) {
            return; // don't cancel
        }

        // Check if the victim has armor on
        if (hasArmor(victim)) return;

        // Check if the victim is in combat
        if (isInCombat(victim)) return;

        // Check if the victim is in a danger region
        if (isInDangerRegion(victim)) return;

        // Cancel the damage event
        event.setCancelled(true);

        // Send the attacker a message to say they can't kill the player
        attackerEntity.sendMessage(this.plugin.getLangUtil().getFormattedOrEmpty("pvp.cancelled-naked"));
    }

    @EventHandler
    public void onPlayerDamageByOther(EntityDamageEvent event) {
        // Ignore cancelled interactions
        if (event.isCancelled()) return;

        // Ignore entity damage by entity
        if (event instanceof EntityDamageByEntityEvent) return;

        // Should we allow other damage to naked players?
        if (isOtherDamageAllowed()) return;

        // get the victim who took the damage
        Entity victimEntity = event.getEntity();

        // If the victim entity is not a player, stop
        if (!(victimEntity instanceof Player)) return;
        Player victim = (Player) victimEntity;

        // Check if the victim has armor on
        if (hasArmor(victim)) return;

        // Check if the victim is in combat
        if (isInCombat(victim)) return;

        // Check if the victim is in a danger region
        if (isInDangerRegion(victim)) return;

        // Cancel the damage event
        event.setCancelled(true);
    }

    private static boolean hasArmor(Player victim) {
        ItemStack[] victimArmor = victim.getInventory().getArmorContents();
        for (ItemStack itemStack : victimArmor) {
            if (itemStack != null && itemStack.getType() != Material.ELYTRA) {
                return true;
            }
        }
        return false;
    }

    private boolean isInCombat(Player victim) {
        return combatManager.isInCombat(victim.getUniqueId());
    }

    private boolean isInDangerRegion(Player victim) {
        return regionManager.isInDangerRegion(victim);
    }

    private static Entity getEffectiveAttacker(Entity attackerEntity) {
        if (!(attackerEntity instanceof Projectile)) return attackerEntity;

        ProjectileSource src = ((Projectile) attackerEntity).getShooter();
        if (src instanceof Entity) return (Entity) src;

        return attackerEntity;
    }

    private boolean isPlayerDamageAllowed() {
        return plugin.getConfig().getBoolean("allow-player-damage", false);
    }

    private boolean isMobDamageAllowed() {
        return plugin.getConfig().getBoolean("allow-mob-damage", false);
    }

    private boolean isOtherDamageAllowed() {
        return plugin.getConfig().getBoolean("allow-other-damage", true);
    }
}
