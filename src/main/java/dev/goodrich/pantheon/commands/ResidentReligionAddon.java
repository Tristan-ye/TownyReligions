package dev.goodrich.pantheon.commands;

import com.palmergames.bukkit.towny.TownyCommandAddonAPI;
import com.palmergames.bukkit.towny.command.BaseCommand;
import com.palmergames.bukkit.towny.object.AddonCommand;
import dev.goodrich.pantheon.data.ReligionData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class ResidentReligionAddon extends BaseCommand implements TabExecutor {

    public ResidentReligionAddon() {
        // Register as a resident command addon under /res religion.
        AddonCommand residentReligionCommand = new AddonCommand(
                TownyCommandAddonAPI.CommandType.RESIDENT,
                "religion",
                this
        );
        TownyCommandAddonAPI.addSubCommand(residentReligionCommand);
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, Command command, String label, String[] args) {
        try {
            // Use the helper that accepts a String if an argument is provided;
            // otherwise, assume the sender is a player.
            // (Ensure you cast to Player so that the correct getResidentOrThrow() is used.)
            var resident = (args.length > 0)
                    ? getResidentOrThrow(args[0])
                    : getResidentOrThrow((Player) sender);
            String religion = ReligionData.getResidentReligion(resident.getUUID());
            sender.sendMessage(ChatColor.DARK_GREEN + resident.getName() + "'s Religion: " + ChatColor.GREEN + religion);
        } catch (Exception e) {
            sender.sendMessage("Resident not found.");
        }
        return true;
    }
}
