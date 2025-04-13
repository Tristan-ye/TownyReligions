package dev.goodrich.pantheon.commands;

import dev.goodrich.pantheon.Pantheon;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.Collections;
import java.util.List;

public class PantheonReloadCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Expect the command to be: /pantheon reload religions
        if (args.length >= 2 && args[0].equalsIgnoreCase("reload") && args[1].equalsIgnoreCase("religions")) {
            Pantheon plugin = Pantheon.getInstance();
            boolean success = plugin.reloadReligions();
            if (success) {
                sender.sendMessage("Religions reloaded successfully.");
            } else {
                sender.sendMessage("Failed to reload religions. Please check the server log for errors.");
            }
        } else {
            sender.sendMessage("Usage: /pantheon reload religions");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        // Provide simple tab completion: the first argument only accepts "reload" and the second "religions"
        if (args.length == 1) {
            return List.of("reload");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("reload")) {
            return List.of("religions");
        }
        return Collections.emptyList();
    }
}
