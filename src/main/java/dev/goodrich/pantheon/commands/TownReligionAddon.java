package dev.goodrich.pantheon.commands;

import com.palmergames.bukkit.towny.TownyCommandAddonAPI;
import com.palmergames.bukkit.towny.command.BaseCommand;
import com.palmergames.bukkit.towny.object.AddonCommand;
import com.palmergames.bukkit.towny.object.Town;
import dev.goodrich.pantheon.util.ReligionCalculator;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class TownReligionAddon extends BaseCommand implements TabExecutor {

    public TownReligionAddon() {
        // Register as a town command addon under /t religion.
        AddonCommand townReligionCommand = new AddonCommand(
                TownyCommandAddonAPI.CommandType.TOWN,
                "religion",
                this
        );
        TownyCommandAddonAPI.addSubCommand(townReligionCommand);
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, String label, String[] args) {
        Town town;
        try {
            if (args.length > 0) {
                // When a town name is provided.
                town = getTownOrThrow(args[0]);
            } else {
                // When no argument is provided, derive the town from the sender.
                // First get the Resident from the sender (casting to Player) then get their town.
                town = getTownFromResidentOrThrow(getResidentOrThrow((Player) sender));
            }
        } catch (Exception e) {
            sender.sendMessage("Town not found.");
            return true;
        }

        String breakdown = ReligionCalculator.getTownBreakdown(town);
        sender.sendMessage(ChatColor.DARK_GREEN + "Religion breakdown for the town of" + town.getName() + ": " + ChatColor.GREEN + breakdown);
        return true;
    }
}
