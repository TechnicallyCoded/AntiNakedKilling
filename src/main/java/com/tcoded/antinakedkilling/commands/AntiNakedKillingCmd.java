package com.tcoded.antinakedkilling.commands;

import com.tcoded.antinakedkilling.AntiNakedKilling;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AntiNakedKillingCmd implements CommandExecutor, TabCompleter {

    private final AntiNakedKilling plugin;

    public AntiNakedKillingCmd(AntiNakedKilling plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        this.plugin.reloadConfig();
        commandSender.sendMessage(ChatColor.GREEN + "Config reloaded!");

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] args) {
        List<String> options = new ArrayList<>();

        if (args.length == 1) {
            options.add("reload");
        }

        return options.stream()
                .filter(option -> option.startsWith(args[args.length - 1]))
                .collect(Collectors.toList());
    }
}
