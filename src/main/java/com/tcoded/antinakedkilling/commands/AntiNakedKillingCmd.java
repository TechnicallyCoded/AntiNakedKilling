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
        if (args.length < 1) {
            commandSender.sendMessage(this.plugin.getLangUtil().getFormattedOrEmpty("command.antinakedkilling.usage"));
            return true;
        }

        String arg0Lower = args[0].toLowerCase();

        switch (arg0Lower) {
            case "reload":
                this.plugin.reloadAllConfig();
                commandSender.sendMessage(this.plugin.getLangUtil().getFormattedOrEmpty("reload-success"));
                break;
            default:
                commandSender.sendMessage(this.plugin.getLangUtil().getFormattedOrEmpty("command.antinakedkilling.usage"));
                break;
        }

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
