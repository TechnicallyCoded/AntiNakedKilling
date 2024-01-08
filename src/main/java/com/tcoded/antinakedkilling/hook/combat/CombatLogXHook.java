package com.tcoded.antinakedkilling.hook.combat;

import com.github.sirblobman.combatlogx.api.ICombatLogX;
import com.github.sirblobman.combatlogx.api.manager.ICombatManager;
import com.github.sirblobman.combatlogx.api.object.TagInformation;
import com.tcoded.antinakedkilling.AntiNakedKilling;
import com.tcoded.antinakedkilling.struct.CombatHook;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import java.util.UUID;

public class CombatLogXHook implements CombatHook {

    private final AntiNakedKilling plugin;
    private final ICombatLogX clx;

    public CombatLogXHook(AntiNakedKilling plugin) {
        this.plugin = plugin;
        PluginManager pluginManager = Bukkit.getPluginManager();
        this.clx = (ICombatLogX) pluginManager.getPlugin("CombatLogX");
    }

    @Override
    public String getName() {
        return "CombatLogX";
    }

    @Override
    public boolean isInCombat(UUID uuid) {
        Player player = plugin.getServer().getPlayer(uuid);
        if (player == null) return false;
        return this.clx.getCombatManager().isInCombat(player);
    }

    @Override
    public UUID getCombatTarget(UUID uuid) {
        ICombatManager combatManager = this.clx.getCombatManager();

        Player player = plugin.getServer().getPlayer(uuid);
        if (player == null) return null;

        TagInformation tagInformation = combatManager.getTagInformation(player);
        if (tagInformation == null) return null;

        Entity currentEnemy = tagInformation.getCurrentEnemy();
        if (currentEnemy == null) return null;

        return currentEnemy.getUniqueId();
    }
}
