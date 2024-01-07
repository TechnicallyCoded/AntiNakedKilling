package com.tcoded.antinakedkilling.manager;

import com.tcoded.antinakedkilling.AntiNakedKilling;
import com.tcoded.antinakedkilling.struct.CombatHook;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractHookManager <HookType> {

    protected final AntiNakedKilling plugin;
    private final List<HookType> activeHooks = new ArrayList<>();

    public AbstractHookManager(AntiNakedKilling plugin) {
        this.plugin = plugin;
    }

    public void addHook(HookType hook) {
        activeHooks.add(hook);
    }

    public void removeHook(HookType hook) {
        activeHooks.remove(hook);
    }

    public void removeAllHooks() {
        activeHooks.clear();
    }

    public List<HookType> getActiveHooks() {
        return activeHooks;
    }

    protected boolean isHookEnabledAndActive(String pluginName) {
        if (!isHookEnabledInConfig(pluginName)) return false;

        Plugin hookPlugin = this.plugin.getServer().getPluginManager().getPlugin(pluginName);
        if (hookPlugin == null) return false;

        return hookPlugin.isEnabled();
    }

    protected abstract boolean isHookEnabledInConfig(String pluginName);
}
