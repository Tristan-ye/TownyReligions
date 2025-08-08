package dev.goodrich.pantheon.commands;

import dev.goodrich.pantheon.util.ReligionCalculator;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class ServerReligionCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender,
                             @NotNull Command command,
                             @NotNull String label,
                             String[] args) {
        String breakdown = ReligionCalculator.getServerBreakdown();
        sender.sendMessage(ChatColor.DARK_GREEN
                + "Server Religion Distribution: "
                + ChatColor.GREEN + breakdown);
        return true;
    }

    @Override
    public @NotNull List<String> onTabComplete(@NotNull CommandSender sender,
                                               @NotNull Command command,
                                               @NotNull String alias,
                                               String[] args) {
        return Collections.emptyList();
    }
}