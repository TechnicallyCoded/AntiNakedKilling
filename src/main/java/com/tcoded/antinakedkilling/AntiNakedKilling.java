package com.tcoded.antinakedkilling;

import com.tcoded.antinakedkilling.commands.AntiNakedKillingCmd;
import com.tcoded.antinakedkilling.listener.DamageListener;
import com.tcoded.antinakedkilling.manager.CombatHookManager;
import com.tcoded.antinakedkilling.manager.RegionHookManager;
import com.tcoded.folialib.FoliaLib;
import com.tcoded.legacycolorcodeparser.LegacyColorCodeParser;
import com.tcoded.lightlibs.bukkitlangutil.BukkitMultiLangUtil;
import com.tcoded.updatechecker.SimpleUpdateChecker;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SimplePie;
import org.bukkit.ChatColor;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class AntiNakedKilling extends JavaPlugin {

    private static final int BSTATS_ID = 20663;
    public static final String FALLBACK_LANG = "en_us";

    private FoliaLib foliaLib;

    private String serverLang;
    private BukkitMultiLangUtil<String> langUtil;

    private CombatHookManager combatHookManager;
    private RegionHookManager regionHookManager;

    private List<String> dangerRegionNames;
    private Metrics metrics;

    @Override
    public void onEnable() {
        // Utils
        this.foliaLib = new FoliaLib(this);

        // Config
        saveDefaultConfig();
        loadConfigOptions();

        // Post Config Utils
        this.langUtil = BukkitMultiLangUtil.createCustom(this, FALLBACK_LANG, serverLang, this::formatMessage);

        // Managers
        this.combatHookManager = new CombatHookManager(this);
        this.regionHookManager = new RegionHookManager(this);
        if (!this.applyConfigOptions()) return;

        // Commands
        AntiNakedKillingCmd antiNakedKillingCmd = new AntiNakedKillingCmd(this);
        PluginCommand antiNakedKillingPlCmd = this.getCommand("antinakedkilling");
        antiNakedKillingPlCmd.setExecutor(antiNakedKillingCmd);
        antiNakedKillingPlCmd.setTabCompleter(antiNakedKillingCmd);

        // Listeners
        this.getServer().getPluginManager().registerEvents(new DamageListener(this), this);

        // Update checker
        SimpleUpdateChecker.checkUpdate(this, String.format("[%s] ", this.getDescription().getName()), 114395,
                runnable -> this.foliaLib.getImpl().runAsync(consumer -> runnable.run()));

        // bStats
        this.metrics = new Metrics(this, BSTATS_ID);
        this.metrics.addCustomChart(new SimplePie("region_count",
                () -> String.valueOf(this.dangerRegionNames.size())
        ));
        this.metrics.addCustomChart(new SimplePie("region_name_1",
                () -> String.valueOf(this.dangerRegionNames.get(0))
        ));
        this.metrics.addCustomChart(new SimplePie("region_name_2",
                () -> String.valueOf(this.dangerRegionNames.get(1))
        ));
        this.metrics.addCustomChart(new SimplePie("region_name_3",
                () -> String.valueOf(this.dangerRegionNames.get(1))
        ));
        this.metrics.addCustomChart(new SimplePie("active_combat_hooks_1",
                () -> String.valueOf(this.combatHookManager.getActiveHooks().get(0).getName())
        ));
        this.metrics.addCustomChart(new SimplePie("active_region_hooks_1",
                () -> String.valueOf(this.regionHookManager.getActiveHooks().get(0).getName())
        ));
    }

    public void reloadAllConfig() {
        this.reloadConfig();
        this.applyConfigOptions();
    }

    private void loadConfigOptions() {
        this.serverLang = getConfig().getString("lang", FALLBACK_LANG);
        this.dangerRegionNames = getConfig().getStringList("danger-regions");
    }

    private boolean applyConfigOptions() {
        this.combatHookManager.init();
        this.regionHookManager.init(dangerRegionNames);

        // Check for at least 1 combat hook
        if (this.combatHookManager.getActiveHooks().isEmpty()) {
            getLogger().severe("No active combat hooks found! Plugin will not work!");
            return false;
        }
        // Check for at least 1 region hook
        if (this.regionHookManager.getActiveHooks().isEmpty()) {
            getLogger().severe("No active region hooks found! Plugin will not work!");
            return false;
        }

        return true;
    }

    @Override
    public void onDisable() {
        // bStats
        this.metrics.shutdown();

        // Listeners
        HandlerList.unregisterAll(this);

        // Commands

        // Managers
        this.regionHookManager.removeAllHooks();
        this.combatHookManager.removeAllHooks();

        // Config

        // Utils
    }

    public BukkitMultiLangUtil<String> getLangUtil() {
        return this.langUtil;
    }

    public CombatHookManager getCombatManager() {
        return this.combatHookManager;
    }

    public RegionHookManager getRegionManager() {
        return this.regionHookManager;
    }

    private String formatMessage(String translationKey) {
        String toLegacy = LegacyColorCodeParser.convertHexToLegacy('&', translationKey);
        return ChatColor.translateAlternateColorCodes('&', toLegacy);
    }

}
