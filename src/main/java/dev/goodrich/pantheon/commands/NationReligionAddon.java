package dev.goodrich.pantheon.commands;

import com.palmergames.bukkit.towny.TownyCommandAddonAPI;
import com.palmergames.bukkit.towny.command.BaseCommand;
import com.palmergames.bukkit.towny.object.AddonCommand;
import com.palmergames.bukkit.towny.object.Nation;
import dev.goodrich.pantheon.util.ReligionCalculator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class NationReligionAddon extends BaseCommand implements TabExecutor {

    public NationReligionAddon() {
        // Register as a nation command addon under /n religion.
        AddonCommand nationReligionCommand = new AddonCommand(
                TownyCommandAddonAPI.CommandType.NATION,
                "religion",
                this
        );
        TownyCommandAddonAPI.addSubCommand(nationReligionCommand);
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, String label, String[] args) {
        Nation nation;
        try {
            if (args.length > 0) {
                // When a nation name is provided.
                nation = getNationOrThrow(args[0]);
            } else {
                // When no argument is provided, determine the nation from the sender.
                // We first get the resident, then their town, then the nation of that town.
                nation = getNationOrThrow(getTownFromResidentOrThrow(getResidentOrThrow((Player) sender)).getName());
            }
        } catch (Exception e) {
            sender.sendMessage("Nation not found.");
            return true;
        }

        String breakdown = ReligionCalculator.getNationBreakdown(nation);
        sender.sendMessage("Religion breakdown for nation " + nation.getName() + ": " + breakdown);
        return true;
    }
}
