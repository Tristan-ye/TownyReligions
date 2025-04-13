package dev.goodrich.pantheon.commands;

import com.palmergames.bukkit.towny.TownyCommandAddonAPI;
import com.palmergames.bukkit.towny.command.BaseCommand;
import com.palmergames.bukkit.towny.object.AddonCommand;
import dev.goodrich.pantheon.Pantheon;
import dev.goodrich.pantheon.data.ReligionData;
import dev.goodrich.pantheon.events.PreReligionSetEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SetReligionCommand extends BaseCommand implements TabExecutor {

    public SetReligionCommand() {
        // Registers this command as a sub-command for residents (using TownyCommandAddonAPI.CommandType.RESIDENT_SET)
        AddonCommand residentSetReligionCommand = new AddonCommand(
                TownyCommandAddonAPI.CommandType.RESIDENT_SET,
                "religion",
                this
        );
        TownyCommandAddonAPI.addSubCommand(residentSetReligionCommand);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        Pantheon plugin = Pantheon.getInstance();
        if (args.length == 1) {
            String partial = args[0].toLowerCase();
            return plugin.getAllowedReligions().stream()
                    .filter(r -> r.toLowerCase().startsWith(partial))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        // Ensure that only players can execute this command.
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Only players can set their religion.");
            return true;
        }
        Player player = (Player) commandSender;

        // Check if an argument (the religion) is provided.
        if (args.length == 0) {
            player.sendMessage("Usage: /res set religion <religion>");
            return true;
        }

        // Join multiple arguments in case the religion name includes spaces.
        String chosenReligion = String.join(" ", args).trim();

        // Validate that the chosen religion exists in the allowed list.
        Pantheon plugin = Pantheon.getInstance();
        boolean valid = plugin.getAllowedReligions().stream().anyMatch(r -> r.equalsIgnoreCase(chosenReligion));
        if (!valid) {
            player.sendMessage("That religion is not available. Please choose an allowed religion.");
            return true;
        }

        // Optionally, fire a PreReligionSetEvent to allow other plugins or parts of your system
        // to cancel or modify the religion change.
        PreReligionSetEvent event = new PreReligionSetEvent(chosenReligion, player);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            player.sendMessage(event.getCancelMessage());
            return true;
        }

        // Set the player's religion in your data store.
        ReligionData.setResidentReligion(player.getUniqueId(), chosenReligion);
        player.sendMessage("Your religion has been set to " + chosenReligion + ".");

        return true;
    }
}
