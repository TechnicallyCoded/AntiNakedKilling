package com.tcoded.antinakedkilling.manager;

import com.tcoded.antinakedkilling.AntiNakedKilling;
import com.tcoded.antinakedkilling.hook.combat.CombatLogXHook;
import com.tcoded.antinakedkilling.struct.CombatHook;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class CombatHookManager extends AbstractHookManager<CombatHook> {
    private final ConfigurationSection activePluginHooksConfig;

    public CombatHookManager(AntiNakedKilling plugin) {
        super(plugin);
        this.activePluginHooksConfig = plugin.getConfig().getConfigurationSection("combat-hooks");
    }

    public void init() {
        if (isHookEnabledAndActive("CombatLogX")) this.addHook(new CombatLogXHook(this.plugin));
    }

    public boolean isInCombat(UUID uuid) {
        for (CombatHook hook : this.getActiveHooks()) {
            if (hook.isInCombat(uuid)) return true;
        }
        return false;
    }

    // INTERNAL

    @Override
    protected boolean isHookEnabledInConfig(String pluginName) {
        return this.activePluginHooksConfig.getBoolean(pluginName.toLowerCase(), true);
    }

}
