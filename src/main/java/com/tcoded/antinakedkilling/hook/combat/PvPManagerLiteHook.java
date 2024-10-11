package com.tcoded.antinakedkilling.hook.combat;

import com.github.sirblobman.combatlogx.api.ICombatLogX;
import com.github.sirblobman.combatlogx.api.manager.ICombatManager;
import com.github.sirblobman.combatlogx.api.object.TagInformation;
import com.tcoded.antinakedkilling.AntiNakedKilling;
import com.tcoded.antinakedkilling.struct.CombatHook;
import me.NoChance.PvPManager.PvPManager;
import me.NoChance.PvPManager.PvPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import java.util.UUID;

@SuppressWarnings("deprecation")
public class PvPManagerLiteHook implements CombatHook {

    private final AntiNakedKilling plugin;
    private final PvPManager pvpManager;

    public PvPManagerLiteHook(AntiNakedKilling plugin) {
        this.plugin = plugin;
        this.pvpManager = PvPManager.getInstance();
    }

    @Override
    public String getName() {
        return "PvPManager";
    }

    @Override
    public boolean isInCombat(UUID uuid) {
        Player player = plugin.getServer().getPlayer(uuid);
        if (player == null) return false;
        PvPlayer pvPlayer = this.pvpManager.getPlayerHandler().get(player);
        return pvPlayer.isInCombat();
    }

    @Override
    public UUID getCombatTarget(UUID uuid) {
        Player player = plugin.getServer().getPlayer(uuid);
        if (player == null) return null;

        PvPlayer pvPlayer = this.pvpManager.getPlayerHandler().get(player);

        PvPlayer attacker = pvPlayer.getEnemy();
        if (attacker == null) return null;

        return attacker.getUUID();
    }
}
