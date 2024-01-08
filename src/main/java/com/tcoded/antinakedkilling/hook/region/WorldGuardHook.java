package com.tcoded.antinakedkilling.hook.region;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.tcoded.antinakedkilling.AntiNakedKilling;
import com.tcoded.antinakedkilling.manager.RegionHookManager;
import com.tcoded.antinakedkilling.struct.RegionHook;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class WorldGuardHook implements RegionHook {

    private final AntiNakedKilling plugin;
    private final RegionHookManager regionManager;

    public WorldGuardHook(AntiNakedKilling plugin) {
        this.plugin = plugin;
        this.regionManager = this.plugin.getRegionManager();
    }

    @Override
    public String getName() {
        return "WorldGuard";
    }

    @Override
    public boolean isInDangerRegion(Player player) {
        WorldGuard wg = WorldGuard.getInstance();
        RegionContainer regionContainer = wg.getPlatform().getRegionContainer();
        RegionManager wgRegionManager = regionContainer.get(BukkitAdapter.adapt(player.getWorld()));

        if (wgRegionManager == null) return false;

        List<String> dangerRegionNames = this.regionManager.getDangerRegionNames();

        Location location = player.getLocation();
        BlockVector3 blockVec3 = BlockVector3.at(location.getX(), location.getY(), location.getZ());

        for (String regionName : dangerRegionNames) {
            ProtectedRegion region = wgRegionManager.getRegion(regionName);
            if (region == null) continue;
            if (region.contains(blockVec3)) return true;
        }

        return false;
    }

}
