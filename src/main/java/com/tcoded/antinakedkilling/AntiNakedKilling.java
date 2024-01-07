package com.tcoded.antinakedkilling;

import com.tcoded.antinakedkilling.commands.AntiNakedKillingCmd;
import com.tcoded.antinakedkilling.listener.DamageListener;
import com.tcoded.antinakedkilling.manager.CombatHookManager;
import com.tcoded.antinakedkilling.manager.RegionHookManager;
import org.bstats.bukkit.Metrics;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class AntiNakedKilling extends JavaPlugin {

    private static final int BSTATS_ID = 20663;

    private CombatHookManager combatHookManager;
    private RegionHookManager regionHookManager;

    private List<String> dangerRegionNames;
    private Metrics metrics;

    @Override
    public void onEnable() {
        // Utils

        // Config
        saveDefaultConfig();
        loadConfigOptions();

        // Managers
        this.combatHookManager = new CombatHookManager(this);
        this.regionHookManager = new RegionHookManager(this);
        this.applyConfigOptions();

        // Commands
        AntiNakedKillingCmd antiNakedKillingCmd = new AntiNakedKillingCmd(this);
        PluginCommand antiNakedKillingPlCmd = this.getCommand("antinakedkilling");
        antiNakedKillingPlCmd.setExecutor(antiNakedKillingCmd);
        antiNakedKillingPlCmd.setTabCompleter(antiNakedKillingCmd);

        // Listeners
        this.getServer().getPluginManager().registerEvents(new DamageListener(this), this);

        // bStats
        this.metrics = new Metrics(this, BSTATS_ID);
    }

    public void reloadAllConfig() {
        this.reloadConfig();
        this.applyConfigOptions();
    }

    private void loadConfigOptions() {
        this.dangerRegionNames = getConfig().getStringList("danger-regions");
    }

    private void applyConfigOptions() {
        this.combatHookManager.init();
        this.regionHookManager.init(dangerRegionNames);
    }

    @Override
    public void onDisable() {
        // Listeners
        HandlerList.unregisterAll(this);

        // Commands

        // Managers
        this.combatHookManager.removeAllHooks();

        // Config

        // Utils
        this.metrics.shutdown();
    }

    public CombatHookManager getCombatManager() {
        return this.combatHookManager;
    }

    public RegionHookManager getRegionManager() {
        return this.regionHookManager;
    }

}
