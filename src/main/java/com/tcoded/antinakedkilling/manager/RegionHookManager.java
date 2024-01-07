package com.tcoded.antinakedkilling.manager;

import com.tcoded.antinakedkilling.AntiNakedKilling;
import com.tcoded.antinakedkilling.struct.RegionHook;
import com.tcoded.antinakedkilling.hook.region.WorldGuardHook;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class RegionHookManager extends AbstractHookManager<RegionHook> {

    private final ConfigurationSection activePluginHooksConfig;

    private List<String> dangerRegionNames;

    public RegionHookManager(AntiNakedKilling plugin) {
        super(plugin);
        this.activePluginHooksConfig = plugin.getConfig().getConfigurationSection("region-hooks");
    }

    public void init(List<String> dangerRegionNames) {
        this.dangerRegionNames = dangerRegionNames;

        if (isHookEnabledAndActive("WorldGuard")) this.addHook(new WorldGuardHook(this.plugin));
    }

    public boolean isInDangerRegion(Player player) {
        for (RegionHook hook : this.getActiveHooks()) {
            if (hook.isInDangerRegion(player)) return true;
        }
        return false;
    }

    // INTERNAL

    @Override
    protected boolean isHookEnabledInConfig(String pluginName) {
        return this.activePluginHooksConfig.getBoolean(pluginName.toLowerCase(), true);
    }

    public List<String> getDangerRegionNames() {
        return dangerRegionNames;
    }

}
