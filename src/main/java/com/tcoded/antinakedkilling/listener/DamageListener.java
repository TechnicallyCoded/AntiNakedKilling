package com.tcoded.antinakedkilling.listener;

import com.tcoded.antinakedkilling.AntiNakedKilling;
import com.tcoded.antinakedkilling.manager.CombatHookManager;
import com.tcoded.antinakedkilling.manager.RegionHookManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class DamageListener implements Listener {

    private final AntiNakedKilling plugin;
    private final CombatHookManager combatManager;
    private final RegionHookManager regionManager;

    public DamageListener(AntiNakedKilling plugin) {
        this.plugin = plugin;
        this.combatManager = plugin.getCombatManager();
        this.regionManager = plugin.getRegionManager();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerHit(EntityDamageByEntityEvent event) {
        // Ignore cancelled interactions
        if (event.isCancelled()) {
            return;
        }

        // get the attacker who dealt the damage
        Entity attackerEntity = event.getDamager();
        // get the victim who took the damage
        Entity victimEntity = event.getEntity();

        // pre-checks
        // - check if the victim is a player
        // if the victim entity is not a player, then stop
        if (!(victimEntity instanceof Player)) {
            return;
        }
        Player victim = (Player) victimEntity;

        // checks
        // - check if the victim has armor on
        ItemStack[] victimArmor = victim.getInventory().getArmorContents();
        for (ItemStack itemStack : victimArmor) {
            if (itemStack != null && itemStack.getType() != Material.ELYTRA) {
                return;
            }
        }

        // - check if the victim is in combat
        if (combatManager.isInCombat(victim.getUniqueId())) {
            return;
        }

        // - check if the victim is in a danger region
        if (regionManager.isInDangerRegion(victim)) {
            return;
        }

        // cancel the damage event
        event.setCancelled(true);

        // send the attacker a message to say they cant kill the player
        attackerEntity.sendMessage(this.plugin.getLangUtil().format("pvp.cancelled-naked"));
    }
}
